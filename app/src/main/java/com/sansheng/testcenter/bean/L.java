package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

/**
 * Created by hua on 12/28/15.
 */
public class L {
    public int type;
    public int len;
    public void parse(byte[] data){
        byte[] l = new byte[2];
        l[0] = data[1];
        l[1] = data[2];
        String bin = ProtocolUtils.byteToBit(l[1])+ProtocolUtils.byteToBit(l[0]);
        System.out.println(bin);
//        bin = bin.substring(5,7) + bin.substring(0,5);
        type = Integer.valueOf(bin.substring(14,15),2);
        len = Integer.valueOf(bin.substring(0,14),2);
        System.out.println(bin);
        System.out.println(type);
        System.out.println(len);
    }

    public static final void main(String[] args){
        byte[] l = {123,0x32,0x00,2,35};
        com.sansheng.testcenter.bean.L x = new com.sansheng.testcenter.bean.L();
        x.parse(l);
    }
}
