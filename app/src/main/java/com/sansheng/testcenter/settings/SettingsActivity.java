package com.sansheng.testcenter.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.SettingsPreference;
import com.sansheng.testcenter.upgrade.AppUpgrade;

/**
 * Created by sunshaogang on 12/18/15.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    private SettingsPreference mCheckUpgradePreference;
//    android:key="default_protocol"
//    SettingsPreference:s_title="修改默认规约"
//    android:key="resend_frequency"
//    SettingsPreference:s_title="任务重发次数"
//    android:key="request_time_out"
//    SettingsPreference:s_title="超时时间"
//    android:key="log_text_size"
//    SettingsPreference:s_title="日志字体大小"
//    android:key="protocol_show_length"
//    SettingsPreference:s_title="报文显示长度"
//    android:key="max_test_count"
//    SettingsPreference:s_title="任务最大测量点数"
//    android:key="time_inaccuracy"
//    SettingsPreference:s_title="表计时间误差(分钟)"
//    android:key="write_to_log"
//    android:title="记录日志文件"
//    android:key="server_address"
//    SettingsPreference:s_title="接口服务地址"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
        mCheckUpgradePreference = (SettingsPreference) findPreference("check_upgrade");
        mCheckUpgradePreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (TextUtils.equals(key, "open_log_folder")) {

        } else if (TextUtils.equals(key, "back_up_data")) {

        } else if (TextUtils.equals(key, "check_upgrade")) {
            new AppUpgrade(SettingsActivity.this).check(false);
            return true;
        }
        return false;
    }

}
