package com.sansheng.testcenter.tools.protocol;

import com.sansheng.testcenter.bean.A;
import com.sansheng.testcenter.bean.C;
import com.sansheng.testcenter.bean.UserData;

/**
 * Created by hua on 12/23/15.
 */
public class TerProtocolCreater {

    public static final String HEAD = "68";//68
    public static final byte HEAD_B = 104;//68
    public static final String END = "16";//16
    public static final byte END_B  = 22;//16
    public StringBuffer commandBuffer = new StringBuffer();
    public StringBuffer commandBufferCenter = new StringBuffer();
    int sum = 0;
    A ma ;
    C mc;
    UserData userData;
    public  final static void main(String[] args){
        TerProtocolCreater clazz = new TerProtocolCreater();
        //68 49 00 49 00 68
        // C: 4A
        // A : 10 12 64 00 02
        // AFN:0C
        // SEQ:F0
        // DA : 00 00
        // DT:01 00
        // suerdata:00 35 24 09 25 00
        // 56 16
        C c =new C(false,true,false,false,10);
        A a = new A(4114,25600,true,1);
        UserData u = new UserData();
        u.setAFN("0C");
        u.setSEQ("F0");
        u.setDataUnitTip_DA1("00");
        u.setDataUnitTip_DA2("00");
        u.setDataUnitTip_DT1("01");
        u.setDataUnitTip_DT2("00");
        u.setDataUnit("00 35 24 09 25 00".replace(" ",""));
        System.out.println("68 49 00 49 00 68 4A 10 12 64 00 02 0C F0 00 00 01 00 00 35 24 09 25 00 56 16".replace(" ",""));
        byte[] data = clazz.makeCommand(a,c ,u);

    }

    /**
     * 生成指令
     * @return
     */
    public byte[] makeCommand(A a,C c,UserData data){
        ma =a;
        mc =c;
        userData = data;
        commandBuffer = new StringBuffer();
        commandBuffer.append(HEAD);
        getCommandCenter();
        getL();
        commandBuffer.append(HEAD);
        commandBuffer.append(commandBufferCenter);
//        System.out.println("makeCmd the sum is "+sum);
        getCommandEnd();
        System.out.println(commandBuffer.toString());
        System.out.println(commandBuffer.toString().length()/2);
        return ProtocolUtils.hexStringToBytes(commandBuffer.toString());
    }

    public void getCommandCenter(){
        getC(mc);
        getA(ma);
        sum = sum+userData.getSum();
            commandBufferCenter.append(userData.getCommand());
    }
    public void getCommandEnd(){
        commandBuffer.append(getCRC());//add crc
        commandBuffer.append(END);
    }

    private void getL(){
        int type = 2;
        int len = commandBufferCenter.toString().length()/2;
        len = (len <<2) +type;
        String o = ProtocolUtils.get2HexFromInt(len);
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
    private void getC(C c){
        commandBufferCenter = new StringBuffer();
        String h = c.getCommand();

        sum = sum+Integer.parseInt(h,16);
        commandBufferCenter.append(h);
    }

    private void getA(A a){
        sum = sum + a.getSum();
        commandBufferCenter.append(a.getCommand());
    }


}
