package com.mohan.covid19admin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * create an instance of this fragment.
 */
public class FoodCrisis extends Fragment implements FoodCrisisAdapter.CallInterface {
    private RecyclerView foodCrisisRecyclerView;
    ArrayList<FoodCrisisEntry> entries;
    private FoodCrisisAdapter foodCrisisAdapter;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference db = firebaseDatabase.getReference();
    private static final int REQUEST_CODE = 779;
    private String contact;
    private String longitude;
    private String latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_crisis, container, false);
        foodCrisisRecyclerView = view.findViewById(R.id.recycler_view);
        entries = new ArrayList<>();
        foodCrisisAdapter = new FoodCrisisAdapter(this, getContext(), entries);
        entries.clear();
        db.
                child("foodEntries").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        entries.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            HashMap<String, Object> temp = (HashMap<String, Object>) postSnapshot.getValue();
                            FoodCrisisEntry entry = new FoodCrisisEntry();
                            if (temp != null) {
                                entry.setAmount(Float.parseFloat(temp.get("amount").toString()));
                                entry.setDays(Integer.parseInt(temp.get("days").toString()));
                                entry.setContact(temp.get("contact").toString());
                                entry.setLatitude(temp.get("latitude").toString());
                                entry.setLongitude(temp.get("longitude").toString());
                                ArrayList<String> foodArray = (ArrayList<String>)temp.get("food");
                                entry.setFood(foodArray);
                                entries.add(entry);
                            }
                        }
                        foodCrisisAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        foodCrisisRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodCrisisRecyclerView.setAdapter(foodCrisisAdapter);
        return view;
    }

    private void checkPermission(String task){
        if(task.equals("CALL")) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact));
                getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Permission denied to make call", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCallClick(String contact) {
        this.contact = contact;
        String task = "CALL";
        checkPermission(task);
    }

    @Override
    public void onLocationClick(String longitude, String latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        String task = "LOCATION";
        String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }
}
