package com.sansheng.testcenter.server.mina;

import android.os.Handler;
import com.sansheng.testcenter.base.ConnInter;
import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;
import hstt.data.DataItem;
import hstt.data.ref;
import hstt.proto.IProto;
import hstt.proto.ProtoFactory;
import hstt.proto.ProtoType;
import hstt.proto.mp07.TaskInterface;
import hstt.proto.upgw.GwTask;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
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
    Handler mainHandler;
    int protocol_type;
    private TaskInterface task;
    private byte[] byteBuffer = null;

    public  MinaSocketClient(Handler handler , String ip ,int port, int type){
        this.port = port;
        this.ip = ip;
        this.mainHandler = handler;
        protocol_type = type;

    }
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

                connector.setHandler(new ClientHandler());

                // 建立连接
                connectFuture = connector.connect(new InetSocketAddress(ip, port));
                // 等待连接创建完成
                connectFuture.awaitUninterruptibly();
//                byte[] tmp = new byte[]{11,22,33,44};
//                cf.getSession().write(tmp);
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
    public void cancel() {
        if(connector != null && !connector.isDisposed() && !connector.isDisposing()
                && !connectFuture.isCanceled()){
            connectFuture.cancel();
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
        this.task = task;
        IProto mp = null;
        if(task instanceof GwTask){
            mp = ProtoFactory.Create(ProtoType.UpGw);
        }else{
            mp = ProtoFactory.Create(ProtoType.Mp07);
        }
        sendMessage(mp.Build(task));
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

    class ClientHandler extends IoHandlerAdapter {

        public void messageReceived(IoSession session, Object message) throws Exception {
            IoBuffer ioBuffer = (IoBuffer) message;
            int limit = ioBuffer.limit();
            byte[] b = new byte[limit];
            ioBuffer.get(b);
            byte[] buffer = b;
            try {

                IProto mp = null;

                switch (protocol_type) {
                    case BeanMark.METER_PROTOCOL:
                        mp = ProtoFactory.Create(ProtoType.Mp07);
                        byte[][] validPackets = mp.SearchValid(buffer, buffer.length);
                        if(validPackets[0].length == 0){
                            // == 0 is inValid
                            byteBuffer =  ProtocolUtils.byteMerger(byteBuffer,buffer);
                        }else{
                            if(task != null){
                                DataItem dataItem = (DataItem) mp.Parse(task, validPackets[0]);
                                MainHandler.sendMessage(mainHandler, Const.RECV_MSG,dataItem);
                                byteBuffer = null;
                            }else{
                                //解析错误
                                MainHandler.sendMessage(mainHandler,Const.RECV_MSG_PARSE_ERR,"");
                            }
                        }
                        break;

                    case BeanMark.GW_PROTOCOL:
                        mp = ProtoFactory.Create(ProtoType.UpGw);
                        validPackets = mp.SearchValid(buffer, buffer.length);
                        if(validPackets[0].length == 0){
                            // == 0 is inValid
                            byteBuffer =  ProtocolUtils.byteMerger(byteBuffer,buffer);
                        }else if(task != null){
                            DataItem dataItem = (DataItem) mp.Parse(task, validPackets[0]);
                            MainHandler.sendMessage(mainHandler,Const.RECV_MSG,dataItem);
                            byteBuffer = null;
                        }else{
                            // 解析错误
                            MainHandler.sendMessage(mainHandler,Const.RECV_MSG_PARSE_ERR,"");
                        }


                        break;
                }


//			callback.setValue(bean);
            } catch (Exception e) {
                MainHandler.sendMessage(mainHandler,Const.RECV_MSG_PARSE_ERR,ProtocolUtils.bytes2hex(buffer));
            }
        }

        public void messageSent(IoSession session, Object message) throws Exception {
            System.out.println("messageSent -> ：" + message);
        }

    }
}