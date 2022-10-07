package com.app.habit.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.habit.activities.MainActivity;
import com.app.habit.logic.SettingsManager;


public class StartUsageServiceOnPhoneBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            return;

        if(SettingsManager.getUsageServiceSharedPreferences(context).getBoolean(SettingsManager.USAGE_SERVICE_isEnabled, false))
            context.startActivity(new Intent(context, MainActivity.class));
    }
}
