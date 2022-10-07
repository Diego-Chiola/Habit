package com.app.habit.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.app.habit.data.AppDatabaseManager;
import com.app.habit.data.model.Usage;
import com.app.habit.databinding.FragmentSettingsBinding;
import com.app.habit.logic.SettingsManager;
import com.app.habit.logic.service.UsageService;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding _binding;
    private LocationManager lm;

    ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
                    _binding.swEnableHabitTracker.setChecked(true);
                    ManageServiceIntent();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
                    _binding.swEnableHabitTracker.setChecked(false);
                }

            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // enable binding
        _binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setupSwitchEnableDisableTracker();

        _binding.btnGrantUsagePermission.setOnClickListener(view1 -> {
            var intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        });

    }



    private void setupSwitchEnableDisableTracker() {

        _binding.swEnableHabitTracker.setChecked(UsageService.isServiceRunning());

        _binding.swEnableHabitTracker.setOnClickListener(l -> {
            check_requestPermission_Location();
        });
    }

    private void ManageServiceIntent() {
        Intent serviceIntent = new Intent(getActivity(), UsageService.class);
        boolean serviceStatus;
        if (_binding.swEnableHabitTracker.isChecked()) {  // if checked start UsageService
            serviceStatus = true;
            requireActivity().startService(serviceIntent);
            Toast.makeText(getContext(), "Start tracking", Toast.LENGTH_LONG).show();
        } else {
            serviceStatus = false;
            requireActivity().stopService(serviceIntent);
            Toast.makeText(getContext(), "Stop tracking", Toast.LENGTH_LONG).show();
        }

        SettingsManager.getUsageServiceSharedPreferences(getContext()).edit()
                .putBoolean(SettingsManager.USAGE_SERVICE_isEnabled, serviceStatus)
                .apply();
    }

    private void check_requestPermission_Location(){
        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gps_enabled) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("startTracking", "GOT ACCESS");
                ManageSuccess();
            } else {
                Log.i("startTracking", "NOT GOT ACCESS");
                ManageFailure();
            }
        }else {
            create_Alert_Gps();
        }
    }



    /**
     * Manage Success
     * **/
    private void ManageSuccess() {
            ManageServiceIntent();
    }

    /**
     * In case User is not giving Position this function create a pop up to invite the user to use GPS - connect to onRequestPermissionsResult in Principal Activity
     * **/
    private void ManageFailure() {
           requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void create_Alert_Gps() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Active Localization")
                .setMessage("Before all you need to active Position of your phone")
                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                .create().show();
        _binding.swEnableHabitTracker.setChecked(UsageService.isServiceRunning());
    }

}