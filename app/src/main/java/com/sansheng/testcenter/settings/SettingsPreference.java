/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sansheng.testcenter.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsPreference {
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

    private static SettingsPreference sPreferences;

    private final SharedPreferences mSharedPreferences;

    private SettingsPreference(Context context) {
//        mSharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);//
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);//使用settings默认的preferences。
    }

    public static synchronized SettingsPreference getPreferences(Context context) {
        if (sPreferences == null) {
            sPreferences = new SettingsPreference(context);
        }
        return sPreferences;
    }

    //默认规约类型
    public void setDefaultAgreement(int type){
        mSharedPreferences.edit().putInt(DEFAULT_AGREEMENT, type).apply();
    }

    public int getDefaultAgreement(){
        return mSharedPreferences.getInt(DEFAULT_AGREEMENT, 0);
    }

    //重发次数
    public void setSendFrequency(int times){
        mSharedPreferences.edit().putInt(RESEND_FREQUENCY, times).apply();
    }

    public int getSendFrequency(){
        return mSharedPreferences.getInt(RESEND_FREQUENCY, 0);
    }

    //超时时间
    public void setRequestTimeOut(int time){
        mSharedPreferences.edit().putInt(REQUEST_TIME_OUT, time).apply();
    }

    public int getRequestTimeOut(){
        return mSharedPreferences.getInt(REQUEST_TIME_OUT, 1);
    }

    //日志字体 大中小
    public void setLogTextSize(int type){
        mSharedPreferences.edit().putInt(LOG_TEXT_SIZE, type).apply();
    }

    public int getLogTextSize(){
        return mSharedPreferences.getInt(LOG_TEXT_SIZE, 1);
    }

    //报文显示长度
    public void setAgreementShowLength(int type){
        mSharedPreferences.edit().putInt(AGREEMENT_SHOW_LENGTH, type).apply();
    }

    public int getAgreementShowLength(){
        return mSharedPreferences.getInt(AGREEMENT_SHOW_LENGTH, 50);
    }

    //任务最大测量点数
    public void setMaxTestCount(int type){
        mSharedPreferences.edit().putInt(MAX_TEST_COUNT, type).apply();
    }

    public int getMaxTestCount(){
        return mSharedPreferences.getInt(MAX_TEST_COUNT, 10);
    }

    //表计时间误差 分钟
    public void setTimeInaccuracy(int type){
        mSharedPreferences.edit().putInt(TIME_INACCURACY, type).apply();
    }

    public int getTimeInaccuracy(){
        return mSharedPreferences.getInt(TIME_INACCURACY, 60);
    }

    //启用日志
    public void setWriteToLog(boolean log){
        mSharedPreferences.edit().putBoolean(WRITE_TO_LOG, log).apply();
    }

    public boolean getWriteToLog(){
        return mSharedPreferences.getBoolean(WRITE_TO_LOG, true);
    }

    //服务器地址
    public void setServerAddress(String url){
        mSharedPreferences.edit().putString(SERVER_ADDRESS, url).apply();
    }

    public String getServerAddress(){
        return mSharedPreferences.getString(SERVER_ADDRESS, "");
    }

    /**
     * 删除数据库同时清空preferences
     */
    public void deletePreference() {
        mSharedPreferences.edit().remove(DEFAULT_AGREEMENT).apply();
        mSharedPreferences.edit().remove(RESEND_FREQUENCY).apply();
        mSharedPreferences.edit().remove(REQUEST_TIME_OUT).apply();
        mSharedPreferences.edit().remove(LOG_TEXT_SIZE).apply();
        mSharedPreferences.edit().remove(AGREEMENT_SHOW_LENGTH).apply();
        mSharedPreferences.edit().remove(MAX_TEST_COUNT).apply();
        mSharedPreferences.edit().remove(TIME_INACCURACY).apply();
        mSharedPreferences.edit().remove(WRITE_TO_LOG).apply();
        mSharedPreferences.edit().remove(SERVER_ADDRESS).apply();
    }
}
