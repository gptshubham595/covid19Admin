package com.mohan.covid19admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        tabLayout = findViewById(R.id.main_tab);
        pager = findViewById(R.id.main_viewPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mSectionPagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }
}
