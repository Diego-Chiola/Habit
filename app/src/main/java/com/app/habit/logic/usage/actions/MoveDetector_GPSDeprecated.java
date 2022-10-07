package com.app.habit.logic.usage.actions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

@Deprecated
public class MoveDetector_GPSDeprecated {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static double meter_second_to_km_h = 3.6;
    private static float Meters = 10.0F;

    public boolean isDriving() {
        return isDriving;
    }

    public void setDriving(boolean driving) {
        isDriving = driving;
    }

    public boolean isDriving=false;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public int getCounter_driving() {
        return counter_driving;
    }

    public void setCounter_driving(int counter_driving) {
        this.counter_driving = counter_driving;
    }

    private int counter_driving=0;

    private boolean location_permission = true;
    // Time and Location
    private Location last_location = null;
    private Location new_location ;
    private long last_time = 0;
    private long new_time;

    private double km_to_h;
    private double km_to_h2;

    private Context Activity_to_use; // activity or context where use the Gps Move Detector

    /**
     * Constructor , parameters Context to use and TextView to Update
     **/
    public MoveDetector_GPSDeprecated(Context context) {
        // create location request
        locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(300)
                .setInterval(1000);

        // callback function called every time we get access to GPS
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                //super.onLocationResult(locationResult);
                Log.i("onLocationResult 0", "Hi");
                if (locationResult == null) {
                    return;
                }

                new_time = System.currentTimeMillis(); // get current time
                new_location = locationResult.getLastLocation(); // get last location of android
                DetectSpeedinKmH2();
            }
        };
        Activity_to_use=context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Activity_to_use);
    }

    /**
     * Function used to detect speed in Km for hour via famous function and Home made functions AND RETURN EACH OTHER
     */
    private void DetectSpeedinKmH2() {
        // compute Distance and velocity
        if ((new_location.hasSpeed())) { // ApiLevels 1
            km_to_h = new_location.getSpeed() * meter_second_to_km_h; // Meters per seconds
            Log.i("Callback 2.1", "I got speed , i go at " + km_to_h + "km-h");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // ApiLevels 26
                if (new_location.hasSpeedAccuracy()) {
                    km_to_h = new_location.getSpeedAccuracyMetersPerSecond() * meter_second_to_km_h;
                    Log.i("Callback 2.1.1", "I got speedAccuracy , i go at " + km_to_h + "km-h");
                }
            }
            if (km_to_h > 0.5) {
                isDriving=true;
                Toast.makeText(Activity_to_use,"U going at more than 5 km-h but exactly at "+ km_to_h, Toast.LENGTH_LONG).show();
                counter_driving++;
            }else {
                isDriving=false;
            }
            //UpdateVariable();
        }

        if (last_time == 0 && last_location == null) {
            UpdateVariable();
            Toast.makeText(Activity_to_use, "First time retrieving Location : " + String.valueOf(last_time), Toast.LENGTH_LONG).show();
        } else {
            NoSpeedDetectKmtoHour();
        }
        //UpdateUI();
    }


    private void UpdateVariable() {
        last_time = new_time;
        last_location = new_location;
    }

    private void UpdateUI() {
        double longitude = new_location.getLongitude();
        double latitude = new_location.getLatitude();
        /*String mex = "(" + String.valueOf(latitude) + "," + String.valueOf(longitude)+")";
        Log.i("Callback",mex);*/
    }

    public void RemoveCallBack(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    /**
     * Average Velocity 13 km/h man , 11 km/h woman
     * 8,3 meters at second is 30 km/h , 44,3 bolt max speed
     * Not detected speed via Famous function , my implementation of getSpeed() if this is not available
     **/
    private void NoSpeedDetectKmtoHour() {
        Log.i("Callback 2.2", "I have not speed ");
        float meters_distance = new_location.distanceTo(last_location);
        // Compute velocity
        long diff_in_ms = new_time - last_time;
        double diff_in_second = diff_in_ms / 1000;
        if (meters_distance >= 1.0) {   // every 1 meters detect velocity
            //Toast.makeText(MainActivity.this, "You made " + String.valueOf(meters_distance) + " Meters " +
            //" in " + String.valueOf(diff_in_second) + " second ", Toast.LENGTH_LONG).show();
            //Toast.makeText(Context_app, "First time : TimeInMills" + String.valueOf(_lastTimeInMillis) + " | Location (null) ", Toast.LENGTH_LONG).show();
            //Log.i(TAG,"First time : TimeInMills" + String.valueOf(_lastTimeInMillis) + " | Location (null) ");
            //Log.i(TAG, "Distanza dai 2 posti: " + String.valueOf(deltaDistance));
            //Log.i(TAG, "In quanto il GPS è stato aggiornato : " + String.valueOf(diff_time_gps));
            //Log.i(TAG, "Differenza dai 2 posti in sec: " + String.valueOf(deltaTimeInSeconds));
            double meters_to_second = meters_distance / diff_in_second;
            //km_to_h = meters_to_second * meter_second_to_km_h;
            km_to_h2 = meters_to_second * meter_second_to_km_h;
            //Toast.makeText(MainActivity.this, "You are going at " + String.valueOf(km_to_h) + " km/h", Toast.LENGTH_LONG).show();
            String mex = "You made " + meters_distance + " in " + diff_in_second + " seconds ";
            Log.i("Callback 2", mex);
            UpdateVariable();
        }
    }

    /**
     *  Start tracking ckecking permission of GPS , and Manage Success or not
     **/
    @SuppressLint("MissingPermission")
    public void startTracking() {
        if (fusedLocationProviderClient == null)
            return;
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null); // ref : https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi#requestLocationUpdates(com.google.android.gms.common.api.GoogleApiClient,%20com.google.android.gms.location.LocationRequest,%20com.google.android.gms.location.LocationCallback,%20android.os.Looper)
        Toast.makeText(Activity_to_use, "Start tracking" ,Toast.LENGTH_LONG).show();
    }


    public void setLocation_permission(boolean location_permission) {
        this.location_permission = location_permission;
    }
}

