package com.sansheng.testcenter.center;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.CollectParam;

/**
 * Created by sunshaogang on 2/17/16.
 */
public class Dialog4to3 extends BaseDialog {
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;


    public Dialog4to3(DialogCallback callback, CollectParam param) {
        super(callback, param);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mDialog.setTitleText("4-3");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.four_to_three, null);
        editText1 = (EditText) mRootView.findViewById(R.id.edit1);
        editText2 = (EditText) mRootView.findViewById(R.id.edit2);
        editText3 = (EditText) mRootView.findViewById(R.id.edit3);
        editText4 = (EditText) mRootView.findViewById(R.id.edit4);
        editText5 = (EditText) mRootView.findViewById(R.id.edit5);
        mDialog.setCustomView(mRootView);
        return mDialog;
    }

    @Override
    protected void resetParam() {
        if (mValueChanged) {//如果参数被修改，根据afn、fn 重构mParam
            Context context = getActivity();
            String[] keys = context.getResources().getStringArray(R.array.four_three_key);
            int[] flags = context.getResources().getIntArray(R.array.four_three_flag);
            String[] values = context.getResources().getStringArray(R.array.four_three_value);
            String host = editText1.getText().toString();
            String a = "\\.";
            Log.e("ssg", " " + a);
            values[0] = host;
            String[] hostArray = host.split(a);
            Log.e("ssg", " " + hostArray.length);
            values[1] = hostArray[0];
            values[2] = hostArray[1];
            values[3] = hostArray[2];
            values[4] = hostArray[3];
            values[5] = editText2.getText().toString();
            String hostBac = editText3.getText().toString();
            String[] hostArrayBac = host.split(a);
            values[6] = hostBac;
            values[7] = hostArrayBac[0];
            values[8] = hostArrayBac[1];
            values[9] = hostArrayBac[2];
            values[10] = hostArrayBac[3];
            values[11] = editText4.getText().toString();
            values[12] = editText5.getText().toString();
            mParam.resetDataList(keys, values, flags);
            mParam.saveOrUpdate(context);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}

