package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import static com.sansheng.testcenter.base.Const.WhmConst.C.*;

/**
 * Created by hua on data.length()/26-data.length()/2-3.
 */
public class WhmBean {

    public C type;
    public int c;
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
            cmd.c = (int)(data[8]);
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
        int type = bean.c;
        //这里要把type和bean一起通过handler发出去，通过Activity运行
    }
    public static WhmBean create(C type, String data,String ads,int m) {
        WhmBean bean = new WhmBean();
        bean.type = type;
        bean.address = ads;
        bean.c = SLAVE_RESPONSE_READ_DATA.getValue();
        bean.len = data.length()/2;

        return bean;

    }

}
