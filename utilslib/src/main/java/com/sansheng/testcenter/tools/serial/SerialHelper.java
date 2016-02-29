package com.sansheng.testcenter.tools.serial;


import android.os.Handler;
import android.os.Message;
import android_serialport_api.SerialPort;
import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.bean.ComBean;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * @author benjaminwan
 *���ڸ������
 */
public  class SerialHelper{
	private SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private SendThread mSendThread;
	private Handler mainHandler;
	public String sPort="/dev/s3c2410_serial0";
	public int iBaudRate=9600;
	private boolean _isOpen=false;
	private byte[] _bLoopData=new byte[]{0x30};
	private int iDelay=500;
	private IServiceHandlerCallback callback;
	//----------------------------------------------------
	public SerialHelper(){}
	public SerialHelper(String sPort,int iBaudRate,Handler handler,IServiceHandlerCallback cb){
		this.sPort = sPort;
		this.iBaudRate=2400;
		mainHandler = handler;
		callback = cb;
	}
	//----------------------------------------------------
	public void open() throws SecurityException, IOException,InvalidParameterException{
		mSerialPort =  new SerialPort(new File(sPort), iBaudRate, 8, 1, 'E', 0);
		mOutputStream = mSerialPort.getOutputStream();
		mInputStream = mSerialPort.getInputStream();
		mReadThread = new ReadThread();
		mReadThread.start();
		mSendThread = new SendThread();
		mSendThread.setSuspendFlag();
		mSendThread.start();
		_isOpen=true;
	}
	//----------------------------------------------------
	public void close(){
		if (mReadThread != null)
			mReadThread.interrupt();
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
		_isOpen=false;
	}
	//----------------------------------------------------
	public void send(byte[] bOutArray){
		try
		{
			mOutputStream.write(bOutArray);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private String tempCommand = "";
	//----------------------------------------------------
	public void sendHex(String sHex){
		tempCommand = sHex;
//		String hex = "fefefefe68 12 00 00 00 10 20 68 11 04 33 32 34 33 F3 16".replace(" ","");
		byte[] bOutArray = MyFunc.HexToByteArr(sHex);
		send(bOutArray);
	}
	//----------------------------------------------------
	public void sendTxt(String sTxt){
		byte[] bOutArray =sTxt.getBytes();
		tempCommand = sTxt;
//		String hex = "fefefefe68aaaaaaaaaaaa681300df16";
//		bOutArray = ProtocolUtils.hexStringToBytes(hex);
//		System.out.println("hua : "+hex);
		send(bOutArray);		
	}
	//----------------------------------------------------
	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				try
				{
					if (mInputStream == null) return;
					byte[] buffer=new byte[512];
					int size = mInputStream.read(buffer);
					if (size > 0){
						ComBean ComRecData = new ComBean(sPort,buffer,size);
						onDataReceived(ComRecData);
					}
					try
					{
						Thread.sleep(50);//��ʱ50ms
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				} catch (Throwable e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}
	//----------------------------------------------------
	private class SendThread extends Thread{
		public boolean suspendFlag = true;// �����̵߳�ִ��
		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				synchronized (this)
				{
					while (suspendFlag)
					{
						try
						{
							wait();
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
				send(getbLoopData());
				try
				{
					Thread.sleep(iDelay);
				} catch (InterruptedException e)
				{
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
	public int getBaudRate()
	{
		return iBaudRate;
	}
	public boolean setBaudRate(int iBaud)
	{
		if (_isOpen)
		{
			return false;
		} else
		{
			iBaudRate = iBaud;
			return true;
		}
	}
	public boolean setBaudRate(String sBaud)
	{
		int iBaud = Integer.parseInt(sBaud);
		return setBaudRate(iBaud);
	}
	//----------------------------------------------------
	public String getPort()
	{
		return sPort;
	}
	public boolean setPort(String sPort)
	{
		if (_isOpen)
		{
			return false;
		} else
		{
			this.sPort = sPort;
			return true;
		}
	}
	//----------------------------------------------------
	public boolean isOpen()
	{
		return _isOpen;
	}
	//----------------------------------------------------
	public byte[] getbLoopData()
	{
		return _bLoopData;
	}
	//----------------------------------------------------
	public void setbLoopData(byte[] bLoopData)
	{
		this._bLoopData = bLoopData;
	}
	//----------------------------------------------------
	public void setTxtLoopData(String sTxt){
		this._bLoopData = sTxt.getBytes();
	}
	//----------------------------------------------------
	public void setHexLoopData(String sHex){
		this._bLoopData = MyFunc.HexToByteArr(sHex);
	}
	//----------------------------------------------------
	public int getiDelay()
	{
		return iDelay;
	}
	//----------------------------------------------------
	public void setiDelay(int iDelay)
	{
		this.iDelay = iDelay;
	}
	//----------------------------------------------------
	public void startSend()
	{
		if (mSendThread != null)
		{
			mSendThread.setResume();
		}
	}
	//----------------------------------------------------
	public void stopSend()
	{
		if (mSendThread != null)
		{
			mSendThread.setSuspendFlag();
		}
	}
	//----------------------------------------------------
	public  void onDataReceived(ComBean ComRecData){
		WhmBean bean =null;
		byte[] barr = ComRecData.bRec;
		try{

			bean = WhmBean.parse(barr);
			if(bean == null)throw new NullPointerException();
			bean.tempCommand = tempCommand;
			callback.setValue(bean);
		}catch (Exception e){
//			e.printStackTrace();
			Message msg = new Message();
			msg.what= Const.RECV_MSG;
			msg.obj = ProtocolUtils.bytes2hex(barr);
			System.out.println("====>  "+new String(barr));
			mainHandler.sendMessage(msg);
		}
	};

}