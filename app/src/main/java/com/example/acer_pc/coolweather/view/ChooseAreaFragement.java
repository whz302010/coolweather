package com.example.acer_pc.coolweather.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer_pc.coolweather.Activity.MainActivity;
import com.example.acer_pc.coolweather.Activity.WeatherActivity;
import com.example.acer_pc.coolweather.R;
import com.example.acer_pc.coolweather.Utils.HttpUtil;
import com.example.acer_pc.coolweather.Utils.Utility;
import com.example.acer_pc.coolweather.db.City;
import com.example.acer_pc.coolweather.db.Country;
import com.example.acer_pc.coolweather.db.Province;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by acer-pc on 2017/5/28.
 */

public class ChooseAreaFragement extends Fragment {
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTRY = 2;
    private TextView tv_title;
    private Button btn_back;
    private ListView lv_area;
    private List<String> datalist = new ArrayList();
    private List<City> cityList;
    private List<Country> countryList;
    private List<Province> provinceList;
    private int currentLevel;
    private Province selectedProvince;
    private City selectedCity;
    private ArrayAdapter<String> adapter;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_back = (Button) view.findViewById(R.id.back_button);
        lv_area = (ListView) view.findViewById(R.id.lv_area);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, datalist);
        lv_area.setAdapter(adapter);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        queryProvinces();
        super.onActivityCreated(savedInstanceState);
        lv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if(currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if (currentLevel==LEVEL_COUNTRY){
                    String weatherId = countryList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getContext(), WeatherActivity.class);
                        intent.putExtra("weatherId",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity= (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swip_refresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }

                }
            }


        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTRY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });

    }

    /**
     * 查询所有县，优先从数据库中查询没有到服务器查询
     */
    private void queryCounties() {
        tv_title.setText(selectedCity.getCityName());
        btn_back.setVisibility(View.VISIBLE);
        countryList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(Country.class);
        if (countryList.size() > 0) {
            datalist.clear();
            for (Country country:countryList) {
                datalist.add(country.countryName);
            }
            adapter.notifyDataSetChanged();
            lv_area.setSelection(0);
            currentLevel = LEVEL_COUNTRY;

        } else {
            int procinceId=selectedProvince.getProvinceId();
            int cityId=selectedCity.getCityId();
            String address = "http://guolin.tech/api/china/"+procinceId+"/"+cityId;
            queryFromServer(address, "country");
        }
    }

    /**
     * 查询所有市，优先从数据库中查询没有到服务器查询
     */
    private void queryCities() {
        tv_title.setText(selectedProvince.getProvinceName());
        btn_back.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceId = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            datalist.clear();
            for (City city:cityList) {
                datalist.add(city.cityName);
            }
            adapter.notifyDataSetChanged();
            lv_area.setSelection(0);
            currentLevel = LEVEL_CITY;

        } else {
            int procinceId=selectedProvince.getProvinceId();
            String address = "http://guolin.tech/api/china/"+procinceId;
            queryFromServer(address, "city");
        }

    }

    /**
     * 查询所有省，优先从数据库中查询没有到服务器查询
     */
    private void queryProvinces() {

        tv_title.setText("中国");
        btn_back.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            datalist.clear();
            for (Province province : provinceList) {
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            lv_area.setSelection(0);
            currentLevel = LEVEL_PROVINCE;

        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    private void queryFromServer(String address, final String type) {
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /**
                 * 从服务器获取数据失败，答应吐司，注意okhttp在子线程里
                 */
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
/**
 * 加载数据成功，将数据放入litepal封装的累中
 */
                showProgressDialog();
                String responseText = response.body().string();
                boolean result = false;
                if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("country".equals(type)) {
                    result = Utility.handleCountryResponse(responseText, selectedCity.getId());

                } else if ("province".equals(type)) {
                    result = Utility.handleProcinceResponse(responseText);
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("country".equals(type)) {
                                queryCounties();
                            } else if ("city".equals(type)) {
                                queryCities();
                            }
                        }
                    });
                }
            }
        });
    }

    private void closeProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("正在加载中。。。。。。。");
            dialog.setCanceledOnTouchOutside(false);
        }

        dialog.show();
    }
}
