package com.webview.pallav.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    private static String DATE_TIME_KEY  = "dateTimekey";
    private static String TAG = "Helper";

    public static void saveDateTime(Context context) {
        Date date = new Date();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(DATE_TIME_KEY, date.toString());
        editor.apply();
        Log.d(TAG, "saveDateTime: current date and time is" + date.toString());
    }

    public static String getDateTime(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(DATE_TIME_KEY,null);
    }
}
