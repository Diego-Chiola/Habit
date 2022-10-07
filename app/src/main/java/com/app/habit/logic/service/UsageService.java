package com.app.habit.logic.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.app.habit.R;
import com.app.habit.activities.MainActivity;
import com.app.habit.data.AppDatabaseManager;
import com.app.habit.data.model.Usage;
import com.app.habit.logic.usage.actions.Actions;
import com.app.habit.logic.usage.actions.ActionsUsages;
import com.app.habit.logic.usage.applications.ApplicationsPackage;
import com.app.habit.logic.usage.applications.AppsUsages;
import com.app.habit.logic.usage.UsagesNotificationChannel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsageService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private static final String TAG = UsageService.class.getName();

    // service executor
    private ExecutorService _executorService;
    private static final int THREAD_NUMBER = 10;

    // some timers time in millis
    private static final int TIMER_UPDATE_APPS_USAGE = 5000;  // 5 seconds
    private static final int TIMER_UPDATE_ACTIONS_USAGE = 5000;  // 1        seconds

    // maps in memory for the apps and actions
    private Map<String, UsageStats> _appsUsagesMap;
    private Map<String, Long> _actionsUsagesMap;
    private final Map<String, Long> _databaseDataMap = new HashMap<>();

    private Date _dayOfService;

    // application usage stats  for get the apps info from the system
    private AppsUsages _applicationUsages;
    // New Action usage stats  for get the apps info from the system
    private ActionsUsages _actionUsages;

    // timers used for schedule on the executor
    private Timer _updateAppsTimer;
    private Timer _updateActionsTimer;

    // is service running boolean
    private volatile static boolean isServiceRunning = false;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * As this service is designed the onStartCommand method is called only one time and when the service stops
     * the onDestroy method is called
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand() service " + UsageService.class.getName());

        _dayOfService = com.app.habit.helpers.Date.getTime_atMidnight();

        _executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
        _applicationUsages = new AppsUsages(getBaseContext(), TIMER_UPDATE_APPS_USAGE);
        _actionUsages = new ActionsUsages(getBaseContext());

        _actionUsages.startListenActions();

        _actionsUsagesMap = new HashMap<>();
        _appsUsagesMap = new HashMap<>();

        // Initialize the timers
        _updateAppsTimer = new Timer();
        _updateAppsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                _executorService.execute(() -> updateApplicationUsage());
            }
        }, 0, TIMER_UPDATE_APPS_USAGE);

        _updateActionsTimer = new Timer();
        _updateActionsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                _executorService.execute(() -> updateActionUsage());
            }
        }, 0, TIMER_UPDATE_ACTIONS_USAGE);


        isServiceRunning = true;

        startForeground(1, createServiceNotification());
        return START_REDELIVER_INTENT;
    }

    /**
     * Create the notification for the foreground service
     *
     * @return the notification
     */
    private Notification createServiceNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Builder(this, UsagesNotificationChannel.CHANNEL_ID)
                .setContentTitle(getString(R.string.habit_tracking))
                .setContentText(getString(R.string.habit_working_for_you))
                .setSmallIcon(R.drawable.ic_logo2)
                .setContentIntent(pendingIntent)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stops the timers
        _updateActionsTimer.cancel();
        _updateAppsTimer.cancel();

        _executorService.shutdown();


        // save  the remaining data on database
        new Thread(() -> {
            updateApplicationUsage();
            updateActionUsage();

            _actionUsages.stopListenActions();
            isServiceRunning = false;
        }).start();
    }


    public static boolean isServiceRunning() {
        return isServiceRunning;
    }


    private void updateApplicationUsage() {

        Log.i(TAG, "updateApplicationUsage");

        // get the daily apps usages
        _appsUsagesMap = _applicationUsages.getDailyAppsUsage();

        //Log the apps taken
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.i(TAG, AppsUsages.toString(_appsUsagesMap, getBaseContext()));
        }

        // Save on database
        if (!_appsUsagesMap.isEmpty()) {

            Usage usage = new Usage();
            UsageStats usageStats;

            usage.usageDay = com.app.habit.helpers.Date.getTime_atMidnight();

            usageStats = _appsUsagesMap.get(ApplicationsPackage.INSTAGRAM);
            if (usageStats != null) {
                usage.instagram = usageStats.getTotalTimeInForeground();
                usage.lastTimeUsed_instagram = usageStats.getLastTimeUsed();
            }

            usageStats = _appsUsagesMap.get(ApplicationsPackage.FACEBOOK);
            if (usageStats != null) {
                usage.facebook = usageStats.getTotalTimeInForeground();
                usage.lastTimeUsed_facebook = usageStats.getLastTimeUsed();
            }

            usageStats = _appsUsagesMap.get(ApplicationsPackage.YOUTUBE);
            if (usageStats != null) {
                usage.youtube = usageStats.getTotalTimeInForeground();
                usage.lastTimeUsed_youtube = usageStats.getLastTimeUsed();
            }

            usageStats = _appsUsagesMap.get(ApplicationsPackage.PINTEREST);
            if (usageStats != null) {
                usage.pinterest = usageStats.getTotalTimeInForeground();
                usage.lastTimeUsed_pinterest = usageStats.getLastTimeUsed();
            }

            usageStats = _appsUsagesMap.get(ApplicationsPackage.LINKEDIN);
            if (usageStats != null) {
                usage.linkedin = usageStats.getTotalTimeInForeground();
                usage.lastTimeUsed_linkedin = usageStats.getLastTimeUsed();
            }

            usageStats = _appsUsagesMap.get(ApplicationsPackage.TWITTER);
            if (usageStats != null) {
                usage.twitter = usageStats.getTotalTimeInForeground();
                usage.lastTimeUsed_twitter = usageStats.getLastTimeUsed();
            }

            AppDatabaseManager.getDatabase(getBaseContext())
                    .UsagesDao()
                    .insertOrUpdateApps(usage);
        }

    }


    private void updateActionUsage() {
        Log.i(TAG, "updateActionUsages");

        if (_actionsUsagesMap.isEmpty()) {
            var usage = AppDatabaseManager.getDatabase(getBaseContext())
                    .UsagesDao()
                    .get(_dayOfService);

            Log.i(TAG, "Get last value from database first time: usage null?  " + ((usage == null) ? "yes" : "no"));
            if (usage == null)
                usage = new Usage();

            Log.i(TAG, "first values:\n " + Usage.toString(usage, getBaseContext()));

            _databaseDataMap.put(Actions.DRIVING, usage.driving);
            _databaseDataMap.put(Actions.MOVING, usage.moving);
            _databaseDataMap.put(Actions.BE_SIT, usage.beSit);
        }

        // get the actions data
        _actionsUsagesMap = _actionUsages.getActionsUsage();

        // Save on database
        Usage usage = new Usage();
        Long actionTime;


        usage.usageDay = com.app.habit.helpers.Date.getTime_atMidnight();
        if (_dayOfService.getTime() < usage.usageDay.getTime()){
            usage.usageDay = _dayOfService;
            _actionUsages.resetData();
            _dayOfService = com.app.habit.helpers.Date.getTime_atMidnight();
        }


        actionTime = _actionsUsagesMap.get(Actions.DRIVING);
        if (actionTime != null) {
            usage.driving = actionTime + _databaseDataMap.get(Actions.DRIVING);
        }

        actionTime = _actionsUsagesMap.get(Actions.MOVING);
        if (actionTime != null) {
            usage.moving = actionTime + _databaseDataMap.get(Actions.MOVING);;
        }

        actionTime = _actionsUsagesMap.get(Actions.BE_SIT);
        if (actionTime != null) {
            usage.beSit = actionTime + _databaseDataMap.get(Actions.BE_SIT);;
        }

        Log.i(TAG, "saving: " + Usage.toString(usage, getBaseContext()));

        AppDatabaseManager.getDatabase(getBaseContext())
                .UsagesDao()
                .insertOrUpdateActions(usage);

    }

}
