package com.example.acer_pc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWarsh carWarsh;


    public Sport sport;
    public class Comfort{
        @SerializedName("txt")
        public String info;
    }
    public class CarWarsh{
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
