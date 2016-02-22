package com.example.mina.test3;

/**
 * Created by hua on 2/22/16.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Server server = new Server();
        boolean flag = server.start();
        if(flag) System.out.println("服务端开始");
    }
    static final int PORT = 20087;
    NioSocketAcceptor acceptor = null;

    public Server() {

    }

    public boolean start() {
        //创建一个非阻塞的server端的Socket
        acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setUseReadOperation(true);
        //绑定过滤器
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        //必须要设置ExecutorFilter，否则无法messageReceived中向客户端反馈同步消息会在wfutrue.awaitUninterruptibly()处长期挂起
        //同时客户端要设置connector.getSessionConfig().setUseReadOperation(true);
        acceptor.getFilterChain().addLast("exceutor", new ExecutorFilter());
        //设置为非延迟发送，为true则不组装成打包发送，收到东西马上发出
        acceptor.getSessionConfig().setTcpNoDelay(true);

        //绑定处理类
        acceptor.setHandler(new ServerHandler2());
        try {
            //绑定端口
            acceptor.bind(new InetSocketAddress(PORT));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
