//package com.sansheng.testcenter.server;
//
//import android.content.Context;
//import com.sansheng.testcenter.controller.MainHandler;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by hua on 16-1-1.
// */
//public class ClientManager {
//    public List<SocketClient> clientList = new ArrayList<SocketClient>();
//    private static ClientManager manager;
//    private Context context;
//    private MainHandler mHandler;
//
//    private ClientManager(Context ctx, MainHandler handler) {
//        this.context = ctx;
//        this.mHandler = handler;
//    }
//
//    public static ClientManager getInstance(Context ctx, MainHandler handler) {
//        if (manager == null) {
//            manager = new ClientManager(ctx, handler);
//        }
//        return manager;
//    }
//
//    public SocketClient createClient(String ip, int port) {
//        SocketClient client = new SocketClient(context, mHandler, ip, port, this);
//            client.startClient();
//            clientList.add(client);
//        return client;
//    }
//
//    public void rmClient(SocketClient cli) {
//        cli.stopClient();
//        clientList.remove(cli);
//    }
//
//    public void clearClients() {
//        for (SocketClient cli : clientList) {
//            cli.stopClient();
//        }
//        clientList.clear();
//    }
//    private void getClient(){
//
//    }
////    public void sendMessage(SocketClient client, String msg) {
////        if (client == null) {
////            for (SocketClient cli : clientList) {
////                cli.sendMessage(msg);
////            }
////        }else{
////            client.sendMessage(msg);
////        }
////    }
//    public void sendMessage(SocketClient client, byte[] data) {
//        if (client == null) {
//            for (SocketClient cli : clientList) {
//                cli.sendMessage(data);
//            }
//        }else{
//            client.sendMessage(data);
//        }
//    }
//
////    public void sendMessage(SocketClient client, byte[] msgArr) {
////        sendMessage(client,new String(msgArr));
////    }
//}
