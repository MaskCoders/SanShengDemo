package com.example.mina.test2;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {
    public static final void main(String[] args){
        byte [] message={68,1,1,1,1,68};
        try {
            MinaClient.sendMessage("127.0.0.1:8800", message);
//            MinaClient.sendMessage("127.0.0.1:8800", "quit".getBytes());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ConnectFuture cf=null;
    /**
     * 创建连接
     * @param address
     * @param port
     */
    static void getconnect(String address,int port){
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
//        connector.getFilterChain().addLast(
//                "codec",new ProtocolCodecFilter(new DemuxingProtocolCodecFactory())); // 设置编码过滤器
        connector.setConnectTimeout(300);
        connector.setHandler(new ClientHandler());// 设置事件处理器
        cf = connector.connect(new InetSocketAddress(address,port));// 建立连接
    }
    /**
     * 发送报文
     * @param serviceAddress
     * @param message
     * @throws Exception
     */
    public static void sendMessage(String serviceAddress,byte[] message) throws Exception{
        String[] address = serviceAddress.split(":");
        int port =0;
        try {
            port = Integer.valueOf(address[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new Exception("服务器地址错误！");
        }
        if(cf==null){
            getconnect(address[0],port);
        }

        cf.awaitUninterruptibly();// 等待连接创建完成
//        cf.getSession().write(IoBuffer.wrap(message));// 发送消息 这里是发送字节数组的重点
//        cf.getSession().write('\n');// 发送消息 这里是发送字节数组的重点
//        Thread.sleep(5000);
        cf.getSession().write("xxx");// 发送消息 这里是发送字节数组的重点
//        cf.getSession().write("xx");// 发送消息 这里是发送字节数组的重点
        cf.getSession().getCloseFuture().awaitUninterruptibly();// 等待连接断开
    }

    /**
     * 发送报文String
     * @param serviceAddress
     * @param message
     * @throws Exception
     */
    public static void sendMessage(String serviceAddress,String message) throws Exception{
        String[] address = serviceAddress.split(":");
        int port =0;
        try {
            port = Integer.valueOf(address[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new Exception("服务器地址错误！");
        }
        if(cf==null){
            getconnect(address[0],port);
        }
        cf.awaitUninterruptibly();// 等待连接创建完成
        cf.getSession().write(message);// 发送消息
        cf.getSession().getCloseFuture().awaitUninterruptibly();// 等待连接断开
    }

    /**
     * 回复报文
     * @param serviceAddress
     * @param message
     * @throws Exception
     */
    public static void returnMessage(String serviceAddress,String message) throws Exception{
        String[] address = serviceAddress.split(":");
        int port =0;
        try {
            port = Integer.valueOf(address[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new Exception("服务器地址错误！");
        }
        if(cf==null){
            getconnect(address[0],port);
        }
        cf.awaitUninterruptibly();// 等待连接创建完成
        cf.getSession().write(message);// 发送消息
        cf.getSession().getCloseFuture().awaitUninterruptibly();// 等待连接断开
    }
}
