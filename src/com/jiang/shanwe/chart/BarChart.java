package com.jiang.shanwe.chart;

import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Paint.Align;

public class BarChart {
    // 柱状图标题
    public String ChartTitle;
    // 横轴标题
    public String XTitle;
    // 纵轴标题
    public String YTitle;
    // 柱体个数
    public int BarNum;
    // 纵轴最大值
    public int YValueMax;

    /**
     * @param ChartTitle 柱状图标题
     * @param XTitle 横轴标题
     * @param YTitle 纵轴标题
     * @param BarNum 柱体个数
     * @param YValueMax 纵轴最大值
     */
    public BarChart(String ChartTitle, String XTitle, String YTitle, int BarNum, int YValueMax) {
        this.ChartTitle = ChartTitle;
        this.XTitle = "";
        this.YTitle = "";
        this.BarNum = BarNum;
        this.YValueMax = YValueMax;
    }

    /**
     * 说明：完成将柱体添加到渲染器中，设置柱体颜色以及渲染器的各种设置
     * @param seriesnum 柱体组数
     * @param rdcolor 柱体颜色，注意：此处要设置透明度，如没有设置（例0x0000ff）则无法显示
     * @param textlable 横轴标签文本
     * @return
     */
    public XYMultipleSeriesRenderer GetRenderer(int seriesnum, int[] rdcolor, String[] textlable) {
        // 构造显示渲染图
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        // 新建柱状图
        XYSeriesRenderer rd = new XYSeriesRenderer();
        //        SimpleSeriesRenderer rd = new SimpleSeriesRenderer();
        // 设置柱状图的颜色（有几个柱状图就要设几次，否则会出现异常）
        for (int i = 0; i < seriesnum; i++) {
            rd.setColor(rdcolor[i]);
        }
        // 将柱体添加到渲染图
        renderer.addSeriesRenderer(rd);
        // 自定义方法，renderer设置
        RendererSet(renderer, textlable);
        return renderer;
    }

    /**
     * 功能：渲染器renderer相关设置
     * 说明：除了柱体的颜色，别的设置均在此方法中设置
     * @param rd
     * @param tl
     */
    @SuppressWarnings("deprecation")
    protected void RendererSet(XYMultipleSeriesRenderer rd, String[] tl) {
        // 设置标题
        rd.setChartTitle(ChartTitle);
        rd.setChartTitleTextSize(18);
        // 设置横轴标题
        rd.setXTitle(XTitle);
        // 设置纵轴标题
        rd.setYTitle(YTitle);
        // 设置拖动时X轴Y轴允许的最大值最小值（貌似必须有四个数，第一个是最小值，第二个是最大值，后面两个没什么用）
        rd.setPanLimits(new double[] { 0, BarNum, 0, 0 });
        // 设置拖动时X轴标签文本，注：这里必须设置setXLabels(0)
        rd.setXLabels(0);
        for (int i = 0; i < BarNum; i++) {
            rd.addTextLabel(i + 1, tl[i]);
        }
        // 设置默认横轴最小值和最大值
        rd.setXAxisMin(0.5);
        // x轴多设置一个，右边会有空余，美观一点
        rd.setXAxisMax(BarNum + 1);
        // 设置默认纵轴最小值和最大值
        rd.setYAxisMin(0);
        rd.setYAxisMax(YValueMax);
        // 设置纵轴标签个数（决定纵轴每格表示值，如纵轴最大值为100，标签个数为20，则每格表示5）
        rd.setYLabels(5);
        // 设置每个柱形相隔距离1.0表示和柱体同宽，2.0表示是柱体的2倍，以此类推
        rd.setBarSpacing(0.5);
        // 设置横纵轴是否能放大
        rd.setZoomEnabled(false, false);
        // 设置横纵轴是否能移动
        rd.setPanEnabled(false, false);
        // 设置是否显示网格
        rd.setShowGrid(false);
        // 刻度线与刻度标注之间的相对位置关系（刻度线在标注的上方）
        rd.setXLabelsAlign(Align.CENTER);
        // 刻度线在标注的右边
        rd.setYLabelsAlign(Align.RIGHT);
        // 设置颜色
        rd.setApplyBackgroundColor(true);
        // 坐标轴内的颜色
        rd.setBackgroundColor(0xffffff);
        // 坐标轴外的颜色
        rd.setMarginsColor(0xffffff);

        rd.setDisplayChartValues(true);
        rd.setShowLegend(false);
        rd.setAxesColor(0xffFF0000);
        // Sets the X/Y axis labels color.
        rd.setXLabelsColor(0xffFF0000);
        rd.setYLabelsColor(0, 0xffFF0000);

        rd.setChartValuesTextSize(22f);
        rd.setLabelsTextSize(20f);
        rd.setChartTitleTextSize(25f);
    }

    /**
     *
     * @param seriesnum 柱体组数
     * @param seriesname 主体集名称
     * @param val 数据，决定柱体高度
     * @return
     */
    public XYMultipleSeriesDataset GetDataset(int seriesnum, String[] seriesname, double[][] val) {
        // 构建柱状图的数据
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // 循环的次数为柱体的个数
        for (int i = 0; i < seriesnum; i++) {
            // 构造柱形图设置柱状标题
            CategorySeries series = new CategorySeries(seriesname[i]);
            // 填充数据
            for (int k = 1; k <= BarNum; k++) {
                // 填y值（柱形高度）
                series.add(val[i][k]);
            }
            // 需要绘制的柱体放进dataset中
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }
}
