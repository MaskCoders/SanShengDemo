package com.sansheng.testcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.MeterTestItemsDialog;
import com.sansheng.testcenter.base.view.ConnectTypeDialog;
import com.sansheng.testcenter.base.view.UIRevisableView;
import com.sansheng.testcenter.base.view.WaySelectMeterDialog;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.ConnectionService;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.provider.EquipmentPreference;
import com.sansheng.testcenter.server.ClientManager;
import com.sansheng.testcenter.server.MSocketServer;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 1/4/16.
 */
public class MeterTestActivity extends BaseActivity implements IServiceHandlerCallback,
        MeterTestItemsDialog.MeterTestCallback, WaySelectMeterDialog.WaySelectMeterCallback, ConnectTypeDialog.ConnectTypeCallback {
        Button text1;
    Button text2;
    Button text3;
    Button text4;
    Button text5;
    Button text6;
    UIRevisableView mAddressView;
    UIRevisableView mConnTypeView;
    UIRevisableView mReadAddressView;
    UIRevisableView mSelectAddressView;

    private ListView mListView;
    private MeterTestCenterListAdapter mAdapter;
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
        text6 = (Button) inflate.findViewById(R.id.text6);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        text5.setOnClickListener(this);
        text6.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {
//        View inflate = getLayoutInflater().inflate(R.layout.meter_test_connect_layout, null);
//        mAddressView = (UIRevisableView) inflate.findViewById(R.id.meter_address);
//        mConnTypeView = (UIRevisableView) inflate.findViewById(R.id.conn_type);
//        mReadAddressView = (UIRevisableView) inflate.findViewById(R.id.read_address);
//        mSelectAddressView = (UIRevisableView) inflate.findViewById(R.id.select_meter);
//        mAddressView.setOnClickListener(this);
//        mConnTypeView.setOnClickListener(this);
//        mReadAddressView.setOnClickListener(this);
//        mSelectAddressView.setOnClickListener(this);
//        main_layout_conn.addView(inflate);
        setDrawerDisable();
    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.meter_test_center_layout, null);
        mListView = (ListView) inflate.findViewById(R.id.list_view);
        mAdapter = new MeterTestCenterListAdapter(this);
        String result = EquipmentPreference.getPreferences(this).getSelectedMeterTest();
        if (!TextUtils.isEmpty(result)) {
            mAdapter.setSelectedItemts(stringToMap(result));
        }
        mListView.setAdapter(mAdapter);
        main_info.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1://显示日志
                if (main_whole_log.getVisibility() == View.VISIBLE) {
                    showWholeLog(false);
                    text1.setText("隐藏日志");
                } else {
                    showWholeLog(true);
                    text1.setText("显示日志");
                }
                break;
            case R.id.text2:
                //选择电表,弹出对话框选择 1、输入地址 2、选择已有电表 3、读地址，从已连接的设备中获取电表地址
                showWaySelectMeterDialog();
                break;
            case R.id.text3:
                //开始按钮
                startTest();
                break;
            case R.id.text4:
                //设置时钟
                setMeterTime();
                break;
            case R.id.text5:
                showTestItemsDialog();
                //选择检测项目
                break;
            case R.id.text6:
                showConnectTypeDialog();
                //弹出对话框选择通讯类型
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

    private void showWaySelectMeterDialog() {
        WaySelectMeterDialog waySelectMeterDialog = new WaySelectMeterDialog(MeterTestActivity.this);
        waySelectMeterDialog.show(getFragmentManager(), "way_select_meter");
    }

    private void showConnectTypeDialog() {
        ConnectTypeDialog connectTypeDialog = new ConnectTypeDialog(MeterTestActivity.this);
        connectTypeDialog.show(getFragmentManager(), "select_connect_type");
    }

    private void startTest() {
        Log.e("ssg", "开始检测");
    }

    private void setMeterTime() {
        Log.e("ssg", "设置电表时间");
    }

    @Override
    public void onMeterItemNegativeClick() {

    }

    @Override
    public void onMeterItemPositiveClick(HashMap<Integer, String> itemMap) {
        if (itemMap != null) {
            Log.e("ssg", "选择测试项目的数量 ＝ " + itemMap.size());
            mAdapter.setSelectedItemts(itemMap);
            mAdapter.notifyDataSetChanged();
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int index : itemMap.keySet()) {
                list.add(index);
            }
            EquipmentPreference.getPreferences(this).setSelectedMeterTest(list.toString());
            Log.e("ssg", "" + list.toString());
        }
    }

    @Override
    public void onSelectMeterPositiveClick(HashMap<String, Meter> meters) {

    }

    @Override
    public void onItemClick(int position) {
        Log.e("ssg", "选择的通讯类型 ＝ " + getResources().getStringArray(R.array.select_connect_type)[position]);
    }

    public HashMap<Integer, String> stringToMap(String mapText) {
        if (TextUtils.isEmpty(mapText)) {
            return null;
        }
        Log.e("ssg","mapText = " + mapText);
        Log.e("ssg","mapText.length() = " + mapText.length());
//        if (mapText.contains(", ")) {
        mapText = mapText.substring(mapText.indexOf("[") + 1, mapText.indexOf("]"));
//        }
        Log.e("ssg","mapText = " + mapText);
        if (TextUtils.isEmpty(mapText)) {
            return null;
        }
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String[] text = mapText.split(", "); // 转换为数组
        for (String str : text) {
            map.put(Integer.valueOf(str), mAdapter.getAllItems()[Integer.valueOf(str)]);
        }
        return map;
    }

}
