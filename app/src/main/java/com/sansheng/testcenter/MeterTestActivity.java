package com.sansheng.testcenter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.base.MeterSelectFragment;
import com.sansheng.testcenter.base.MeterTestItemsDialog;
import com.sansheng.testcenter.base.view.ConnectTypeDialog;
import com.sansheng.testcenter.base.view.DrawableCenterTextView;
import com.sansheng.testcenter.base.view.WaySelectMeterDialog;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.ConnectionService;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.module.ModuleUtilites;
import com.sansheng.testcenter.provider.EquipmentPreference;
import com.sansheng.testcenter.server.ClientManager;
import com.sansheng.testcenter.server.MSocketServer;
import com.sansheng.testcenter.server.SocketClient;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 1/4/16.
 */
public class MeterTestActivity extends BaseActivity implements IServiceHandlerCallback,
        MeterTestItemsDialog.MeterTestCallback, WaySelectMeterDialog.WaySelectMeterCallback, ConnectTypeDialog.ConnectTypeCallback {
    DrawableCenterTextView text1;
    //    DrawableCenterTextView text2;
    DrawableCenterTextView text3;
    DrawableCenterTextView text4;
    //    DrawableCenterTextView text5;
//    DrawableCenterTextView text6;
    DrawableCenterTextView confirm;
    DrawableCenterTextView cancel;
//    UIRevisableView mAddressView;
//    UIRevisableView mConnTypeView;
//    UIRevisableView mReadAddressView;
//    UIRevisableView mSelectAddressView;

    Button conn;
    EditText whm_ip;
    EditText whm_port;

    private LinearLayout mHomeController;
    private LinearLayout mSelectMeterController;

    private EditText mEditMeterAddressView;
    private LinearLayout mSelectChanel;
    private TextView mChanelValue;
    private LinearLayout mSelectItem;
    private TextView mSelectItemValue;
    private ImageView mSelectMeter;

    private ListView mListView;
    private MeterTestCenterListAdapter mAdapter;
    private MainHandler mMainHandler;
    private SocketClient mClient;
    private MSocketServer myService;  //我们自己的service
    private ClientManager mClientManager;
    private TerProtocolCreater cmdCreater;
    private Meter mMeter;
    private HashMap<String, Meter> mSelectMeters;


    private static int mMeterType = MeterUtilies.METER_TEST_TYPE_SINGLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMeterType = savedInstanceState.getInt(MeterUtilies.PAMAR_METER_TYPE);
        }
//        getActionBar().hide();
        mMainHandler = new MainHandler(this, this);
        mClientManager = ClientManager.getInstance(this, mMainHandler);
        cmdCreater = new TerProtocolCreater();
//        initData();
        hideBottomLog();
        setActionBar(METER_TEST);
    }

    private void initData() {
        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
//        main_sort_log.setText("show the sort log");
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.meter_test_control_button_list, null);
        mHomeController = (LinearLayout) inflate.findViewById(R.id.meter_test_control_home);
        mSelectMeterController = (LinearLayout) inflate.findViewById(R.id.meter_test_control_select_meter);
        text1 = (DrawableCenterTextView) inflate.findViewById(R.id.text1);
//        text2 = (DrawableCenterTextView) inflate.findViewById(R.id.text2);/
        text3 = (DrawableCenterTextView) inflate.findViewById(R.id.text3);
        text4 = (DrawableCenterTextView) inflate.findViewById(R.id.text4);
//        text5 = (Button) inflate.findViewById(R.id.text5);
//        text6 = (Button) inflate.findViewById(R.id.text6);
        confirm = (DrawableCenterTextView) inflate.findViewById(R.id.confirm);
        cancel = (DrawableCenterTextView) inflate.findViewById(R.id.cancel);

        mHomeController.setOnClickListener(this);
        mSelectMeterController.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        text1.setOnClickListener(this);
