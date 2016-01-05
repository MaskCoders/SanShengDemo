package com.sansheng.testcenter.callback;

import android.text.SpannableString;

/**
 * Created by hua on 12/17/15.
 */
public interface IServiceHandlerCallback {
    void pullShortLog(String info);
    void pullShortLog(SpannableString info);
    void pullWholeLog(String info);
    void pullWholeLog(SpannableString info);
    void pullStatus(String info);

}
