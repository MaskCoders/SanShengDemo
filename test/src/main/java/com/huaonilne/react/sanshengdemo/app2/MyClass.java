package com.huaonilne.react.sanshengdemo.app2;

import java.math.BigInteger;
import java.util.Random;

public class MyClass {
    public static final byte[] HEAD = "68H".getBytes();
    public static final byte[] END = "16H".getBytes();
    public  final static void main(String[] args){
        MyClass clazz = new MyClass();
        byte[] data = clazz.makeCommand();
        System.out.println("data size: "+data.length);
        clazz.printit(data);
        clazz.checkCommand(data);
    }
    class Command{
        public byte[] l  = new byte[2];
        public byte c;
        public byte[] a = new byte[3] ;
        public byte cyc;
        public byte[] data;
    }
    public boolean hasHEAD(byte[] data,Command cmd){
        if(data[0] == HEAD[0] &&  data[1] == HEAD[1] && data[2] == HEAD[2] &&
                data[5] == HEAD[0] &&  data[6] == HEAD[1] && data[7]== HEAD[2] ){
            cmd.l[0] = data[3];
            cmd.l[1] = data[4];
            return true;
        }else{
            return false;
        }
    }
    public boolean sumOK(byte[] data,Command cmd){
        int sum = 0;
        byte[] tmp = new byte[data.length-16];
        sum = sum+data[8]+data[9]+data[10]+data[11];
        for(int i=12 ;i<data.length-4;i++){
            System.out.print("("+data[i]+")+");
            sum = sum+ data[i];
            tmp[i-12] = data[i];
        }
        System.out.println();
        byte sumbyte = (byte)sum;
        System.out.println("sumbyte : "+sumbyte+"  ,  in data: "+data[data.length-4]);
        if (sumbyte == data[data.length-4]) {
            cmd.data = tmp;
            cmd.cyc = data[data.length-4];
            cmd.c = data[8];
            cmd.a[0] = data[9];
            cmd.a[1] = data[10];
            cmd.a[2] = data[11];
            return true;
        }else{
            return false;
        }
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
    /**
     * 生成指令
     * @return
     */
    public byte[] makeCommand(){
        byte[] d1 = getCommandHead();
        CenterBean bean = getCommandCenter();
        byte[] d2 = bean.getData();
        int sum = bean.getSum();
        System.out.println("makeCmd the sum is "+sum);
        byte[] d3 = getCommandEnd(sum);
        printit(d1);
        printit(d2);
        printit(d3);

        byte[] data = new byte[d1.length+d2.length+d3.length];
        System.arraycopy(d1,0,data,0,d1.length);
        System.arraycopy(d2,0,data,d1.length,d2.length);
        System.arraycopy(d3,0,data,d2.length+d1.length,d3.length);
        return data;
    }

    public  byte[]  getCommandHead(){
        System.out.println("getHEAD");
        byte[] data = new byte[8];
        byte[] l = getL();
        data[0]=HEAD[0];
        data[1]=HEAD[1];
        data[2]=HEAD[2];
        data[3]=l[0];
        data[4]=l[1];
        data[5]=HEAD[0];
        data[6]=HEAD[1];
        data[7]=HEAD[2];
        printit(data);
        return data;
    }
    class CenterBean{
        private int sum = 0;
        private byte[] data ;
        public CenterBean(int s,byte[] d){
            sum = s;
            data =d;
        }
        public int getSum(){return sum;}
        public byte[] getData(){return data;};
    }
    public CenterBean getCommandCenter(){
        System.out.println("getCENTER");
        byte[] byteArr = new byte[12];
        byte[] c = getC();
        byte[] a = getA();
        int sum = 0;
        byteArr[0]=c[0];
        byteArr[1]=a[0];
        byteArr[2]=a[1];
        byteArr[3]=a[2];
        for(int i=0 ;i<byteArr.length;i++){
            if(i>3) {
                byteArr[i] = getRandom();
            }
            System.out.print("(" + byteArr[i] + ")+");
            sum = sum+byteArr[i];
        }
        System.out.println("");
        printit(byteArr);
        return new CenterBean(sum,byteArr);
    }
    public byte[] getCommandEnd(int sum){
        System.out.println("getEND");
        byte[] data = new byte[4];
        data[0]= (byte) sum;
        data[1]=END[0];
        data[2]=END[1];
        data[3]=END[2];
        printit(data);
        return data;
    }
    public void test3(){
        System.out.println(BitToByte("11001100"));
        System.out.println(byteToBit(BitToByte("11001100")));
        MyClass clazz = new MyClass();
        System.out.println(byteToBit((byte) 9));
        System.out.println(bit2byte("10101010"));
        for(int i=0;i<100;i++){
            byte [] b=new byte[1];
            Random random=new Random();
            random.nextBytes(b);
            System.out.println(b[0]);
            System.out.println(byteToBit(b[0]));
        }
    }
    public void test2() {
        String s = "w";
        byte[] bytes = s.getBytes();

        System.out.println("将woaini转为不同进制的字符串：");
        System.out.println("可以转换的进制范围：" + Character.MIN_RADIX + "-" + Character.MAX_RADIX);
        System.out.println("2进制："	+ binary(bytes,	2));
//        System.out.println("5进制："	+ binary(bytes,	5));
//        System.out.println("8进制："	+ binary(bytes,	8));
        System.out.println("16进制："	+ binary(bytes,	16));
//        System.out.println("32进制："	+ binary(bytes,	32));
//        System.out.println("64进制："	+ binary(bytes,	64));// 这个已经超出范围，超出范围后变为10进制显示
        System.out.println(bit2byte("10101010"));
        System.out.println(decodeBinaryString("10"));

    }
    private byte[] getL(){
        System.out.println("getL");
        byte[] byteArr = new byte[2];
        for(int i=0 ;i<byteArr.length;i++){
            byteArr[i] = getRandom();
        }
        printit(byteArr);
        return byteArr;
    }
    private byte[] getCRC(){
        System.out.println("getCRC");
        byte[] byteArr = new byte[1];
        for(int i=0 ;i<byteArr.length;i++){
            byteArr[i] = getRandom();
        }
        printit(byteArr);
        return byteArr;
    }
    private byte[] getC(){
        System.out.println("getC");
        byte[] byteArr = new byte[8];
        for(int i=0 ;i<byteArr.length;i++){
            byteArr[i] = getRandom();
        }
        printit(byteArr);
        return byteArr;
    }
    private byte[] getA(){
        System.out.println("getA");
        byte[] byteArr = new byte[3];
        for(int i=0 ;i<byteArr.length;i++){
            byteArr[i] = getRandom();
        }
        printit(byteArr);
        return byteArr;
    }
    /**
     * 二进制字符串转byte
     */
    public static byte decodeBinaryString(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }
    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }
    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }


    /**
     * Bit转Byte
     */
    public  byte BitToByte(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {//4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    //该方法等同于Integer.toBinaryString(b)

    public static String byte2bits(byte b) {

        int z = b;
        z |= 256;
        String str = Integer.toBinaryString(z);
        int len = str.length();
        return str.substring(len - 8, len);
    }

//将二进制字符串转换回字节

    public static byte bit2byte(String bString){
        byte result=0;
        for(int i=bString.length()-1,j=0;i>=0;i--,j++){
            result+=(Byte.parseByte(bString.charAt(i)+"")*Math.pow(2, j));
        }
        return result;
    }
    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    private void test(){
        String head = "68H";
        int x = 11111111;
        System.out.println( Integer.toHexString(x));
        //十进制转二进制
        System.out.println( Integer.toBinaryString(3));
        //二进制转十进制
        System.out.println( Integer.valueOf(String.valueOf(x),2).toString());
    }


    private void printit(byte[] ba){
        for(int i=0;i<ba.length;i++){
            System.out.print(ba[i] + " ");
        }
        System.out.println();
    }
    private byte getRandom(){
        Random random=new Random();
        byte [] b=new byte[1];
        random.nextBytes(b);
        return b[0];
    }
}
