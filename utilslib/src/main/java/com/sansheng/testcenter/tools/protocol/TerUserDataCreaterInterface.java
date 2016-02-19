package com.sansheng.testcenter.tools.protocol;

import com.sansheng.testcenter.bean.BaseCommandData;

/**
 * Created by hua on 16-2-18.
 */
public interface TerUserDataCreaterInterface {
    /*
   在ｃｒｅａｔｅ里，解析输入的用户数据，然后组包，每个组包对应一套算法
     */
    public BaseCommandData create();
}
