package com.example.acer_pc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public  Suggestion suggestion;

    @SerializedName("daily_forcast")
    public List<Forecast> forecastList;
}
