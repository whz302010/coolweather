package com.example.acer_pc.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class City extends DataSupport {
    public int id;
    public int cityId;
    public String cityName;
    public int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
