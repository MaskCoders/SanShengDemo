package com.sansheng.testcenter.demo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.equipmentmanager.CollectManagerActivity;
import com.sansheng.testcenter.equipmentmanager.MeterManagerActivity;
import hstt.data.DataItem;

/**
 * Created by sunshaogang on 1/27/16.
 */
public class EquipmentManagerActivity extends BaseActivity {

    private LinearLayout collectBtn;
    private LinearLayout meterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_manager_layout);
        setActionBar(FILE_MANAGER_VIEW);
        hideBottomLog();
        collectBtn = (LinearLayout) findViewById(R.id.collect_manager);
        meterBtn = (LinearLayout) findViewById(R.id.meter_manager);
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EquipmentManagerActivity.this, CollectManagerActivity.class);
                startActivity(intent);
            }
        });
        meterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EquipmentManagerActivity.this, MeterManagerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initButtonList() {

    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {

    }

    @Override
    public void setValue(DataItem bean) {

    }
}
