package com.sansheng.testcenter.server;

import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.tools.ProtocolCreater;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 12/18/15.
 */
public class SocketServer {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ServerSocket server = null;
    //    Socket client = null;
    private ArrayList<ConnClient> clientList = new ArrayList<ConnClient>();

    private MainHandler mainHandler;
    public SocketServer(MainHandler handler){
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
                        Socket socket = server.accept();
                        ConnClient client = new ConnClient(socket);
                        clientList.add(client);
                        System.out.println(" by hua client conn ... ");
                        String msg = Const.CONN_SUCCESS + socket.getInetAddress();
                        sendmsg(socket.getInetAddress().toString(),msg,false);
                        (new Thread(client)).start();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }
    public List<ConnClient> getClients(){
        return clientList;
    }
    public void sendmsg(String ip,String msg,boolean all) {
        for(ConnClient client : clientList){
            if(all || (client.connStatus && equals(client.clientIP))) {
                client.sendmsg(msg);
            }
        }
    }
    public static class ConnClient implements Runnable{
        public String clientName;
        public String clientIP;
        public boolean connStatus = false;
        private Socket mSocket;
        private BufferedReader serin = null;
        public Socket getSocket(){
            return mSocket;
        }
        public ConnClient(Socket socket){
            mSocket = socket;
            connStatus = true;
            try {
                serin = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                connStatus = false;
            }
        }

        public void sendmsg(String msg) {
                if(connStatus) {
                    BufferedOutputStream pout = null;
                    try {

                        pout = new BufferedOutputStream(getSocket().getOutputStream());
                        ProtocolCreater creater = new ProtocolCreater();
                        while(true) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                            String str = br.readLine();
                            System.out.println("you input cmd is "+str);
                            if(!str.equalsIgnoreCase("end")) {
                                byte[] cmd = creater.makeCommand(null,null,null);
                                pout.write(cmd);
                                pout.flush();
                            }else{
                                System.out.println("we will quit ====> ...");
                                System.exit(0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                    }
                }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String msg="";
                    if ((msg = serin.readLine()) != null) {
                        System.out.println(" by hua client info is "+msg);
                        if (msg.equals("0x68 0x16")) {
                            msg = "0x16 0x68";
                        } else {
                            msg = "ERRCODE";
                        }
                        sendmsg(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
