package com.example.acer_pc.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.acer_pc.coolweather.Utils.HttpUtil;
import com.example.acer_pc.coolweather.Utils.SpUtil;
import com.example.acer_pc.coolweather.Utils.Utility;
import com.example.acer_pc.coolweather.gson.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 服务中更新数据存储即可
 */
public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                updateWeather();
                updateBingpic();
            }
        }.start();
        //采用android提供的Alarm机制来定时更新,timer不可靠
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int time=8*60*60*1000;
        //系统开机至今时间
        long triggerAtTime= SystemClock.elapsedRealtime()+time;
        Intent intent1 = new Intent(getApplicationContext(), AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent1, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingpic() {
        String requestBingpic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingpic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (responseText!=null){
                    SpUtil.putString(getApplicationContext(),"bing_pic",responseText);

                }
            }
        });
    }

    private void updateWeather() {
        String weather = SpUtil.getString(getApplicationContext(), "weather");
        if (weather != null) {
            Weather weather1 = Utility.handlerWeatherResponse(weather);
            String weatherID = weather1.basic.weatherId;
            String address = "http://guolin.tech/api/weather?cityid=" + weatherID + "&key=879eefe982094a63af50503f1e32bf45";
            HttpUtil.sendOkhttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utility.handlerWeatherResponse(responseText);
                    //注意此处为子线程
                    if (weather != null && "ok".equals(weather.status)) {
                        SpUtil.putString(getApplicationContext(), "weather", responseText);
                    }

                }
            });
        }
    }


}
