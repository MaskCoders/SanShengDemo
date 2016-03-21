package com.huaonline.mina;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 服务器端业务逻辑
 */
public class TimeServerHandler extends IoHandlerAdapter {

    /**
     * 连接创建事件
     */
    @Override
    public void sessionCreated(IoSession session){
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
        IoBuffer ioBuffer = (IoBuffer)message;
        int limit = ioBuffer.limit();
        System.out.println("limit = "+ limit);
        byte[] b = new byte[limit];
        ioBuffer.get(b);
        System.out.println("收到客户端发来的消息为: [" + ProtocolUtils.bytes2hex(b)+"]");

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
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE" + session.getIdleCount(status));
    }

}