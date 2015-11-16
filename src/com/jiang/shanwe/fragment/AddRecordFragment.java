package com.jiang.shanwe.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.activity.AddRecordActivity;
import com.jiang.shanwe.activity.EditDiaryActivity;
import com.jiang.shanwe.activity.MainActivity;
import com.jiang.shanwe.activity.MainActivity.FragmentTouchListener;
import com.jiang.shanwe.db.DBUtil;
import com.jiang.shanwe.model.Diary;
import com.jiang.shanwe.model.Record;
import com.jiang.shanwe.model.WeekDesc;
import com.jiang.shanwe.net.HttpCallbackListener;
import com.jiang.shanwe.net.HttpMethod;
import com.jiang.shanwe.net.HttpUtil;
import com.jiang.shanwe.uidesign.R;
import com.jiang.shanwe.util.DateUtil;
import com.jiang.shanwe.util.NumberUtil;

public class AddRecordFragment extends Fragment implements OnClickListener, OnItemClickListener,
        OnItemLongClickListener {

    private DBUtil dbUtil;

    private View view;

    private TextView tvDay;
    private TextView tvDate;
    private TextView tvCity;
    private TextView tvWeatherDesc;
    private TextView tvTemp;
    private Button btnRefreshWeather;
    private TextView tvBreakfast;
    private TextView tvLunch;
    private TextView tvDinner;
    private TextView tvDiary;
    private Button btnAddRecord;

    private ListView lvDiayRecord;
    private List<Record> dailyRecords = new ArrayList<Record>();
    private RecordAdapter recordAdapter;

    private String wuhanWeatherTemp = "";
    private String wuhanWeatherDesc = "";

    float xdown = 0, xup = 0; // 记录手指滑动位置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_record, container, false);
        dbUtil = DBUtil.getInstance(getActivity());
        ((MainActivity) getActivity()).registerFragmentTouchListener(mTouchListener);

        initView();
        initEvent();

        recordAdapter = new RecordAdapter(getActivity(), R.layout.record_list_item, dailyRecords);
        lvDiayRecord.setAdapter(recordAdapter);

        initDailyInfo();

        return view;
    }

    private void initDailyInfo() {
        refreshDateInfo();
        // refreshWeatherInfo();
        refreshDailyDiet();
        refreshDailyRecord();
        refreshDairy();
    }

    private void refreshDailyRecord() {
        dailyRecords.clear();
        dailyRecords.addAll(dbUtil.getDailyRecord(Config.getCacheLocationDate(getActivity()),
                Config.getCacheUserId(getActivity())));
        recordAdapter.notifyDataSetChanged();
    }

    private void refreshDairy() {
        Diary todayDiary = dbUtil.getDiary(Config.getCacheLocationDate(getActivity()),
                Config.getCacheUserId(getActivity()));
        tvDiary.setText("");
        if (todayDiary != null) {
            tvDiary.setText(todayDiary.getContent());
        }
    }

    private void refreshDailyDiet() {
        tvBreakfast.setText("");
        tvLunch.setText("");
        tvDinner.setText("");
        double diet[] = dbUtil.getDailyDiet(Config.getCacheLocationDate(getActivity()),
                Config.getCacheUserId(getActivity()));
        if (diet[0] != 0) {
            tvBreakfast.setText(NumberUtil.getSimpleDouble(diet[0]));
        }
        if (diet[1] != 0) {
            tvLunch.setText(NumberUtil.getSimpleDouble(diet[1]));
        }
        if (diet[2] != 0) {
            tvDinner.setText(NumberUtil.getSimpleDouble(diet[2]));
        }
    }

    private void refreshDateInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Config.getCacheLocationDate(getActivity()));
        String day = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case 1:
            day = WeekDesc.Sun.toString();
            break;
        case 2:
            day = WeekDesc.Mon.toString();
            break;
        case 3:
            day = WeekDesc.Tue.toString();
            break;
        case 4:
            day = WeekDesc.Wed.toString();
            break;
        case 5:
            day = WeekDesc.Thu.toString();
            break;
        case 6:
            day = WeekDesc.Fri.toString();
            break;
        case 7:
            day = WeekDesc.Sat.toString();
            break;
        default:
            break;
        }
        tvDay.setText(day);
        tvDate.setText(calendar.get(Calendar.DATE) + "");
    }

    private void initView() {
        tvDay = (TextView) view.findViewById(R.id.tv_day);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvCity = (TextView) view.findViewById(R.id.tv_city);
        tvWeatherDesc = (TextView) view.findViewById(R.id.tv_weather_desc);
        tvTemp = (TextView) view.findViewById(R.id.tv_temp);
        btnRefreshWeather = (Button) view.findViewById(R.id.btn_refresh_weather);
        tvBreakfast = (TextView) view.findViewById(R.id.tv_breakfast);
        tvLunch = (TextView) view.findViewById(R.id.tv_lunch);
        tvDinner = (TextView) view.findViewById(R.id.tv_dinner);
        tvDiary = (TextView) view.findViewById(R.id.tv_diary);
        btnAddRecord = (Button) view.findViewById(R.id.btn_add_record);
        lvDiayRecord = (ListView) view.findViewById(R.id.lv_today_record);

    }

    private void initEvent() {
        tvCity.setOnClickListener(this);
        btnRefreshWeather.setOnClickListener(this);
        tvBreakfast.setOnClickListener(this);
        tvLunch.setOnClickListener(this);
        tvDinner.setOnClickListener(this);
        tvDiary.setOnClickListener(this);
        btnAddRecord.setOnClickListener(this);
        lvDiayRecord.setOnItemClickListener(this);
        lvDiayRecord.setOnItemLongClickListener(this);

        tvDate.setOnClickListener(this);
    }

    @SuppressWarnings("unused")
    private void refreshWeatherInfo() {

        /*       HttpUtil.sendHttpRequest(Config.HE_WEATHER_QUERY_PREFIX_URL + Config.HE_WEATHER_WUHAN_CITY_ID
                       + Config.HE_WEATHER_QUERY_LINK_URL + Config.HE_WEATHER_KEY, HttpMethod.GET, new HttpCallbackListener() {

                   @Override
                   public void onFinish(String response) {
                       try {
                           JSONObject responseJsonObject = new JSONObject(response);
                           JSONArray hds3 = responseJsonObject.getJSONArray("HeWeather data service 3.0");
                           JSONObject dailyForecasJsonObject = hds3.getJSONObject(0);
                           JSONArray dfJsonObject = dailyForecasJsonObject.getJSONArray("daily_forecast");
                           JSONObject temp = dfJsonObject.getJSONObject(2);
                           System.out.println(dailyForecasJsonObject.toString());
                       } catch (JSONException e) {
                           e.printStackTrace();
                           System.out.println(e);
                           Toast.makeText(getActivity(), "天气更新失败", Toast.LENGTH_SHORT).show();
                       }
                   }
                   @Override
                   public void onError() {
                       Toast.makeText(getActivity(), "天气更新失败", Toast.LENGTH_SHORT).show();
                   }
               }, "");
         */

        HttpUtil.sendHttpRequest(Config.GET_WEATHER_CODE_PREFIX_URL + Config.HANGZHOU_AREA_CODE
                + Config.POINT_XML_SUFFIX_URL, HttpMethod.GET, new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                if (response != null && !response.equals("")) {
                    HttpUtil.sendHttpRequest(
                            Config.GET_WEATHER_INFO_PREFIX_URL + response.substring(response.indexOf("|") + 1)
                                    + Config.POINT_HTML_SUFFIX_URL, HttpMethod.POST, new HttpCallbackListener() {

                                @Override
                                public void onFinish(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject weatherInfo = jsonObject.getJSONObject(Config.KEY_WEATHERINFO);
                                        wuhanWeatherDesc = weatherInfo.getString(Config.KEY_WEATHER_DESCRIPTION);
                                        wuhanWeatherTemp = weatherInfo.getString(Config.KEY_TEMP1) + "~"
                                                + weatherInfo.getString(Config.KEY_TEMP2);
                                        tvWeatherDesc.setText(wuhanWeatherDesc);
                                        tvTemp.setText(wuhanWeatherTemp);
                                        Toast.makeText(getActivity(), "天气更新成功", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "天气更新失败", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(getActivity(), "天气更新失败", Toast.LENGTH_SHORT).show();
                                }
                            }, "");
                }
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "天气更新失败", Toast.LENGTH_SHORT).show();
            }
        }, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tv_city:
            break;
        case R.id.btn_refresh_weather:
            // refreshWeatherInfo();
            break;
        case R.id.tv_breakfast:
            final EditText etBreakfastCost = new EditText(this.getActivity());
            etBreakfastCost.setFocusable(true);
            etBreakfastCost.setHint("早餐消费");
            etBreakfastCost.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!TextUtils.isEmpty(tvBreakfast.getText())) {
                etBreakfastCost.setText(tvBreakfast.getText());
                etBreakfastCost.setSelection(tvBreakfast.getText().length());
            }
            AlertDialog.Builder builderBreakfast = new AlertDialog.Builder(this.getActivity());
            builderBreakfast.setTitle("Breakfast").setIcon(R.drawable.ic_coins_l).setView(etBreakfastCost)
                    .setNegativeButton("Cancel", null);
            builderBreakfast.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String costStr = etBreakfastCost.getText().toString().equals("") ? "0" : etBreakfastCost.getText()
                            .toString();
                    try {
                        dbUtil.saveOrUpdateDiet(Double.parseDouble(costStr), Config.DB_VALUE_TAG_ID_BREAKFAST,
                                Config.getCacheLocationDate(getActivity()), Config.getCacheUserId(getActivity()));
                        refreshDailyDiet();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builderBreakfast.show();
            break;
        case R.id.tv_lunch:
            final EditText etLunchCost = new EditText(this.getActivity());
            etLunchCost.setFocusable(true);
            etLunchCost.setHint("中餐消费");
            etLunchCost.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!TextUtils.isEmpty(tvLunch.getText())) {
                etLunchCost.setText(tvLunch.getText());
                etLunchCost.setSelection(tvLunch.getText().length());
            }
            AlertDialog.Builder builderLunch = new AlertDialog.Builder(this.getActivity());
            builderLunch.setTitle("Lunch").setIcon(R.drawable.ic_coins_l).setView(etLunchCost)
                    .setNegativeButton("Cancel", null);
            builderLunch.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String costStr = etLunchCost.getText().toString().equals("") ? "0" : etLunchCost.getText()
                            .toString();
                    try {
                        dbUtil.saveOrUpdateDiet(Double.parseDouble(costStr), Config.DB_VALUE_TAG_ID_LUNCH,
                                Config.getCacheLocationDate(getActivity()), Config.getCacheUserId(getActivity()));
                        refreshDailyDiet();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builderLunch.show();
            break;
        case R.id.tv_dinner:
            final EditText etDinnerCost = new EditText(this.getActivity());
            etDinnerCost.setFocusable(true);
            etDinnerCost.setHint("晚餐消费");
            etDinnerCost.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!TextUtils.isEmpty(tvDinner.getText())) {
                etDinnerCost.setText(tvDinner.getText());
                etDinnerCost.setSelection(tvDinner.getText().length());
            }
            AlertDialog.Builder builderDinner = new AlertDialog.Builder(this.getActivity());
            builderDinner.setTitle("Dinner").setIcon(R.drawable.ic_coins_l).setView(etDinnerCost)
                    .setNegativeButton("Cancel", null);
            builderDinner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String costStr = etDinnerCost.getText().toString().equals("") ? "0" : etDinnerCost.getText()
                            .toString();
                    try {
                        dbUtil.saveOrUpdateDiet(Double.parseDouble(costStr), Config.DB_VALUE_TAG_ID_DINNER,
                                Config.getCacheLocationDate(getActivity()), Config.getCacheUserId(getActivity()));
                        refreshDailyDiet();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builderDinner.show();
            break;
        case R.id.tv_diary:
            Intent intent = new Intent(getActivity(), EditDiaryActivity.class);
            startActivityForResult(intent, Config.REQUEST_CODE_DIARY_CONTENT);
            break;
        case R.id.btn_add_record:
            Intent addRecordIntent = new Intent(getActivity(), AddRecordActivity.class);
            startActivityForResult(addRecordIntent, Config.REQUEST_CODE_DAILY_RECORD);
            break;
        case R.id.tv_date:
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Config.getCacheLocationDate(getActivity()));
            new DatePickerDialog(getActivity(), new OnDateSetListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Config.cacheLocationDate(getActivity(), new Date(year - 1900, monthOfYear, dayOfMonth));
                    initDailyInfo();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            break;
        default:
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case Config.REQUEST_CODE_DIARY_CONTENT:
            if (resultCode == Activity.RESULT_OK) {
                refreshDairy();
            }
            break;
        case Config.REQUEST_CODE_DAILY_RECORD:
            if (resultCode == Activity.RESULT_OK) {
                refreshDailyRecord();
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent updateRecordIntent = new Intent(getActivity(), AddRecordActivity.class);
        updateRecordIntent.putExtra(Config.EXTRA_KEY_RECORD_ID, (int) recordAdapter.getItemId(position));
        startActivityForResult(updateRecordIntent, Config.REQUEST_CODE_DAILY_RECORD);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final long recordId = recordAdapter.getItemId(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除").setMessage("确定删除 ?").setCancelable(true).setIcon(android.R.drawable.ic_delete)
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbUtil.deleteRecord(recordId);
                        dailyRecords.remove(recordAdapter.getItem(position));
                        recordAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("保留", null).create();
        builder.show();
        return false;
    }

    /**
    * Fragment中，注册
    * 接收MainActivity的Touch回调的对象
    * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
    */
    private MainActivity.FragmentTouchListener mTouchListener = new FragmentTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                xdown = event.getX();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                xup = event.getX();
                Log.d(Config.LOG_TAG, xdown + ":DOWN");
                Log.d(Config.LOG_TAG, xup + ":UP");
                if (xup - xdown > 350) {
                    // 想右滑动,时间往前一天
                    Config.cacheLocationDate(getActivity(),
                            DateUtil.getPreviousDate(Config.getCacheLocationDate(getActivity())));
                    initDailyInfo();
                }
                if (xdown - xup > 350) {
                    // 想左滑动,时间往后一天
                    Config.cacheLocationDate(getActivity(),
                            DateUtil.getNextDate(Config.getCacheLocationDate(getActivity())));
                    initDailyInfo();
                }
            }
        }
    };

}
