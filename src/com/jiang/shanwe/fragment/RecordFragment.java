package com.jiang.shanwe.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.activity.AddRecordActivity;
import com.jiang.shanwe.activity.EditDiaryActivity;
import com.jiang.shanwe.activity.MainActivity;
import com.jiang.shanwe.activity.MainActivity.FragmentTouchListener;
import com.jiang.shanwe.activity.WeatherActivity;
import com.jiang.shanwe.adapter.CitySpinnerAdapter;
import com.jiang.shanwe.adapter.RecordAdapter;
import com.jiang.shanwe.bean.HoursWeatherBean;
import com.jiang.shanwe.bean.PMBean;
import com.jiang.shanwe.bean.WeatherBean;
import com.jiang.shanwe.db.DBUtil;
import com.jiang.shanwe.loveaccount.R;
import com.jiang.shanwe.model.City;
import com.jiang.shanwe.model.Diary;
import com.jiang.shanwe.model.Record;
import com.jiang.shanwe.model.WeekDesc;
import com.jiang.shanwe.service.WeatherService;
import com.jiang.shanwe.service.WeatherService.OnParserCallBack;
import com.jiang.shanwe.service.WeatherService.WeatherServiceBinder;
import com.jiang.shanwe.util.DateUtil;
import com.jiang.shanwe.util.NumberUtil;

