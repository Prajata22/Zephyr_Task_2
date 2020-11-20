package com.applex.zephyr_task_2.Preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class IntroPref {

    private SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private static final String PREF_NAME = "com.applex.miskaa";
    private static final String ORDER_BY = "order_by";

    @SuppressLint("CommitPrefEdits")
    public IntroPref(Context context) {
        if(context != null) {
            preferences = context.getSharedPreferences(PREF_NAME, 0);
        }
        editor = preferences.edit();
    }

    public void setSortOrder(int order) {
        editor.putInt(ORDER_BY, order);
        editor.apply();
    }

    public int getSortOrder() { return preferences.getInt(ORDER_BY, 0); }
}