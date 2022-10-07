package com.app.habit.logic.usage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class UsagesNotificationChannel {

    public static final String CHANNEL_ID = "usages_services";

    public static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O) {
            NotificationChannel serviceNotification = new NotificationChannel(
                    CHANNEL_ID,
                    "habit channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceNotification);
        }
    }
}
