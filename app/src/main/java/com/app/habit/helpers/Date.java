package com.app.habit.helpers;

import android.util.Log;

import java.util.Calendar;
import java.util.Map;

public class Date {

    public static java.util.Date getTime_atMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        return calendar.getTime();
    }

    public static java.util.Date getTime_atEndOfTheCurrentDay()  {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR, 23);
        return calendar.getTime();
    }

    public static Map.Entry<java.util.Date, java.util.Date> getTimeInterval(float minInterval) {
        if (minInterval < 0)
            throw new IllegalArgumentException("minInterval must be a positive number");

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarStart.setTimeInMillis(calendarEnd.getTimeInMillis() - 60000L);

        return Map.entry(calendarStart.getTime(), calendarEnd.getTime());
    }

    public static java.util.Date setHourMinSecMill_At0(long timeInMillis) {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        return calendar.getTime();
    }

    public static java.util.Date getPreviousDay(long timeInMillis) {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        return calendar.getTime();
    }

    public static java.util.Date getNextDay(long timeInMillis) {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        return calendar.getTime();
    }

    public static java.util.Date getCurrentTimeDate()  {
        return Calendar.getInstance().getTime();
    }

    public static int[] getDayMonthYear(long timeInMillis) {
        var dayMonthYear = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        dayMonthYear[0] = calendar.get(Calendar.DAY_OF_MONTH);
        dayMonthYear[1] = calendar.get(Calendar.MONTH);
        dayMonthYear[2] = calendar.get(Calendar.YEAR);
        return dayMonthYear;
    }

    public static java.util.Date removeDayFromTime(long dateInMillis, int dayToRemove) {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, - dayToRemove);
        return calendar.getTime();
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

}
