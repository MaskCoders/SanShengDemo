package com.sansheng.testcenter.tools.protocol.terdataparsecluster;

import com.sansheng.testcenter.bean.BaseCommandData;
import com.sansheng.testcenter.tools.protocol.TerUserDataParseInterface;

/**
 * Created by hua on 16-2-18.
 */
public abstract  class TerUserDataPBase implements TerUserDataParseInterface {
    protected BaseCommandData baseBean;
    public TerUserDataPBase(BaseCommandData bean){
        this.baseBean = bean;
    }

}
