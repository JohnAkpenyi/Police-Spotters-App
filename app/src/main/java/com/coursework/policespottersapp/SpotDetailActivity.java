package com.coursework.policespottersapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SpotDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        //Get all values passed through by the main activity
        int id = getIntent().getIntExtra("id", 0);
        String carReg = getIntent().getStringExtra("carReg");
        String loc = getIntent().getStringExtra("location");
        String date = getIntent().getStringExtra("date");
        String makeModel = getIntent().getStringExtra("makeModel");
        String result = getIntent().getStringExtra("result");
        String notes = getIntent().getStringExtra("notes");
        boolean emailed = getIntent().getBooleanExtra("emailed", true);

        //Get View components
        TextView dId = findViewById(R.id.dId);
        TextView dCarReg = findViewById(R.id.dCarReg);
        TextView dLoc = findViewById(R.id.dLoc);
        TextView dDate = findViewById(R.id.dDate);
        TextView dMakeModel = findViewById(R.id.dMakeModel);
        TextView dResult = findViewById(R.id.dResult);
        ImageView dResultClr = findViewById(R.id.resultColour);
        TextView dNotes = findViewById(R.id.dNotes);
        TextView dEmailed = findViewById(R.id.dEmailed);

        //Set the values for the view components
        dId.setText(Integer.toString(id));
        dCarReg.setText(carReg);
        dLoc.setText(loc);
        dDate.setText(date);
        dMakeModel.setText(makeModel);
        dResult.setText(result);
        dResultClr.setImageResource(MainActivity.getResultImg(result));
        dNotes.setText(notes);
        if (emailed){
            dEmailed.setText("Has been emailed");
        }else {
            dEmailed.setText("Needs to be emailed");
        }


        //Turn toolbar into Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detail For Spot Check With ID: " + id);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}