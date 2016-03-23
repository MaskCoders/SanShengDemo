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
import hstt.proto.upgw.UpGw;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public class MinaSocketServer  implements ConnInter {
    // 定义监听端口
    private NioSocketAcceptor acceptor;
    int port;
    String ip;
    Handler mainHandler;
    int protocol_type;
    private TaskInterface task;
    private byte[] byteBuffer = null;
    private ref<String> address;

    public  MinaSocketServer(Handler handler , String ip ,int port, int type){
        this.port = port;
        this.ip = ip;
        this.mainHandler = handler;
        protocol_type = type;

    }

    private void runServer(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    // 创建服务端监控线程
                    acceptor = new NioSocketAcceptor();
//                    acceptor.getSessionConfig().setReadBufferSize(2048);
//                    acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
                    // 设置日志记录器
                    acceptor.getFilterChain().addLast("logger", new LoggingFilter());
                    // 设置编码过滤器
                    //                    acceptor.getFilterChain().addLast(
//                            "codec",
//                            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
                    //同时客户端要设置connector.getSessionConfig().setUseReadOperation(true);
                    acceptor.getFilterChain().addLast("exceutor", new ExecutorFilter());
                    //设置为非延迟发送，为true则不组装成打包发送，收到东西马上发出
                    acceptor.getSessionConfig().setTcpNoDelay(true);
                    // 指定业务逻辑处理器
                    acceptor.setHandler(new ServerHandler());
                    // 设置端口号
                    acceptor.bind(new InetSocketAddress(port));
                    // 启动监听线程
                    acceptor.bind();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    @Override
    public void open() throws IOException {
        runServer();
    }
    @Override
    public void close() {
        if(acceptor != null){
            acceptor.unbind();
        }
    }
    private void getClientList(){
    }

    @Override
    public void sendMessage(String hex) {
        Map<Long, IoSession> map =  acceptor.getManagedSessions();

        for(IoSession session :  map.values()){
            if(session!=null && session.isActive() && session.isConnected()){
                session.write(IoBuffer.wrap(hex.getBytes()));
            }
        }
    }

    @Override
    public void sendMessage(byte[] arr) {
        Map<Long, IoSession> map =  acceptor.getManagedSessions();

        for(IoSession session :  map.values()){
            session.write(arr);
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

    @Override
    public void setAddress(ref<String> address) {
        this.address = address;
    }

    @Override
    public String getConnInfo() {
        String info = "err";
        try{
            info = "ip="+acceptor.getLocalAddress()+" port="+port;
        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 服务器端业务逻辑
     */
    class ServerHandler extends IoHandlerAdapter {

        /**
         * 连接创建事件
         */
        @Override
        public void sessionCreated(IoSession session) {
            // 显示客户端的ip和端口
            System.out.println(session.getRemoteAddress().toString());
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            cause.printStackTrace();
        }

        /**
         * 消息接收事件
         */
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            IoBuffer ioBuffer = (IoBuffer) message;
            int limit = ioBuffer.limit();
            byte[] b = new byte[limit];
            ioBuffer.get(b);
            byte[] buffer = b;
            UpGw upGw = new UpGw();
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
                        byte[] data = validPackets[0];
                        if(data.length == 0){
                            // == 0 is inValid
                            byteBuffer =  ProtocolUtils.byteMerger(byteBuffer,buffer);
                        }else if(upGw.IsXinTiaoPacket(buffer,address))/*处理心跳 应答*/{
                            //// TODO: 3/23/16  get the right params
                            byte[] answer = upGw.XinTiaoAnswer(address.v,buffer);
                            sendMessage(answer);
                        }
                        else if(task != null){
                            DataItem dataItem = (DataItem) mp.Parse(task, data);
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
            System.out.println("收到客户端发来的消息为: [" + ProtocolUtils.bytes2hex(b) + "]");
            brodcast(session);
        }

        private void brodcast(IoSession session) {
            System.out.println("回复客户端");
            byte[] returnStr = "收到".getBytes();
            //将测试消息会送给客户端
            IoBuffer returnMsg = IoBuffer.wrap(returnStr);

            WriteFuture wfutrue = session.write(returnMsg);

            wfutrue.awaitUninterruptibly();
            if (wfutrue.isWritten()) {
                System.out.println("服务端回复完毕");
            } else {
                System.out.println("服务端回复失败");
            }
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            System.out.println("IDLE" + session.getIdleCount(status));
        }
    }
}