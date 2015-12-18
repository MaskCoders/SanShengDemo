package com.sansheng.testcenter.server;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.sansheng.testcenter.callback.IServiceHandler;
import com.sansheng.testcenter.callback.ServiceCallback;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hua on 12/17/15.
 */
public class MSocketServer extends Service implements ServiceCallback {
    IServiceHandler mHandler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //*return mMessenger.getBinder();*/
        return new MBuild();
    }

    @Override
    public void setHandler(IServiceHandler handler) {
        mHandler = handler;
    }
    static final int MSG_SAY_HELLO = 1;
    /**
     * 在Service处理Activity传过来消息的Handler
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    @Override
    public void onStart(Intent intent, int startId) {
        startServer();
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
    private static final int PORT = 8001;
    private ServerSocket server = null;
    Socket client = null;
    BufferedReader serin = null;
    private String msg = "";
    private static final String ERRCODE = "ERRCODE";
    private static final String CONN_SUCCESS = "CONN_SUCCESS";

    public void sendmsg() {
        PrintWriter pout = null;
        try {
            pout = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream())), true);
            pout.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private static final String HOST = "127.0.0.1";

    private static final int CONN_SER_CLS = -2;
    private static final int CONN_ERR = -1;
    private static final int CONN_OK = 0;
    private static final int RECV_MSG = 1;
    private static final int INPUT_ERR = -3;

    private Message getMessage(String content, int type) {
        Message msg = new Message();
        msg.obj = content;
        msg.what = type;
        return msg;
    }

    private void startServer() {
        System.out.println(" by hua start server ... ");
//        if(server != null && server.isBound() ){
//            Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip3), 0).show();
//        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(PORT);

                    while (true) {
                        client = server.accept();
                        System.out.println(" by hua client conn ... ");
                        serin = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        msg = CONN_SUCCESS + client.getInetAddress();
                        sendmsg();
                        Thread send = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        if ((msg = serin.readLine()) != null) {
                                            System.out.println(" by hua client info is "+msg);
                                            if (msg.equals("0x68 0x16")) {
                                                msg = "0x16 0x68";
                                            } else {
                                                msg = "ERRCODE";
                                            }
                                            sendmsg();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        send.start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }
}
