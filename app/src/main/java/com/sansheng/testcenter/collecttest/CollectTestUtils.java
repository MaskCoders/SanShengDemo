package com.sansheng.testcenter.collecttest;

import android.content.Context;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class CollectTestUtils {

    private static String[] channelItems;
    public static final String PARAM_COLLECT = "collect";

    public static String[] channelItems(Context context){
        if (channelItems == null || channelItems.length == 0) {
            channelItems = context.getResources().getStringArray(R.array.collect_channel_type);
        }
        return channelItems;
    }

}
