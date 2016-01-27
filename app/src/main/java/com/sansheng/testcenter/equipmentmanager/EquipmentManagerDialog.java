package com.sansheng.testcenter.equipmentmanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;

/**
 * Created by sunshaogang on 1/27/16.
 */
public class EquipmentManagerDialog extends DialogFragment {
    private View mRootView;
    private AnswerDialog mDialog;
    private LinearLayout collectBtn;
    private LinearLayout meterBtn;
    public EquipmentManagerDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AnswerDialog(getActivity());
        mDialog.show();
//        mDialog.setTitleText("选择要管理的设备");
        mDialog.setPositiveButtonDismiss();
        mDialog.setNegativeButtonDismiss();
        mDialog.setTitleDismiss();
        mDialog.setContainDividerDismiss();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.equipment_manager_layout, null);
        mDialog.setCustomView(mRootView);
        collectBtn = (LinearLayout) mRootView.findViewById(R.id.collect_manager);
        meterBtn = (LinearLayout) mRootView.findViewById(R.id.meter_manager);
        mDialog.setPositiveButtonDismiss();
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CollectManagerActivity.class);
                startActivity(intent);
                mDialog.dismiss();
            }
        });
        meterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MeterManagerActivity.class);
                startActivity(intent);
                mDialog.dismiss();
            }
        });
//        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        mDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
        return mDialog;
    }
}
