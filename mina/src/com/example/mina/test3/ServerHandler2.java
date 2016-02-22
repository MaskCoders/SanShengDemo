package com.example.mina.test3;

/**
 * Created by hua on 2/22/16.
 */
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ServerHandler2 extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable)
            throws Exception {
        System.out.println("发生异常: " +throwable.getMessage());
        session.close(true);

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        IoBuffer ioBuffer = (IoBuffer)message;
        int limit = ioBuffer.limit();
        System.out.println("limit = "+ limit);
        byte[] b = new byte[6];
        ioBuffer.get(b);

        String msg=new String(b);

        byte[] bb = new byte[limit-6];
        ioBuffer.get(bb);

        String msg2=new String(bb);

        System.out.println("收到客户端发来的消息为: [" + msg+msg2+"]");

        brodcast(session);

    }

    private void brodcast(IoSession session) {
        System.out.println("回复客户端");
        byte[] returnStr = "收到".getBytes();
        //将测试消息会送给客户端
        IoBuffer returnMsg = IoBuffer.wrap(returnStr);

        WriteFuture wfutrue =session.write(returnMsg);

        wfutrue.awaitUninterruptibly();
        if(wfutrue.isWritten()){
            System.out.println("服务端回复完毕");
        }else{
            System.out.println("服务端回复失败");
        }
    }


    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println(session.getRemoteAddress() + "is Disconnection");
        session = null;
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
