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
//    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("SettingsActivity");
//    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);
//    private ProgressDailog mProgressDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
        mCheckUpgradePreference = (SettingsPreference) findPreference("check_upgrade");
        mCheckUpgradePreference.setOnPreferenceClickListener(this);
//        showProgressDialog();
//        ReadPreferenceTask task = new ReadPreferenceTask();
//        task.executeOnExecutor(sThreadPool);
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

//    private class ReadPreferenceTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            //TODO:耗时操作
//            hideProgressDialog();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            //refresh view
//        }
//    }
//
//    private void showProgressDialog() {
//        mProgressDailog = new ProgressDailog(this);
//        mProgressDailog.setCanceledOnTouchOutside(false);
//        mProgressDailog.show();
//        mProgressDailog.setActivity(this);
//        mProgressDailog.setMessage(this.getResources().getString(R.string.loading_settings));
//        mProgressDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//            }
//        });
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDailog != null && mProgressDailog.isShowing()) {
//            mProgressDailog.dismiss();
//            mProgressDailog = null;
//        }
//    }
}
