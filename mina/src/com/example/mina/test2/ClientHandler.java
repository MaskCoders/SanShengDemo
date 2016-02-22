package com.example.mina.test2;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * Created by hua on 2/22/16.
 */
public class ClientHandler extends IoHandlerAdapter {

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("==> ");
        System.out.println("==> "+message);
        session.close();
        super.messageReceived(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        IoBuffer ioBuffer = (IoBuffer)message;
        byte[] bytes = ioBuffer.array();
        for(byte b : bytes){
            System.out.print(b+" ");
        }
        System.out.println();
        super.messageSent(session, message);
    }
}
