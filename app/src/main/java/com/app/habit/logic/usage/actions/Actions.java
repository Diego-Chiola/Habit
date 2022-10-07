package com.app.habit.logic.usage.actions;

import com.app.habit.R;

import java.util.HashMap;
import java.util.Map;

public class Actions {

    public static final String DRIVING = "driving";
    public static final String MOVING = "moving";
    public static final String BE_SIT = "be_sit";

    public static final String DRIVING_NAME = "Driving";
    public static final String MOVING_NAME = "Moving";
    public static final String BE_SIT_NAME = "Be sit";

    public static String[] actionsArray = null;
    public static Map<String, Integer> iconMap;
    public static Map<String, String> nameMap;

    static {
        actionsArray = new String[] {
                DRIVING,
                MOVING,
                BE_SIT
        };

        iconMap = new HashMap<>();
        iconMap.put(DRIVING, R.drawable.ic_rounded_car);
        iconMap.put(MOVING, R.drawable.ic_rounded_man_running);
        iconMap.put(BE_SIT, R.drawable.ic_rounded_chair);

        nameMap = new HashMap<>();
        nameMap.put(DRIVING, DRIVING_NAME);
        nameMap.put(MOVING, MOVING_NAME);
        nameMap.put(BE_SIT, BE_SIT_NAME);
    }

    public static final int DRIVING_ICON_ID = R.drawable.ic_rounded_car;
    public static final int MOVING_ICON_ID = R.drawable.ic_rounded_man_running;
    public static final int BE_SIT_ICON_ID = R.drawable.ic_rounded_chair;
}
