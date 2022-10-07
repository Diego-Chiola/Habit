package com.app.habit.logic;

import android.app.Application;

import com.app.habit.logic.usage.UsagesNotificationChannel;
import com.app.habit.logic.user.UserManager;

public class App extends Application {

    public App() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();


        UserManager.initialize(this);
        UsagesNotificationChannel.createNotificationChannel(this);
    }
}
