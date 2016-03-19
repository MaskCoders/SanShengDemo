package com.sansheng.testcenter.server.mina;

import android.os.Handler;
import com.sansheng.testcenter.base.ConnInter;
import hstt.data.ref;
import hstt.proto.mp07.TaskInterface;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaSocketClient  implements ConnInter {
    NioSocketConnector connector;
    ConnectFuture connectFuture;
    int port;
    String ip;
    Handler mHandler;

    public void MinaSocketClient(Handler handler , String ip ,int port){
        this.port = port;
        this.ip = ip;
        this.mHandler = handler;

    }
    public void runClient(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建客户端连接器.
                connector = new NioSocketConnector();
                connector.getFilterChain().addLast("logger", new LoggingFilter());
                connector.getFilterChain().addLast("codec",
                        new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

                // 设置连接超时检查时间
                connector.setConnectTimeoutCheckInterval(30);
                connector.setHandler(new ClientHandler());

                // 建立连接
                connectFuture = connector.connect(new InetSocketAddress(ip, port));
                // 等待连接创建完成
                connectFuture.awaitUninterruptibly();
//                byte[] tmp = new byte[]{11,22,33,44};
//                connectFuture.getSession().write(tmp);
//        cf.getSession().write(IoBuffer.wrap(message));
                // 等待连接断开
                connectFuture.getSession().getCloseFuture().awaitUninterruptibly();
                // 释放连接
                connector.dispose();
            }
        });
        thread.start();
    }


    @Override
    public void open() throws IOException {
        runClient();
    }

    @Override
    public void close() {
        if(connector != null && !connector.isDisposed() && !connector.isDisposing()){
            connector.dispose();
        }
    }

    @Override
    public void sendMessage(String hex) {
        if(connectFuture != null) {
            connectFuture.getSession().write(hex.getBytes());
        }
    }

    @Override
    public void sendMessage(byte[] arr) {
        if(connectFuture != null) {
            connectFuture.getSession().write(arr);
        }
    }

    @Override
    public void sendMessage(TaskInterface task) {

    }
    ref<String> address;
    @Override
    public void setAddress(ref<String> address) {
        this.address = address;
    }

    @Override
    public String getConnInfo() {
        String info = "err";
        try{
            info = "ip="+connector.getDefaultLocalAddress()+" port="+port;
        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }
}