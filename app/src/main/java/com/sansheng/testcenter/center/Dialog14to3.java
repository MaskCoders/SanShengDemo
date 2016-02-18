package com.sansheng.testcenter.center;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;
import com.sansheng.testcenter.module.CollectParam;

/**
 * Created by sunshaogang on 2/16/16.
 */
public class Dialog14to3 extends BaseDialog {
    private View mRootView;
    private EditText startContant;
    private EditText endContant;
    private AnswerDialog mDialog;
    private DialogCallback callback;

    public Dialog14to3(DialogCallback callback, CollectParam param) {
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
        mDialog.setTitleText("14-3");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.fourteen_to_three, null);
        startContant = (EditText) mRootView.findViewById(R.id.start_point_contant);
        endContant = (EditText) mRootView.findViewById(R.id.end_point_contant);

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
                callback.onPositiveClick(mParam);
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

