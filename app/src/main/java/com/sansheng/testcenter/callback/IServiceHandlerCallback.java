package com.sansheng.testcenter.callback;

import android.text.SpannableString;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.WhmBean;

/**
 * Created by hua on 12/17/15.
 */
public interface IServiceHandlerCallback {
    void pullShortLog(String info);
    void pullShortLog(SpannableString info);
    void pullWholeLog(String info);
    void setValue(BeanMark bean);
    void pullWholeLog(SpannableString info);
    void pullStatus(String info);

}
