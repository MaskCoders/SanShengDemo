package com.sansheng.testcenter.bean;

import android.os.Handler;
import realarm.hardware.HardwareControl;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.tools.serial.SerialHelper;

/**
 * Created by hua on 16-1-24.
 */
public class ChannelFactory {
    /**
     * /dev/ttyGS3,2,1,0
     * /dev/ttyAMA5,4,3,2,1,0
     */
    public static SerialHelper INFRA_RED;
    public static String INFRA_RED_DN = "/dev/ttyAMA5";
    public static String RS485_1 = "/dev/ttyAMA3";
    public static String RS485_2 = "/dev/ttyAMA3";
    public static String RS232 = "/dev/ttyAMA3";
    public static String ZB = "/dev/ttyAMA2";
    public static String ZB2 = "/dev/ttyAMA2";

    public static SerialHelper getInstance(int index, boolean isMeter, Handler handler, IServiceHandlerCallback cb){
        SerialHelper tmp = null;
        if(isMeter) {
            switch (index) {
                case 0:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
                    break;
                case 1:
                    (new HardwareControl()).UartModeSetup(1); //RS485
                    tmp = new SerialHelper(RS485_1,2400,handler,cb);
                    break;
                case 2:
                    (new HardwareControl()).UartModeSetup(1); //RS485
                    tmp = new SerialHelper(RS485_2,2400,handler,cb);
                    break;
                case 3:
                    (new HardwareControl()).UartModeSetup(0); //RS232

                    tmp = new SerialHelper(RS232,9600,handler,cb);
                    break;
                case 4:
                    tmp = new SerialHelper(ZB2,115200,handler,cb);
                    break;
            }
        }else{
            switch (index) {
                case 3:
                    tmp = new SerialHelper(RS485_1,2400,handler,cb);
                    break;
                case 4:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
                    break;
            }
        }
//        if(null != name) {
//            tmp.setName(name);
//        }
        return  tmp;
    }
}