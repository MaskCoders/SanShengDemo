package com.example.mina.test3;

/**
 * Created by hua on 2/22/16.
 */
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


public class Client {
    public static void main(String[] args) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Client client = new Client();
                client.start();
            }
        });
        thread.start();
    }
    public void start() {
        //创建一个非阻塞的客户端程序
        NioSocketConnector connector = new NioSocketConnector();

        //必须要配置
        connector.getSessionConfig().setUseReadOperation(true);
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        //设置为非延迟发送，为true则不组装成打包发送，收到东西马上发出
        connector.getSessionConfig().setTcpNoDelay(true);
        //添加业务逻辑处理类
        connector.setHandler(new ClientHandler());
        String hostName = "127.0.0.1";
        try {
            InetAddress ipAddress = InetAddress.getByName(hostName);
            int port = 20087;
            //创建连接
            ConnectFuture confuture = connector.connect(new InetSocketAddress(ipAddress, port));
            //等待连接创建完成
            confuture.awaitUninterruptibly();
            if(confuture.isConnected()){
                System.out.println("客户端启动");
                IoSession session = confuture.getSession();
//                byte[] arr = "hello world".getBytes();
//                IoBuffer returnMsg = IoBuffer.wrap(arr);
                Scanner sc = new Scanner(System.in);
                boolean quit = false;

                while (!quit) {

                    String str = sc.next();
                    if (str.equalsIgnoreCase("quit")) {
                        quit = true;
                    }
                    WriteFuture wfuture =  session.write(IoBuffer.wrap(str.getBytes()));
                    wfuture.awaitUninterruptibly();
//                    if(wfuture.isWritten()){
//                        System.out.println("客户端发送成功, 等待服务端反馈信息");
//                        ReadFuture readFuture = session.read();
//                        readFuture.awaitUninterruptibly();
//                        //是否响应成功
//                        if(readFuture.isRead()){
//                            //获取消息
//                            Object message  = readFuture.getMessage();
//                            IoBuffer ioBuffer = (IoBuffer)message;
//                            byte[] b = new byte[ioBuffer.limit()];
//                            ioBuffer.get(b);
//                            String msg=new String(b);
//                            System.out.println("服务端反馈:["+msg+"]");
//                        }
//
//                    }else{
//                        System.out.println("客户端发送失败");
//                    }
                }
//                WriteFuture wfuture = session.write(returnMsg);


            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
