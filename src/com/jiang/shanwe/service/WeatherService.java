package com.jiang.shanwe.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jiang.shanwe.bean.FutureWeatherBean;
import com.jiang.shanwe.bean.HoursWeatherBean;
import com.jiang.shanwe.bean.PMBean;
import com.jiang.shanwe.bean.WeatherBean;
import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.air.AirData;
import com.thinkland.juheapi.data.weather.WeatherData;

public class WeatherService extends Service {

    private WeatherServiceBinder binder = new WeatherServiceBinder();

    private boolean isRunning = false;
    private int count = 0;

    private String city;
    private List<HoursWeatherBean> hoursBeanList;
    private PMBean pmBean;
    private WeatherBean weatherBean;

    //    private static final int REPEAT_MSG = 0x01;

    private OnParserCallBack callBack;

    //    private Handler mHandler = new Handler() {
    //        public void handleMessage(Message msg) {
    //            if (msg.what == REPEAT_MSG) {
    //                System.out.println("handleMessage in service");
    //                getCitysWeather();
    //                sendEmptyMessageDelayed(REPEAT_MSG, 30 * 60 * 1000);
    //            }
    //        };
    //    };

    public interface OnParserCallBack {
        public void onParserComplete(List<HoursWeatherBean> hoursBeanList, PMBean pmBean,
                WeatherBean weatherBean);
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate");
        super.onCreate();
        //        city = "杭州";
        //        mHandler.sendEmptyMessage(REPEAT_MSG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    public class WeatherServiceBinder extends Binder {
        public WeatherService getService() {
            return WeatherService.this;
        }
    }

    public void setCallBack(OnParserCallBack callBack) {
        this.callBack = callBack;
    }

    public void removeCallBack() {
        this.callBack = null;
    }

    public void getCitysWeather(String city) {
        this.city = city;
        getCitysWeather();
    }

    public void getCitysWeather() {
        System.out.println(city);
        if (isRunning) {
            return;
        }
        isRunning = true;
        count = 0;
        WeatherData weatherData = WeatherData.getInstance();
        weatherData.getByCitys(city, 2, new JsonCallBack() {

            @Override
            public void jsonLoaded(JSONObject weatherJson) {
                count++;
                weatherBean = parserWeather(weatherJson);
                if (weatherBean != null) {
                }
                if (count == 3) {
                    isRunning = false;
                    if (callBack != null) {
                        callBack.onParserComplete(hoursBeanList, pmBean, weatherBean);
                    }
                }
            }
        });

        weatherData.getForecast3h(city, new JsonCallBack() {

            @Override
            public void jsonLoaded(JSONObject weatherJson) {
                count++;
                hoursBeanList = parseForecast3h(weatherJson);
                if (hoursBeanList != null && hoursBeanList.size() >= 5) {
                }
                if (count == 3) {
                    isRunning = false;
                    if (callBack != null) {
                        callBack.onParserComplete(hoursBeanList, pmBean, weatherBean);
                    }
                }
            }
        });

        AirData airData = AirData.getInstance();
        airData.cityAir(city, new JsonCallBack() {

            @Override
            public void jsonLoaded(JSONObject airJson) {
                count++;
                pmBean = parsePM(airJson);
                if (pmBean != null) {
                }
                if (count == 3) {
                    isRunning = false;
                    if (callBack != null) {
                        callBack.onParserComplete(hoursBeanList, pmBean, weatherBean);
                    }
                }
            }
        });
    }

    /**
     * 解析PM2.5
     * @param json
     * @return
     */
    private PMBean parsePM(JSONObject json) {
        PMBean bean = null;
        try {
            int code = json.getInt("resultcode");
            int error_code = json.getInt("error_code");
            if (error_code == 0 && code == 200) {
                bean = new PMBean();
                JSONObject pmJson = json.getJSONArray("result").getJSONObject(0)
                        .getJSONObject("citynow");
                bean.setAqi(pmJson.getString("AQI"));
                bean.setQuality(pmJson.getString("quality"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 解析城市查询
     * @param json
     * @return
     */
    private WeatherBean parserWeather(JSONObject json) {
        WeatherBean bean = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            int code = json.getInt("resultcode");
            int error_code = json.getInt("error_code");
            if (error_code == 0 && code == 200) {
                JSONObject resultJson = json.getJSONObject("result");
                bean = new WeatherBean();

                // today
                JSONObject todayJson = resultJson.getJSONObject("today");
                bean.setCity(todayJson.getString("city"));
                bean.setUv_index(todayJson.getString("uv_index"));
                bean.setTemp(todayJson.getString("temperature"));
                bean.setWeather_str(todayJson.getString("weather"));
                bean.setWeather_id(todayJson.getJSONObject("weather_id").getString("fa"));
                bean.setDressing_index(todayJson.getString("dressing_index"));

                // sk
                JSONObject skJson = resultJson.getJSONObject("sk");
                bean.setWind(skJson.getString("wind_direction") + " "
                        + skJson.getString("wind_strength"));
                bean.setNow_temp(skJson.getString("temp"));
                bean.setRelease(skJson.getString("time"));
                bean.setHumidity(skJson.getString("humidity"));

                // future
                Date date = new Date(System.currentTimeMillis());
                JSONArray futureArray = resultJson.getJSONArray("future");
                List<FutureWeatherBean> futureList = new ArrayList<FutureWeatherBean>();
                for (int i = 0; i < futureArray.length(); i++) {
                    JSONObject futureJson = futureArray.getJSONObject(i);
                    FutureWeatherBean futureBean = new FutureWeatherBean();
                    Date datef = sdf.parse(futureJson.getString("date"));
                    if (!datef.after(date)) {
                        continue;
                    }
                    futureBean.setTemp(futureJson.getString("temperature"));
                    futureBean.setWeek(futureJson.getString("week"));
                    futureBean.setWeather_id(futureJson.getJSONObject("weather_id")
                            .getString("fa"));
                    futureList.add(futureBean);
                    if (futureList.size() == 3) {
                        break;
                    }
                }
                bean.setFutureList(futureList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * 解析未来每三小时的天气情况
     * @param json
     * @return
     */
    private List<HoursWeatherBean> parseForecast3h(JSONObject json) {
        List<HoursWeatherBean> list = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINESE);
        Date date = new Date(System.currentTimeMillis());
        try {
            int code = json.getInt("resultcode");
            int error_code = json.getInt("error_code");
            if (code == 200 && error_code == 0) {
                list = new ArrayList<HoursWeatherBean>();
                JSONArray resultArray = json.getJSONArray("result");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject hourJson = resultArray.getJSONObject(i);
                    Date hDate = sdf.parse(hourJson.getString("sfdate"));
                    if (!hDate.after(date)) {
                        continue;
                    }
                    HoursWeatherBean bean = new HoursWeatherBean();
                    bean.setWeather_id(hourJson.getString("weatherid"));
                    bean.setTemp(hourJson.getString("temp1"));
                    Calendar c = Calendar.getInstance();
                    c.setTime(hDate);
                    bean.setTime(c.get(Calendar.HOUR_OF_DAY) + "");
                    list.add(bean);
                    if (list.size() == 5) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        return list;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public WeatherBean getWeatherBean() {
        return weatherBean;
    }

}
