package com.sansheng.testcenter.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.tools.log.LogUtils;

/**
 * Created by hua on 12/18/15.
 */
public class MainHandler extends Handler {
    private Context mContext;
    private IServiceHandlerCallback mMainUI;
    LogUtils logUtils;
    public MainHandler(Context ctx, IServiceHandlerCallback ui) {
        mContext = ctx;
        mMainUI = ui;
        logUtils = new LogUtils();
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String content = msg.obj.toString();
        mMainUI.pullWholeLog(content);

    }
}
