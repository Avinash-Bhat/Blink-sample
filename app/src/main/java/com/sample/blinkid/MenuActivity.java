package com.sample.blinkid;

import com.sample.blinkid.databinding.ActivityMenuBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMenuBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_menu);

        setSupportActionBar(binding.toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        MenuAdapter menuAdapter = new MenuAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        binding.container.setAdapter(menuAdapter);
        binding.tabs.setupWithViewPager(binding.container);
    }
}
