package android_serialport_api;

import java.io.FileDescriptor;

public class HardwareControl {
	
	public native int LedSetState(int ledState);
	public native static FileDescriptor OpenSerialPort(String path, int baudrate, int databits, int stopbits, int parity,
			int flags);
	public native static void CloseSerialPort();
	
	public native static  void InitCan(int baudrate);
	public native static  int OpenCan();
	public native static  int CanWrite(int canId,String data);
	public native static  CanFrame CanRead(CanFrame mcanFrame, int time);
	public native static   void CloseCan();

	// UART mode setup Hi is RS485, Lo is RS232
	public native int UartModeSetup(int uartMode);//485模式uartMode==1,232uartMode=0
	// 1 enable power, 0 disable power
	public native int PlcPowerSetup(int powerEnable);//用载波打开此方法true，通讯完毕，关了
	// 1 enable PWM, 0 disable PWM
	public native int PwmEnableSetup(int pwmEnable);//红外，调用此方法，为true
	
	static {
		System.loadLibrary("RealarmHardwareJni");
	}

}

