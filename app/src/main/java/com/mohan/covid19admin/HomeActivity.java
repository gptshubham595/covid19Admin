package com.mohan.covid19admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ViewPager pager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference quickMessage;
    private DatabaseReference openChat;
    private TabLayout tabLayout;
    private ProgressDialog dialog;

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference2.child("User").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences sp2 = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp2.edit();
                editor.putString("name", dataSnapshot.child("name").getValue().toString());
                editor.putString("email", dataSnapshot.child("email").getValue().toString());
                editor.putString("id", mAuth.getUid());
                editor.putString("image", dataSnapshot.child("image").getValue().toString());
                editor.putString("state", dataSnapshot.child("state").getValue().toString());
//                editor.putBoolean("superviser", Boolean.parseBoolean(dataSnapshot.child("superviser").getValue().toString()));
                editor.apply();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(mAuth.getUid() == null){
            finish();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Covid19 Admin");
        tabLayout = findViewById(R.id.main_tab);
        pager = findViewById(R.id.main_viewPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mSectionPagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }
}
