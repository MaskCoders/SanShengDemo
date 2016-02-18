package com.sansheng.testcenter.center;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;
import com.sansheng.testcenter.module.CollectParam;

/**
 * Created by sunshaogang on 2/18/16.
 */
public abstract class BaseDialog extends DialogFragment {

    protected DialogCallback mCallback;
    protected CollectParam mParam;

    protected AnswerDialog mDialog;
    protected View mRootView;
    protected boolean mValueChanged = true;

    public BaseDialog(DialogCallback callback, CollectParam param) {
        this.mCallback = callback;
        this.mParam = param;
    }

    public interface DialogCallback {
        void onPositiveClick(CollectParam param);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AnswerDialog(getActivity());
        mDialog.show();
        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                resetParam();
                mCallback.onPositiveClick(mParam);
            }
        });
        return mDialog;
    }

    protected abstract void resetParam();
}