//        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
//        text5.setOnClickListener(this);
//        text6.setOnClickListener(this);
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
//        setDrawerDisable();
        View inflate = getLayoutInflater().inflate(R.layout.whmconnlist, null);
        conn = (Button) inflate.findViewById(R.id.conn);
        whm_ip = (EditText) inflate.findViewById(R.id.whm_ip);
        whm_port = (EditText) inflate.findViewById(R.id.whm_port);
        conn.setOnClickListener(this);
        main_layout_conn.addView(inflate);
    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.meter_test_center_layout, null);
        mEditMeterAddressView = (EditText) inflate.findViewById(R.id.meter_test_edit_text);
        mEditMeterAddressView.setText("201000000012");
        mSelectChanel = (LinearLayout) inflate.findViewById(R.id.meter_test_select_channel);
        mChanelValue = (TextView) inflate.findViewById(R.id.meter_test_channel);
        mSelectItem = (LinearLayout) inflate.findViewById(R.id.meter_test_select_test_item);
        mSelectItemValue = (TextView) inflate.findViewById(R.id.meter_test_item);
        mSelectMeter = (ImageView) inflate.findViewById(R.id.meter_test_other_input);

        mSelectChanel.setOnClickListener(this);
        mSelectItem.setOnClickListener(this);
        mSelectMeter.setOnClickListener(this);

        mListView = (ListView) inflate.findViewById(R.id.list_view);
        mAdapter = new MeterTestCenterListAdapter(this);
        String result = EquipmentPreference.getPreferences(this).getSelectedMeterTest();
        Log.e("ssg", "result = " + result);
        if (TextUtils.isEmpty(result)) {
            result = "[\"0\",\"1\",\"2\"]";
        }
        mAdapter.setSelectedItemts(ModuleUtilites.jsonToMapForMeterTest(result, getResources().getStringArray(R.array.meter_test_items)));
        mListView.setAdapter(mAdapter);
        main_info.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                MeterSelectFragment fragment = (MeterSelectFragment) getFragmentManager().findFragmentByTag("MeterSelectFragment");
                if (fragment != null) {
                    mSelectMeters = fragment.getSelectedMeters();
                    if (mSelectMeters != null) {
                        Log.e("ssg", "选中的电表数量 = " + mSelectMeters.size());
                    }
                }
                showHomeController();
                removeFragment("MeterSelectFragment");
                break;
            case R.id.cancel:
                showHomeController();
                removeFragment("MeterSelectFragment");
                break;
            case R.id.meter_test_select_channel:
                showConnectTypeDialog();
                break;
            case R.id.meter_test_other_input:
                showWaySelectMeterDialog();
                break;
            case R.id.meter_test_select_test_item:
                showTestItemsDialog();
                break;
            case R.id.text1://显示日志
                if (wholeIsShow()) {
                    showWholeLog(false);
                    text1.setText("显示日志");
                } else {
                    showWholeLog(true);
                    text1.setText("关闭日志");
                }
                break;
//            case R.id.text2:
//                //选择电表,弹出对话框选择 1、输入地址 2、选择已有电表 3、读地址，从已连接的设备中获取电表地址init
//                showWaySelectMeterDialog();
//                break;
            case R.id.text3:
                //开始按钮
                startTest();
                break;
            case R.id.text4:
                //设置时钟
                setMeterTime();
                break;
//            case R.id.text5:
//                showTestItemsDialog();
//                //选择检测项目
//                break;
//            case R.id.text6:
//                showConnectTypeDialog();
//                //弹出对话框选择通讯类型
//                break;
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
//                mClientManager.clearClients();
                mClientManager.createClient(null, -100);
                break;
            case R.id.select_meter:
