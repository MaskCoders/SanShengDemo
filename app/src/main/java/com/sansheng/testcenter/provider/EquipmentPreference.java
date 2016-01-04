package com.sansheng.testcenter.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by sunshaogang on 2016/1/5.
 */
public class EquipmentPreference {
    private static final String TAG = "EquipmentPreference";
    private static final String PREFERENCES_FILE = "Equipment";
    //    com.sansheng.testcenter_preferences
    private static final String DEFAULT_AGREEMENT = "default_protocol";
    private static final String RESEND_FREQUENCY = "resend_frequency";
    private static final String REQUEST_TIME_OUT = "request_time_out";
    private static final String LOG_TEXT_SIZE = "log_text_size";
    private static final String AGREEMENT_SHOW_LENGTH = "protocol_show_length";
    private static final String MAX_TEST_COUNT = "max_test_count";
    private static final String TIME_INACCURACY = "time_inaccuracy";
    private static final String WRITE_TO_LOG = "write_to_log";
    private static final String SERVER_ADDRESS = "server_address";

    private static EquipmentPreference sPreferences;

    private final SharedPreferences mSharedPreferences;

    private EquipmentPreference(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);//使用settings默认的preferences。
    }

    public static synchronized EquipmentPreference getPreferences(Context context) {
        if (sPreferences == null) {
            sPreferences = new EquipmentPreference(context);
        }
        return sPreferences;
    }


    //记录上次选择的电表检测项目
    public void setSelectedMeterTest(Set<String> selected){
        mSharedPreferences.edit().putStringSet(DEFAULT_AGREEMENT, selected).apply();
    }

    public Set<String> getSelectedMeterTest(){
        return mSharedPreferences.getStringSet(DEFAULT_AGREEMENT, null);
    }


    /**
     * 删除数据库同时清空preferences
     */
    public void deletePreference() {

    }
}
