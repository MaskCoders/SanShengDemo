package com.sansheng.testcenter.demo.view;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sansheng.testcenter.R;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketDemo extends BaseActivity implements View.OnClickListener {
    private EditText ed_log = null;
    private TextView tv_info = null;
    private EditText ed_msg = null;
    private Button btn_send = null;
    private Button btn_conn = null;
    private Button btn_cls = null;
    private Button btn_ser = null;
    // private Button btn_login = null;
//    private static final String HOST = "192.168.134.82";
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8001;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    private static final int CONN_SER_CLS = -2;
    private static final int CONN_ERR = -1;
    private static final int CONN_OK = 0;
    private static final int RECV_MSG = 1;
    private static final int INPUT_ERR = -3;
    private static final String ERRCODE = "ERRCODE";
    private static final String CONN_SUCCESS = "CONN_SUCCESS";

    //接收线程发送过来信息，并用TextView显示
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = msg.obj.toString();
            switch (msg.what) {
                case CONN_SER_CLS:
                    break;
                case RECV_MSG:
                    if (content.contains(ERRCODE)) {
                        content = SocketDemo.this.getResources().getString(R.string.conn_ser_err2);
                    } else if (content.contains(CONN_SUCCESS)) {
                        tv_info.setText(SocketDemo.this.getResources().getString(R.string.server)
                                + content.replace(CONN_SUCCESS, "").replace("\n", "") + "," +
                                SocketDemo.this.getResources().getString(R.string.ser_port) + PORT);
//                        btn_ser.setText(SocketDemo.this.getResources().getString(R.string.start_ser_close));
                        return;
                    }
                    break;
                case CONN_OK:
                    break;
                case CONN_ERR:
                    content = SocketDemo.this.getResources().getString(R.string.conn_ser_err1) + ":" + content;
                default:
            }
            SpannableString ss = new SpannableString(ed_log.getText().toString() + "\n " + SocketDemo.this.getResources().getString(R.string.server) + content);
//            ss.setSpan(new ForegroundColorSpan(SocketDemo.this.getResources().getColor(R.color.download_text_color)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ed_log.setText(ss);
            ed_log.setSelection(ed_log.getText().length() - 1);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        ed_log = (EditText) findViewById(R.id.log);
        tv_info = (TextView) findViewById(R.id.info);
        ed_log.setMovementMethod(ScrollingMovementMethod.getInstance());
        ed_msg = (EditText) findViewById(R.id.et);
        btn_send = (Button) findViewById(R.id.send);
        btn_conn = (Button) findViewById(R.id.conn);
        btn_cls = (Button) findViewById(R.id.cls);
        btn_ser = (Button) findViewById(R.id.startser);
        ed_msg.setText("0x68 0x16");
        btn_ser.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_conn.setOnClickListener(this);
        btn_cls.setOnClickListener(this);
        setActionBar(SOCKET_VIEW);
    }

    private Message getMessage(String content, int type) {
        Message msg = new Message();
        msg.obj = content;
        msg.what = type;
        return msg;
    }

    private boolean connSerLogic() {
        if (server == null || server.isClosed()) {
            Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip1), Toast.LENGTH_SHORT).show();
            return false;
        } else if (client == null || !client.isConnected()) {
            Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip2), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.conn:
                if (server == null || server.isClosed()) {
                    Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip1), Toast.LENGTH_SHORT).show();
                }
                if(client!=null && client.isConnected()){
                    Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip4), Toast.LENGTH_SHORT).show();
                    break;
                }
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
                                                mHandler.sendMessage(getMessage(content, RECV_MSG));
                                            } else {

                                            }
                                        }
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            ex.printStackTrace();
                            Log.d("", "login exception" + ex.getMessage());
                            mHandler.sendMessage(getMessage("conn err", CONN_ERR));
                        }
                    }
                });
                thread.start();
                break;

            case R.id.send:
                if (!connSerLogic()) {
                    return;
                }
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this, this.getResources().getString(R.string.conn_ser_err3), 0).show();
                    break;
                }
                String msg = ed_msg.getText().toString();
                SpannableString ss = new SpannableString(ed_log.getText().toString() + "\n " + SocketDemo.this.getResources().getString(R.string.client) + msg);
//                ss.setSpan(new ForegroundColorSpan(SocketDemo.this.getResources().getColor(R.color.head_icon_bg_color0)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                ed_log.setText(ss);
                if (socket != null && socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println(msg);
                    }
                }
                break;
            case R.id.cls:
                ed_log.setText("");
                break;
            case R.id.startser:
                startServer();
                break;
        }
    }

    private ServerSocket server = null;
    Socket client = null;
    BufferedReader serin = null;
    private String msg = "";

    private void startServer() {
        if(server != null && server.isBound() ){
            Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip3), 0).show();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(PORT);

                    while (true) {
                        client = server.accept();
                        serin = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        msg = CONN_SUCCESS + client.getInetAddress();
                        sendmsg();
                        Thread send = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        if ((msg = serin.readLine()) != null) {
                                            //当客户端发送的信息为：exit时，关闭连接
//                            if(msg.equals("exit")) {
//                                System.out.println("ssssssss");
//                                mList.remove(socket);
//                                in.close();
//                                msg = "user:" + socket.getInetAddress()
//                                    + "exit total:" + mList.size();
//                                socket.close();
//                                this.sendmsg();
//                                break;
//                                //接收客户端发过来的信息msg，然后发送给客户端。
//                            } else {
                                            if (msg.equals("0x68 0x16")) {
                                                msg = "0x16 0x68";
                                            } else {
                                                msg = "ERRCODE";
                                            }
                                            sendmsg();
//                            }
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
}