package com.jiang.shanwe.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

import com.jiang.shanwe.util.NumberUtil;

public class PieChart {

    private DefaultRenderer mRenderer = new DefaultRenderer();
    private CategorySeries mSeries = new CategorySeries("Category Series");

    private double[] valueList;
    private String[] nameList;
    private int[] colorList;

    public PieChart(double[] valueList, String[] nameList) {
        this.valueList = valueList;
        this.nameList = nameList;
        this.colorList = NumberUtil.getDifferentRandomColorList(valueList.length);
    }

    public PieChart(double[] valuesList, String[] nameList, int[] colorList) {
        this.valueList = valuesList;
        this.nameList = nameList;
        this.colorList = colorList;
    }

    /*    private static double[] VALUES = new double[] { 110, 11, 1222, 93 };
        private static String[] NAME_LIST = new String[] { "A", "B", "C", "D" };
        private DefaultRenderer mRenderer = new DefaultRenderer();
        private CategorySeries mSeries = new CategorySeries("Category Series");*/

    public GraphicalView getPieChartView(Context context) {
        mRenderer.setApplyBackgroundColor(false);
        mRenderer.setLabelsTextSize(18);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setZoomButtonsVisible(false); //显示缩放按钮
        //        mRenderer.setZoomEnabled(false); //放大/缩小
        //        mRenderer.setPanEnabled(false); //拖动
        mRenderer.setShowLegend(false);
        mRenderer.setLegendTextSize(25);
        mRenderer.setMargins(new int[] { 10, 10, 10, 10 });
        mRenderer.setStartAngle(90);
        for (int i = 0; i < valueList.length; i++) {
            mSeries.add("   " + nameList[i] + "   " + valueList[i] + "   " + NumberUtil.getPercentagesFromValueList(valueList)[i], valueList[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(colorList[(mSeries.getItemCount() - 1) % colorList.length]);
            mRenderer.addSeriesRenderer(renderer);
        }
        return ChartFactory.getPieChartView(context, mSeries, mRenderer);
    }
}
