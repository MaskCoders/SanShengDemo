package com.sansheng.testcenter.center;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;
import com.sansheng.testcenter.module.CollectParam;

/**
 * Created by sunshaogang on 2/17/16.
 */
public class Dialog4to1 extends BaseDialog {
    private View mRootView;
    private EditText delayTime;
    private EditText terminalDelayTime;
    private EditText terminalOutTime;
    private EditText resendTime;
    private EditText heartCycle;

    private CheckBox oneCheckBox;
    private CheckBox twoCheckBox;
    private CheckBox threeCheckBox;
    private AnswerDialog mDialog;

    public Dialog4to1(DialogCallback callback, CollectParam param) {
        super(callback, param);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AnswerDialog(getActivity());
        mDialog.show();
        mDialog.setTitleText("4-1");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.four_to_one, null);

        delayTime = (EditText) mRootView.findViewById(R.id.delay_time);
        terminalDelayTime = (EditText) mRootView.findViewById(R.id.terminal_delay_time);
        terminalOutTime = (EditText) mRootView.findViewById(R.id.terminal_out_time);
        resendTime = (EditText) mRootView.findViewById(R.id.terminal_resend_times);
        heartCycle = (EditText) mRootView.findViewById(R.id.heart_cycle);

        oneCheckBox = (CheckBox) mRootView.findViewById(R.id.one_data_checkbox);
//        twoCheckBox = (CheckBox) mRootView.findViewById(R.id.two_data_checkbox);
//        threeCheckBox = (CheckBox) mRootView.findViewById(R.id.three_data_checkbox);

        mDialog.setCustomView(mRootView);
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
                mCallback.onPositiveClick(mParam);
            }
        });
        return mDialog;
    }

    @Override
    protected void resetParam() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}

