package com.jiang.shanwe.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Jiang.Shanwe
 * 2015-11-8 下午5:46:07
 *
 */
public class DateUtil {

    public static final int DAY_MILLISECOND = 86400000;

    public static long[] getDateRange(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start = calendar.getTime().getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        long end = calendar.getTime().getTime();
        long[] range = { start, end };
        return range;
    }

    /* public static Date getDateFromLong(long dateLong) {
         return new Date(dateLong);
     }*/

    public static Date getPreviousDate(Date date) {
        return new Date(date.getTime() - DAY_MILLISECOND);
    }

    public static Date getNextDate(Date date) {
        return new Date(date.getTime() + DAY_MILLISECOND);
    }
}
