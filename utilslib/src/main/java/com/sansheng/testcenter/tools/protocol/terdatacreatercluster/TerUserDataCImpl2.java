package com.sansheng.testcenter.tools.protocol.terdatacreatercluster;

import com.sansheng.testcenter.bean.BaseCommandData;

/**
 * Created by hua on 16-2-18.
 */
public class TerUserDataCImpl2 extends TerUserDataCBase {


    public TerUserDataCImpl2(String j, BaseCommandData bean) {
        super(j, bean);
    }

    @Override
    public BaseCommandData create() {
        return baseBean;
    }
}
