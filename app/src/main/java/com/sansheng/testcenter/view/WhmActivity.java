package com.sansheng.testcenter.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.ConnectionService;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.server.ClientManager;
import com.sansheng.testcenter.server.MSocketServer;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;

/**
 * Created by hua on 12/17/15.
 */
public class WhmActivity extends BaseActivity implements IServiceHandlerCallback {
    Button whm_bl_read_address;
    Button whm_bl_choose_db;
    Button whm_bl_start;
    Button whm_bl_choose_items;
    Button whm_bl_show_log;
    private MainHandler mMainHandler;
    private MSocketServer myService;  //我们自己的service
    private ClientManager mClientManager;
    private TerProtocolCreater cmdCreater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        mMainHandler = new MainHandler(this, this);
        mClientManager = ClientManager.getInstance(this, mMainHandler);
        cmdCreater = new TerProtocolCreater();
        initData();
        setTitle("电表检测");
    }

    private void initData() {
        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
        main_sort_log.setText("show the sort log");
        main_whole_log.setText("show long test\nshow long test\nshow long test\nshow long test\nshow long test\n");
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.whmbuttonlist, null);
        whm_bl_read_address = (Button) inflate.findViewById(R.id.whm_bl_read_address);
        whm_bl_choose_db = (Button) inflate.findViewById(R.id.whm_bl_choose_db);
        whm_bl_start = (Button) inflate.findViewById(R.id.whm_bl_start);
        whm_bl_choose_items = (Button) inflate.findViewById(R.id.whm_bl_choose_items);
        whm_bl_show_log = (Button) inflate.findViewById(R.id.whm_bl_show_log);

        whm_bl_read_address.setOnClickListener(this);
        whm_bl_choose_db.setOnClickListener(this);
        whm_bl_start.setOnClickListener(this);
        whm_bl_choose_items.setOnClickListener(this);
        whm_bl_show_log.setOnClickListener(this);
        main_button_list.addView(inflate);


    }

    @Override
    protected void initConnList() {
        View inflate = getLayoutInflater().inflate(R.layout.connbuttonlist, null);
//        conn1 = (Button) inflate.findViewById(R.id.conn1);
//        conn2 = (Button) inflate.findViewById(R.id.conn2);
//        conn3 = (Button) inflate.findViewById(R.id.conn3);
//        conn4 = (Button) inflate.findViewById(R.id.conn4);
//        conn1.setOnClickListener(this);
//        conn2.setOnClickListener(this);
//        conn3.setOnClickListener(this);
//        conn4.setOnClickListener(this);
//        main_layout_conn.addView(inflate);
    }

    @Override
    protected void initCenter() {
//        View inflate = getLayoutInflater().inflate(R.layout.connbuttonlist, null);
//        main_info.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whm_bl_read_address:
                break;
            case R.id.whm_bl_choose_db:
                break;
            case R.id.whm_bl_start:
                break;
            case R.id.whm_bl_choose_items:
                break;
            case R.id.whm_bl_show_log:
                if(wholeIsShow()){
                    showWholeLog(false);
                    ((Button)findViewById(R.id.whm_bl_show_log)).setText(R.string.whm_bl_close_log);
                }else{
                    showWholeLog(true);
                    ((Button)findViewById(R.id.whm_bl_show_log)).setText(R.string.whm_bl_show_log);
                }
                break;
        }
        super.onClick(v);
    }


    @Override
    public void pullShortLog(String info) {

    }

    @Override
    public void pullWholeLog(String info) {
        SpannableString ss = new SpannableString(main_whole_log.getText().toString() + "\n " +
                getResources().getString(R.string.server) + info);
//            ss.setSpan(new ForegroundColorSpan(SocketDemo.this.getResources().getColor(R.color.download_text_color)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_whole_log.setText(ss);
        main_whole_log.setSelection(main_whole_log.getText().length() - 1);
    }

    @Override
    public void pullStatus(String info) {
        main_status_info.setText(info);
    }

}
