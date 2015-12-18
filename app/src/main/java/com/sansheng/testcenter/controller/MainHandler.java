package com.sansheng.testcenter.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;

import static com.sansheng.testcenter.base.Const.*;

/**
 * Created by hua on 12/18/15.
 */
public class MainHandler extends Handler {
    private Context mContext;
    private IServiceHandlerCallback mMainUI;
    public MainHandler(Context ctx,IServiceHandlerCallback ui){
        mContext = ctx;
        mMainUI = ui;
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String content = msg.obj.toString();
        switch (msg.what) {
            case CONN_SER_CLS:
                break;
            case RECV_MSG:
                if (content.contains(ERRCODE)) {
                    content = mContext.getResources().getString(R.string.conn_ser_err2);
                } else if (content.contains(CONN_SUCCESS)) {
                    mMainUI.pullStatus(mContext.getResources().getString(R.string.server)
                            + content.replace(CONN_SUCCESS, "").replace("\n", "") + "," +
                            mContext.getResources().getString(R.string.ser_port) + PORT);

//                        btn_ser.setText(SocketDemo.this.getResources().getString(R.string.start_ser_close));
                    return;
                }
                break;
            case CONN_OK:
                break;
            case CONN_ERR:
                content = mContext.getResources().getString(R.string.conn_ser_err1) + ":" + content;
            default:
        }
        mMainUI.pullWholeLog(content);

    }
}
