package com.example.acer_pc.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class Province extends DataSupport {
    public int id;
    public int provinceId;

    public String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
