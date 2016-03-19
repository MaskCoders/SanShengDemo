package com.sansheng.testcenter.tools.serial;


import android.os.Handler;
import android.os.Message;
import android_serialport_api.SerialPort;
import com.sansheng.testcenter.base.ConnInter;
import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.ComBean;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;
import hstt.data.DataItem;
import hstt.data.ref;
import hstt.proto.IProto;
import hstt.proto.ProtoFactory;
import hstt.proto.ProtoType;
import hstt.proto.mp07.TaskInterface;
import hstt.proto.upgw.GwTask;
import hstt.proto.upgw.UpGw;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author benjaminwan
 *         ���ڸ������
 */
public class SerialHelper implements ConnInter {
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private Handler mainHandler;
    public String sPort = "/dev/s3c2410_serial0";
    public int iBaudRate = 9600;
    private boolean _isOpen = false;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 500;
    private int protocol_type = BeanMark.METER_PROTOCOL;
    ref<String> address;
    private TaskInterface task;
    //----------------------------------------------------
    public SerialHelper() {
    }

    public SerialHelper(String sPort, int iBaudRate, Handler handler, int type) {
        this.sPort = sPort;
        this.iBaudRate = 2400;
        mainHandler = handler;
        protocol_type = type;

    }

    //----------------------------------------------------
    public void open() throws SecurityException, IOException, InvalidParameterException {
        mSerialPort = new SerialPort(new File(sPort), iBaudRate, 8, 1, 'E', 0);
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        mReadThread = new ReadThread();
        mReadThread.start();
        mSendThread = new SendThread();
        mSendThread.setSuspendFlag();
        mSendThread.start();
        _isOpen = true;
    }

    public boolean hasreturn;
    private Thread timer;

