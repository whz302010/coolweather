package com.example.acer_pc.coolweather.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class HttpUtil {
    public static void sendOkhttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
