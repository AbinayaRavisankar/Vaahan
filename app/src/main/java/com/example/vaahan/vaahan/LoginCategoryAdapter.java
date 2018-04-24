package com.example.vaahan.vaahan;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LoginCategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public LoginCategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new PassengerLoginFragment();
        } else {
            return new DriverLoginFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return ("Passenger");
        } else {
            return ("Driver");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
