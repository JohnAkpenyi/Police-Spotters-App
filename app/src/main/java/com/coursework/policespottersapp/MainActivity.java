package com.coursework.policespottersapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.policespottersapp.model.Spot;
import com.coursework.policespottersapp.model.viewmodel.SpotViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SpotViewModel spotViewModel;

   
    List<Spot> spotCheckList = new ArrayList<>();
    List<Spot> spotCheckSearchList = new ArrayList<>();
    List<Spot> spotCheckPlaceHolderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SpotListAdapter adapter;
    Spinner filter;
    TextView searchField;
    TextView beginnerText;

    boolean searching = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Turn toolbar into Action Bar*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_search);
        setSupportActionBar(toolbar);

        //Spinner setup
        filter = findViewById(R.id.filter);
        String[] items = new String[]{"Car Reg", "Id", "Date", "Location", "Result", "Make Model"};
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        filter.setAdapter(a);

        beginnerText = findViewById(R.id.beginnerText);

        setupViewModel();
        setupRecyclerView();
        setupSearch();

    }

    private void setupViewModel(){
        //View model setup
        spotViewModel = ViewModelProviders.of(this).get(SpotViewModel.class);
        spotViewModel.getAllSpotChecks().observe(this, new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                for (Spot spot: spots){
                    //Add to top of array
                    //Newest First
                    spotCheckList.add(0, spot);
                }
                adapter.notifyDataSetChanged();
                //Make the placeholder list the same as the spot check list as spot check list will be changed when searching
                spotCheckPlaceHolderList = spotCheckList;
                if (spotCheckList.isEmpty()){
                    beginnerText.setVisibility(View.VISIBLE);
                }else{
                    beginnerText.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupRecyclerView(){
        //recycle view setup
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new SpotListAdapter(this, spotCheckList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //When the view loads, make the recycle view take up the whole screen under the toolbar.
        //This is done because the search components will be hidden, which were on top of the recycle view.
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.height = 0;
        recyclerView.setLayoutParams(layoutParams);
    }

    private void setupSearch(){
        searchField = findViewById(R.id.searchText);
        //keyboard edit for search field
        searchField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchField.setRawInputType(InputType.TYPE_CLASS_TEXT);

        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ///
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Turn the search text into lower case
                String str = s.toString().toLowerCase();

                if (count == 0){
                    //If there's nothing in the search field, return spot check list back to normal
                    spotCheckList = spotCheckPlaceHolderList;
                }else{
                    //variable for if the spot check should be added into the spot check search list
                    boolean shouldAdd = true;
                    //clear the search list after every change if the search field isn't empty
                    spotCheckSearchList.clear();
                    //Get the value from the filter
                    String filter = (String) MainActivity.this.filter.getSelectedItem();

                    for (Spot spot: spotCheckPlaceHolderList){
                        shouldAdd = true;
                        str.toString().toLowerCase();
                        for (int i = 0; i < str.length(); i++){
                            try {
                                if (getFilter(spot, filter).toLowerCase().charAt(i) != str.charAt(i)){
                                    shouldAdd = false;
                                }
                            }catch (StringIndexOutOfBoundsException e){
                                if (shouldAdd){
                                    shouldAdd = false;
                                }
                            }

                        }
                        if (shouldAdd){
                            spotCheckSearchList.add(spot);
                        }
                    }


                    spotCheckList = spotCheckSearchList;
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                ////
            }
        });
    }

    public String getFilter(Spot spot, String result){
        //Check which value has been selected in the filter
        switch(result){
            case "Id":
                return Integer.toString(spot.getId());
            case "Car Reg":
                return spot.getCarReg();
            case "Date":
                return spot.getDate();
            case "Location":
                return spot.getLocation();
            case "Result":
                return spot.getResult();
            case "Make Model":
                return spot.getMakeModel();
            default:
                return spot.getCarReg();
        }
    }

    public static void dismissKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    protected void sendLatestEmail() {
        //variable for all the spot check which haven't been emailed yet
        List<Spot> unemailedSpots = new ArrayList<>();
        String format = "";

        //Cycle through the main list and if the spot check is not emailed
        //add it to the unemailed list and then change the emailed variable.
        for (Spot spot: spotCheckList){
            if (!spot.getEmailed()){
                unemailedSpots.add(spot);
                Spot newSpot = spot;
                newSpot.setEmailed(true);
                spotViewModel.delete(spot);
                spotViewModel.insert(newSpot);
            }
        }

        //If the list is not empty, start making the message
        if (!unemailedSpots.isEmpty()){
            for (Spot spot: unemailedSpots){
                format = format
                        + "_______________" + "\n"
                        + "Id = " +spot.getId() + "\n"
                        + "Car Reg = " +spot.getCarReg() + "\n"
                        + "Location = " +spot.getLocation() + "\n"
                        + "Make & Model = " +spot.getMakeModel() + "\n"
                        + "Result of check = " +spot.getResult() + "\n"
                        + "Notes =" +spot.getNotes() + "\n";
            }
        }

        //Start sending email process
        Log.i("Send email", "");
        String[] recipient = {"johnakpenyi@hotmail.com"};

        Intent eIntent = new Intent(Intent.ACTION_SEND);
        eIntent.setData(Uri.parse("mailto:"));
        eIntent.setType("text/plain");

        eIntent.putExtra(Intent.EXTRA_EMAIL, recipient);
        eIntent.putExtra(Intent.EXTRA_SUBJECT, "Latest Spot Checks");
        eIntent.putExtra(Intent.EXTRA_TEXT, format);

        try {
            startActivity(Intent.createChooser(eIntent, "Send mail..."));
            finish();
            Log.i("Email sent", "");
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendAllEmail(){
        String format = "";

        for (Spot spot: spotCheckList){
            format = format
                    + "_______________" + "\n"
                    + "Id = " +spot.getId() + "\n"
                    + "Car Reg = " +spot.getCarReg() + "\n"
                    + "Location = " +spot.getLocation() + "\n"
                    + "Make & Model = " +spot.getMakeModel() + "\n"
                    + "Result of check = " +spot.getResult() + "\n"
                    + "Notes =" +spot.getNotes() + "\n";
        }

        //Start sending email process
        Log.i("Send email", "");
        String[] recipient = {"johnakpenyi@hotmail.com"};

        Intent eIntent = new Intent(Intent.ACTION_SEND);
        eIntent.setData(Uri.parse("mailto:"));
        eIntent.setType("text/plain");

        eIntent.putExtra(Intent.EXTRA_EMAIL, recipient);
        eIntent.putExtra(Intent.EXTRA_SUBJECT, "Latest Spot Checks");
        eIntent.putExtra(Intent.EXTRA_TEXT, format);

        try {
            startActivity(Intent.createChooser(eIntent, "Send mail..."));
            finish();
            Log.i("Email sent", "");
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSearch(){
        //Toggle search fields
        if (!searching){
            searchField.setVisibility(View.VISIBLE);
            searchField.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            filter.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams constraintLayout = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
            constraintLayout.topToBottom = searchField.getId();
            constraintLayout.height = 0;
            recyclerView.setLayoutParams(constraintLayout);

            searching = true;
        }else{
            dismissKeyboard(MainActivity.this);

            searchField.setText("");

            searchField.setVisibility(View.GONE);
            filter.setVisibility(View.GONE);

            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.height = 0;
            recyclerView.setLayoutParams(layoutParams);

            searching = false;
        }
    }

    protected static int getResultImg(String result){
        //Get the drawable based on the result
        switch (result){
            case "No action required":
                return R.drawable.ic_dot_blue;
            case "Advice given":
                return R.drawable.ic_dot_green;
            case "Produce documents":
                return R.drawable.ic_dot_yellow;
            case "Driver detained":
                return R.drawable.ic_dot_red;
            default:
                return R.drawable.ic_dot_blue;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Creates the options in the toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Check which button on the toolbar was pressed
        switch (id){
            //'home' was changed into a search icon
            case android.R.id.home:
                showSearch();
                break;
            case R.id.info:
                //When info is clicked, show an alert, which shows information
                String[] info = {"1. To add a spot check, click the plus icon(+) in the toolbar at the top",
                        "2. To search for spot checks, click on the magnifier icon in the toolbar at the top",
                        "3. To email spot checks, click on the mail icon in the toolbar at the top",
                        "4. To view more details about a spot check, click on the spot check",
                        "HOPE YOU LIKE THE APP :D",
                        "CREATED BY: JOHN AKPENYI"};

                builder.setTitle("Useful Information");
                builder.setItems(info, null);
                builder.show();
                break;
            case R.id.mail:
                //When main is clicked, show an alert, which gives you two options and then allows you to send the email
                String[] choice = {"Email All", "Email Latest"};

                builder.setTitle("Email");
                builder.setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                sendAllEmail();
                                break;
                            case 1:
                                sendLatestEmail();
                                break;
                            default:
                                System.out.println("Error occured trying to send email");
                        }
                    }
                });
                builder.show();
                break;
            case R.id.add:
                //When add is clicked, it goes to the add spot check screen
                Intent addScreen = new Intent( this, AddScreen.class);
                startActivityForResult(addScreen, 0);
                break;
            default:

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Create Id for new spot check
        int numId = spotCheckList.size() + 1;

        //Get the current date
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd \n hh:mm:ss");
        String date = dateFormat.format(currentDate);

        //Get all the values which was passed on from addScreen
        assert data != null;
        String location = data.getStringExtra("locationFromAdd");
        String carReg = data.getStringExtra("carRegFromAdd");
        String makeNModel = data.getStringExtra("makeNModelFromAdd");
        String result = data.getStringExtra("resultFromAdd");
        String notes = data.getStringExtra("notesFromAdd");

        //add a new spot heck into the database
        Spot newSpot = new Spot(numId, date, location, carReg, makeNModel, result,false, notes);
        spotCheckList.clear();
        spotViewModel.insert(newSpot);

    }

    public class SpotListAdapter extends RecyclerView.Adapter<SpotViewHolder> {

        private final List<Spot> spotList;
        private LayoutInflater inflater;

        public SpotListAdapter(Context context, List<Spot> spotList) {
            inflater = LayoutInflater.from(context);
            this.spotList = spotList;
        }

        @NonNull
        @Override
        public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.spot_item, parent, false);
            return new SpotViewHolder(itemView, this);
        }

        @Override
        public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
            Spot spot = spotCheckList.get(position);

            holder.date.setText(spot.getDate());
            holder.plate.setText(spot.getCarReg());
            holder.choice.setImageResource(getResultImg(spot.getResult()));
            holder.location.setText(spot.getLocation());
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent detailScreen = new Intent( MainActivity.this, SpotDetailActivity.class);
                    detailScreen.putExtra("id", spot.getId());
                    detailScreen.putExtra("carReg", spot.getCarReg());
                    detailScreen.putExtra("date", spot.getDate());
                    detailScreen.putExtra("location", spot.getLocation());
                    detailScreen.putExtra("makeModel", spot.getMakeModel());
                    detailScreen.putExtra("result", spot.getResult());
                    detailScreen.putExtra("notes", spot.getNotes());
                    detailScreen.putExtra("emailed", spot.getEmailed());
                    startActivity(detailScreen);
                }
            });

        }

        @Override
        public int getItemCount() {
            return spotCheckList.size();
        }
    }

    public class SpotViewHolder extends RecyclerView.ViewHolder {

        public final TextView date;
        public final TextView plate;
        public final ImageView choice;
        public final TextView location;
        final SpotListAdapter adapter;

        public SpotViewHolder(@NonNull View itemView, SpotListAdapter adapter) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            plate = itemView.findViewById(R.id.plate);
            choice = itemView.findViewById(R.id.choice);
            location = itemView.findViewById(R.id.location);
            this.adapter = adapter;
        }
    }

}