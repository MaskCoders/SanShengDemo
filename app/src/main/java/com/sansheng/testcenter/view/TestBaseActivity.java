package com.sansheng.testcenter.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.callback.IServiceHandler;
import com.sansheng.testcenter.controller.ConnectionService;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.server.MSocketServer;

import java.io.*;
import java.net.Socket;

/**
 * Created by hua on 12/17/15.
 */
public class TestBaseActivity extends BaseActivity implements IServiceHandler {
    Button text1;
    Button text2;
    Button text4;
    Button text3;
    Button conn1;
    Button conn2;
    Button conn4;
    Button conn3;
    private MainHandler mMainHandler;
    private MSocketServer myService;  //我们自己的service
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        mMainHandler = new MainHandler();
        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
        main_sort_log.setText("show the sort log");
        main_whole_log.setText("show long test\nshow long test\nshow long test\nshow long test\nshow long test\n");
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.testbuttonlist,null);
        text1  = (Button) inflate.findViewById(R.id.text1);
        text2  = (Button) inflate.findViewById(R.id.text2);
        text3  = (Button) inflate.findViewById(R.id.text3);
        text4  = (Button) inflate.findViewById(R.id.text4);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        main_button_list.addView(inflate);

    }

    @Override
    protected void initConnList() {
        View inflate = getLayoutInflater().inflate(R.layout.connbuttonlist,null);
        conn1  = (Button) inflate.findViewById(R.id.conn1);
        conn2  = (Button) inflate.findViewById(R.id.conn2);
        conn3  = (Button) inflate.findViewById(R.id.conn3);
        conn4  = (Button) inflate.findViewById(R.id.conn4);
        conn1.setOnClickListener(this);
        conn2.setOnClickListener(this);
        conn3.setOnClickListener(this);
        conn4.setOnClickListener(this);
        main_layout_conn.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text1:
                showShortLog(true);
                break;
            case R.id.text2:
                showWholeLog(true);
                break;
            case R.id.text3:
                showShortLog(false);
                break;
            case R.id.text4:
                showWholeLog(false);
                break;
            case R.id.conn1:
                Intent intent = new Intent(TestBaseActivity.this,MSocketServer.class);
                startService(intent);
                break;
            case R.id.conn2:
                intent = new Intent(TestBaseActivity.this,MSocketServer.class);
                ConnectionService connSer = new ConnectionService(mMainHandler,myService);
                bindService(intent, connSer, Context.BIND_AUTO_CREATE);
                break;
            case R.id.conn3:
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
//                                                mHandler.sendMessage(getMessage(content, RECV_MSG));
                                                System.out.println("by hua -- "+content);
                                            } else {

                                            }
                                        }
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            ex.printStackTrace();
                            Log.d("", "login exception" + ex.getMessage());
//                            mHandler.sendMessage(getMessage("conn err", CONN_ERR));
                        }
                    }
                });

                thread.start();
                break;
            case R.id.conn4:
                if (socket != null && socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println("xx");
                    }
                }
                break;
        }
    }

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private static final int PORT = 8001;
    private static final String HOST = "127.0.0.1";
    private void startClient(){
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
//                                        ((Handler)mHandler).sendMessage(getMessage(content, RECV_MSG));
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
