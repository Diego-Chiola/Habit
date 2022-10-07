package com.app.habit.logic.user;

import android.content.ContextWrapper;

import com.app.habit.logic.SettingsManager;

public class UserManager {

    private static UserManager _instance = null;
    private static ContextWrapper _contextWrapper = null;

    private static String _fullName;
    private static String _email;

    private UserManager() {
        if (_contextWrapper == null)
            throw new NullPointerException("user manager need to be initialized ");

        update();
    }

    public static void initialize(ContextWrapper contextWrapper)  {
        _contextWrapper = contextWrapper;
    }

    public static UserManager get() {
        if (_instance == null)
            _instance = new UserManager();
        return _instance;
    }

    /**
     * Getter and Setter methods
     */
    public String get_fullName() { return _fullName; }
    public String get_email() { return _email; }

    /**
     * Update the values of the username
     */
    public void update() {
        _fullName = SettingsManager.getUserSharedPreferences(_contextWrapper).getString(SettingsManager.USER_INFO_fullName, "name error");
        _email = SettingsManager.getUserSharedPreferences(_contextWrapper).getString(SettingsManager.USER_INFO_email, "email error");
    }
}
