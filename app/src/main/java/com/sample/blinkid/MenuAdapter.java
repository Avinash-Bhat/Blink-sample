package com.sample.blinkid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class MenuAdapter extends FragmentPagerAdapter {

    private static final int NATIONAL_ID = 0;

    private static final int PASSPORT = 1;

    private static final int DRIVING_LICENCE_CARD = 2;

    private final Context context;

    MenuAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case NATIONAL_ID:
                return NationalIdScanningFragment.newInstance();
            case PASSPORT:
                return PassportScanningFragment.newInstance();
            case DRIVING_LICENCE_CARD:
                return DrivingLicenceScanningFragment.newInstance();
        }
        throw new IllegalStateException("unknown position: " + position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case NATIONAL_ID:
                return context.getString(R.string.title_national_id);
            case PASSPORT:
                return context.getString(R.string.title_passport);
            case DRIVING_LICENCE_CARD:
                return context.getString(R.string.title_driving_licence_card);
        }
        return null;
    }
}
