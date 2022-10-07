package com.app.habit.helpers;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Keyboard {

    public static void HideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
