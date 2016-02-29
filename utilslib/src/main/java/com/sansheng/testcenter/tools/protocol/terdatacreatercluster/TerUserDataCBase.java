package com.sansheng.testcenter.tools.protocol.terdatacreatercluster;

import com.sansheng.testcenter.bean.BaseCommandData;
import com.sansheng.testcenter.tools.protocol.TerUserDataCreaterInterface;

/**
 * Created by hua on 16-2-18.
 */
public abstract class TerUserDataCBase implements TerUserDataCreaterInterface {
    protected String json;//这个是数据的json字符串，或者以后用别的
    public TerUserDataCBase(String j) {
        this.json = j;
    }
}
