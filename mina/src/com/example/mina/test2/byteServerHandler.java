package com.example.mina.test2;

/**
 * Created by hua on 2/22/16.
 */

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 自定义的消息处理器，必须实现IoHandlerAdapter类
 *
 * @author 何明
 */
public class byteServerHandler extends IoHandlerAdapter {

    private int count = 0;

    /**
     * 当一个客户端连接进入时
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {

        System.out.println("incoming client: " + session.getRemoteAddress());
        session.write("conn ok");

    }

    /**
     * 当一个客户端关闭时
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {

        System.out.println(session.getRemoteAddress() + "is Disconnection");

    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if(message.toString().length()<5){

            IoBuffer ioBuffer = (IoBuffer)message;
            byte[] bytes = ioBuffer.array();
            System.out.println("ioBuffer.limit() is "+ioBuffer.limit());
            for(int i=0; i<ioBuffer.limit();i++){
                System.out.print(bytes[i]+" ");
            }
            System.out.println();
            byte[] b = new byte[ioBuffer.limit()];
            ioBuffer.get(b);

            String msg = new String(b);
            message = msg;
            System.out.println("收到客户端发来的消息为" + "  " + message.toString());

            //将测试消息会送给客户端
            session.write(IoBuffer.wrap("test".getBytes()));
            session.write(" ");
        }else{
            System.out.println("收到客户端发来的消息为" + "  " + message.toString());
            session.write(IoBuffer.wrap("test2".getBytes()));
            session.write(" ");
        }
    }

}
