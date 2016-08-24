package com.sample.blinkid;

import com.sample.blinkid.databinding.ActivityMainBinding;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private MainViewModel model;

    static {
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.microblink.util.Log.setLogLevel(com.microblink.util.Log.LogLevel.LOG_VERBOSE);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        model = new MainViewModel(this);
        binding.setScanner(model);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!model.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

