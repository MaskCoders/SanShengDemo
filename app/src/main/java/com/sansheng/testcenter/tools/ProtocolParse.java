package com.sansheng.testcenter.tools;

import java.util.Random;

/**
 * Created by hua on 12/23/15.
 */
public class ProtocolParse {

    public static final String HEAD = "68";//68
    public static final byte HEAD_B = 104;//68
    public static final String END = "16";//16
    public static final byte END_B  = 22;//16
    public StringBuffer commandBuffer = new StringBuffer();
    public StringBuffer commandBufferCenter = new StringBuffer();
    int sum = 0;
    public  final static void main(String[] args){
        ProtocolParse clazz = new ProtocolParse();

        byte[] data = clazz.makeCommand();
        clazz.checkCommand(data);
        String o = "d";
        o = "0000"+o;
        o=o.substring(o.length()-4);
        System.out.println(o);
    }
    /**
     * 解析指令
     */
    public void checkCommand(byte[] data){
        Command cmd = new Command();
        boolean hashead = hasHEAD(data,cmd);
        boolean sumOK = sumOK(data,cmd);
        if(hashead && sumOK){
            parseCommand(cmd);
        }
    }
    public void parseCommand(Command cmd){
        System.out.println(cmd.a);
        System.out.println(" parseCommand !!");
    }

    class Command{
        public byte[] l  = new byte[2];
        public byte c;
        public byte[] a = new byte[5] ;
        public byte cyc;
        public byte[] data;
    }
    public boolean hasHEAD(byte[] data,Command cmd){
        if(data[0] == HEAD_B &&  data[5] == HEAD_B ){
            cmd.l[0] = data[3];
            cmd.l[1] = data[4];
            return true;
        }else{
            return false;
        }
    }
    public boolean sumOK(byte[] data,Command cmd){
        int sum = 0;
        byte[] tmp = new byte[data.length-14];
        for(int i=6 ;i < data.length-2 ;i++){
            System.out.print("("+data[i]+")+");
            sum = sum+Integer.parseInt(Integer.toHexString(data[i] & 0xFF),16);
            if(i>11){
                tmp[i-12] = data[i];
            }
        }
        System.out.println();
        String hexsum = getCRC(sum);
        String sumInData = byte2hex(data[data.length-2]);
        System.out.println("sumbyte : "+hexsum+"  ,  in data: "+
                sumInData);
        if (hexsum.equalsIgnoreCase(sumInData)) {
            cmd.data = tmp;
            cmd.cyc = data[data.length-1];
            cmd.c = data[7];
            cmd.a[0] = data[8];
            cmd.a[1] = data[9];
            cmd.a[2] = data[10];
            cmd.a[3] = data[11];
            cmd.a[4] = data[12];
            return true;
        }else{
            return false;
        }
    }

    /**
     * 生成指令
     * @return
     */
    public byte[] makeCommand(){
        commandBuffer.append(HEAD);
        getCommandCenter();
        getL();
        commandBuffer.append(HEAD);
        commandBuffer.append(commandBufferCenter);
        System.out.println("makeCmd the sum is "+sum);
        getCommandEnd();
        System.out.println(commandBuffer.toString());
        System.out.println(commandBuffer.toString().length()/2);
        return hexStringToBytes(commandBuffer.toString());
    }

    public void getCommandCenter(){
        System.out.println("getCENTER");
        getC();
        getA();
        for(int i=0 ;i<6;i++){
            String h = getRandom();
            sum = sum+Integer.parseInt(h,16);
            commandBufferCenter.append(h);
        }
    }
    public void getCommandEnd(){
        System.out.println("getEND");
        commandBuffer.append(getCRC());//add crc
        commandBuffer.append(END);
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public String byte2hex( byte b) {
        return Integer.toHexString(b & 0xFF);
    }
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
    private void getL(){
        int type = 2;
        int len = commandBufferCenter.toString().length()/2;
        len = (len <<2) +type;
//        int a = len;
//        int b = len;
//        String o = Integer.toHexString((a>>8)+(b<<8));
        String o = Integer.toHexString(len);
        o = ("0000"+o);
        o=o.substring(o.length()-4);

//        String bin = Integer.toBinaryString((a>>8)+(b<<8));
//        Integer.parseInt(len,16);
        commandBuffer.append(o.substring(2,4)).append(o.substring(0,2)).append(o.substring(2,4)).append(o.substring(0,2));
    }
    private String getCRC(){
        String s = Integer.toHexString(sum).toUpperCase();
        if(s.length() == 1){
            s = "0"+s;
        }else if(s.length() >2){
            s = s.substring(s.length()-2,s.length());
        }
        return s;
    }
    private String getCRC(int sum){
        String s = Integer.toHexString(sum).toUpperCase();
        if(s.length() == 1){
            s = "0"+s;
        }else if(s.length() >2){
            s = s.substring(s.length()-2,s.length());
        }
        return s;
    }
    private void getC(){
        commandBufferCenter = new StringBuffer();
        String h = getRandom();
        sum = sum+Integer.parseInt(h,16);
        commandBufferCenter.append(h);
    }
    private void getA(){
        for(int i=0 ;i<5;i++){
            String h = getRandom();
            sum = sum+Integer.parseInt(h,16);
            commandBufferCenter.append(h);
        }
    }

    private void printit(byte[] ba){
//        System.out.println("-----------start");
//        printHexString(ba);
//        System.out.println("\n-----------end");
        for(int i=0;i<ba.length;i++){
            System.out.print(ba[i] + " ");
        }
        System.out.println();
    }
    private String getRandom(){
        Random random = new Random();
        String h = Integer.toHexString(random.nextInt(256)).toUpperCase();
        if(h.length() == 1){
            h="0"+h;
        }
        System.out.println(h);
        return h;
    }
}
