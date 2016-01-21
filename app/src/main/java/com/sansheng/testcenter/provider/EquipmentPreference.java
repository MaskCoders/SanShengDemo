package com.sansheng.testcenter.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sunshaogang on 2016/1/5.
 */
public class EquipmentPreference {
    private static final String TAG = "EquipmentPreference";
    private static final String PREFERENCES_FILE = "Equipment";
    //    com.sansheng.testcenter_preferences
    private static final String METER_TEST_SELECT_ITEM = "meter_test";
    private static final String COLLECT_TEST_SELECT_ITEM = "collect_test";
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
    public void setSelectedMeterTest(String selected){
        mSharedPreferences.edit().putString(METER_TEST_SELECT_ITEM, selected).apply();
    }

    public String getSelectedMeterTest(){
        return mSharedPreferences.getString(METER_TEST_SELECT_ITEM, null);
    }

    //记录上次选择的集中器检测项目
    public void setSelectedCollectTest(String selected){
        mSharedPreferences.edit().putString(COLLECT_TEST_SELECT_ITEM, selected).apply();
    }

    public String getSelectedCollectTest(){
        return mSharedPreferences.getString(COLLECT_TEST_SELECT_ITEM, null);
    }

    /**
     * 删除数据库同时清空preferences
     */
    public void deletePreference() {

    }
}
