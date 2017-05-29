package com.example.acer_pc.coolweather.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.acer_pc.coolweather.R;
import com.example.acer_pc.coolweather.Utils.SpUtil;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 2017/5/29 创建Litepal数据库，不知道要不要
        LitePal.getDatabase();
        if (SpUtil.getString(getApplicationContext(), "weather") != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
