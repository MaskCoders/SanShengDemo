package com.sansheng.testcenter.controller;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.sansheng.testcenter.callback.ServiceCallback;
import com.sansheng.testcenter.server.MSocketServer;

/**
 * Created by hua on 12/18/15.
 */
public class ConnectionService implements ServiceConnection {
    MainHandler mHandler;
    Service mService;

    public ConnectionService(MainHandler handler, Service service) {
        this.mHandler = handler;
        this.mService = service;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MSocketServer.MBuild binder = (MSocketServer.MBuild) service;
        mService = binder.getService();

        ((ServiceCallback) mService).setHandler(mHandler); //把当前对象传递给myservice


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
System.out.println("by hua connection service disconnedted");
    }


}
