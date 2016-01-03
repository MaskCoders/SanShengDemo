package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import static com.sansheng.testcenter.base.Const.WhmConst.C.*;

/**
 * Created by hua on data.length()/26-data.length()/2-3.
 */
public class WhmBean {

    public TYPE type;
    public String c;
    public String address ;
    public String userData;
    public static final byte HEAD_B = 104;//68
    public static final byte END_B  = 22;//16
    public int len = 1;

    public WhmBean() {

    }
    public static boolean hasHEAD(byte[] data,WhmBean cmd){
        if(data[0] == HEAD_B &&  data[5] == HEAD_B ){
            String add = new String();
            for(int i = 1;i<7;i++){
                add = add+ProtocolUtils.byte2hex(data[i]);
            }
            cmd.address = add;
            return true;
        }else{
            return false;
        }
    }
    private static String getCRC(int sum){
        String s = Integer.toHexString(sum).toUpperCase();
        if(s.length() == 1){
            s = "0"+s;
        }else if(s.length() >2){
            s = s.substring(s.length()-2,s.length());
        }
        return s;
    }
    public static boolean sumOK(byte[] data,WhmBean cmd){
        int sum = 0;
        byte[] tmp = new byte[data.length-10];
        for(int i=8 ;i < data.length-2 ;i++){
            sum = sum+Integer.parseInt(Integer.toHexString(data[i] & 0xFF),16);
            if(i>11){
                tmp[i-12] = data[i];
            }
        }
        String hexsum = getCRC(sum);
        String sumInData = ProtocolUtils.byte2hex(data[data.length-2]);

        if (hexsum.equalsIgnoreCase(sumInData)) {
            cmd.c = ProtocolUtils.byte2hex(data[8]);
            cmd.len = (int)data[9];
            cmd.address = ProtocolUtils.getStrFromBytes(data,1,5);
            cmd.userData = ProtocolUtils.getStrFromBytes(data,8,data.length-2);
            return true;
        }else{
            return false;
        }
    }
    @Override
    public String toString() {
        String sum_str =  c+ ProtocolUtils.dec2hex(len)+userData;
        return "68"+address+"68"+sum_str+getCs(sum_str)+"16";
    }
    private static String getCs(String hexs){
        int sum = 0;
        for(int i = 2 ; i <hexs.length();i=i+2){
            sum = sum+ Integer.parseInt(hexs.substring(i-2,i),16);
        }
        return ProtocolUtils.dec2hex(sum);
    }
    public static WhmBean parse(byte[] data) {
        WhmBean bean = new WhmBean();
        boolean hashead = hasHEAD(data,bean);
        boolean sumOK = sumOK(data,bean);
        if(hashead && sumOK){
            parseType(bean);
            return bean;
        }else{
            return null;
        }
    }
    public static void parseType(WhmBean bean){
        int c = ProtocolUtils.hex2dec(bean.c);

    }
    public static WhmBean create(TYPE type, String data,String ads,int m) {
        WhmBean bean = new WhmBean();
        bean.type = type;
        bean.address = ads;
        switch (type) {
            case SLAVE_READ_DATA:
                bean = new WhmBean();
                bean.c =
                        SLAVE_RESPONSE_READ_DATA;
                bean.len = data.length()/2;
                break;
            case SLAVE_FRAME:
                bean.c =
                        SLAVE_RESPONSE_FRAME;
                bean.len = data.length()/2;
                break;
            case SLAVE_WRITE:
                bean.c = SLAVE_RESPONSE_WRITE;
                ;
                bean.len = data.length()/2;
                break;
            case SLAVE_RED_ADDRESS:
                bean.c = SLAVE_RESPONSE_RED_ADDRESS;
                bean.len = data.length()/2;
                break;
            case SLAVE_WRITE_ADDRESS:
                bean.c = SLAVE_RESPONSE_WRITE_ADDRESS;
                bean.len = data.length()/2;
                break;
            case MASTER_CALIBRATION_TIME:
                bean.c = CALIBRATION_TIME;
                bean.len = data.length()/2;
                break;
            case SLAVE_FROZEN_DATA:
                bean.c = SLAVE_RESPONSE_FROZEN_DATA;
                bean.len = data.length()/2;
                break;
            case SLAVE_COMMUNICATION_RATE:
                bean.c = SLAVE_RESPONSE_COMMUNICATION_RATE;
                bean.len = data.length()/2;
                break;
            case SLAVE_CHANGE_PW:
                bean.c = SLAVE_RESPONSE_CHANGE_PW;
                bean.len = data.length()/2;
                break;
            case SLAVE_ALL_CLEAR:
                bean.c = SLAVE_RESPONSE_ALL_CLEAR;
                bean.len = data.length()/2;
                break;
            case SLAVE_CLEAR:
                bean.c = SLAVE_RESPONSE_CLEAR;
                bean.len = data.length()/2;
                break;
            case SLAVE_EVENT_CLEAR:
                bean.c = SLAVE_RESPONSE_EVENT_CLEAR;
                bean.len = data.length()/2;
                bean.len = data.length()/2;
                break;
            case SLAVE_CTL:
                bean.c = SLAVE_RESPONSE_CTL;
                bean.len = data.length()/2;
                break;
            case SLAVE_CTL_CMD:
                bean.c = SLAVE_RESPONSE_CTL_CMD;
                bean.len = data.length()/2;
                break;
            case SLAVE_AUTH:
                bean.c = SLAVE_RESPONSE_AUTH;
                bean.len = data.length()/2;
                break;

            case MASTER_READ_DATA:
                bean.c = MAIN_REQUEST_READ_DATA;
                bean.len = data.length()/2;
                break;
            case MASTER_FRAME:
                bean.c = MAIN_REQUEST_FRAME;
                bean.len = data.length()/2;
                break;
            case MASTER_WRITE:
                bean.c = MAIN_REQUEST_WRITE;
                bean.len = data.length()/2;
                break;
            case MASTER_RED_ADDRESS:
                bean.c = MAIN_REQUEST_RED_ADDRESS;
                bean.len = data.length()/2;
                break;
            case MASTER_WRITE_ADDRESS:
                bean.c = MAIN_REQUEST_WRITE_ADDRESS;
                bean.len = data.length()/2;
                break;
            case MASTER_FROZEN_DATA:
                bean.c = MAIN_REQUEST_FROZEN_DATA;
                bean.len = data.length()/2;
                break;
            case MASTER_COMMUNICATION_RATE:
                bean.c = MAIN_REQUEST_COMMUNICATION_RATE;
                bean.len = data.length()/2;
                break;
            case MASTER_CHANGE_PW:
                bean.c = MAIN_REQUEST_CHANGE_PW;
                bean.len = data.length()/2;
                break;
            case MASTER_ALL_CLEAR:
                bean.c = MAIN_REQUEST_ALL_CLEAR;
                bean.len = data.length()/2;
                break;
            case MASTER_CLEAR:
                bean.c = MAIN_REQUEST_CLEAR;
                bean.len = data.length()/2;
                break;
            case MASTER_EVENT_CLEAR:
                bean.c = MAIN_REQUEST_EVENT_CLEAR;
                bean.len = data.length()/2;
                break;
            case MASTER_CTL:
                bean.c = MAIN_REQUEST_CTL;
                bean.len = data.length()/2;
                break;
            case MASTER_CTL_CMD:
                bean.c = MAIN_REQUEST_CTL_CMD;
                bean.len = data.length()/2;
                break;
            case MASTER_AUTH:
                bean.c = MAIN_REQUEST_AUTH;
                bean.len = data.length()/2;
                break;

            case ERR_READ_DATA:
                bean.c = SLAVE_RESPONSE_READ_DATA_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_FRAME:
                bean.c = SLAVE_RESPONSE_FRAME_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_WRITE:
                bean.c = SLAVE_RESPONSE_WRITE_ERR;
                bean.len = data.length()/2;
                break;
//            case ERR_RED_ADDRESS:
//                bean.c =; bean.len = data.length()/2;
//                break;
//            case ERR_WRITE_ADDRESS:
//                bean.c =; bean.len = data.length()/2;
//                break;
            case ERR_FROZEN_DATA:
                bean.c = SLAVE_RESPONSE_FROZEN_DATA_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_COMMUNICATION_RATE:
                bean.c = SLAVE_RESPONSE_COMMUNICATION_RATE_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_CHANGE_PW:
                bean.c = SLAVE_RESPONSE_CHANGE_PW_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_ALL_CLEAR:
                bean.c = SLAVE_RESPONSE_ALL_CLEAR_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_CLEAR:
                bean.c = SLAVE_RESPONSE_CLEAR_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_EVENT_CLEAR:
                bean.c = SLAVE_RESPONSE_EVENT_CLEAR_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_CTL:
                bean.c = SLAVE_RESPONSE_CTL_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_CTL_CMD:
                bean.c = SLAVE_RESPONSE_CTL_CMD_ERR;
                bean.len = data.length()/2;
                break;
            case ERR_AUTH:
                bean.c = SLAVE_RESPONSE_AUTH_ERR;
                bean.len = data.length()/2;
                break;
        }

        return bean;

    }

