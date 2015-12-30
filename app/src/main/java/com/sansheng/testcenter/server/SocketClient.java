package com.sansheng.testcenter.server;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.tools.ProtocolUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by hua on 12/18/15.
 */
public class SocketClient {
    private Socket socket = null;
    private BufferedInputStream in = null;
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
                        in = new BufferedInputStream(socket.getInputStream());;
                    }

                    while (true) {
                        if (!socket.isClosed()) {
                            if (socket.isConnected()) {
                                if (!socket.isInputShutdown()) {

                                    ArrayList<Byte> list = new ArrayList<Byte>();
                                    int count = 0;
                                    int i=0;
                                    System.out.println("===>"  );
                                    while( (count = in.available()) > 0 )
                                    {
                                        // get the number of bytes available
//                                                Integer nBytes = in.available();
//                                                System.out.println("Available bytes = " + nBytes );

                                        // read next available character
                                        byte ch =  (byte)in.read();
                                        list.add(ch);
                                    }
                                    System.out.println("===>"+count);
                                    byte[] bs = new byte[list.size()];
                                    for(int j=0;j<list.size();j++){
                                        bs[j] = list.get(j);
                                    }
                                    ProtocolUtils.printByte(bs);
//                                            in.read(last);

                                    in.close();
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
