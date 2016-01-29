package com.jiang.shanwe.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiang.shanwe.loveaccount.R;
import com.jiang.shanwe.model.City;

public class CitySpinnerAdapter extends BaseAdapter {

    private List<City> cityList;
    private LayoutInflater mInflater;

    public CitySpinnerAdapter(List<City> cityList, Context mContext) {
        super();
        cityList.add(new City(-1, " ＋ 增加城市"));
        this.cityList = cityList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public City getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cityList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        if (convertView == null) {
            rowView = mInflater.inflate(R.layout.select_city_list, null);
        } else {
            rowView = convertView;
        }
        TextView tv_city = (TextView) rowView.findViewById(R.id.tv_city);
        tv_city.setText(getItem(position).getCityName());
        return rowView;
    }

}
