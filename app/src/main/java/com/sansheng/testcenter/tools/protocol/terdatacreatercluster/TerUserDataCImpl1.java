package com.sansheng.testcenter.tools.protocol.terdatacreatercluster;

import com.sansheng.testcenter.bean.BaseCommandData;

/**
 * Created by hua on 16-2-18.
 */
public class TerUserDataCImpl1 extends TerUserDataCBase {


    public TerUserDataCImpl1(String j) {
        super(j);
    }

    @Override
    public BaseCommandData create() {
        return new BaseCommandData(new byte[]{});
    }
}
