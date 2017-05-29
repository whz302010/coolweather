package com.example.acer_pc.coolweather.global;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by acer-pc on 2017/5/27.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(context);
    }



}
