package com.app.habit.logic.usage.actions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.habit.helpers.Date;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class MoveDetectorGPS extends LocationCallback {

    private static final String TAG = MoveDetectorGPS.class.getName();

    private final LocationRequest _localRequest;
    private final FusedLocationProviderClient _fusedLocationProviderClient;

    private long _lastTimeInMillis = 0;
    private Location _lastLocation = null;

    private boolean isStarted = false;

    private volatile float _speedAshKh = 0;


    public MoveDetectorGPS(Context context) {

        if (context == null)
            throw new NullPointerException("context cannot be null");

        _localRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(300)
                .setInterval(1000);

        _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }


    @SuppressLint("MissingPermission")
    public void startGPS() {
        if (isStarted)
            return;

        _speedAshKh = 0;
        _fusedLocationProviderClient.requestLocationUpdates(_localRequest, this, null);
    }

    @SuppressLint("MissingPermission")
    public void stopGPS() {
        _fusedLocationProviderClient.removeLocationUpdates(this);
        _lastLocation = null;
        _lastTimeInMillis = 0L;
        isStarted = false;
        _speedAshKh = 0;
    }

    @Override
    public void onLocationResult(@NonNull LocationResult locationResult) {
        super.onLocationResult(locationResult);
        computeAction(locationResult.getLastLocation());
    }

    private void computeAction(@Nullable Location currentLocation) {

        Log.i(TAG, "Updating Gps ");
        if (currentLocation == null) {
            return;
        }

        if (_lastTimeInMillis == 0L && _lastLocation == null) {

            _lastTimeInMillis = Date.getCurrentTimeDate().getTime();
            _lastLocation = currentLocation;

        } else {

            var deltaDistance = currentLocation.distanceTo(_lastLocation);
            long diff_time_gps = Date.getCurrentTimeDate().getTime() - _lastTimeInMillis;
            Log.i(TAG, "lastimemillis: " + _lastTimeInMillis + "   deltaDistance = " + deltaDistance + "   diffTimeGPS = " + diff_time_gps);

            if (deltaDistance > 1 && diff_time_gps > 100.0) { // why we need to update _speed if we don't move? | if u use remember to update speed under the threshold otherwise speed remain over if previously catch and continue to set that is driving | also u need to do it because if update very fast and the space is big enough can go to 120 km/h

                // need to be float because 100/1000 = 0.1 and in int is 0 , so x / 0 in _speedAshkn is equal to infinity
                float deltaTimeInSeconds = ((float) (diff_time_gps / 1000.0));

                if (deltaTimeInSeconds == 0)
                    return;

                _speedAshKh = (deltaDistance / deltaTimeInSeconds) * 3.6f;
                _lastTimeInMillis = Date.getCurrentTimeDate().getTime();
                _lastLocation = currentLocation;
            }
        }
        Log.i(TAG, "2. My_speed = " + _speedAshKh);

    }

    public float getCurrentSpeedInKh() {
        return _speedAshKh;
    }
}
