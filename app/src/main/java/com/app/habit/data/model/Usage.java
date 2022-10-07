package com.app.habit.data.model;


import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.app.habit.helpers.Converter;
import com.app.habit.logic.usage.actions.Actions;
import com.app.habit.logic.usage.applications.ApplicationsPackage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "usages")
public class Usage {


    @PrimaryKey
    @ColumnInfo(name = "usage_day")
    public Date  usageDay;

    // fields for usages time as floats
    @ColumnInfo(defaultValue = "0")
    public long instagram;
    @ColumnInfo(defaultValue = "0")
    public long lastTimeUsed_instagram;

    @ColumnInfo(defaultValue = "0")
    public long facebook;
    @ColumnInfo(defaultValue = "0")
    public long lastTimeUsed_facebook;

    @ColumnInfo(defaultValue = "0")
    public long youtube;
    @ColumnInfo(defaultValue = "0")
    public long lastTimeUsed_youtube;

    @ColumnInfo(defaultValue = "0")
    public long pinterest;
    @ColumnInfo(defaultValue = "0")
    public long lastTimeUsed_pinterest;

    @ColumnInfo(defaultValue = "0")
    public long linkedin;
    @ColumnInfo(defaultValue = "0")
    public long lastTimeUsed_linkedin;

    @ColumnInfo(defaultValue = "0")
    public long twitter;
    @ColumnInfo(defaultValue = "0")
    public long lastTimeUsed_twitter;



    @ColumnInfo(defaultValue = "0")
    public long moving;

    @ColumnInfo(defaultValue = "0")
    public long driving;

    @ColumnInfo(defaultValue = "0")
    public long beSit;


    public static Usage emptyUsage() {
        var usage = new Usage();
        usage.usageDay = new Date(0);
        usage.instagram = 0;
        usage.lastTimeUsed_instagram = 0;
        usage.facebook = 0;
        usage.lastTimeUsed_facebook = 0;
        usage.youtube = 0;
        usage.lastTimeUsed_youtube = 0;
        usage.pinterest = 0;
        usage.lastTimeUsed_pinterest = 0;
        usage.linkedin = 0;
        usage.lastTimeUsed_linkedin = 0;
        usage.twitter = 0;
        usage.lastTimeUsed_twitter = 0;
        usage.driving = 0;
        usage.moving = 0;
        usage.beSit = 0;

        return usage;
    }

    public static String toString(Usage usage, Context context) {
        if (usage  == null)
            return "usage is null";

        StringBuilder returnString =  new StringBuilder();
        returnString.append(" " + "\n" + "usage day = " + usage.usageDay + "\n");

        Map.Entry<String, String> timeUsage;
        timeUsage = Converter.fromLongToString_TimeElapsed(usage.instagram, context);
        returnString.append("   instagram  ------------------------------ " + "\n");
        returnString.append("       usage : " + timeUsage.getKey() + timeUsage.getValue() + "\n");
        returnString.append("       opened: " + Converter.fromLongToString_dayHourAndMinutes(usage.lastTimeUsed_instagram, context) + "\n");

        timeUsage = Converter.fromLongToString_TimeElapsed(usage.facebook, context);
        returnString.append("   facebook:  ------------------------------" + "\n");
        returnString.append("       usage : " + timeUsage.getKey() + timeUsage.getValue() + "\n");
        returnString.append("       opened: " + Converter.fromLongToString_dayHourAndMinutes(usage.lastTimeUsed_facebook, context) + "\n");

        timeUsage = Converter.fromLongToString_TimeElapsed(usage.youtube, context);
        returnString.append("   youtube:  ------------------------------" + "\n");
        returnString.append("       usage : " + timeUsage.getKey() + timeUsage.getValue() + "\n");
        returnString.append("       opened: " + Converter.fromLongToString_dayHourAndMinutes(usage.lastTimeUsed_youtube, context) + "\n");

        timeUsage = Converter.fromLongToString_TimeElapsed(usage.pinterest, context);
        returnString.append("   pinterest: ------------------------------" + "\n");
        returnString.append("       usage : " + timeUsage.getKey() + timeUsage.getValue() + "\n");
        returnString.append("       opened: " + Converter.fromLongToString_dayHourAndMinutes(usage.lastTimeUsed_pinterest, context) + "\n");

        timeUsage = Converter.fromLongToString_TimeElapsed(usage.linkedin, context);
        returnString.append("   linkedin:    pinterest: ------------------------------" + "\n");
        returnString.append("       usage : " + timeUsage.getKey() + timeUsage.getValue() + "\n");
        returnString.append("       opened: " + Converter.fromLongToString_dayHourAndMinutes(usage.lastTimeUsed_linkedin, context) + "\n");

        timeUsage = Converter.fromLongToString_TimeElapsed(usage.twitter, context);
        returnString.append("   twitter:    pinterest: ------------------------------" + "\n");
        returnString.append("       usage : " + timeUsage.getKey() + timeUsage.getValue() + "\n");
        returnString.append("       opened: " + Converter.fromLongToString_dayHourAndMinutes(usage.lastTimeUsed_twitter, context) + "\n\n");



        timeUsage = Converter.fromLongToString_TimeElapsed(usage.driving, context);
        returnString.append("   driving: " + timeUsage.getKey() + timeUsage.getValue() + "\n");

        timeUsage = Converter.fromLongToString_TimeElapsed(usage.moving, context);
        returnString.append("   moving: " + timeUsage.getKey() + timeUsage.getValue() + "\n");

        timeUsage = Converter.fromLongToString_TimeElapsed(usage.beSit, context);
        returnString.append("   beSit: " + timeUsage.getKey() + timeUsage.getValue());

        return returnString.toString();
    }

    public static Map<String, Long> toMapUsages(Usage usage) {
        var returnMap = new HashMap<String, Long>();

        if (usage != null) {
            returnMap.put(ApplicationsPackage.INSTAGRAM, usage.instagram);
            returnMap.put(ApplicationsPackage.FACEBOOK, usage.facebook);
            returnMap.put(ApplicationsPackage.YOUTUBE, usage.youtube);
            returnMap.put(ApplicationsPackage.PINTEREST, usage.pinterest);
            returnMap.put(ApplicationsPackage.LINKEDIN, usage.linkedin);
            returnMap.put(ApplicationsPackage.TWITTER, usage.twitter);

            returnMap.put(Actions.DRIVING, usage.driving);
            returnMap.put(Actions.MOVING, usage.moving);
            returnMap.put(Actions.BE_SIT, usage.beSit);

        } else {
            returnMap.put(ApplicationsPackage.INSTAGRAM, 0L);
            returnMap.put(ApplicationsPackage.FACEBOOK, 0L);
            returnMap.put(ApplicationsPackage.YOUTUBE, 0L);
            returnMap.put(ApplicationsPackage.PINTEREST, 0L);
            returnMap.put(ApplicationsPackage.LINKEDIN, 0L);
            returnMap.put(ApplicationsPackage.TWITTER, 0L);

            returnMap.put(Actions.DRIVING, 0L);
            returnMap.put(Actions.MOVING, 0L);
            returnMap.put(Actions.BE_SIT, 0L);
        }
        return returnMap;
    }
}
