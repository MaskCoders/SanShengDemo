package com.sansheng.testcenter.tools;

/**
 * Created by hua on 15-12-26.
 */
public class A {
    private String a1;
    private String a2;
    private String a3;

    /**
     * A3的D0位为终端组地址标志，D0=0表示终端地址A2为单地址；D0=1表示终端地址A2为组地址；A3的D1～D7组成0～127个主站地址MSA。
     */
    public A(int city,int address,boolean a3d0,int a3maa){
        a1 = Integer.toHexString(city);
        if(a1.length()<2){
            a1="0"+a1;
        }
        a2 = ProtocolUtils.get2HexFromInt(address);
        int a3num =  a3d0?0:1 +(a3maa<<1);
        a3 = ProtocolUtils.get2HexFromInt(a3num);
    }

    public String getCommand(){
        return a1+a2+a3;
    }

}
