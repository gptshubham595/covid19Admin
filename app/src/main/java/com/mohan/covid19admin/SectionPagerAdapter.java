package com.mohan.covid19admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                SuspectReport suspectReport = new SuspectReport();
                return suspectReport;
            case 1:
                FoodCrisis foodCrisis = new FoodCrisis();
                return foodCrisis;
            case 2:
                FindSuspect findSuspect = new FindSuspect();
                return  findSuspect;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "SUSPECT REPORTED";
            case 1:
                return "FOOD CRISIS";
            case 2:
                return "FIND SUSPECT";
            default:
                return null;
        }
    }
}
