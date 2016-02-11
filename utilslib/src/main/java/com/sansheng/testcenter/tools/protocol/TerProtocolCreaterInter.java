package com.sansheng.testcenter.tools.protocol;

import com.sansheng.testcenter.bean.A;
import com.sansheng.testcenter.bean.C;
import com.sansheng.testcenter.bean.UserData;

/**
 * Created by hua on 16-2-11.
 */
public interface TerProtocolCreaterInter {

    public byte[] makeCommand(A a, C c, UserData data);
    public void getCommandCenter();
    public void getCommandEnd();


}
