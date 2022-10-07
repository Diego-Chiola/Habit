package com.app.habit.logic.usage.actions;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActionsUsages {

    private static final String TAG = ActionsUsages.class.getName();

    private static final float THRESHOLD_DRIVING = 3f;
    private final Map<String, Long> _actionsUsages;
    private volatile boolean isListening = false;

    private final MoveDetectorAccelerometer _moveDetectorAccelerometer;
    private final MoveDetectorGPS _moveDetectorGPS;

    public ActionsUsages(Context context) {
        _actionsUsages = new HashMap<>();
        for (var action : Actions.actionsArray)
            _actionsUsages.put(action, 0L);

        _moveDetectorAccelerometer = new MoveDetectorAccelerometer(context);
        _moveDetectorAccelerometer.addOnStateChangeListener(this::calculateTimeForState);

        _moveDetectorGPS = new MoveDetectorGPS(context);
    }

    public void resetData() {
        _actionsUsages.replaceAll((a, v) -> 0L);
    }

    private void calculateTimeForState(@NotNull String currentState, @NotNull  Date currentStateSetDate,
                                       @Nullable String oldState, @Nullable Date oldStateSetDate) {

        // if is the first time that the state has been set do nothing
        if(oldState == null || oldStateSetDate == null)
            return;

        var stateTimeInMillis = currentStateSetDate.getTime() - oldStateSetDate.getTime();

        if (oldState.equals(Actions.BE_SIT) && _moveDetectorGPS.getCurrentSpeedInKh() > THRESHOLD_DRIVING) {
            Log.i(TAG, "Driving state");
            _actionsUsages.put(Actions.DRIVING, _actionsUsages.get(Actions.DRIVING) + stateTimeInMillis);
        } else {
            Log.i(TAG, oldState + " state");
            _actionsUsages.put(oldState, _actionsUsages.get(oldState) + stateTimeInMillis);
        }
    }


    public void startListenActions() {
        if(isListening)
            return;

        _moveDetectorAccelerometer.startAccelerometer();
        _moveDetectorGPS.startGPS();
        isListening = true;
    }

    public void stopListenActions() {
        _moveDetectorAccelerometer.stopAccelerometer();
        _moveDetectorGPS.stopGPS();
        isListening = false;
    }

    public Map<String, Long> getActionsUsage() {

        var deepCopyMap = new HashMap<String, Long>();
        for (var action : Actions.actionsArray) {
            if (_actionsUsages.get(action) != null)
                deepCopyMap.put(action, _actionsUsages.get(action));
        }

        for (var actionKey : deepCopyMap.keySet()) {
            Log.i(TAG, actionKey + " = " + deepCopyMap.get(actionKey));
        }

        return deepCopyMap;
    }
}
