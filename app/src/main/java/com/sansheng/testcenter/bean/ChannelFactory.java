package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.tools.serial.SerialHelper;

/**
 * Created by hua on 16-1-24.
 */
public class ChannelFactory {
    public static SerialHelper INFRA_RED;
    public static String INFRA_RED_DN = "/dev/xxx";

    public static SerialHelper getInstance(int index,boolean isMeter, MainHandler handler, IServiceHandlerCallback cb){
        SerialHelper tmp = null;
        if(isMeter) {
            switch (index) {
                case 0:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
                    break;
                case 1:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
                    break;
                case 2:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
                    break;
                case 3:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
                    break;
                case 4:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
                    break;
            }
        }else{
            switch (index) {
                case 3:
                    tmp = new SerialHelper(INFRA_RED_DN,1200,handler,cb);
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
