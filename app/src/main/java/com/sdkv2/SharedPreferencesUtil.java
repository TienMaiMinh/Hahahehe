package com.sdkv2;


import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtil {
    private static final String NAME_PREF = "swipe_pref";
    public static final String TAG_ENABLE_FISRT_INSTALLAPP = "TAG_FISRT_INSTALLAPP";
    public static final String TAG_ENABLE_SWIPE = "TAG_ENABLE_SWIPE";
    public static final String TAG_ENABLE_TUTORIAL = "TAG_ENABLE_TUTORIAL";
    public static final String TAG_ENABLE_SOUND = "TAG_ENABLE_SOUND";
    public static final String TAG_ENABLE_INTRO= "TAG_ENABLE_INTRO";
    public static final String TAG_ENABLE_MARKET = "TAG_ENABLE_MARKET";
    public static final String TAG_VALUE_DISTANCE = "TAG_VALUE_DISTANCE";
    public static final String TAG_VALUE_USERID = "TAG_VALUE_USERID";
    public static final String TAG_VALUE_TIME_SHOW_UPDATE="TAG_VALUE_TIME_SHOW_UPDATE";


    /**************************
     * Boloean
     **************************/

    public static void setShowing(Context context, String tag, boolean enable) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences(NAME_PREF, 0).edit();
        localEditor.putBoolean(tag, enable);
        localEditor.commit();
    }
    public static boolean isShowing(Context context, String tag) {
        return context.getSharedPreferences(NAME_PREF, 0).getBoolean(tag, false);
    }


    /**************************
     * String
     **************************/
    public static void setTagValueStr(Context context, String tag, String value) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences(NAME_PREF, 0).edit();
        localEditor.putString(tag, value);
        localEditor.commit();
    }

    public static String getTagValueStr(Context context, String tag) {
        return context.getSharedPreferences(NAME_PREF, 0).getString(tag, "");
    }

    public static String getTagValueStr(Context context, String tag, String defaultParam) {
        return context.getSharedPreferences(NAME_PREF, 0).getString(tag, defaultParam);
    }

    /**************************
     * Integer
     **************************/
    public static void setTagValueInt(Context context, String tag, int value) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences(NAME_PREF, 0).edit();
        localEditor.putInt(tag, value);
        localEditor.commit();
    }

    public static int getTagValueInt(Context context, String tag) {
        return context.getSharedPreferences(NAME_PREF, 0).getInt(tag, 0);
    }

    public static int getTagValueInt(Context context, String tag, int defaultParam) {
        return context.getSharedPreferences(NAME_PREF, 0).getInt(tag, defaultParam);
    }


}
