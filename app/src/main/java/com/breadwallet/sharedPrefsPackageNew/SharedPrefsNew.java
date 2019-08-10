package com.breadwallet.sharedPrefsPackageNew;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsNew {
    public static final String MY_PREFS_NAME = "WALLET_NEW";

    public static SharedPreferences sharedPreferences;


    public SharedPrefsNew(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);

    }

    public static String getStrings(Context mContext, String key){
        SharedPreferences pref = mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static void putString(Context mContext, String key, String value ){
        SharedPreferences pref= mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putInt(Context mContext, String key, int value ){
        SharedPreferences pref= mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context mContext, String key){
        SharedPreferences pref = mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }





    public static void clear(Context mContext){
        SharedPreferences pref= mContext.getSharedPreferences(MY_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
