package com.sample.blinkid;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NationalIdScanningFragment extends ScanningFragment<NationalIdScanner> {

    public NationalIdScanningFragment() {
        scanner = new NationalIdScanner(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        com.sample.blinkid.databinding.FragmentNationalIdScanBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_national_id_scan, container, false);
        binding.setScanner(scanner);
        return binding.getRoot();
    }

    @NonNull
    public static NationalIdScanningFragment newInstance() {
        return new NationalIdScanningFragment();
    }
}
