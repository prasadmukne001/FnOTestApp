package com.prasadmukne.android.fnotestapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Prasad Mukne on 25-07-2020.
 */
public class SharedPrefUtility {

    public static final String FRAG_STACK = "FRAG_STACK";
    public static final String FRAG_SHA_PREF = "FRAG_DATA";
    private static SharedPrefUtility sharedPrefUtility;
    private SharedPreferences sharedPreferences;

    private SharedPrefUtility(Context context) {
        sharedPreferences = context.getSharedPreferences(FRAG_SHA_PREF, MODE_PRIVATE);
    }

    public static SharedPrefUtility getInstance(Context context) {
        if (null == sharedPrefUtility)
            sharedPrefUtility = new SharedPrefUtility(context);
        return sharedPrefUtility;
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    public boolean putValue(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }
}
