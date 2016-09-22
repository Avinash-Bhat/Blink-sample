package com.sample.blinkid;

import com.sample.blinkid.databinding.FragmentPassportScanBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PassportScanningFragment extends ScanningFragment<PassportScanner> {

    public PassportScanningFragment() {
        scanner = new PassportScanner(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        FragmentPassportScanBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_passport_scan, container, false);
        binding.setScanner(scanner);
        return binding.getRoot();
    }

    @NonNull
    public static PassportScanningFragment newInstance() {
        return new PassportScanningFragment();
    }
}
