package com.app.habit.helpers;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


public class ActivityChanger {

    public static void ChangeActivity(Context context, Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }
    public static void ChangeActivityAndFinish(AppCompatActivity activity, Class<?> activityClass) {
        activity.finish();
        ChangeActivity(activity, activityClass);
    }

}
