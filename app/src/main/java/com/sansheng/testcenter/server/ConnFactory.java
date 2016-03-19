package com.sansheng.testcenter.server;

import android.os.Handler;
import com.sansheng.testcenter.base.ConnInter;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.tools.serial.SerialHelper;
import hstt.data.ref;
import realarm.hardware.HardwareControl;

/**
 * Created by hua on 3/3/16.
 */
public class ConnFactory {

    public static String INFRA_RED_DN = "/dev/ttyAMA5";
    public static String RS485_1 = "/dev/ttyAMA3";
    public static String RS485_2 = "/dev/ttyAMA3";
    public static String RS232 = "/dev/ttyAMA3";
    public static String ZB = "/dev/ttyAMA2";
    public static String ZB2 = "/dev/ttyAMA2";

    public static final int INFRA_RED_DN_TYPE = 0;
    public static final int RS485_1_TYPE = 1;
    public static final int RS485_2_TYPE = 2;
    public static final int RS232_TYPE = 3;
    public static final int ZB_TYPE = 4;
    public static final int ZB2_TYPE = 5;
    public static final int SOCKET_CLIENT_TYPE = 6;
    public static final int SOCKET_SERVER_TYPE = 7;
    public MainHandler mHandler;
    public void setSocketInfo(MainHandler handler){

    }
    public static ConnInter getInstance(int type, Handler handler, String ip, int port, int protocol_type) {
        ConnInter tmp = null;
        switch (type) {
            case INFRA_RED_DN_TYPE:
                tmp = new SerialHelper(INFRA_RED_DN, 1200, handler, protocol_type);
                break;
            case RS485_1_TYPE:
                (new HardwareControl()).UartModeSetup(1); //RS485
                tmp = new SerialHelper(RS485_1, 2400, handler, protocol_type);
                break;
            case RS485_2_TYPE:
                (new HardwareControl()).UartModeSetup(1); //RS485
                tmp = new SerialHelper(RS485_2, 2400, handler, protocol_type);
                break;
            case RS232_TYPE:
                (new HardwareControl()).UartModeSetup(0); //RS232

                tmp = new SerialHelper(RS232, 9600, handler, protocol_type);
                break;
            case ZB_TYPE:
                tmp = new SerialHelper(RS232, 9600, handler, protocol_type);
                break;
            case SOCKET_CLIENT_TYPE://client
                tmp = new SocketClient(handler,ip,port,protocol_type);
                break;
            case SOCKET_SERVER_TYPE://server
//                tmp = new SerialHelper(ZB2, 115200, handler);
                //服务的还没弄
                break;
        }
        return tmp;
    }
}
