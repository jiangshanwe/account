package com.jiang.shanwe;

import android.app.Application;

import com.jiang.shanwe.loveaccount.R;
import com.thinkland.juheapi.common.CommonFun;

public class AccountApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CommonFun.initialize(getApplicationContext());
    }
}
