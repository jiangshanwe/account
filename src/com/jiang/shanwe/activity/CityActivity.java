package com.jiang.shanwe.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jiang.shanwe.loveaccount.R;
import com.jiang.shanwe.adapter.CityListAdapter;
import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.weather.WeatherData;

public class CityActivity extends Activity {

    private ListView lv_city;
    List<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        getCities();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_city = (ListView) findViewById(R.id.lv_city);
    }

    private void getCities() {
        WeatherData data = WeatherData.getInstance();
        data.getCities(new JsonCallBack() {

            @Override
            public void jsonLoaded(JSONObject json) {
                try {
                    int code = json.getInt("resultcode");
                    int error_code = json.getInt("error_code");
                    if (error_code == 0 && code == 200) {
                        cityList = new ArrayList<String>();
                        JSONArray resultArray = json.getJSONArray("result");
                        Set<String> citySet = new HashSet<String>();
                        for (int i = 0; i < resultArray.length(); i++) {
                            String city = resultArray.getJSONObject(i).getString("city");
                            citySet.add(city);
                        }
                        cityList.addAll(citySet);
                        CityListAdapter adapter = new CityListAdapter(CityActivity.this,
                                cityList);
                        lv_city.setAdapter(adapter);
                        lv_city.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                                Intent intent = new Intent();
                                intent.putExtra("city", cityList.get(position));
                                setResult(1, intent);
                                finish();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        });
    }

}
