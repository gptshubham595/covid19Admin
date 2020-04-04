package com.mohan.covid19admin;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodCrisisAdapter extends RecyclerView.Adapter<FoodCrisisAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FoodCrisisEntry> itemList;
    CallInterface callInterface;

    public FoodCrisisAdapter(CallInterface callInterface, Context context, ArrayList<FoodCrisisEntry> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.callInterface = callInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.food_entry_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodCrisisEntry f = itemList.get(position);
        final String contact = f.getContact();
        final String days = f.getDays() + " Days.";
        final String amount = f.getAmount() + " Kg";
        final ArrayList<String> food = f.getFood();
        final String latitude = f.getLatitude();
        final String longitude = f.getLongitude();
        StringBuilder foodList = new StringBuilder();
        for (String foodItem : food)
            foodList.append(foodItem).append("\n");
        String foodItems = foodList.toString();
        holder.amountTextView.setText(amount);
        holder.daysTextView.setText(days);
        holder.foodTextView.setText(foodItems);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callInterface.onCallClick(contact);
            }
        });
        holder.location.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               callInterface.onLocationClick(longitude, latitude);
           }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView amountTextView;
        private TextView daysTextView;
        private TextView foodTextView;
        private Button call;
        private Button location;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amount);
            daysTextView = itemView.findViewById(R.id.days);
            foodTextView = itemView.findViewById(R.id.items);
            call = itemView.findViewById(R.id.call);
            location = itemView.findViewById(R.id.location);
        }
    }

    interface CallInterface{
        void onCallClick(String contact);
        void onLocationClick(String longitude, String latitude);
    }
}
