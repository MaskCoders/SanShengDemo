package com.sansheng.testcenter.settings;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.SettingsPreference;
import com.sansheng.testcenter.upgrade.AppUpgrade;

/**
 * Created by sunshaogang on 12/18/15.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    private SettingsPreference mCheckUpgradePreference;
    private ActionBar mActionBar;
    private FrameLayout mActionBarView;
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
        customizeActionbar();
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

    private void customizeActionbar() {
        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBarView = (FrameLayout) getLayoutInflater().inflate(
                    R.layout.base_actionbar_view, null);
            mActionBarView.findViewById(R.id.ic_back).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            onBackPressed();
                        }
                    });
            TextView title = (TextView) mActionBarView.findViewById(R.id.ab_title);
            title.setText("基本设置");
            mActionBar.setCustomView(mActionBarView, new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

}
