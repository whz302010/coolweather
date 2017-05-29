package com.example.acer_pc.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class Country extends DataSupport {
    public int id;
    public String weatherId;
    public int cityId;
    public String countryName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
