package com.jiang.shanwe.util;

import java.text.DecimalFormat;
import java.util.Random;

import android.graphics.Color;

public class NumberUtil {

    /**
     * 去除小数后面的 ".0"
     * @param num
     * @return
     */
    public static String getSimpleDouble(Double num) {
        String countStr = num + "";
        return countStr.endsWith(".0") ? countStr.substring(0, (countStr).indexOf(".")) : countStr;
    }

    /**
     * 获得二维double数组指定维数的和
     * @param data 数据
     * @param dimension 维数
     * @return
     */
    public static double getSumOfTwoDimensionalDoubleArray(double[][] data, int dimension) {
        double result = 0;
        for (int i = 0; i < data[dimension - 1].length; i++) {
            result += data[dimension - 1][i];
        }
        return result;
    }

    /**
     * 计算Y轴最大值
     * @param barValue 序列值
     * @param dimension 维数
     * @return 最大值×1.2
     */

    public static int getYAxisUpperLimit(double[][] barValue, int dimension) {
        double max = getMaxOfDoubleArray(barValue[dimension - 1]);
        if (max < 50) {
            return (int) (max * 1.5);
        }
        return (int) (max * 1.12);
    }

    /**
     * 获取double数组中的最大值
     * @param var
     * @return
     */
    public static double getMaxOfDoubleArray(double[] var) {
        double max = var[0];
        for (int i = 1; i < var.length; i++) {
            max = max < var[i] ? var[i] : max;
        }
        return max;
    }

    /**
     * 判读数组中是否包含key值
     * @param values 数组
     * @param key
     * @return
     */
    public static boolean intListContainsValue(int[] values, int key) {
        if (values.length == 0) {
            return false;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i] == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * 随机产生颜色
     * @return
     */
    public static int getRandomColor() {
        Random random = new Random();
        int R = random.nextInt(255);
        int G = random.nextInt(255);
        int B = random.nextInt(255);
        return Color.rgb(R, G, B);
    }

    /**
     * 返回不同颜色的数组
     * @param size 要求的颜色数量
     * @return
     */
    public static int[] getDifferentRandomColorList(int size) {
        int[] colorList = new int[size];
        int temp = 0;
        while (temp < size) {
            int color = getRandomColor();
            if (!intListContainsValue(colorList, color)) {
                colorList[temp] = color;
                temp++;
            }
        }
        return colorList;
    }

    /**
     * 取数组的和
     * @param valueList
     * @return
     */
    public static double getSum(double[] valueList) {
        double sum = 0;
        for (int i = 0; i < valueList.length; i++) {
            sum += valueList[i];
        }
        return sum;
    }

    /**
     * 取valueList各值的百分比
     * @param valueList
     * @return
     */
    public static String[] getPercentagesFromValueList(double[] valueList) {
        String[] percentages = new String[valueList.length];
        double sumOfValueList = getSum(valueList);
        DecimalFormat df = new DecimalFormat("0.00%"); //##.00%   百分比格式，后面不足2位的用0补齐
        for (int i = 0; i < valueList.length; i++) {
            percentages[i] = df.format(valueList[i] / sumOfValueList);
        }
        return percentages;
    }
}
