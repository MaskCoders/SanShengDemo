package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

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
    public byte[] originData;
    public String tempCommand = null;
    public boolean isSumOK;
    public WhmBean() {

    }
    public byte[] getUserData(){
        return ProtocolUtils.hexStringToBytesDecode(userData);
    }
    public String getSecType(){
        if(len>0) {
            return ProtocolUtils.bytes2hex(ProtocolUtils.bytearr2bcd(originData,true,9,9+type.getLen()));
        }else{
            return null;
        }
    }
    public double[] getUserDataArr(int key_len/*key_len是几个字符表示一个字*/){
        byte[] decodeData = getUserData();
        int len = (decodeData.length -type.getLen())/key_len;
        if(len <=0) return null;
        double[] data = new double[len];
        int j = 0;
        for(int i=type.getLen();i<decodeData.length-1;i=i+key_len){
            double x= ProtocolUtils.getbcdDec4bytes2(decodeData[i],decodeData[i+1]);
            data[j++] = x;
        }
        return data;
    }
    public static boolean hasHEAD(byte[] data, com.sansheng.testcenter.bean.WhmBean cmd){
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
    public boolean sumOK(byte[] data, com.sansheng.testcenter.bean.WhmBean cmd){
        int sum = 0;
        byte[] tmp = new byte[data.length-10];
        for(int i=0 ;i < data.length-2 ;i++){
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
            isSumOK = true;
            return true;
        }else{
            isSumOK = false;
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
    public static com.sansheng.testcenter.bean.WhmBean parse(byte[] data) {
        try{
            com.sansheng.testcenter.bean.WhmBean bean = new com.sansheng.testcenter.bean.WhmBean();
            boolean hashead = hasHEAD(data,bean);
            boolean sumOK = bean.sumOK(data,bean);
            if(!sumOK){
                return bean;
            }
            if(hashead && sumOK){
                bean.originData = data;
                return bean;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public static com.sansheng.testcenter.bean.WhmBean create(Const.WhmConst.C type, String data, String ads) {
        com.sansheng.testcenter.bean.WhmBean bean = new com.sansheng.testcenter.bean.WhmBean();
        bean.type = type;
        bean.userData =
        ProtocolUtils.bytes2hex(ProtocolUtils.hexStringToBytesEncode(data));
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
//         byte[] bytes = ProtocolUtils.hexStringToBytesEncode("00ff");
//        System.out.println(ProtocolUtils.bytes2hex(bytes));

//        byte b = (byte) (0x33+ProtocolUtils.hex2dec("ff"));
//        byte b = (byte) (0x33+255);
//        System.out.println(ProtocolUtils.bytes2hex(bytes));
//        System.out.println(ProtocolUtils.bytes2hex(ProtocolUtils.hexStringToBytesDecode("3332")));

        //68 02 00 00 00 10 20 68 11 04 33 32 34 33 e1 16
        //68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5D 16
        String address = "02 00 00 00 10 20".replace(" ","");
        Const.WhmConst.C type = Const.WhmConst.C.MAIN_REQUEST_READ_DATA;
        String data = "00 FF 01 00".replace(" ","");
        com.sansheng.testcenter.bean.WhmBean bean =  com.sansheng.testcenter.bean.WhmBean.create(type,data,address);
//        WhmBean bean2 = WhmBean.parse(ProtocolUtils.hexStringToBytes(bean.toString()));
        com.sansheng.testcenter.bean.WhmBean bean3 = com.sansheng.testcenter.bean.WhmBean.parse(ProtocolUtils.
                hexStringToBytes("68 12 00 00 00 10 20 68 91 07 34 33 33 50 33 33 33 2D 16".replace(" ","")));

        System.out.println(ProtocolUtils.bytes2hex(bean3.getUserData()));
        byte[] bdata =  bean3.getUserData();
        String value = "";
        for(int i = bdata.length-1;i >=bean3.type.getLen();i--){
            //// 2015-10-9 0:00:00
            value = value +ProtocolUtils.byte2hex(bdata[i]);
            System.out.println(ProtocolUtils.byte2hex(bdata[i]));
        }
        System.out.println(Double.valueOf(value));
    }
}
