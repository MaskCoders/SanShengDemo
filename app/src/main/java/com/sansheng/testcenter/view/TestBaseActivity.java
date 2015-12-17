package com.sansheng.testcenter.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;

/**
 * Created by hua on 12/17/15.
 */
public class TestBaseActivity extends BaseActivity {
    Button text1;
    Button text2;
    Button text4;
    Button text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
        main_sort_log.setText("show the sort log");
        main_whole_log.setText("show long test\nshow long test\nshow long test\nshow long test\nshow long test\n");
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.testbuttonlist,null);
        text1  = (Button) inflate.findViewById(R.id.text1);
        text2  = (Button) inflate.findViewById(R.id.text2);
        text3  = (Button) inflate.findViewById(R.id.text3);
        text4  = (Button) inflate.findViewById(R.id.text4);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {
        View inflate = getLayoutInflater().inflate(R.layout.testbuttonlist,null);
        main_layout_conn.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text1:
                showShortLog(true);
                break;
            case R.id.text2:
                showWholeLog(true);
                break;
            case R.id.text3:
                showShortLog(false);
                break;
            case R.id.text4:
                showWholeLog(false);
                break;
        }
    }
}
