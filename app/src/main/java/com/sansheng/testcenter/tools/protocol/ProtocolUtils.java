package com.sansheng.testcenter.tools.protocol;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hua on 15-12-26.
 */
public class ProtocolUtils {
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    //byte to 16
    public static String byte2hex( byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length()<2){
            hex="0"+hex;
        }
        return hex;
    }
    //byte to dec
    public static int byte2dec( byte b) {
     return b&0xff;
    }
    public static final String dec2hex(int dec){
        String hex = Integer.toHexString(dec);
        if(hex.length()==1){
            hex = "0"+hex;
        }
        return hex;
    }
    public static final int hex2dec(String hex){
        return Integer.parseInt(hex,16);

    }
    public static final void main(String[] args){

        System.out.println(bcd2dec4hex("fe"));
    }
    public static int bcd2dec4byte(int b){
        int h = (((int)b)<<4)&0xff;
        int l = ((int)b)>>4;

        return h+l;
    }
    public static int bcd2dec4hex(String hex){
        int b = hex2dec(hex);
        return bcd2dec4byte(b);

    }
    public static String getStrFromBytes(byte[] data,int s,int e){
        try{
            String tmp = "" ;
            for(int i=s;i<=e;i++){
                tmp = tmp+byte2hex(data[i]);
            }
            return tmp;
        }catch(Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
    /**
     * 16 to bytes
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    //byte 2 bin
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }
    // 10 to 16
    public static String get2HexFromInt(int num){
        String o = Integer.toHexString(num);
        o = ("0000"+o);
        return o.substring(o.length()-4);
    }
        //2 转 16
            public static String binaryString2hexString(String bString)
            {
                if (bString == null || bString.equals("") || bString.length() % 8 != 0)
                    return null;
                StringBuffer tmp = new StringBuffer();
                int iTmp = 0;
                for (int i = 0; i < bString.length(); i += 4)
                {
                    iTmp = 0;
                    for (int j = 0; j < 4; j++)
                    {
                        iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
                    }
                    tmp.append(Integer.toHexString(iTmp));
                }
                return tmp.toString();
            }
        //16转2
        public static String hexString2binaryString(String hexString)
        {
            if (hexString == null || hexString.length() % 2 != 0)
                return null;
            String bString = "", tmp;
            for (int i = 0; i < hexString.length(); i++)
            {
                tmp = "0000"
                        + Integer.toBinaryString(Integer.parseInt(hexString
                        .substring(i, i + 1), 16));
                bString += tmp.substring(tmp.length() - 4);
            }
            return bString;
        }
    public static  String printByte(byte[] data){
//        System.out.println("-----------start");
//        printHexString(ba);
//        System.out.println("\n-----------end");
        StringBuffer sbbyte = new StringBuffer();
        StringBuffer hexbyte_withspace = new StringBuffer();
        StringBuffer hexbyte = new StringBuffer();
        for(int i=0;i<data.length;i++){
            sbbyte.append(data[i]).append(" ");
            hexbyte.append(byte2hex(data[i]));
            hexbyte_withspace.append(byte2hex(data[i])).append(" ");
        }
        System.out.println("by hua sbbyte == >"+sbbyte.toString());
        System.out.println("by hua  hexbyte == >"+hexbyte.toString());
        System.out.println("by hua hexbyte_withspace == >"+hexbyte_withspace.toString());
        System.out.println("by hua string == >"+(new String(data)));
        return hexbyte_withspace.toString();
    }

    public static String getTimeStamp(){
        Date nowTime=new Date();
        SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss");
        return time.format(nowTime);
    }

}
