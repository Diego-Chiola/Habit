package com.app.habit.logic.usage.actions;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

@Deprecated
public class MoveDetectorDeprecated implements SensorEventListener{

    public static final String RUNNING = "Running";
    public static final String WALKING = "Walking";
    public static final String SITTING = "Sitting";
    public static final String MOVING = "Moving";
    private final String TAG = "MoveDetector";
    /*private TextView Doing=null;
    private TextView TextCount=null;*/
    private SensorManager sensormanager=null;

    private int counter_sitting=0;
    private int counter_moving=0;


    public int getCounter_sitting() {
        return counter_sitting;
    }

    public int getCounter_moving() {
        return counter_moving;
    }

    public void setCounter_sitting(int counter_sitting) {
        this.counter_sitting = counter_sitting;
    }

    public void setCounter_moving(int counter_moving) {
        this.counter_moving = counter_moving;
    }


    //private ArrayList<Float[]> accValues = null;
    // private ArrayList<Float[]> accValues = null;

    /****** new *******/

    private float[] mGravity;
    private double mAccel= 0.00f;
    private double mAccelCurrent=SensorManager.GRAVITY_EARTH;

    private int hitCount = 0;
    private double hitSum = 0;

    private final int SAMPLE_SIZE = 25; // change this sample size as you want, higher is more precise but slow measure.
    private final double THRESHOLD_WALKING = 0.3; // change this threshold as you want, higher is more spike movement
    private final double THRESHOLD_RUNNING = 2.2; // change this threshold as you want, higher is more spike movement

    //public String Movement;  // public field to return

    /***** SECTION FOR GPS **********/

    private Activity Activity_to_use; // activity or context where use the Gps Move Detector
    MoveDetector_GPSDeprecated moveDetector_gps;

    /*******************************/

    public MoveDetectorDeprecated(SensorManager sensorManager) {
        sensormanager=sensorManager;
    }

    public MoveDetectorDeprecated(SensorManager sensorManager, TextView textCount){  // pass everything u want to update
        sensormanager=sensorManager;
        //TextCount = textCount;
        //moveDetector_gps = new MoveDetector_GPS(Activity_to_use); Idea MoveDetectorGps inside MoveDetector
    }

    public MoveDetectorDeprecated(SensorManager sensorManager, TextView textCount, TextView doing) {
        sensormanager=sensorManager;
        /*TextCount = textCount;
        Doing = doing;*/
    }


    /**
     * Called when there is a new sensor event. Get and Manage Data from Accelerometer
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            double x = mGravity[0];
            double y = mGravity[1];
            double z = mGravity[2];
            double mAccelLast = mAccelCurrent;
            mAccelCurrent = Math.sqrt(x * x + y * y + z * z);
            double delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if(hitCount <= SAMPLE_SIZE){
                hitCount++;
                hitSum += Math.abs(mAccel);
                //TextCount.setText(counter.toString());
                //Log.i(TAG, "Sampling information");
            }else{
                double hitResult = hitSum / SAMPLE_SIZE;

                //Log.i(TAG, String.valueOf(hitResult));
                //TextCount.setText(String.valueOf(hitResult));

                if(hitResult > THRESHOLD_RUNNING){
                    //Log.i(TAG, "Running");
                    //Doing.setText(R.string.Running);
                    counter_moving++;
                }else if (hitResult > THRESHOLD_WALKING) {
                    //Log.i(TAG, "Walking");
                    //Doing.setText(R.string.Walking);
                    counter_moving++;
                } else {
                    //Log.i(TAG, "Stop Walking");
                    //Doing.setText(R.string.Sitting);
                    counter_sitting++;
                }
                hitCount = 0;
                hitSum = 0;
            }
        }else{
            Log.i(TAG,"Error Accelerometer not found");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do something here if sensor accuracy changes.
    }

    public void RegisterListener(Sensor mSensor){
        sensormanager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Sensor Event Listener is equal to this
     * **/
    public void UnRegisterListener(){
        sensormanager.unregisterListener(this); // se attaccato a altri sensori posso specificare solo 1
    }

}
