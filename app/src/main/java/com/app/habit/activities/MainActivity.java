package com.app.habit.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.habit.R;
import com.app.habit.databinding.ActivityMainBinding;
import com.app.habit.fragments.AnalyticsFragment;
import com.app.habit.fragments.HomeFragment;
import com.app.habit.fragments.SettingsFragment;
import com.app.habit.helpers.Fragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // enable binding
        _binding  = ActivityMainBinding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);

        // bottom navigation initialization
        bottomNavigationBarInit();
    }

    private void bottomNavigationBarInit() {
        _binding.bottomNavigation.setItemIconTintList(null);
        AppCompatActivity thisClass = this;

        _binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId)  {
                    case R.id.bnm_home:
                        _binding.tvToolbarFragmentName.setText(getResources().getText(R.string.home));
                        Fragment.ChangeFragment(thisClass,  R.id.main_fragment_container, HomeFragment.class);
                        return true;

                    case R.id.bnm_analytics:
                        _binding.tvToolbarFragmentName.setText(getResources().getText(R.string.analytics));
                        Fragment.ChangeFragment(thisClass,  R.id.main_fragment_container, AnalyticsFragment.class);
                        return true;

                    case R.id.bnm_settings:
                        _binding.tvToolbarFragmentName.setText(getResources().getText(R.string.settings));
                        Fragment.ChangeFragment(thisClass,  R.id.main_fragment_container, SettingsFragment.class);
                        return true;
                }
                return false;
            }
        });
        _binding.bottomNavigation.setSelectedItemId(R.id.bnm_home);
    }

}