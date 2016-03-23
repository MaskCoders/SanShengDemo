package com.huaonline.mina;

        import java.io.IOException;
        import java.net.InetSocketAddress;
        import java.util.Iterator;

        import org.apache.mina.core.buffer.IoBuffer;
        import org.apache.mina.core.future.WriteFuture;
        import org.apache.mina.core.service.IoAcceptor;
        import org.apache.mina.core.session.IoSession;
        import org.apache.mina.filter.executor.ExecutorFilter;
        import org.apache.mina.filter.logging.LoggingFilter;
        import org.apache.mina.transport.socket.nio.NioSession;
        import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaTimeServer {
    // 定义监听端口
    private static final int PORT = 6488;
    NioSocketAcceptor acceptor;
    public void runServer(){
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
                    acceptor.setHandler(new TimeServerHandler(acceptor));
                    // 设置端口号
                    acceptor.bind(new InetSocketAddress(PORT));
                    // 启动监听线程
                    acceptor.bind();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
    public void sendMessage(String msg){
        Iterator it =  acceptor.getManagedSessions().keySet().iterator();
        while(it.hasNext()){
            NioSession obj = (NioSession) acceptor.getManagedSessions().get(it.next());
            if(obj != null && obj.isActive() && obj.isConnected()){
                System.out.println("回复客户端");
                byte[] returnStr = msg.getBytes();
                //将测试消息会送给客户端
                IoBuffer returnMsg = IoBuffer.wrap(returnStr);

                WriteFuture wfutrue =obj.write(returnMsg);

                wfutrue.awaitUninterruptibly();
                if(wfutrue.isWritten()){
                    System.out.println("服务端回复完毕");
                }else{
                    System.out.println("服务端回复失败");
                }
            }
        }

    }
    public void close(){
        try{
            acceptor.unbind();
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
    }
}