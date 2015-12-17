package com.sansheng.testcenter;

import android.app.Application;

/**
 * Created by sunshaogang on 12/17/15.
 */
public class TestCenterApplication extends Application {

    private static TestCenterApplication instance;

    public static TestCenterApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }


}
