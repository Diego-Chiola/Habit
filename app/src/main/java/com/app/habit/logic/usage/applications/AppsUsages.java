package com.app.habit.logic.usage.applications;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.app.habit.helpers.Converter;
import com.app.habit.helpers.Date;

import java.util.HashMap;
import java.util.Map;

public class AppsUsages {

    private UsageStatsManager _usageStatsManager = null;
    private long _usageInterval;

    public AppsUsages(Context context) {
        this(context, 5);
    }

    public AppsUsages(Context context, long usageIntervalAsMs) {
        _usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        set_usageInterval(usageIntervalAsMs);
    }


    public Map<String, UsageStats> getDailyAppsUsage() {

        var startTime  = com.app.habit.helpers.Date.getTime_atMidnight().getTime();
        var endTime = Date.getCurrentTimeDate().getTime();

        var usageStatsMap = _usageStatsManager.queryAndAggregateUsageStats(startTime, endTime);

        return remapQuery(usageStatsMap);
    }


    private Map<String, UsageStats> remapQuery(Map<String, UsageStats> queryResultMap) {

        Map<String, UsageStats> remapUsageStatsMap = new HashMap<>();
        for (String appPackage : ApplicationsPackage.PackageArray) {
            UsageStats stats = queryResultMap.get(appPackage);
            if (stats != null) {
                remapUsageStatsMap.put(appPackage, stats);
            }
        }
        return remapUsageStatsMap;
    }

    public void set_usageInterval(long usageIntervalAsMs) {
        if (usageIntervalAsMs < 0L)
            throw new IllegalArgumentException("usageInterval must be a  positive value");
        _usageInterval = usageIntervalAsMs;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String toString(Map<String, UsageStats> usageStatsMap, Context context) {
        StringBuilder returnString = new StringBuilder();
        for (var usageStat : usageStatsMap.values()) {
            returnString.append(" " + "\n" + usageStat.getPackageName() + "\n");

            var timeConverted = Converter.fromLongToString_TimeElapsed(usageStat.getTotalTimeVisible(), context);
            returnString.append("   time usage: " + timeConverted.getKey() + timeConverted.getValue() + "\n");
            returnString.append("   last time used: " + Converter.fromLongToString_dayHourAndMinutes(usageStat.getLastTimeVisible(), context));
        }
        return returnString.toString();
    }

}