//                mClientManager.clearClients();
                mClientManager.createClient(null, -100);
                break;
            case R.id.conn:
                mClient = mClientManager.createClient(whm_ip.getText().toString(), Integer.valueOf(whm_port.getText().toString()));
                break;
        }
        super.onClick(v);
    }


    @Override
    public void pullShortLog(String info) {
        main_sort_log.append(info);
        showShortLog(true);
    }

    @Override
    public void pullWholeLog(String info) {
        SpannableString ss = new SpannableString(main_whole_log.getText().toString() + "\n " +
                getResources().getString(R.string.server) + info);
        main_whole_log.setText(ss);
        main_whole_log.setSelection(main_whole_log.getText().length() - 1);
    }

    @Override
    public void setValue(WhmBean bean) {
        mAdapter.setmSelectedItemsValues(bean);
    }

    @Override
    public void pullWholeLog(SpannableString info) {
        main_whole_log.append(info);
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
        final String address;
        try {
            address = mEditMeterAddressView.getText().toString();
            ProtocolUtils.hex2bcd(address);

        } catch (Exception e) {
//            Toast.makeText(this, "地址格式有误", 1).show();
            Utility.showToast(this, "地址格式有误");
            return;
        }
        Thread command = new Thread(new Runnable() {
            private void sendCommand(String data) throws InterruptedException {
                data = ProtocolUtils.bytes2hex(ProtocolUtils.hexStringToBytesDecode(data));
                System.out.println("data: " + data);
                Thread.sleep(1000);
                String address = "12 00 00 00 10 20".replace(" ", "");
                Const.WhmConst.C type = Const.WhmConst.C.MAIN_REQUEST_READ_DATA;
                WhmBean bean = WhmBean.create(type, data, address);
                Message msg = new Message();
                msg.obj = bean.toString();
                msg.what = Const.SEND_MSG;
                mMainHandler.sendMessage(msg);
                System.out.println("by hua : " + bean.toString());
                mClientManager.sendMessage(mClient, ProtocolUtils.hexStringToBytes(bean.toString()));
            }

            @Override
            public void run() {
                try {
                    String data = "33 32 34 33 ".replace(" ", "");
                    sendCommand(data);
                    data = "33 32 34 35".replace(" ", "");
                    sendCommand(data);
                    data = "34 34 33 37".replace(" ", "");
                    sendCommand(data);
                    data = "35 34 33 37".replace(" ", "");
                    sendCommand(data);
                    data = "34 33 39 38".replace(" ", "");
                    sendCommand(data);
                    data = "33 35 C3 33".replace(" ", "");
                    sendCommand(data);
                    data = "33 40 63 36".replace(" ", "");
                    sendCommand(data);
                    data = "34 33 33 50".replace(" ", "");
                    sendCommand(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        command.start();

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
            ArrayList<String> list = new ArrayList<String>();
            for (int index : itemMap.keySet()) {
                list.add(String.valueOf(index));
            }
            EquipmentPreference.getPreferences(this).setSelectedMeterTest(ModuleUtilites.listToJson(list));
            Log.e("ssg", "onMeterItemPositiveClick json = " + ModuleUtilites.listToJson(list));
        }
    }

    @Override
    public void onSelectMeterPositiveClick(HashMap<String, Meter> meters) {
        if (meters == null || meters.size() == 0) {
            Log.e("ssg", "selected no meter");
        } else {
            Log.e("ssg", "selected meters size = " + meters.size());
        }
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case 0://选择已有电表
                showSelectFragment();
                showSelectMeterController();
                break;
            case 1://读地址
                readMeterAddress();
                break;
            case 2://扫描一维码
                scanMeterAddress();
                break;
            default:
                break;
        }
    }

    private void showSelectMeterController() {
        mHomeController.setVisibility(View.GONE);
        mSelectMeterController.setVisibility(View.VISIBLE);
    }

    private void showHomeController() {
        mHomeController.setVisibility(View.VISIBLE);
        mSelectMeterController.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position) {
        Log.e("ssg", "选择的通讯类型 ＝ " + getResources().getStringArray(R.array.select_connect_type)[position]);
        Log.e("ssg", "选择的通讯类型 ＝ " + position);
        mChanelValue.setText(getResources().getStringArray(R.array.select_connect_type)[position]);
    }

//    @Override
//    public void onMeterPositiveClick(HashMap<String, Meter> meters) {
//        if (meters == null || meters.size() == 0) {
//            Log.e("ssg", "selected no meter");
//        }else {
//            Log.e("ssg", "selected meters size = " + meters.size());
//        }
//    }

    public void showSelectFragment() {
        MeterSelectFragment fragment = new MeterSelectFragment();
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content,
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN, "MeterSelectFragment");
    }

    public void removeFragment(String tag) {
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void readMeterAddress() {
        Log.e("ssg", "从已连接的设备中读取电表地址");
    }

    private void scanMeterAddress() {
        Log.e("ssg", "扫描一维码获取电表地址");
    }

}
