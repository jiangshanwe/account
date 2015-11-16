package com.jiang.shanwe;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Config {

    public static final String CHARSET = "utf-8";
    public static final String APP_ID = "com.jiang.shanwe.loveaccount";

    public static final String WUHAN_AREA_CODE = "200105";
    public static final String HANGZHOU_AREA_CODE = "210101";

    public static final String GET_WEATHER_CODE_PREFIX_URL = "http://www.weather.com.cn/data/list3/city";
    public static final String GET_WEATHER_INFO_PREFIX_URL = "http://www.weather.com.cn/data/cityinfo/";

    public static final String HE_WEATHER_QUERY_PREFIX_URL = "https://api.heweather.com/x3/weather?cityid=";
    public static final String HE_WEATHER_WUHAN_CITY_ID = "CN101200105";
    public static final String HE_WEATHER_QUERY_LINK_URL = "&key=";
    public static final String HE_WEATHER_KEY = "411e06400af54893bc5b179f3a6ae5cf";

    public static final String HE_WEATHER_KEY_DAILY_FORCAST = "daily_forecast";

    public static final String POINT_HTML_SUFFIX_URL = ".html";
    public static final String POINT_XML_SUFFIX_URL = ".xml";

    public static final String KEY_WEATHERINFO = "weatherinfo";
    public static final String KEY_TEMP1 = "temp1";
    public static final String KEY_TEMP2 = "temp2";
    public static final String KEY_WEATHER_DESCRIPTION = "weather";
    public static final String KEY_LOCATION_DATE = "locationDate";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_PHONE_NUM = "phone";
    public static final String KEY_USER_ID = "userId";

    public static final String EXTRA_KEY_DIARY_CONTENT = "diaryContent";
    public static final String EXTRA_KEY_RECORD_CONTENT_ARRAY = "recordContentArray";
    public static final String EXTRA_KEY_RECORD_ID = "recordId";

    public static final int DB_VALUE_SYSTEM_CREATERID = -1;
    public static final int DB_VALUE_STATUS_DISABLE = 0;
    public static final int DB_VALUE_STATUS_USABLE = 1;

    public static final int DB_VALUE_SYNC_STATUS_NOT = 0;
    public static final int DB_VALUE_SYNC_STATUS_ALREADY = 1;

    public static final int DB_VALUE_TAG_ID_BREAKFAST = 1;
    public static final int DB_VALUE_TAG_ID_LUNCH = 2;
    public static final int DB_VALUE_TAG_ID_DINNER = 3;

    public static final int DEFAULT_EXIST_ID = -1;

    public static final int REQUEST_CODE_DIARY_CONTENT = 1001;
    public static final int REQUEST_CODE_DAILY_RECORD = 1002;
    public static final String LOG_TAG = "ACCOUT_TAG";

    public static String getCachedToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
    }

    public static void cacheToken(Context context, String token) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_TOKEN, token);
        e.commit();
    }

    public static void cachePhoneNum(Context context, String phoneNum) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_PHONE_NUM, phoneNum);
        e.commit();
    }

    public static String getCachePhoneNum(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_PHONE_NUM, null);
    }

    public static void cacheUserId(Context context, int userId) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putInt(KEY_USER_ID, userId);
        e.commit();
    }

    public static int getCacheUserId(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getInt(KEY_USER_ID, -1);
    }

    public static void cacheLocationDate(Context context, Date date) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putLong(KEY_LOCATION_DATE, date.getTime());
        e.commit();
    }

    public static Date getCacheLocationDate(Context context) {
        return new Date(context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getLong(KEY_LOCATION_DATE,
                new Date().getTime()));
    }
}
