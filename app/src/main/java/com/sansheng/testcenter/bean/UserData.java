package com.sansheng.testcenter.bean;

import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

/**
 * Created by hua on 15-12-26.
 */
public class UserData {
    private String AFN; // 1字节
    private String SEQ; // 1字节
    private String DataUnitTip_DA1; //1字节
    private String DataUnitTip_DA2; //1字节
    private String DataUnitTip_DT1; //1字节
    private String DataUnitTip_DT2; //1字节
    private String DataUnit = ""; // 不确定字节
    private String AUX_PW = ""; // 2字节
    private String AUX_EC = "";//2字节
    private String AUX_Tp = "" ;///6字节
    public void parse(byte[] data){
        AFN = ProtocolUtils.byte2hex(data[12]);
        SEQ = ProtocolUtils.byte2hex(data[13]);
        DataUnitTip_DA1 = ProtocolUtils.byte2hex(data[14]);
        DataUnitTip_DA2 = ProtocolUtils.byte2hex(data[15]);
        DataUnitTip_DT1 = ProtocolUtils.byte2hex(data[16]);
        DataUnitTip_DT2 = ProtocolUtils.byte2hex(data[17]);
        for(int i = 18 ;i< data.length-2;i++ ){
            DataUnit = DataUnit+ProtocolUtils.byte2hex(data[i]);
        }
       //先不考虑aux
    }
    public UserData(){

    }

    public void setAUX_EC(String AUX_EC) {
        this.AUX_EC = AUX_EC;
    }

    public void setAUX_PW(String AUX_PW) {
        this.AUX_PW = AUX_PW;
    }

    public void setAUX_Tp(String AUX_Tp) {
        this.AUX_Tp = AUX_Tp;
    }

    public void setAFN(String AFN) {
        this.AFN = AFN;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public void setDataUnit(String dataUnit) {
        DataUnit = dataUnit;
    }

    public void setDataUnitTip_DA1(String dataUnitTip_DA1) {
        DataUnitTip_DA1 = dataUnitTip_DA1;
    }

    public void setDataUnitTip_DA2(String dataUnitTip_DA2) {
        DataUnitTip_DA2 = dataUnitTip_DA2;
    }

    public void setDataUnitTip_DT1(String dataUnitTip_DT1) {
        DataUnitTip_DT1 = dataUnitTip_DT1;
    }

    public void setDataUnitTip_DT2(String dataUnitTip_DT2) {
        DataUnitTip_DT2 = dataUnitTip_DT2;
    }

    public String getCommand(){
        return AFN+SEQ+DataUnitTip_DA1+DataUnitTip_DA2+DataUnitTip_DT1+DataUnitTip_DT2+DataUnit+AUX_PW+AUX_EC+AUX_Tp;
    }

    @Override
    public String toString() {
        return  getCommand();
    }

    private void getLongSum(String str){
        int sum = 0;
        for(int i = 2 ; i<str.length();i++){
           sum =  Integer.parseInt(AUX_PW.substring(i-2,i),16);
        }
    }
    public int getSum(){
        int sum = 0;
        String str = getCommand();
        for(int i = 2 ; i <str.length();i=i+2){
            sum = sum+ Integer.parseInt(str.substring(i-2,i),16);
        }
        return sum;
    }

}
