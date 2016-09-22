package com.sample.blinkid;

import com.sample.blinkid.databinding.FragmentDrivingLicenceScanBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrivingLicenceScanningFragment extends ScanningFragment<DrivingLicenceScanner> {

    public DrivingLicenceScanningFragment() {
        scanner = new DrivingLicenceScanner(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        FragmentDrivingLicenceScanBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_driving_licence_scan, container, false);
        binding.setScanner(scanner);
        return binding.getRoot();
    }

    @NonNull
    public static DrivingLicenceScanningFragment newInstance() {
        return new DrivingLicenceScanningFragment();
    }
}
