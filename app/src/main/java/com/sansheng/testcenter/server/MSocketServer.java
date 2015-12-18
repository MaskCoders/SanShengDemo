package com.sansheng.testcenter.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import com.sansheng.testcenter.callback.ServiceCallback;
import com.sansheng.testcenter.controller.MainHandler;

/**
 * Created by hua on 12/17/15.
 */
public class MSocketServer extends Service implements ServiceCallback {
    MainHandler mHandler;
    SocketServer mSocketServer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //*return mMessenger.getBinder();*/
        return new MBuild();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(mSocketServer != null) {
            mSocketServer.startServer();
        }
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("by hua socketserver destory");
        super.onDestroy();
    }

    public class MBuild extends Binder {
        public MSocketServer getService() {
            return MSocketServer.this;
        }
    }

    private Message getMessage(String content, int type) {
        Message msg = new Message();
        msg.obj = content;
        msg.what = type;
        return msg;
    }


    //
    @Override
    public void setHandler(MainHandler handler) {
        mHandler = handler;
        mSocketServer = new SocketServer(mHandler);
    }
//    static final int MSG_SAY_HELLO = 1;
    /**
     * 在Service处理Activity传过来消息的Handler
     */
//    class IncomingHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_SAY_HELLO:
//                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    super.handleMessage(msg);
//            }
//        }
//    }
//    final Messenger mMessenger = new Messenger(new IncomingHandler());
}
