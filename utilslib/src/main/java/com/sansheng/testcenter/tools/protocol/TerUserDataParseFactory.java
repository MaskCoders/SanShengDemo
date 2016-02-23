package com.sansheng.testcenter.tools.protocol;

import com.sansheng.testcenter.bean.A;
import com.sansheng.testcenter.bean.C;
import com.sansheng.testcenter.bean.UserData;
import com.sansheng.testcenter.tools.protocol.terdataparsecluster.TerUserDataPImpl1;
import com.sansheng.testcenter.tools.protocol.terdataparsecluster.TerUserDataPImpl2;

/**
 * Created by hua on 16-2-18.
 */
public class TerUserDataParseFactory {
    /**
     * 这里type就是fn，an，data是对应的userdata
     * 返回的是解析的类
     * @param type
     * @param data
     * @return
     */
    public TerUserDataParseInterface getInterface(int type , byte[] data){
        switch (type){
            case 1:
                return new TerUserDataPImpl1(null);

            case 2:
                return new TerUserDataPImpl2(null);
        }
        return null;

    }

    public static final void main(String[] args){
        TerProtocolCreater clazz = new TerProtocolCreater();
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
}
