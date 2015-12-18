package com.sansheng.testcenter;

import android.app.Application;
import android.content.Intent;
import com.sansheng.testcenter.server.MSocketServer;

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
        startSocketServer();
    }
    private void startSocketServer(){
        Intent intent = new Intent(this,MSocketServer.class);
        startService(intent);
    }
    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }


}
