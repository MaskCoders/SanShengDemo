package com.sansheng.testcenter.tools;

/**
 * Created by hua on 12/28/15.
 */
public class BaseCommandData {
    public L l = new L();
    public C c = new C();
    public A a = new A();
    public UserData data  = new UserData();
    public byte CRC;
    private byte[] oraData;
    public BaseCommandData(byte[] data){
        oraData = data;
    }
    @Override
    public String toString() {
        return ProtocolUtils.printByte(oraData);
    }
}
