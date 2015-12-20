package com.jiang.shanwe.util;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import android.annotation.SuppressLint;
import android.util.Log;

import com.jiang.shanwe.Config;

public class DateUtil extends TestCase {

    // 一天的毫秒数
    public static final long DAY_MILLISECOND = 86400000;

    /**
     * 获取指定天的毫秒范围
     * @param date
     * @return 
     */
    public static long[] getDayRange(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTime().getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        long end = calendar.getTime().getTime();
        long[] range = { start, end };
        return range;
    }

    /**
     * 获取指定天所在周的毫秒范围
     * @param date
     * @return
     */
    public static long[] getWeekRange(Date date) {
        long[] range = new long[2];
        Date startDayOfWeek = getStartDayOfWeek(date);
        range[0] = getDayRange(startDayOfWeek)[0];
        Date endDayOfWeek = getEndDayOfWeek(date);
        range[1] = getDayRange(endDayOfWeek)[1];
        return range;
    }

    /**
     * 获取指定天所在月份的毫秒范围
     * @param date
     * @return
     */
    public static long[] getMonthRange(Date date) {
        long[] range = new long[2];
        Date startDayOfMonth = getStartDayOfMonth(date);
        range[0] = getDayRange(startDayOfMonth)[0];
        Date endDayOfMonth = getEndDayOfMonth(date);
        range[1] = getDayRange(endDayOfMonth)[1];
        return range;
    }

    /**
     * @param date
     * @return 指定日期一天前
     */
    public static Date getPreviousDay(Date date) {
        return new Date(date.getTime() - DAY_MILLISECOND);
    }

    /**
     * @param date
     * @return 指定日期一天后
     */
    public static Date getNextDay(Date date) {
        return new Date(date.getTime() + DAY_MILLISECOND);
    }

    /**
     * @param date
     * @return 指定日期一周前
     */
    public static Date getPreviousWeek(Date date) {
        return new Date(date.getTime() - DAY_MILLISECOND * 7);
    }

    /**
     * @param date
     * @return 指定日期一周后
     */
    public static Date getNextWeek(Date date) {
        return new Date(date.getTime() + DAY_MILLISECOND * 7);
    }

    /**
     * @param date
     * @return 指定日期一个月前
     */
    public static Date getPreviousMonth(Date date) {
        return new Date(date.getTime() - DAY_MILLISECOND * (getMonthDayCount(date) - 1));
    }

    /**
     * @param date
     * @return 指定日期一个月后
     */
    public static Date getNextMonth(Date date) {
        return new Date(date.getTime() + DAY_MILLISECOND * getMonthDayCount(date));
    }

    /**
     * @param date
     * @return 指定日期是一周的第几天,从周一/1开始
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * @param date
     * @return 指定日期是所在月份的第几天,从一号/1开始
     */
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定日期所在周的第一天
     * @param date
     * @return
     */
    public static Date getStartDayOfWeek(Date date) {
        int currentDayOfWeek = getDayOfWeek(date);
        return new Date(date.getTime() - (currentDayOfWeek - 1) * DAY_MILLISECOND);
    }

    /**
     * 获取指定日期所在周的最后一天
     * @param date
     * @return
     */
    public static Date getEndDayOfWeek(Date date) {
        int currentDayOfWeek = getDayOfWeek(date);
        return new Date(date.getTime() + (7 - currentDayOfWeek) * DAY_MILLISECOND);
    }

    /**
     * 获取指定日期所在月的第一天
     * @param date
     * @return
     */
    public static Date getStartDayOfMonth(Date date) {
        int currentDayOfMonth = getDayOfMonth(date);
        return new Date(date.getTime() - (currentDayOfMonth - 1) * DAY_MILLISECOND);
    }

    /**
     * 获取指定日期所在月的最后一天
     * @param date
     * @return
     */
    public static Date getEndDayOfMonth(Date date) {
        int currentDayOfMonth = getDayOfMonth(date);
        return new Date(date.getTime() + (getMonthDayCount(date) - currentDayOfMonth) * DAY_MILLISECOND);
    }

    /**
     * 获取指定日期所在月的天数
     * @param date
     * @return
     */
    public static int getMonthDayCount(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定日期所在月份的X 轴统计标签
     * @param date
     * @return
     */
    public static String[] getMonthTextLabel(Date date) {
        String[] textLabel = new String[getMonthDayCount(date) + 1];
        for (int i = 0; i < getMonthDayCount(date) + 1; i++) {
            textLabel[i] = i % 2 == 1 ? "" : i + 1 + "";
        }
        return textLabel;
    }

    @SuppressLint("SimpleDateFormat")
    public void testMonth() {
        long[] range = getWeekRange(new Date());
        Log.i(Config.TEST_TAG, range[0] + " : " + range[1]);
    }
}
