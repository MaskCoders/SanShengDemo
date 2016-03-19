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
    /**
     * 求bcd，变成小树，按16进制算
     * @param a
     * @param b
     * @return
     */
    public static double getbcdDec4bytes(byte a ,byte b){
       return  byte2dec(b)+(double)byte2dec(a)/100;
    }

    /**
     * 求bcd，变成小树，按10进制算
     * @param a
     * @param b
     * @return
     */
    public static double getbcdDec4bytes2(byte a ,byte b){
        try{

            return  Integer.valueOf(byte2hex(b))+Double.valueOf(byte2hex(a))/100;
        }catch (Exception e){
            return -1;
        }
    }
    public static String bytes2hex(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for(byte b:bytes){
            sb.append(byte2hex(b));
        }
        return sb.toString();
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
    public static final String hex2bcd(String hex){
       try{
           StringBuffer sb = new StringBuffer();
            for(int i=hex.length()-1;i>0;i=i-2){
                sb.append(hex.charAt(i-1)).append(hex.charAt(i));
            }
           return sb.toString();
       }catch (Exception e){
           e.printStackTrace();
       }
        return null;


    }
    public static final byte[] bytearr2bcd(byte[] bytes,boolean decode,int from ,int to){
        if(from <0 || to<0){
            from = 0;
            to = bytes.length-1;
        }
        byte[] tmp = new byte[to-from];
        int j=0;
        for(int i = to;i>from;i--){
            byte b = bytes[i];
            if(decode){
                b = (byte) (byte2dec(b) - 0x33);
            }
            System.out.println("from is "+from+",to is "+to+", i is "+i+",b is "+b);
            tmp[j++]=b;
        }
        return tmp;
    }
    public static final String hex2bcd(String hex,boolean decode){
        StringBuffer sb = new StringBuffer();
        for(int i= hex.length()-1;i<=0;i=i-2){
            sb.append(hex.charAt(i)).append(hex.charAt(i-1));
        }
        return sb.toString();
    }
    public static final void main(String[] args){


//        byte[] tmp = bytearr2bcd(hexStringToBytes("34 33 33 50".replace(" ","")),true,0,3);
        String tmp = null;
        System.out.println(addSpaceInCmd(tmp));
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
    /**
     * 16 to bytes 并且加密
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytesEncode(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            byte b = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            d[i] = (byte) (0x33 + b & 0xff);
        }
        return d;
    }
    /**
     * 16 to bytes 并且解密
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytesDecode(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            byte b = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            d[i] = (byte) (byte2dec(b)-0x33);
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
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        if(byte_1 == null)return byte_2;
        if(byte_2 ==null)return byte_1;
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
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
    public static String addSpaceInCmd(String cmd){
        if(cmd== null || !cmd.contains("68") || cmd.length()%2 !=0){
            return  cmd;
        }
        StringBuffer sb = new StringBuffer();
        for(int i=0 ; i<cmd.length();i=i+2){
            sb.append(cmd.charAt(i)).append(cmd.charAt(i+1)).append(" ");
        }
        return sb.toString();
    }

}
