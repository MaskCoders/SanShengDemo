package com.sansheng.testcenter.tools.protocol;

import com.sansheng.testcenter.tools.protocol.terdatacreatercluster.TerUserDataCImpl1;
import com.sansheng.testcenter.tools.protocol.terdatacreatercluster.TerUserDataCImpl2;

/**
 * Created by hua on 16-2-18.
 */
public class TerUserDataCreaterFactory {

    public TerUserDataCreaterInterface getInterface(int type , String json){
        switch (type){
            case 1:
                return new TerUserDataCImpl1(json);

            case 2:
                return new TerUserDataCImpl2(json);
        }
        return null;

    }

}
