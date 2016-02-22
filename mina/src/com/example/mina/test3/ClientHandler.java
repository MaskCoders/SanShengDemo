package com.example.mina.test3;

/**
 * Created by hua on 2/22/16.
 */

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable)
            throws Exception {
        System.out.println("发生异常: "+throwable.getMessage());
        session.close(true);

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        IoBuffer ioBuffer = (IoBuffer)message;
        byte[] b = new byte[ioBuffer.limit()];
        ioBuffer.get(b);

        String msg=new String(b);

        System.out.println("收到客户端发来的消息为" + "  " + msg);
    }


    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println(session.getRemoteAddress() + "is Disconnection");

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * 当一个客户端连接进入时
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {

        System.out.println("incoming client: " + session.getRemoteAddress());

    }

}
