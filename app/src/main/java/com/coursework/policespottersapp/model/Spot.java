package com.coursework.policespottersapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Spot {

    @PrimaryKey
    @NonNull
    private int id;

    @NonNull
    private String date;

    @NonNull
    private String location;

    @NonNull
    private String carReg;

    @NonNull
    private String makeModel;

    @NonNull
    private String result;

    @NonNull
    private Boolean emailed;

    private String notes;

    public Spot(@NonNull int id, @NonNull String date, @NonNull String location, @NonNull String carReg, @NonNull String makeModel, @NonNull String result, @NonNull Boolean emailed, String notes){
        this.id = id;
        this.date = date;
        this.location = location;
        this.carReg = carReg;
        this.makeModel = makeModel;
        this.result = result;
        this.emailed = emailed;
        this.notes = notes;
    }

    public int getId(){
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public  String getCarReg(){
        return carReg;
    }

    public String getMakeModel(){
        return makeModel;
    }

    public String getResult(){
        return result;
    }

    public Boolean getEmailed(){return emailed; }

    public void setEmailed(Boolean b){ emailed = b;}

    public String getNotes(){
        return notes;
    }


}
