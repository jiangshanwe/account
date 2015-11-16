package com.jiang.shanwe.util;

public class NumberUtil {

    public static String getSimpleDouble(Double num) {
        String countStr = num + "";
        return countStr.endsWith(".0") ? countStr.substring(0, (countStr).indexOf(".")) : countStr;
    }
}
