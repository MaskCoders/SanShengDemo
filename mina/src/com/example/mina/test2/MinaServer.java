package com.example.mina.test2;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by hua on 2/22/16.
 */
public class MinaServer {
    public static final void main(String[] args){
        try {
            startByteService(8800,60);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建byte处理服务器
     * @throws IOException
     */
    public static void startByteService(int port,int time) throws IOException {
        // 创建服务器监听
        IoAcceptor acceptor = new NioSocketAcceptor();
        // 增加日志过滤器
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
//        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new DemuxingProtocolCodecFactory()));
//这里不添加字符编码过滤器
        acceptor.getFilterChain().addLast("exceutor", new ExecutorFilter());
        // 指定业务逻辑处理器
        acceptor.setHandler(new byteServerHandler());
        // 设置buffer的长度
        acceptor.getSessionConfig().setReadBufferSize(2048);
        // 设置连接超时时间
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, time);
        // 绑定端口
        acceptor.bind(new InetSocketAddress(port));
        System.out.println("服务器在端口："+port+"已经启动");
    }


}
