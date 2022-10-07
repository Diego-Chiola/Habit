package com.app.habit.logic;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class SettingsManager {

    private static String USER_INFO = "user_info";
    public static String USER_INFO_fullName = "user_info_fullName";
    public static String USER_INFO_email = "user_info_email";

    private static String FIRST_ENTER = "first_enter";
    public static String FIRST_ENTER_isFirstEnter = "first_enter_isFirstEnter";

    private static String USAGE_SERVICE = "tracker";
    public static String USAGE_SERVICE_isEnabled = "isEnabled";

    public static SharedPreferences getUserSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_INFO,  Context.MODE_PRIVATE);
    }

    public static SharedPreferences getFirstTimeEnterSharedPreferences(Context context) {
        return context.getSharedPreferences(FIRST_ENTER,  Context.MODE_PRIVATE);
    }

    public static SharedPreferences getUsageServiceSharedPreferences(Context context) {
        return context.getSharedPreferences(USAGE_SERVICE,  Context.MODE_PRIVATE);
    }
}
