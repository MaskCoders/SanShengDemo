package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

/**
 * Created by hua on 15-12-26.
 */
public class A {
    private String a1;//1
    private String a2;//2
    private String a3;//2
    public void parse(byte[] data){
          a1 = ProtocolUtils.byte2hex(data[8]);
          a2 = ProtocolUtils.byte2hex(data[9])+ProtocolUtils.byte2hex(data[10]);
          a3 = ProtocolUtils.byte2hex(data[11])+ProtocolUtils.byte2hex(data[12]);
    }
    public A(){

    }
    /**
     * A3的D0位为终端组地址标志，D0=0表示终端地址A2为单地址；D0=1表示终端地址A2为组地址；A3的D1～D7组成0～127个主站地址MSA。
     */
    public A(int city,int address,boolean a3d0,int a3maa){
        a1 = Integer.toHexString(city);
        if(a1.length()<2){
            a1="0"+a1;
        }
        a2 = ProtocolUtils.get2HexFromInt(address);
        int a3num =  a3d0?0:1;
        a3num = a3num +(a3maa<<1);
        a3 = ProtocolUtils.dec2hex(a3num);
    }

    public String getCommand(){
//        System.out.println(a1);
//        System.out.println(a2);
//        System.out.println(a3);
        return a1+a2+a3;
    }
    public int getSum(){
        int sum = Integer.parseInt(a3,16)+
                Integer.parseInt(a2.substring(0,2),16)+
                Integer.parseInt(a2.substring(2,4),16)+
                Integer.parseInt(a1.substring(0,2),16)+
                Integer.parseInt(a1.substring(2,4),16);
        return sum;
    }

}
