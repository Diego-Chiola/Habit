package com.app.habit.logic.usage.actions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ActionsUsagesDeprecated {

    private static int ONE_MINUTE = 60; // 60
    private static Long one = 1000L; // In Milliseconds
    private MoveDetectorDeprecated movedetector = null;
    private MoveDetector_GPSDeprecated moveDetector_gps = null;
    private SensorManager sensorManager=null;
    private Sensor mSensor=null;

    boolean registered_gps=false;
    boolean registered_move=false;

    Map<String, Long> queryResultMap=null;
    /*private int counter_sitting=0;
    private int counter_moving=0;
    private int counter_driving=0;*/


    public ActionsUsagesDeprecated(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if ((mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) == null)
            Log.i("ActionsUsages - ", "Accelerometer not Found");
        // Map HashMap
        queryResultMap = new HashMap<String,Long>();
        for (String action_pack : Actions.actionsArray) {
            Long i = Long.valueOf(0L);
            queryResultMap.put(action_pack, i);
        }
        // instantiate move detector
        movedetector = new MoveDetectorDeprecated(sensorManager);

        // instantiate move detector
        moveDetector_gps = new MoveDetector_GPSDeprecated(context);

    }

    public void RegisterListener(){
        if(!registered_gps) {
            moveDetector_gps.startTracking();
            registered_gps = true;
        }
        if(!registered_move) {
            movedetector.RegisterListener(mSensor);
            registered_move = true;
        }
    }

    public void UnRegisterListener(){
        if(registered_gps) {
            moveDetector_gps.RemoveCallBack();
            registered_gps = false;
        }
        if(registered_move) {
            movedetector.UnRegisterListener();
            registered_move = false;
        }
    }

    public void RegisterMoveDetector(){
        if(!registered_move)
            movedetector.RegisterListener(mSensor);
    }

    public void UnRegisterMoveDetector(){
        if(registered_move)
        movedetector.UnRegisterListener();
    }

    /**
     * @return Mapped _actions Usages Map
     */
    public Map<String, Long> getDailyActionsUsage() {

        Log.i("TAG","------------------");
        for (String action_pack : Actions.actionsArray) {
            Long res = queryResultMap.get(action_pack);
            //Log.i("getDailyActionsUsage","Action N: "+action_pack.toString()+" | Res: "+res.toString());

            Log.i("TAG","Driving: "+ moveDetector_gps.getCounter_driving());
            Log.i("TAG","sitting: "+ movedetector.getCounter_sitting());
            Log.i("TAG","moving: "+ movedetector.getCounter_moving());

            boolean driving = moveDetector_gps.isDriving();
            if (driving) UnRegisterMoveDetector(); else RegisterMoveDetector();

            //boolean driving = false; // just for now
            Long sum = Long.sum(res, one);
            switch (action_pack) {
                case Actions.BE_SIT:
                    if (!driving && movedetector.getCounter_sitting() >= ONE_MINUTE) {
                        queryResultMap.put(action_pack, sum);
                        movedetector.setCounter_sitting(0);
                    }
                    break;
                case Actions.MOVING:
                    if (!driving && movedetector.getCounter_moving() >= ONE_MINUTE) {
                        queryResultMap.put(action_pack, sum);
                        movedetector.setCounter_moving(0);
                    }
                    break;
                default:
                    if (driving && moveDetector_gps.getCounter_driving() >= ONE_MINUTE) {
                        queryResultMap.put(action_pack, sum);
                        moveDetector_gps.setCounter_driving(0);
                    }
            }
        }
        return queryResultMap;
    }

    @Override
    public String toString() {
        return "ActionsUsages{" +
                ", queryResultMap=" + queryResultMap +
                '}';
    }
}

