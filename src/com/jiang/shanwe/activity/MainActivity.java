package com.jiang.shanwe.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.fragment.AddRecordFragment;
import com.jiang.shanwe.fragment.SettingsFragment;
import com.jiang.shanwe.fragment.SettingsFragment_;
import com.jiang.shanwe.fragment.StatisticsFragment;
import com.jiang.shanwe.fragment.StatisticsFragment_;
import com.jiang.shanwe.loveaccount.R;
import com.jiang.shanwe.view.ChangeColorIconWithText;
import com.jiang.shanwe.view.CustomViewPager;

public class MainActivity extends FragmentActivity implements OnClickListener,
        OnPageChangeListener {

    private CustomViewPager mViewPager;
    private final List<Fragment> mTabFragments = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        initTestData();

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1, false);
        Config.cacheLocationDate(this, new Date());
        Config.cacheStatisticsWeekLocationDate(this, new Date());
        Config.cacheStatisticsMonthLocationDate(this, new Date());

        initEvent();
    }

    private void initTestData() {
        if (Config.getCacheUserId(this) == 0) {
            Config.cacheUserId(this, 1);
        }
    }

    private void initEvent() {
        // mViewPager.setOnPageChangeListener(this);
    }

    private void initDatas() {
        StatisticsFragment statisticsFragment = new StatisticsFragment_();
        mTabFragments.add(statisticsFragment);

        AddRecordFragment fragment = new AddRecordFragment();
        mTabFragments.add(fragment);

        SettingsFragment settingsFragment = new SettingsFragment_();
        mTabFragments.add(settingsFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabFragments.get(position);
            }
        };
    }

    private void initView() {
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);

        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);

        mTabIndicators.add(one);
        mTabIndicators.add(two);
        mTabIndicators.add(three);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);

        two.setIconAlpha(1.0f);
    }

    @Override
    public void onClick(View v) {
        resetOtherTabs();
        switch (v.getId()) {
        case R.id.id_indicator_one:
            mTabIndicators.get(0).setIconAlpha(1.0f);
            mViewPager.setCurrentItem(0, false);
            break;
        case R.id.id_indicator_two:
            mTabIndicators.get(1).setIconAlpha(1.0f);
            mViewPager.setCurrentItem(1, false);
            break;
        case R.id.id_indicator_three:
            mTabIndicators.get(2).setIconAlpha(1.0f);
            mViewPager.setCurrentItem(2, false);
            break;
        default:
            break;
        }
    }

    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int arg0) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    /**
    * 回调接口
    *
    */
    public interface FragmentTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    /*
    * 保存FragmentTouchListener接口的列表
    */
    private ArrayList<FragmentTouchListener> fragmentTouchListeners = new ArrayList<MainActivity.FragmentTouchListener>();

    /**
    * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
    * @param listener
    */
    public void registerFragmentTouchListener(FragmentTouchListener listener) {
        fragmentTouchListeners.add(listener);
    }

    /**
    * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
    * @param listener
    */
    public void unRegisterFragmentTouchListener(FragmentTouchListener listener) {
        fragmentTouchListeners.remove(listener);
    }

    /**
    * 分发触摸事件给所有注册了FragmentTouchListener的接口
    */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (FragmentTouchListener listener : fragmentTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

}