    public static final void main(String[] args){
        for(FruitEnum t:FruitEnum.values()){
            System.out.println(t);
            System.out.println(t.value());

        }
    }
    public enum FruitEnum{
        APPLE(1), ORANGE(2);    //    调用构造函数来构造枚举项

        private int value = 0;

        private FruitEnum(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }
        public int value() {
            return this.value;
        }
    }
    public enum TYPE {

        MASTER_READ_DATA,
        MASTER_FRAME,
        MASTER_WRITE,
        MASTER_RED_ADDRESS,
        MASTER_WRITE_ADDRESS,
        MASTER_CALIBRATION_TIME,
        MASTER_FROZEN_DATA,
        MASTER_COMMUNICATION_RATE,
        MASTER_CHANGE_PW,
        MASTER_ALL_CLEAR,
        MASTER_CLEAR,
        MASTER_EVENT_CLEAR,
        MASTER_CTL,
        MASTER_CTL_CMD,
        MASTER_AUTH,

        SLAVE_READ_DATA,
        SLAVE_FRAME,
        SLAVE_WRITE,
        SLAVE_RED_ADDRESS,
        SLAVE_WRITE_ADDRESS,
        SLAVE_FROZEN_DATA,
        SLAVE_COMMUNICATION_RATE,
        SLAVE_CHANGE_PW,
        SLAVE_ALL_CLEAR,
        SLAVE_CLEAR,
        SLAVE_EVENT_CLEAR,
        SLAVE_CTL,
        SLAVE_CTL_CMD,
        SLAVE_AUTH,

        ERR_READ_DATA,
        ERR_FRAME,
        ERR_WRITE,
        ERR_RED_ADDRESS,
        ERR_WRITE_ADDRESS,
        ERR_FROZEN_DATA,
        ERR_COMMUNICATION_RATE,
        ERR_CHANGE_PW,
        ERR_ALL_CLEAR,
        ERR_CLEAR,
        ERR_EVENT_CLEAR,
        ERR_CTL,
        ERR_CTL_CMD,
        ERR_AUTH,
    }
}
