package com.app.habit.helpers;

import android.content.Context;

import com.app.habit.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Converter {


    /**
     * Convert a long value time into the duration (first value  is the time , second value the unit  --> hour or minutes)
     * i.g. timeAsMillis = 60000  -- return --> first value: "1"   second value: "min"
     * @param timeAsMillis
     * @return
     */
    public static Map.Entry<String, String> fromLongToString_TimeElapsed(long timeAsMillis, Context context) {

        if (timeAsMillis < 1000)
            return new Map.Entry<String, String>() {
                @Override
                public String getKey() {
                    return context.getString(R.string.not_used);
                }

                @Override
                public String getValue() {
                    return "";
                }

                @Override
                public String setValue(String s) {
                    return null;
                }
            };

        int seconds = (int) (timeAsMillis / 1000);
        if (seconds < 60)
            return new Map.Entry<String, String>() {
                @Override
                public String getKey() {
                    return seconds + "";
                }

                @Override
                public String getValue() {
                    return "seconds";
                }

                @Override
                public String setValue(String s) {
                    return null;
                }
            };

        int minutes = (int) (seconds / 60);
        if (minutes < 60)
            return new Map.Entry<String, String>() {
                @Override
                public String getKey() {
                    return minutes + "";
                }

                @Override
                public String getValue() {
                    return "min";
                }

                @Override
                public String setValue(String s) {
                    return null;
                }
            };

        return new Map.Entry<String, String>() {
            @Override
            public String getKey() {
                return ((int) minutes / 60) + "." + ((int) minutes % 60);
            }

            @Override
            public String getValue() {
                return "h";
            }

            @Override
            public String setValue(String s) {
                return null;
            }
        };
    }

    public static String fromLongToString_dayHourAndMinutes(long  timeAsLong, Context context)  {
        var timeFormatter = new SimpleDateFormat("HH:mm");
        if (timeAsLong ==  0)
            return context.getResources().getString(R.string.not_used);
        return timeFormatter.format(new Date(timeAsLong));
    }
}
