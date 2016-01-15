package com.sansheng.testcenter.server;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by hua on 12/18/15.
 */
public class SocketClient1 {
    private Socket socket = null;
    private BufferedInputStream in = null;
//    private PrintWriter out = null;
    private DataOutputStream out = null;
    private Context mContext;
    private MainHandler mMainHandler;

    private int PORT = 8001;
    static final String ERRCODE = "ERRCODE";
    static final String CONN_SUCCESS = "CONN_SUCCESS";
     private String HOST = "127.0.0.1";

    static final int CONN_SER_CLS = -2;
    static final int CONN_ERR = -1;
    static final int CONN_OK = 0;
    static final int RECV_MSG = 1;
    static final int INPUT_ERR = -3;
    private ClientManager manager;
    SocketClient1(Context ctx , MainHandler handler, ClientManager cm){
        this(ctx,handler,null,-100,cm);
    }
    SocketClient1(Context ctx , MainHandler handler, String ip, int port, ClientManager cm) {
        mContext = ctx;
        mMainHandler = handler;
        this.manager = cm;
        if (!TextUtils.isEmpty(ip)) {
            HOST = ip;
        }
        if (port > 0) {
            PORT = port;
        }
    }
//    void sendMessage(String msg){
//        if (socket != null && socket.isConnected()) {
//            if (!socket.isOutputShutdown()) {
//                out.println(msg);
////                out.write(msg.toCharArray());
////                out.flush();
//            }
//        }
//    }
    void sendMessage(byte[] data){
        if (socket != null && socket.isConnected()) {
            if (!socket.isOutputShutdown()) {
                try {
                    out.write(data);
                    out.write('\n');//这个是为了测试服务有个结尾
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                out.write(msg.toCharArray());
//                out.flush();
            }
        }
    }


    private Message getMessageStr(String content, int type) {
        Message msg = new Message();
        msg.obj = content;
        msg.what = type;
        return msg;
    }
    private Message getMessageObj(Object obj, int type) {
        Message msg = new Message();
        msg.obj = obj;
        msg.what = type;
        return msg;
    }
    void stopClient(){
        if(socket != null && socket.isConnected()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void startClient(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null || socket.isClosed()) {
                        socket = new Socket(HOST, PORT);
                        in = new BufferedInputStream(socket.getInputStream());
                        out = new DataOutputStream(
                                socket.getOutputStream());
                    }

                    while (true) {
                        if (!socket.isClosed()) {
                            if (socket.isConnected()) {
                                if (!socket.isInputShutdown()) {
                                    if (in != null) {


                                        ArrayList<Byte> list = new ArrayList<Byte>();
                                        int count = in.available();
                                        int i = 0;
                                        int ch = in.read();
                                        if(ch == -1){
                                            System.out.println("Service is outof connection !!");
                                            break;
                                        }//如果服务器直接发来-1,说明服务器已经断开
                                        list.clear();
//                                        while (ch != -1 && in.available() > 0) {
                                        while (true) {
                                            if (ch == '\n' || ch == '\r' ){
                                                break;
                                            }else{
                                                //这里应该逐行解析,这里还需要考虑服务段了的情况
                                                list.add((byte) ch);
                                                System.out.println("by hua "+ch);
                                            }
                                            ch = in.read();

                                        }
//                                        list.add((byte) ch);
                                        byte[] bs = new byte[list.size()];
                                        for (int j = 0; j < list.size(); j++) {
                                            bs[j] = list.get(j);
                                        }
//                                        System.out.println("count is " + in.available());
                                        ProtocolUtils.printByte(bs);
//                                        TerProtocolParse parse = new TerProtocolParse();
//                                        BaseCommandData cmd = parse.checkCommand(bs);
                                        WhmBean bean = WhmBean.parse(bs);
                                        if(null == bean){
                                            mMainHandler.sendMessage(getMessageObj(bs, RECV_MSG));
                                        }else {
                                            mMainHandler.sendMessage(getMessageObj(bean, RECV_MSG));
                                        }
//                                        in.close();//这里不能close，如果close，client将不能再处理service数据
                                    }
                                }
                            }
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.d("", "login exception" + ex.getMessage());
                    mMainHandler.sendMessage(getMessageStr("conn err", CONN_ERR));
                }
            }
        });
        thread.start();
    }
}