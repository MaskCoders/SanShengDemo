package com.sansheng.testcenter.server.mina;

import com.sansheng.testcenter.base.ConnInter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

public class MinaSocketServer  implements ConnInter {
    // 定义监听端口
    private static final int PORT = 6488;
    private IoAcceptor acceptor;
    private void runServer(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    // 创建服务端监控线程
                    acceptor = new NioSocketAcceptor();
                    acceptor.getSessionConfig().setReadBufferSize(2048);
                    acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
                    // 设置日志记录器
                    acceptor.getFilterChain().addLast("logger", new LoggingFilter());
                    // 设置编码过滤器
                    acceptor.getFilterChain().addLast(
                            "codec",
                            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
                    // 指定业务逻辑处理器
                    // 设置端口号
                    acceptor.bind(new InetSocketAddress(PORT));
                    acceptor.setHandler(new ServerHandler());
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

    @Override
    public void sendMessage(String hex) {
        Map<Long, IoSession> map =  acceptor.getManagedSessions();

        for(IoSession session :  map.values()){
            session.write(hex.getBytes());
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
    public String getConnInfo() {
        String info = "err";
        try{
            info = "ip="+acceptor.getLocalAddress()+" port="+PORT;
        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }
}