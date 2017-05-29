package com.example.acer_pc.coolweather.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class SpUtil {
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, null);

    }
}
