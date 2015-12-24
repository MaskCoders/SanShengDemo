package com.sansheng.testcenter.server;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.sansheng.testcenter.controller.MainHandler;

import java.io.*;
import java.net.Socket;

/**
 * Created by hua on 12/18/15.
 */
public class SocketClient {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Context mContext;
    private MainHandler mMainHandler;

    static final int PORT = 8001;
    static final String ERRCODE = "ERRCODE";
    static final String CONN_SUCCESS = "CONN_SUCCESS";
    static final String HOST = "127.0.0.1";

    static final int CONN_SER_CLS = -2;
    static final int CONN_ERR = -1;
    static final int CONN_OK = 0;
    static final int RECV_MSG = 1;
    static final int INPUT_ERR = -3;

    public SocketClient(Context ctx , MainHandler handler){
        mContext = ctx;
        mMainHandler = handler;
    }
    public void sendMessage(String msg){
        if (socket != null && socket.isConnected()) {
            if (!socket.isOutputShutdown()) {
                out.println(msg);
            }
        }
    }


    private Message getMessage(String content, int type) {
        Message msg = new Message();
        msg.obj = content;
        msg.what = type;
        return msg;
    }

    public void startClient(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null || socket.isClosed()) {
                        socket = new Socket(HOST, PORT);
                        in = new BufferedReader(new InputStreamReader(socket
                                .getInputStream()));
                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream())), true);
                    }

                    while (true) {
                        if (!socket.isClosed()) {
                            if (socket.isConnected()) {
                                if (!socket.isInputShutdown()) {
                                    String content = null;
                                    if ((content = in.readLine()) != null) {
                                        content += "\n";
                                        ((Handler)mMainHandler).sendMessage(getMessage(content, RECV_MSG));
                                    } else {

                                    }
                                }
                            }
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.d("", "login exception" + ex.getMessage());
//                    ((Handler)mHandler).sendMessage(getMessage("conn err", CONN_ERR));
                }
            }
        });
        thread.start();
    }
}