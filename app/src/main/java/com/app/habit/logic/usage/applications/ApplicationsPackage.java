package com.app.habit.logic.usage.applications;

import com.app.habit.R;

import java.util.HashMap;
import java.util.Map;

public class ApplicationsPackage {

    public static final String INSTAGRAM = "com.instagram.android";
    public static final String FACEBOOK = "com.facebook.katana";
    public static final String YOUTUBE = "com.google.android.youtube";
    public static final String PINTEREST = "com.pinterest";
    public static final String LINKEDIN = "com.linkedin.android";
    public static final String TWITTER = "com.twitter.android";

    public static final String INSTAGRAM_NAME = "Instagram";
    public static final String FACEBOOK_NAME = "Facebook";
    public static final String YOUTUBE_NAME = "Youtube";
    public static final String PINTEREST_NAME = "Pinterest";
    public static final String LINKEDIN_NAME = "Linkedin";
    public static final String TWITTER_NAME = "Twitter";

    public static String[] PackageArray = null;
    public static Map<String, Integer> iconMap;
    public static Map<String, String> nameMap;
    public static Map<String, String> packageMap;

    static {
        PackageArray = new String[] {
                INSTAGRAM,
                FACEBOOK,
                YOUTUBE,
                PINTEREST,
                LINKEDIN,
                TWITTER
        };

        iconMap = new HashMap<>();
        iconMap.put(INSTAGRAM, R.drawable.ic_rounded_instagram);
        iconMap.put(FACEBOOK, R.drawable.ic_rounded_facebook);
        iconMap.put(YOUTUBE, R.drawable.ic_rounded_youtube);
        iconMap.put(PINTEREST, R.drawable.ic_rounded_pinterest);
        iconMap.put(LINKEDIN, R.drawable.ic_rounded_linkedin);
        iconMap.put(TWITTER, R.drawable.ic_rounded_twitter);

        nameMap = new HashMap<>();
        nameMap.put(INSTAGRAM, INSTAGRAM_NAME);
        nameMap.put(FACEBOOK, FACEBOOK_NAME);
        nameMap.put(YOUTUBE, YOUTUBE_NAME);
        nameMap.put(PINTEREST, PINTEREST_NAME);
        nameMap.put(LINKEDIN, LINKEDIN_NAME);
        nameMap.put(TWITTER, TWITTER_NAME);

        packageMap = new HashMap<>();
        packageMap.put(INSTAGRAM_NAME, INSTAGRAM);
        packageMap.put(FACEBOOK_NAME, FACEBOOK);
        packageMap.put(YOUTUBE_NAME, YOUTUBE);
        packageMap.put(PINTEREST_NAME, PINTEREST);
        packageMap.put(LINKEDIN_NAME, LINKEDIN);
        packageMap.put(TWITTER_NAME,  TWITTER);
    }

    public static final int INSTAGRAM_ICON_ID = R.drawable.ic_rounded_instagram;
    public static final int FACEBOOK_ICON_ID = R.drawable.ic_rounded_facebook;
    public static final int YOUTUBE_ICON_ID = R.drawable.ic_rounded_youtube;
    public static final int PINTEREST_ICON_ID = R.drawable.ic_rounded_pinterest;
    public static final int LINKEDIN_ICON_ID = R.drawable.ic_rounded_linkedin;
    public static final int TWITTER_ICON_ID = R.drawable.ic_rounded_twitter;
}
