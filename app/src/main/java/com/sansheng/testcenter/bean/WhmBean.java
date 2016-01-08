package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.base.*;
import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import static com.sansheng.testcenter.base.Const.WhmConst.C.*;

/**
 * Created by hua on data.length()/26-data.length()/2-3.
 */
public class WhmBean {

    public Const.WhmConst.C type;
    public String address;
    public String userData;
    public static final byte HEAD_B = 104;//68
    public static final byte END_B  = 22;//16
    public int len = 1;

    public WhmBean() {

    }
    public static boolean hasHEAD(byte[] data,WhmBean cmd){
        if(data[0] == HEAD_B &&  data[7] == HEAD_B ){
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
            cmd.type = Const.WhmConst.C.getC(ProtocolUtils.byte2dec(data[8]));
            cmd.len = (int)data[9];
            cmd.address = ProtocolUtils.getStrFromBytes(data,1,6);
            cmd.userData = ProtocolUtils.getStrFromBytes(data,10,data.length-3);
            return true;
        }else{
            return false;
        }
    }
    @Override
    public String toString() {
        String sum_str =  "68"+address+"68"+ProtocolUtils.dec2hex(type.getValue())+ ProtocolUtils.dec2hex(len)+userData;
        return sum_str+getCs(sum_str)+"16";
    }
    private static String getCs(String hexs){
        int sum = 0;
        for(int i = 2 ; i <hexs.length()+2;i=i+2){
            sum = sum+ProtocolUtils.hex2dec(hexs.substring(i-2,i));
        }
        String hex = ProtocolUtils.dec2hex(sum);
        if(hex.length()>2){
            hex = hex.substring(hex.length()-2,hex.length());
        }
        return hex ;
    }
    public static WhmBean parse(byte[] data) {
        WhmBean bean = new WhmBean();
        boolean hashead = hasHEAD(data,bean);
        boolean sumOK = sumOK(data,bean);
        if(hashead && sumOK){
            return bean;
        }else{
            return null;
        }
    }
    public static WhmBean create(Const.WhmConst.C type, String data, String ads) {
        WhmBean bean = new WhmBean();
        bean.type = type;
        bean.userData = data;
        bean.address = ads;
        bean.len = data.length()/2;

        return bean;

    }
    private byte[] decryptionUserData(){
        String userData = "33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33".replace(" ","");
        byte[] data = ProtocolUtils.hexStringToBytes(userData);
        return data;

    }
    private byte[] encryptionUserData(){
        String userData = "33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33".replace(" ","");
        byte[] data = ProtocolUtils.hexStringToBytesEncode(userData);
        return data;
    }
    public static final void main(String[] args){
         byte[] bytes = ProtocolUtils.hexStringToBytesEncode("00ff");
//        System.out.println(ProtocolUtils.bytes2hex(bytes));

        byte b = (byte) (0x33+ProtocolUtils.hex2dec("ff"));
//        byte b = (byte) (0x33+255);
        System.out.println(ProtocolUtils.bytes2hex(bytes));
        System.out.println(ProtocolUtils.bytes2hex(ProtocolUtils.hexStringToBytesDecode("3332")));

        //68 02 00 00 00 10 20 68 11 04 33 32 34 33 e1 16
        //68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5D 16
//        String address = "02 00 00 00 10 20".replace(" ","");
//        Const.WhmConst.C type = Const.WhmConst.C.MAIN_REQUEST_READ_DATA;
//        String data = "33 32 34 33 ".replace(" ","");
//        WhmBean bean =  WhmBean.create(type,data,address);
//        WhmBean bean2 = WhmBean.parse(ProtocolUtils.hexStringToBytes(bean.toString()));
//        WhmBean bean3 = WhmBean.parse(ProtocolUtils.hexStringToBytes("68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5B 16".replace(" ","")));
//        System.out.println(bean3.toString());
//        System.out.println(bean2.toString());
//        System.out.println(bean3.toString());
//        System.out.println("68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5B 16".replace(" ",""));
//        System.out.println(bean3.toString().equalsIgnoreCase("68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5B 16".replace(" ","")));
    }
}
