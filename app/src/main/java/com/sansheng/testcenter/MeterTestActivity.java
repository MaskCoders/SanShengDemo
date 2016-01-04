package com.sansheng.testcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.MeterTestItemsDialog;
import com.sansheng.testcenter.base.view.UIRevisableView;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.ConnectionService;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.server.ClientManager;
import com.sansheng.testcenter.server.MSocketServer;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;

import java.util.HashMap;

/**
 * Created by sunshaogang on 1/4/16.
 */
public class MeterTestActivity extends BaseActivity implements IServiceHandlerCallback, MeterTestItemsDialog.MeterTestCallback {
    Button text1;
    Button text2;
    Button text3;
    Button text4;
    Button text5;
    UIRevisableView mAddressView;
    UIRevisableView mConnTypeView;
    UIRevisableView mReadAddressView;
    UIRevisableView mSelectAddressView;
    private MainHandler mMainHandler;
    private MSocketServer myService;  //我们自己的service
    private ClientManager mClientManager;
    private TerProtocolCreater cmdCreater;
    private Meter mMeter;

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
        View inflate = getLayoutInflater().inflate(R.layout.meter_test_control_button_list, null);
        text1 = (Button) inflate.findViewById(R.id.text1);
        text2 = (Button) inflate.findViewById(R.id.text2);
        text3 = (Button) inflate.findViewById(R.id.text3);
        text4 = (Button) inflate.findViewById(R.id.text4);
        text5 = (Button) inflate.findViewById(R.id.text5);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        text5.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {
        View inflate = getLayoutInflater().inflate(R.layout.meter_test_connect_layout, null);
        mAddressView = (UIRevisableView) inflate.findViewById(R.id.meter_address);
        mConnTypeView = (UIRevisableView) inflate.findViewById(R.id.conn_type);
        mReadAddressView = (UIRevisableView) inflate.findViewById(R.id.read_address);
        mSelectAddressView = (UIRevisableView) inflate.findViewById(R.id.select_meter);
        mAddressView.setOnClickListener(this);
        mConnTypeView.setOnClickListener(this);
        mReadAddressView.setOnClickListener(this);
        mSelectAddressView.setOnClickListener(this);
        main_layout_conn.addView(inflate);
    }

    @Override
    protected void initCenter() {
//        View inflate = getLayoutInflater().inflate(R.layout.connbuttonlist, null);
//        main_info.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.text5:
                showTestItemsDialog();
                break;
            case R.id.meter_address:
                Intent intent = new Intent(MeterTestActivity.this, MSocketServer.class);
                startService(intent);
                break;
            case R.id.conn_type:
                intent = new Intent(MeterTestActivity.this, MSocketServer.class);
                ConnectionService connSer = new ConnectionService(mMainHandler, myService);
                bindService(intent, connSer, Context.BIND_AUTO_CREATE);
                break;
            case R.id.read_address:
                mClientManager.createClient(null, -100);
                break;
            case R.id.select_meter:
                mClientManager.createClient(null, -100);
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

    private void showTestItemsDialog() {
        MeterTestItemsDialog testItemsDialog = new MeterTestItemsDialog(this);
        testItemsDialog.show(getFragmentManager(), "select_items");
    }

    @Override
    public void onCollectNegativeClick() {

    }

    @Override
    public void onCollectPositiveClick(HashMap<Integer, String> itemMap) {
        if (itemMap != null) {
            Log.e("ssg", "选择测试项目的数量 ＝ " + itemMap.size());
        }
    }

}
