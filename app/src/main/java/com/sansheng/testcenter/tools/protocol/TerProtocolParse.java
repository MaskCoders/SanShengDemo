package com.sansheng.testcenter.tools.protocol;

import com.sansheng.testcenter.bean.BaseCommandData;

/**
 * Created by hua on 12/23/15.
 */
public class TerProtocolParse {

    public static final String HEAD = "68";//68
    public static final byte HEAD_B = 104;//68
    public static final byte END_B  = 22;//16
    public static final String END = "16";//16
    public StringBuffer commandBuffer = new StringBuffer();
    public StringBuffer commandBufferCenter = new StringBuffer();
    int sum = 0;
    BaseCommandData cmd;
    public TerProtocolParse(){
        
    }
    public  final static void main(String[] args){
        com.sansheng.testcenter.tools.protocol.TerProtocolParse clazz = new com.sansheng.testcenter.tools.protocol.TerProtocolParse();
        TerProtocolCreater creater = new TerProtocolCreater();

        byte[] data = creater.makeCommand(null,null,null);
        BaseCommandData cmd = clazz.checkCommand(data);
        System.out.println(cmd.toString());
    }
    /**
     * 解析指令
     */
    public BaseCommandData checkCommand(byte[] data){
        cmd = new BaseCommandData(data);
        boolean hashead = hasHEAD(data,cmd);
        boolean sumOK = sumOK(data,cmd);
        if(hashead && sumOK){
            parse(data);
            return cmd;
        }else{
            return null;
        }
    }
    public void parse(byte[] data){
        System.out.println(" parseCommand !!");
        cmd.a.parse(data);
        cmd.data.parse(data);
        cmd.CRC = data[data.length-2];
    }
    public boolean hasHEAD(byte[] data,BaseCommandData cmd){
        if(data[0] == HEAD_B &&  data[5] == HEAD_B ){
            cmd.l.parse(data);
            return true;
        }else{
            return false;
        }
    }
    public boolean sumOK(byte[] data,BaseCommandData cmd){
        int sum = 0;
        byte[] tmp = new byte[data.length-14];
        for(int i=6 ;i < data.length-2 ;i++){
            sum = sum+Integer.parseInt(Integer.toHexString(data[i] & 0xFF),16);
            if(i>11){
                tmp[i-12] = data[i];
            }
        }
        System.out.println();
        String hexsum = getCRC(sum);
        String sumInData = ProtocolUtils.byte2hex(data[data.length-2]);
        System.out.println("sumbyte : "+hexsum+"  ,  in data: "+
                sumInData);
        if (hexsum.equalsIgnoreCase(sumInData)) {
            cmd.data.parse(tmp);
            cmd.CRC = data[data.length-2];
            cmd.c.parse(data[7]);
            cmd.a.parse(data);

            return true;
        }else{
            return false;
        }
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

    private void printit(byte[] ba){
        for(int i=0;i<ba.length;i++){
            System.out.print(ba[i] + " ");
        }
        System.out.println();
    }
}
