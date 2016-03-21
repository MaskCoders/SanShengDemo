package com.huaonline.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaTimeClient {
    NioSocketConnector connector;
    ConnectFuture cf;
    public void runClient(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建客户端连接器.
                connector = new NioSocketConnector();
                connector.getSessionConfig().setUseReadOperation(true);

                connector.getFilterChain().addLast("logger", new LoggingFilter());

                connector.getFilterChain().addLast("exceutor", new ExecutorFilter());
                connector.getFilterChain().addLast("codec",
                        new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
                connector.getSessionConfig().setTcpNoDelay(true);
                // 设置连接超时检查时间
//                connector.setConnectTimeoutCheckInterval(30);

                connector.setHandler(new TimeClientHandler());

                // 建立连接
                cf = connector.connect(new InetSocketAddress("localhost", 6488));
                // 等待连接创建完成
                cf.awaitUninterruptibly();
//                byte[] tmp = new byte[]{11,22,33,44};
//                cf.getSession().write(tmp);
//        cf.getSession().write(IoBuffer.wrap(message));

                // 等待连接断开
                cf.getSession().getCloseFuture().awaitUninterruptibly();
                // 释放连接
                connector.dispose();
            }
        });
        thread.start();
    }
    public void sendMessage(){
        if(cf!=null){
            byte[] tmp = new byte[]{11,22,33,44};
            cf.getSession().write(IoBuffer.wrap(tmp));
        }
    }
    public void closeClient(){
        try{
            connector.dispose();
        }catch (Exception e){
            e.getStackTrace();
        }

    }
    public static void main(String[] args){
        // 创建客户端连接器.
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // 设置连接超时检查时间
        connector.setConnectTimeoutCheckInterval(30);
        connector.setHandler(new TimeClientHandler());

        // 建立连接
        ConnectFuture cf = connector.connect(new InetSocketAddress("localhost", 6488));
        // 等待连接创建完成
        cf.awaitUninterruptibly();
        byte[] tmp = new byte[]{11,22,33,44};
        cf.getSession().write(tmp);
//        cf.getSession().write(IoBuffer.wrap(message));

        // 等待连接断开
        cf.getSession().getCloseFuture().awaitUninterruptibly();
        // 释放连接
        connector.dispose();
    }
}