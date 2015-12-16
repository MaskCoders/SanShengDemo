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

package com.sansheng.testcenter.provider;

import android.content.Context;
import android.content.SharedPreferences;

public class EquipmentPreference {
    private static final String TAG = "EquipmentPreference";
    private static final String PREFERENCES_FILE = "Equipment";
    private static final String PREF_DELIMITER = "-";

    private static final String LAST_SYNC_RESULT = "last-sync-result";
    private static final String MAX_SUCCESS_TIMESTAMP = "max-success-timestamp";
    private static final String MAX_UPDATE_TIME = "max-update-time";
    private static final String LATEST_MSG_COUNT = "latest-msg-count";
    private static final String UNREAD_DETAIL_COUNT = "unread-detail-count";
    private static final String MAX_NOTIFICATION_TIME = "max-notification-time";
    private static final String LAST_SEND_TO = "account-last-send-to";
    private static final String LAST_MESSAGE_ID = "account-last-message-id";
    private static final String LAST_MESSAGE_TYPE = "account-last-message-type";
    private static final String CIRCLE_SEND_FAIL = "circle-send-fail";

    private static final String PARAM_CONTACT_ID = "id";
    private static final String PARAM_CONTACT_EMAIL = "email";
    private static final String PARAM_CONTACT_NAME = "name";
    private static final String PARAM_CONTACT_TYPE = "type";

    private static EquipmentPreference sPreferences;

    private final SharedPreferences mSharedPreferences;

    private EquipmentPreference(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public static synchronized EquipmentPreference getPreferences(Context context) {
        if (sPreferences == null) {
            sPreferences = new EquipmentPreference(context);
        }
        return sPreferences;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return getPreferences(context).mSharedPreferences;
    }

    public void setMaxSuccessTimestamp(int type, long timestamp) {
        mSharedPreferences.edit().putLong( MAX_SUCCESS_TIMESTAMP, timestamp).apply();
    }

    public long getMaxSuccessTimestamp(int type) {
        return mSharedPreferences.getLong(MAX_SUCCESS_TIMESTAMP, 0);
    }

    /**
     * 删除数据库同时清空preferences
     */
    public void deleteCirclePreference() {
        mSharedPreferences.edit().remove(MAX_SUCCESS_TIMESTAMP).apply();
        mSharedPreferences.edit().remove(MAX_SUCCESS_TIMESTAMP).apply();
    }

}