    private void startTimer() {
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60000);//1分钟后停止线程
                    if (mReadThread != null && !hasreturn) {
                        mReadThread.interrupt();
                        Message msg = new Message();
                        msg.what = Const.OVER_TIME;
                        System.out.println("====>  serial recive over time");
                        mainHandler.sendMessage(msg);
//						callback.setValue(null);//为了结束activiey一些锁死的状态
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //----------------------------------------------------
    public void close() {
        stopSend();
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        _isOpen = false;
    }


    //----------------------------------------------------
    private void send(byte[] bOutArray) {
        try {
            mOutputStream.write(bOutArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String tempCommand = "";

    //----------------------------------------------------
    public void sendHex(String sHex) {
        tempCommand = sHex;
//		String hex = "fefefefe68 12 00 00 00 10 20 68 11 04 33 32 34 33 F3 16".replace(" ","");
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        send(bOutArray);
    }

    @Override
    public void sendMessage(String hex) {
        tempCommand = hex;
//		String hex = "fefefefe68 12 00 00 00 10 20 68 11 04 33 32 34 33 F3 16".replace(" ","");
        byte[] bOutArray = MyFunc.HexToByteArr(hex);
        sendMessage(bOutArray);
    }

    @Override
    public void sendMessage(byte[] arr) {
        send(arr);
    }

    @Override
    public void sendMessage(TaskInterface task) {
        this.task = task;
        IProto mp = null;
        if(task instanceof GwTask){
            mp = ProtoFactory.Create(ProtoType.UpGw);
        }else{
            mp = ProtoFactory.Create(ProtoType.Mp07);
        }

        sendMessage(mp.Build(task));
    }

    @Override
    public void setAddress(ref<String> address) {
     this.address = address;
    }

    @Override
    public String getConnInfo() {
        return "Port is " + getPort() + " , Rate is " + getBaudRate();
    }

    //----------------------------------------------------
//	public void sendTxt(String sTxt){
//		byte[] bOutArray =sTxt.getBytes();
//		tempCommand = sTxt;
////		String hex = "fefefefe68aaaaaaaaaaaa681300df16";
////		bOutArray = ProtocolUtils.hexStringToBytes(hex);
////		System.out.println("hua : "+hex);
//		send(bOutArray);
//	}
    //----------------------------------------------------
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            startTimer();//开启超时监控
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) return;
                    byte[] buffer = new byte[512];
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        ComBean ComRecData = new ComBean(sPort, buffer, size);
                        onDataReceived(ComRecData);
                        hasreturn = true;
                        timer.interrupt();

                    }
                    try {
                        Thread.sleep(50);//��ʱ50ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    //----------------------------------------------------
    private class SendThread extends Thread {
        public boolean suspendFlag = true;// �����̵߳�ִ��

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    while (suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                send(getbLoopData());
                try {
                    Thread.sleep(iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //�߳���ͣ
        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        //�����߳�
        public synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }
    }

    //----------------------------------------------------
    public int getBaudRate() {
        return iBaudRate;
    }

    public boolean setBaudRate(int iBaud) {
        if (_isOpen) {
            return false;
        } else {
            iBaudRate = iBaud;
            return true;
        }
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return setBaudRate(iBaud);
    }

    //----------------------------------------------------
    public String getPort() {
        return sPort;
    }

    public boolean setPort(String sPort) {
        if (_isOpen) {
            return false;
        } else {
            this.sPort = sPort;
            return true;
        }
    }

    //----------------------------------------------------
    public boolean isOpen() {
        return _isOpen;
    }

    //----------------------------------------------------
    public byte[] getbLoopData() {
        return _bLoopData;
    }

    //----------------------------------------------------
    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    //----------------------------------------------------
    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    //----------------------------------------------------
    public void setHexLoopData(String sHex) {
        this._bLoopData = MyFunc.HexToByteArr(sHex);
    }

    //----------------------------------------------------
    public int getiDelay() {
        return iDelay;
    }

    //----------------------------------------------------
    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    //----------------------------------------------------
    public void startSend() {
        if (mSendThread != null) {
            mSendThread.setResume();
        }
    }

    //----------------------------------------------------
    public void stopSend() {
        if (mSendThread != null) {
            mSendThread.setSuspendFlag();
        }
    }

    //----------------------------------------------------
    private byte[] byteBuffer = null;

    public void onDataReceived(ComBean ComRecData) {
        byte[] buffer = ComRecData.bRec;
        try {

            IProto mp = null;

            switch (protocol_type) {
                case BeanMark.METER_PROTOCOL:
                    mp = ProtoFactory.Create(ProtoType.Mp07);
                    byte[][] validPackets = mp.SearchValid(buffer, buffer.length);
                    if(validPackets[0].length == 0){
                        // == 0 is inValid
                        byteBuffer =  ProtocolUtils.byteMerger(byteBuffer,buffer);
                    }else{
                        if(task != null){
                            DataItem dataItem = (DataItem) mp.Parse(task, validPackets[0]);
                            MainHandler.sendMessage(mainHandler,Const.RECV_MSG,dataItem);
                            byteBuffer = null;
                        }else{
                            //解析错误
                            MainHandler.sendMessage(mainHandler,Const.RECV_MSG_PARSE_ERR,"");
                        }
                    }
                    break;

                case BeanMark.GW_PROTOCOL:
                    mp = ProtoFactory.Create(ProtoType.UpGw);
                    validPackets = mp.SearchValid(buffer, buffer.length);
                     if(validPackets[0].length == 0){
                        // == 0 is inValid
                        byteBuffer =  ProtocolUtils.byteMerger(byteBuffer,buffer);
                    }else if(task != null){
                        DataItem dataItem = (DataItem) mp.Parse(task, validPackets[0]);
                        MainHandler.sendMessage(mainHandler,Const.RECV_MSG,dataItem);
                        byteBuffer = null;
                    }else{
                        // 解析错误
                         MainHandler.sendMessage(mainHandler,Const.RECV_MSG_PARSE_ERR,"");
                    }


                    break;
            }


//			callback.setValue(bean);
        } catch (Exception e) {
            MainHandler.sendMessage(mainHandler,Const.RECV_MSG_PARSE_ERR,ProtocolUtils.bytes2hex(buffer));
        }
    }

    ;

}