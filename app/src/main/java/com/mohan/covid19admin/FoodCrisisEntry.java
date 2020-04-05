package com.mohan.covid19admin;

import java.io.Serializable;
import java.util.ArrayList;

public class FoodCrisisEntry implements Serializable {
    private ArrayList<String> food;
    private float amount;
    private String contact;
    private int days;
    private String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    private String latitude;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public FoodCrisisEntry() {
    }

    public ArrayList<String> getFood() {
        return food;
    }

    public void setFood(ArrayList<String> food) {
        this.food = food;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
