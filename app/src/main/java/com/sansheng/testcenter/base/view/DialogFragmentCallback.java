package com.sansheng.testcenter.base.view;

import android.app.Activity;
import android.util.Log;

public class DialogFragmentCallback implements ComposeDialogFragmentFactory.DlgCallback {
    public String type;
    protected Activity mActivity;
    protected static final String LOG_TAG= "DialogFragmentCallback";

    public DialogFragmentCallback(Activity activity)
    {
        mActivity = activity;
    }
    @Override
    public void onPositiveCallback() {

    }

    @Override
    public void onNegativeCallback() {

    }

    @Override
    public void onItemSelected(int pos) {
        Log.w(LOG_TAG, "unknown position: " + pos);
    }
}