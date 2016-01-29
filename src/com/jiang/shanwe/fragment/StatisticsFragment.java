package com.jiang.shanwe.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.chart.BarChart;
import com.jiang.shanwe.chart.PieChart;
import com.jiang.shanwe.db.DBUtil;
import com.jiang.shanwe.loveaccount.R;
import com.jiang.shanwe.util.DateUtil;
import com.jiang.shanwe.util.NumberUtil;

@EFragment
public class StatisticsFragment extends Fragment {

    private View view;

    @ViewById
    RadioButton rbWeek;
    @ViewById
    RadioButton rbMonth;
    @ViewById
    LinearLayout lytBarChart;
    @ViewById
    LinearLayout lytPieChart;
    @ViewById
    ImageView ivSyncStafisticsChart;
    @ViewById
    RadioGroup rgStatistics;

    float xdown = 0, xup = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        return view;
    }

    /**
     * 首次加载
     * 绘制周消费统计图（条形图）
     */
    @SuppressLint("NewApi")
    @AfterViews
    public void renderWeekBarChart() {
        lytBarChart.removeAllViews();
        final int SERIERS_NUM = 1;
        final String[] SERIERS_NAME = { "" };
        final int[] RENDERER_COLOR = { 0xff0000ff };

        String[] textLable = { "一", "二", "三", "四", "五", "六", "天" };
        double[][] barValue = DBUtil.getInstance(getActivity()).getWeekValue(
                Config.getStatisticsWeekCacheLocationDate(getActivity()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String chartTitle = sdf.format(DateUtil.getStartDayOfWeek(Config
                .getStatisticsWeekCacheLocationDate(getActivity())))
                + " --- "
                + sdf.format(DateUtil.getEndDayOfWeek(Config
                        .getStatisticsWeekCacheLocationDate(getActivity())))
                + "     共消费： "
                + NumberUtil.getSumOfTwoDimensionalDoubleArray(barValue, 1) + " 元";
        BarChart barchart = new BarChart(chartTitle, "", "", 7,
                NumberUtil.getYAxisUpperLimit(barValue, 1));

        GraphicalView chartview = ChartFactory.getBarChartView(getActivity(),
                barchart.GetDataset(SERIERS_NUM, SERIERS_NAME, barValue),
                barchart.GetRenderer(SERIERS_NUM, RENDERER_COLOR, textLable),
                Type.DEFAULT);
        lytBarChart.addView(chartview);
    }

    /**
     * 首次加载
     * 绘制周消费统计图（饼图）
     */
    @AfterViews
    public void renderWeekPieChart() {
        lytPieChart.removeAllViews();
        Map<String, Object> weekCategory = DBUtil.getConsumeCategory(DateUtil
                .getWeekRange(Config.getStatisticsWeekCacheLocationDate(getActivity())),
                Config.getCacheUserId(getActivity()));
        PieChart pieChart = new PieChart(
                (double[]) weekCategory.get(Config.MAP_KEY_CATEGORY_VALUE),
                (String[]) weekCategory.get(Config.MAP_KEY_CATEGORY_NAME));
        lytPieChart.addView(pieChart.getPieChartView(getActivity()));
    }

    /**
     * 绘制月消费条形图
     */
    public void renderMonthBarChart() {
        lytBarChart.removeAllViews();
        final int SERIERS_NUM = 1;
        final String[] SERIERS_NAME = { "" };
        final int[] RENDERER_COLOR = { 0xff0000ff };

        int daysCount = DateUtil.getMonthDayCount(Config
                .getStatisticsMonthCacheLocationDate(getActivity()));
        String[] textLable = DateUtil.getMonthTextLabel(Config
                .getStatisticsMonthCacheLocationDate(getActivity()));
        double[][] barValue = DBUtil.getInstance(getActivity()).getMonthValue(
                Config.getStatisticsMonthCacheLocationDate(getActivity()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String chartTitle = sdf.format(DateUtil.getStartDayOfMonth(Config
                .getStatisticsMonthCacheLocationDate(getActivity())))
                + " --- "
                + sdf.format(DateUtil.getEndDayOfMonth(Config
                        .getStatisticsMonthCacheLocationDate(getActivity())))
                + "     共消费："
                + NumberUtil.getSumOfTwoDimensionalDoubleArray(barValue, 1)
                + " 元";
        BarChart barchart = new BarChart(chartTitle, "", "", daysCount,
                NumberUtil.getYAxisUpperLimit(barValue, 1));

        GraphicalView chartview = ChartFactory.getBarChartView(getActivity(),
                barchart.GetDataset(SERIERS_NUM, SERIERS_NAME, barValue),
                barchart.GetRenderer(SERIERS_NUM, RENDERER_COLOR, textLable),
                Type.DEFAULT);
        lytBarChart.addView(chartview);
    }

    /**
     * 绘制月消费饼图
     */
    public void renderMonthPieChart() {
        lytPieChart.removeAllViews();
        Map<String, Object> weekCategory = DBUtil.getConsumeCategory(
                DateUtil.getMonthRange(Config
                        .getStatisticsMonthCacheLocationDate(getActivity())), Config
                        .getCacheUserId(getActivity()));
        PieChart pieChart = new PieChart(
                (double[]) weekCategory.get(Config.MAP_KEY_CATEGORY_VALUE),
                (String[]) weekCategory.get(Config.MAP_KEY_CATEGORY_NAME));
        lytPieChart.addView(pieChart.getPieChartView(getActivity()));
    }

    /**
     * 点击显示月统计图按钮
     */
    @Click(R.id.rbMonth)
    public void renderMonthChart() {
        renderMonthBarChart();
        renderMonthPieChart();
    }

    /**
     * 点击显示周统计图按钮
     */
    @Click(R.id.rbWeek)
    public void renderWeekChart() {
        renderWeekBarChart();
        renderWeekPieChart();
    }

    /**
     * 点击刷新图表按钮
     */
    @Click(R.id.ivSyncStafisticsChart)
    public void syncStatisticsChart() {
        switch (rgStatistics.getCheckedRadioButtonId()) {
        case R.id.rbWeek:
            Config.cacheStatisticsWeekLocationDate(getActivity(), new Date());
            renderWeekBarChart();
            renderWeekPieChart();
            break;
        case R.id.rbMonth:
            Config.cacheStatisticsMonthLocationDate(getActivity(), new Date());
            renderMonthBarChart();
            renderMonthPieChart();
            break;
        default:
            break;
        }
    }

    /**
     * 点击前一周（月）按钮
     */
    @Click(R.id.btnStatisticsPrevious)
    public void previousRangeChart() {
        switch (rgStatistics.getCheckedRadioButtonId()) {
        // 上一周
        case R.id.rbWeek:
            Config.cacheStatisticsWeekLocationDate(getActivity(), DateUtil
                    .getPreviousWeek(Config
                            .getStatisticsWeekCacheLocationDate(getActivity())));
            renderWeekChart();
            break;
        // 上一月
        case R.id.rbMonth:
            Config.cacheStatisticsMonthLocationDate(getActivity(), DateUtil
                    .getPreviousMonth(Config
                            .getStatisticsMonthCacheLocationDate(getActivity())));
            renderMonthChart();
            break;
        default:
            break;
        }
    }

    /**
     * 点击后一周（月）按钮
     */
    @Click(R.id.btnStatisticsNext)
    public void nextRangeChart() {
        switch (rgStatistics.getCheckedRadioButtonId()) {
        // 下一周
        case R.id.rbWeek:
            Config.cacheStatisticsWeekLocationDate(getActivity(),
                    DateUtil.getNextWeek(Config
                            .getStatisticsWeekCacheLocationDate(getActivity())));
            renderWeekChart();
            break;
        // 下一月
        case R.id.rbMonth:
            Config.cacheStatisticsMonthLocationDate(getActivity(), DateUtil
                    .getNextMonth(Config
                            .getStatisticsMonthCacheLocationDate(getActivity())));
            renderMonthChart();
            break;
        default:
            break;
        }
    }

}
