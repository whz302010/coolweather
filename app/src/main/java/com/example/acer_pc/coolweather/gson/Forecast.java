package com.example.acer_pc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class Forecast {
    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;

    public class Temperature{
        public String max;
        public String min;
    }
    public class More{
        @SerializedName("text_d")
        public String info;
    }
}
