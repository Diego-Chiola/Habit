package com.app.habit.logic.usage.actions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.app.habit.logic.event.ActionEvent;

import java.util.Date;

public class MoveDetectorAccelerometer implements SensorEventListener {

    private static final String TAG = MoveDetectorAccelerometer.class.getName();

    private SensorManager _sensorManager = null;
    private Sensor _accelerometer = null;

    // change this sample size as you want, higher is more precise but slow measure.
    private final int SAMPLE_SIZE = 25;

    // change this threshold as you want, higher is more spike movement
    private final double THRESHOLD_MOVING = 1f;

    private double _acceleration = 0;
    private double _currentAccel = SensorManager.GRAVITY_EARTH;

    private Date _currentStateDate = null;
    private String _currentState = null;
    private final ActionEvent onStateChange = new ActionEvent();

    private int _sampleCount = 0;
    private double _sampleSum = 0;


    private void setMovingAsCurrentState() {
        var oldState = _currentState;
        var oldDate = (_currentStateDate == null) ? null : new Date(_currentStateDate.getTime());

        _currentState = Actions.MOVING;
        _currentStateDate = com.app.habit.helpers.Date.getCurrentTimeDate();
        onStateChange.execute(_currentState, _currentStateDate, oldState, oldDate);
    }

    private void setSittingAsCurrentState() {
        var oldState = _currentState;
        var oldDate = (_currentStateDate == null) ? null : new Date(_currentStateDate.getTime());

        _currentState = Actions.BE_SIT;
        _currentStateDate = com.app.habit.helpers.Date.getCurrentTimeDate();
        onStateChange.execute(_currentState, _currentStateDate, oldState, oldDate);
    }

    public void addOnStateChangeListener(ActionEvent.IActionListener listener) {
        onStateChange.subscribe(listener);
    }

    public MoveDetectorAccelerometer(Context context) {

        // get the system service that handle all the sensors
        _sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // get the accelerometer and check if the phone has one
        if ((_accelerometer =_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) == null)
            throw new IllegalArgumentException("your phone does not have an accelerometer");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        computeAccel(event.values.clone());
        computeAction();

    }

    private void computeAction() {
        if(_sampleCount <= SAMPLE_SIZE) {
            _sampleCount++;
            _sampleSum += Math.abs(_acceleration);
        }
        else {

            var sampleResult = _sampleSum / SAMPLE_SIZE;
            Log.i(TAG, "sampleResult = " + sampleResult);
            if (sampleResult > THRESHOLD_MOVING) {
                setMovingAsCurrentState();
            } else {
                setSittingAsCurrentState();
            }

            _sampleCount = 0;
            _sampleSum = 0;
        }
    }

    private void computeAccel(float[] motion) {

        var posX = motion[0];
        var posY = motion[1];
        var posZ = motion[2];


        var lastAccel = _currentAccel;
        _currentAccel = Math.sqrt(
                posX * posX +
                posY * posY +
                posZ * posZ
        );

        var delta = _currentAccel - lastAccel;
        _acceleration = _acceleration * 0.9d + delta;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }



    /**
     * Register the accelerometer in the sensor manager
     */
    public void startAccelerometer() {
        _sensorManager.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * unregister the accelerometer in the sensor manager
     */
    public void stopAccelerometer() {
        _sensorManager.unregisterListener(this);
    }

}
