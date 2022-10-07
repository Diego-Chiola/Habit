package com.app.habit.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.habit.R;
import com.app.habit.databinding.ActivityPermissionBinding;
import com.app.habit.helpers.ActivityChanger;

public class PermissionActivity extends AppCompatActivity {

    private ActivityPermissionBinding _binding;

    ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app
                    Toast.makeText(this, "Permission GRANTED to Gps", Toast.LENGTH_SHORT).show();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    Toast.makeText(this, "Permission DENIED to Gps", Toast.LENGTH_SHORT).show();
                }

            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);




        // enable binding
        _binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);



        _binding.btnGrantPermission.setOnClickListener(view1 -> {

            // Before App Tracking Authorization
            var intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);


            // After try to take Gps Authorization
            check_requestPermission_Location();
        });

        _binding.btnContinue.setOnClickListener(view1 -> {
            ActivityChanger.ChangeActivityAndFinish(this, MainActivity.class);
        });


    }

    private void check_requestPermission_Location(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ManageSuccess();
        } else {
            ManageFailure();
        }
    }

    /**
     * Manage Success
     * **/
    private void ManageSuccess() {
        Toast.makeText(this, "You already permission to Tracking via GPS", Toast.LENGTH_SHORT).show();
    }

    /**
     * In case User is not giving Position this function create a pop up to invite the user to use GPS - connect to onRequestPermissionsResult in Principal Activity
     * **/
    private void ManageFailure() {
        requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

}