public class RecordFragment extends Fragment implements OnClickListener,
        OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {

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
    private ImageView ivEat;
    private Spinner spCity;

    private ListView lvDiayRecord;
    List<City> cityList;
    private List<Record> dailyRecords = new ArrayList<Record>();
    private RecordAdapter recordAdapter;
    private CitySpinnerAdapter citySpinnerAdapter;

    float xdown = 0, xup = 0; // 记录手指滑动位置

    private Context mContext;
    private WeatherService mService;

    private boolean isFirstLoadWeather = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);
        dbUtil = DBUtil.getInstance(mContext);
        ((MainActivity) mContext).registerFragmentTouchListener(mTouchListener);

        initView();

        recordAdapter = new RecordAdapter(mContext, R.layout.record_list_item,
                dailyRecords);
        cityList = dbUtil.getSortedCities();
        citySpinnerAdapter = new CitySpinnerAdapter(cityList, mContext);

        lvDiayRecord.setAdapter(recordAdapter);
        spCity.setAdapter(citySpinnerAdapter);

        initDailyInfo();
        initService();
        initEvent();

        return view;
    }

    private void initService() {
        Intent intent = new Intent(mContext, WeatherService.class);
        mContext.startService(intent);
        mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService.removeCallBack();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((WeatherServiceBinder) service).getService();
            mService.setCallBack(new OnParserCallBack() {

                @Override
                public void onParserComplete(List<HoursWeatherBean> hoursBeanList,
                        PMBean pmBean, WeatherBean weatherBean) {
                    Log.i(Config.TEST_TAG, "weatherBean:" + weatherBean + ", pmBean:"
                            + pmBean + ", hoursBeanList:" + hoursBeanList);
                    if (weatherBean != null) {
                        tvWeatherDesc.setText(weatherBean.getWeather_str());
                        tvTemp.setText(weatherBean.getTemp());
                        tvCity.setText(weatherBean.getCity());
                        Toast.makeText(mContext, "天气更新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "天气更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mService.getCitysWeather(cityList.get(0).getCityName());
        }
    };

    private void initDailyInfo() {
        refreshDateInfo();
        refreshDailyDiet();
        refreshDailyRecord();
        refreshDairy();
    }

    private void refreshDailyRecord() {
        dailyRecords.clear();
        dailyRecords.addAll(dbUtil.getDailyRecord(Config.getCacheLocationDate(mContext),
                Config.getCacheUserId(mContext)));
        recordAdapter.notifyDataSetChanged();
    }

    private void refreshDairy() {
        Diary todayDiary = dbUtil.getDiary(Config.getCacheLocationDate(mContext),
                Config.getCacheUserId(mContext));
        tvDiary.setText("");
        if (todayDiary != null) {
            tvDiary.setText(todayDiary.getContent());
        }
    }

    private void refreshDailyDiet() {
        tvBreakfast.setText("");
        tvLunch.setText("");
        tvDinner.setText("");
        double diet[] = dbUtil.getDailyDiet(Config.getCacheLocationDate(mContext),
                Config.getCacheUserId(mContext));
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
        calendar.setTime(Config.getCacheLocationDate(mContext));
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
        ivEat = (ImageView) view.findViewById(R.id.iv_eat);
        spCity = (Spinner) view.findViewById(R.id.sp_city);
    }

    private void initEvent() {
        tvCity.setOnClickListener(this);
        tvTemp.setOnClickListener(this);
        tvWeatherDesc.setOnClickListener(this);
        btnRefreshWeather.setOnClickListener(this);
        tvBreakfast.setOnClickListener(this);
        tvLunch.setOnClickListener(this);
        tvDinner.setOnClickListener(this);
        tvDiary.setOnClickListener(this);
        btnAddRecord.setOnClickListener(this);
        lvDiayRecord.setOnItemClickListener(this);
        lvDiayRecord.setOnItemLongClickListener(this);
        ivEat.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        spCity.setOnItemSelectedListener(this);
    }

    private void refreshWeatherInfo() {
        mService.getCitysWeather(citySpinnerAdapter.getItem(0).getCityName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tv_city:
            break;
        case R.id.btn_refresh_weather:
            refreshWeatherInfo();
            break;
        case R.id.tv_temp:
            Intent cityWeatherTmepIntent = new Intent(mContext, WeatherActivity.class);
            startActivity(cityWeatherTmepIntent);
            break;
        case R.id.tv_weather_desc:
            Intent cityWeatherDescIntent = new Intent(mContext, WeatherActivity.class);
            startActivity(cityWeatherDescIntent);
            break;
        case R.id.tv_breakfast:
            final EditText etBreakfastCost = new EditText(this.mContext);
            etBreakfastCost.setFocusable(true);
            etBreakfastCost.setHint("早餐消费");
            etBreakfastCost.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!TextUtils.isEmpty(tvBreakfast.getText())) {
                etBreakfastCost.setText(tvBreakfast.getText());
                etBreakfastCost.setSelection(tvBreakfast.getText().length());
            }
            AlertDialog.Builder builderBreakfast = new AlertDialog.Builder(this.mContext);
            builderBreakfast.setTitle("Breakfast").setIcon(R.drawable.ic_coins_l)
                    .setView(etBreakfastCost).setNegativeButton("Cancel", null);
            builderBreakfast.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String costStr = etBreakfastCost.getText().toString()
                                    .equals("") ? "0" : etBreakfastCost.getText()
                                    .toString();
                            try {
                                dbUtil.saveOrUpdateDiet(Double.parseDouble(costStr),
                                        Config.DB_VALUE_TAG_ID_BREAKFAST,
                                        Config.getCacheLocationDate(mContext),
                                        Config.getCacheUserId(mContext));
                                refreshDailyDiet();
                            } catch (Exception e) {
                                Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
            builderBreakfast.show();
            break;
        case R.id.tv_lunch:
            final EditText etLunchCost = new EditText(this.mContext);
            etLunchCost.setFocusable(true);
            etLunchCost.setHint("中餐消费");
            etLunchCost.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!TextUtils.isEmpty(tvLunch.getText())) {
                etLunchCost.setText(tvLunch.getText());
                etLunchCost.setSelection(tvLunch.getText().length());
            }
            AlertDialog.Builder builderLunch = new AlertDialog.Builder(this.mContext);
            builderLunch.setTitle("Lunch").setIcon(R.drawable.ic_coins_l)
                    .setView(etLunchCost).setNegativeButton("Cancel", null);
            builderLunch.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String costStr = etLunchCost.getText().toString().equals("") ? "0"
                            : etLunchCost.getText().toString();
                    try {
                        dbUtil.saveOrUpdateDiet(Double.parseDouble(costStr),
                                Config.DB_VALUE_TAG_ID_LUNCH,
                                Config.getCacheLocationDate(mContext),
                                Config.getCacheUserId(mContext));
                        refreshDailyDiet();
                    } catch (Exception e) {
                        Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builderLunch.show();
            break;
        case R.id.tv_dinner:
            final EditText etDinnerCost = new EditText(this.mContext);
            etDinnerCost.setFocusable(true);
            etDinnerCost.setHint("晚餐消费");
            etDinnerCost.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!TextUtils.isEmpty(tvDinner.getText())) {
                etDinnerCost.setText(tvDinner.getText());
                etDinnerCost.setSelection(tvDinner.getText().length());
            }
            AlertDialog.Builder builderDinner = new AlertDialog.Builder(this.mContext);
            builderDinner.setTitle("Dinner").setIcon(R.drawable.ic_coins_l)
                    .setView(etDinnerCost).setNegativeButton("Cancel", null);
            builderDinner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String costStr = etDinnerCost.getText().toString().equals("") ? "0"
                            : etDinnerCost.getText().toString();
                    try {
                        dbUtil.saveOrUpdateDiet(Double.parseDouble(costStr),
                                Config.DB_VALUE_TAG_ID_DINNER,
                                Config.getCacheLocationDate(mContext),
                                Config.getCacheUserId(mContext));
                        refreshDailyDiet();
                    } catch (Exception e) {
                        Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builderDinner.show();
            break;
        case R.id.tv_diary:
            Intent intent = new Intent(mContext, EditDiaryActivity.class);
            startActivityForResult(intent, Config.REQUEST_CODE_DIARY_CONTENT);
            break;
        case R.id.btn_add_record:
            Intent addRecordIntent = new Intent(mContext, AddRecordActivity.class);
            startActivityForResult(addRecordIntent, Config.REQUEST_CODE_DAILY_RECORD);
            break;
        case R.id.tv_date:
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Config.getCacheLocationDate(mContext));
            new DatePickerDialog(mContext, new OnDateSetListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                    Config.cacheLocationDate(mContext, new Date(year - 1900, monthOfYear,
                            dayOfMonth));
                    initDailyInfo();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
            break;
        case R.id.iv_eat:
            Config.cacheLocationDate(mContext, new Date());
            initDailyInfo();
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
        Intent updateRecordIntent = new Intent(mContext, AddRecordActivity.class);
        updateRecordIntent.putExtra(Config.EXTRA_KEY_RECORD_ID,
                (int) recordAdapter.getItemId(position));
        startActivityForResult(updateRecordIntent, Config.REQUEST_CODE_DAILY_RECORD);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
            long id) {
        final long recordId = recordAdapter.getItemId(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除").setMessage("确定删除 ?").setCancelable(true)
                .setIcon(android.R.drawable.ic_delete)
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
                if (xup - xdown > 250) {
                    // 向右滑动,时间往前一天
                    Config.cacheLocationDate(mContext, DateUtil.getPreviousDay(Config
                            .getCacheLocationDate(mContext)));
                    initDailyInfo();
                }
                if (xdown - xup > 250) {
                    // 向左滑动,时间往后一天
                    Config.cacheLocationDate(mContext,
                            DateUtil.getNextDay(Config.getCacheLocationDate(mContext)));
                    initDailyInfo();
                }
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(citySpinnerAdapter.getItemId(position));
        if (!isFirstLoadWeather) {
            dbUtil.selectCity(citySpinnerAdapter.getItemId(position));
        }
        isFirstLoadWeather = false;
        if (citySpinnerAdapter.getItem(position).getId() == -1) {
            final EditText etCityName = new EditText(this.mContext);
            etCityName.setFocusable(true);
            etCityName.setHint("请输入城市");
            AlertDialog.Builder builderDinner = new AlertDialog.Builder(this.mContext);
            builderDinner.setTitle("添加城市").setView(etCityName)
                    .setNegativeButton("Cancel", null);
            builderDinner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cityName = etCityName.getText().toString();
                    int result = dbUtil.addCity(cityName,
                            Config.DB_VALUE_SYSTEM_CREATERID);
                    if (result == -1) {
                        // 已存在
                        Toast.makeText(mContext, "该城市已存在", Toast.LENGTH_SHORT).show();
                    }
                    cityList = dbUtil.getSortedCities();
                    citySpinnerAdapter.notifyDataSetChanged();
                    spCity.setSelection(0);
                }
            });
            builderDinner.show();
        } else {
            if (mService != null) {
                mService.getCitysWeather(citySpinnerAdapter.getItem(position)
                        .getCityName());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unbindService(conn);
    }

}
