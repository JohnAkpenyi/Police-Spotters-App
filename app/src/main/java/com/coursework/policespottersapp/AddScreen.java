package com.coursework.policespottersapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class AddScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screen);

        //Turn toolbar into Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Spot Check");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        //Spinner setup
        Spinner spinner = findViewById(R.id.resultInput);
        String[] items = new String[]{"No action required", "Advice given", "Produce documents", "Driver detained"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        //keyboard edit for multiline textview
        TextView t = findViewById(R.id.notesInput);
        t.setImeOptions(EditorInfo.IME_ACTION_DONE);
        t.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void doneClicked(View view) {
        //Get all inputs
        TextView location = findViewById(R.id.locationInput);
        String sLocation = location.getText().toString();

        TextView carReg = findViewById(R.id.carRegInput);
        String sCarReg = carReg.getText().toString();

        TextView make = findViewById(R.id.makeInput);
        TextView model = findViewById(R.id.modelInput);
        String sMakeNModel = make.getText().toString();
        sMakeNModel = sMakeNModel + ", " + model.getText().toString();

        Spinner result = findViewById(R.id.resultInput);
        String sResult = result.getSelectedItem().toString();

        TextView notes = findViewById(R.id.notesInput);
        String sNotes = notes.getText().toString();

        //Check if required fields are empty
        if (sLocation.isEmpty()|| sCarReg.isEmpty() ||
                make.getText().toString().isEmpty() ||
                model.getText().toString().isEmpty()){
            if (sLocation.isEmpty()){
                location.setError("Field is required!");
            }
            if (sCarReg.isEmpty()){
                carReg.setError("Field is required!");
            }
            if (make.getText().toString().isEmpty()){
                make.setError("Field is required!");
            }
            if (model.getText().toString().isEmpty()){
                model.setError("Field is required!");
            }
        }else{
            //Send information back to the main activity
            Intent replyIntent = new Intent();
            replyIntent.putExtra("locationFromAdd", sLocation);
            replyIntent.putExtra("carRegFromAdd", sCarReg);
            replyIntent.putExtra("makeNModelFromAdd", sMakeNModel);
            replyIntent.putExtra("resultFromAdd", sResult);
            replyIntent.putExtra("notesFromAdd", sNotes);
            setResult(RESULT_OK, replyIntent);

            finish();
        }



    }

}