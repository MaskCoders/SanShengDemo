package com.sansheng.testcenter.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by hua on 12/17/15.
 */
public class MSocketServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//    private final IMSocketServer.Stub mBinder = new IMSocketServer.Stub() {
//        @Override
//        public void setHandler() throws RemoteException {
//
//        }
//    };



}
