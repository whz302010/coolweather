package com.example.acer_pc.coolweather.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.acer_pc.coolweather.R;
import com.example.acer_pc.coolweather.Utils.HttpUtil;
import com.example.acer_pc.coolweather.Utils.SpUtil;
import com.example.acer_pc.coolweather.Utils.Utility;
import com.example.acer_pc.coolweather.gson.Forecast;
import com.example.acer_pc.coolweather.gson.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView sv_weather;
    private TextView tv_title;
    private TextView tv_update_time;
    private TextView tv_degree;
    private TextView tv_weatherinfo;
    private TextView tv_aqi;
    private TextView tv_pm25;
    private TextView tv_comfort;
    private TextView tv_crashCar;
    private TextView tv_sport;
    private ViewGroup ll_forecast;
    private ImageView iv_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置背景与状态栏融合
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        sv_weather = (ScrollView) findViewById(R.id.sv_weather);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_update_time = (TextView) findViewById(R.id.tv_update_time);
        tv_degree = (TextView) findViewById(R.id.tv_temp);
        tv_weatherinfo = (TextView) findViewById(R.id.tv_weather_info);
        tv_aqi = (TextView) findViewById(R.id.tv_aqi);
        tv_pm25 = (TextView) findViewById(R.id.tv_pm25);
        tv_comfort = (TextView) findViewById(R.id.tv_suggestion_comfort);
        tv_crashCar = (TextView) findViewById(R.id.tv_suggestion_carCrash);
        tv_sport = (TextView) findViewById(R.id.tv_suggestion_sport);
        ll_forecast = (ViewGroup) findViewById(R.id.ll_forecast);
        iv_bg = (ImageView) findViewById(R.id.iv_weather_bg);
        String weather = SpUtil.getString(getApplicationContext(), "weather");
        //有缓存加载缓存
        if (weather != null) {
            Weather weather1 = Utility.handlerWeatherResponse(weather);
            showWeatherinfo(weather1);
        } else {
            //从服务器获取数据
            String weatherID = getIntent().getStringExtra("weatherId");
            sv_weather.setVisibility(View.INVISIBLE);
            requestWeather(weatherID);
        }
        String bing_pic = SpUtil.getString(getApplicationContext(), "bing_pic");
        if (bing_pic!=null){
            Glide.with(this).load(bing_pic).into(iv_bg);
        }else {
            loadPic();
        }
    }

    private void loadPic() {
        String address="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                if (responseText!=null){
                    SpUtil.putString(getApplicationContext(),"bing_pic",responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this).load(responseText).into(iv_bg);
                        }
                    });
                }
            }
        });
    }

    private void requestWeather(String weatherID) {
        String address = "http;//guolin.tech/api/weather?cityid=" + weatherID + "&879eefe982094a63af50503f1e32bf45";
        loadPic();
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "从服务器获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handlerWeatherResponse(responseText);
                //注意此处为子线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SpUtil.putString(getApplicationContext(), "weather", responseText);
                            showWeatherinfo(weather);
                        } else {
                            Toast.makeText(getApplicationContext(), "从服务器获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    /**
     *
     * @param weather
     * 展示天清气信息在界面上
     */
    private void showWeatherinfo(Weather weather) {
        tv_update_time.setText(weather.basic.update.updateTime.split(" ")[1]);
        tv_degree.setText(weather.now.temperature);
        tv_weatherinfo.setText(weather.now.more.info);
        //先将之前数据清空
        ll_forecast.removeAllViews();
        for (Forecast forecast:weather.forecastList){
            View view = View.inflate(getApplicationContext(), R.layout.forcast_item_view, null);
//            View inflate = LayoutInflater.from(this).inflate(R.layout.forcast_item_view, ll_forecast);
            TextView tv_date= (TextView) view.findViewById(R.id.tv_forcast_date);
            TextView tv_weatherinfo= (TextView) view.findViewById(R.id.tv_forcast_weather_info);
            TextView tv_max= (TextView) view.findViewById(R.id.tv_forcast_max);
            TextView tv_min= (TextView) view.findViewById(R.id.tv_forcast_min);
            tv_date.setText(forecast.date);
            tv_weatherinfo.setText(forecast.more.info);
            tv_max.setText(forecast.temperature.max);
            tv_min.setText(forecast.temperature.min);
            ll_forecast.addView(view);
        }
        String comfor="舒适度"+weather.suggestion.comfort.info;
        String carCrash="洗车指数"+weather.suggestion.carWarsh.info;
        String sport="运动建议"+weather.suggestion.sport;
        tv_comfort.setText(comfor);
        tv_crashCar.setText(carCrash);
        tv_sport.setText(sport);

        sv_weather.setVisibility(View.VISIBLE);
    }


}
