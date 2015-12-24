package com.sansheng.testcenter.server;

import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.controller.MainHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hua on 12/18/15.
 */
public class SocketServer3 {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ServerSocket server = null;
    Socket client = null;
    BufferedReader serin = null;
    private String msg = "";
    private MainHandler mainHandler;
    public SocketServer3(MainHandler handler){
        mainHandler = handler;
    }
    public void startServer() {
        System.out.println(" by hua start server ... ");
//        if(server != null && server.isBound() ){
//            Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip3), 0).show();
//        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(Const.PORT);

                    while (true) {
                        client = server.accept();
                        System.out.println(" by hua client conn ... ");
                        serin = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        msg = Const.CONN_SUCCESS + client.getInetAddress();
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