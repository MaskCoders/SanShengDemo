package com.sansheng.testcenter.callback;

/**
 * Created by hua on 12/17/15.
 */
public interface IServiceHandlerCallback {
    void pullShortLog(String info);
    void pullWholeLog(String info);
    void pullStatus(String info);

}
