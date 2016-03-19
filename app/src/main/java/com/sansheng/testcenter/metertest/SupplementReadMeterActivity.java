package com.sansheng.testcenter.metertest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.ConnInter;
import com.sansheng.testcenter.base.view.ConnectTypeDialog;
import com.sansheng.testcenter.base.view.DrawableCenterTextView;
import com.sansheng.testcenter.base.view.WaySelectMeterDialog;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.server.ConnFactory;
import com.sansheng.testcenter.server.MSocketServer;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.utils.Utility;
import hstt.data.ref;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sansheng.testcenter.base.Const.CONN_ERR;
import static com.sansheng.testcenter.base.Const.CONN_OK;

/**
 * Created by sunshaogang on 3/16/16.
 */
public class SupplementReadMeterActivity extends BaseActivity implements IServiceHandlerCallback,
        WaySelectMeterDialog.WaySelectMeterCallback, ConnectTypeDialog.ConnectTypeCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private DrawableCenterTextView analysisBtn;
    private DrawableCenterTextView readBtn;
    private DrawableCenterTextView deleteBtn;
    private DrawableCenterTextView showLogBtn;
    private DrawableCenterTextView stopBtn;
    private DrawableCenterTextView confirmBtn;
    private DrawableCenterTextView cancelBtn;

    private LinearLayout mHomeController;
    private LinearLayout mSelectMeterController;
    private Button conn;
    private EditText whm_ip;
    private EditText whm_port;
    private EditText mEditMeterAddressView;
    private LinearLayout mSelectChanel;
    private TextView mChanelValue;
    private ConnInter nowChannel;
    private TextView mSelectItemValue;
    private ImageView mSelectMeter;

    private ListView mListView;
    private ReadMeterDataListAdapter mAdapter;
    private long startTime;
    private long endTime;
    private String ids;
    private int dataType = 0;
    private int dataContent = 0;
    private String meterAddress;

    private MainHandler mMainHandler;
    private ConnInter mClient;
    private MSocketServer myService;  //我们自己的service
    private TerProtocolCreater cmdCreater;
    private Meter mMeter;

    public static final int LOADER_ID_DEFAULT = 0;
    public static final int LOADER_ID_FILTER = 1;

    private static int mMeterType = MeterUtilies.METER_TEST_TYPE_SINGLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mMeterType = getIntent().getIntExtra(MeterUtilies.PAMAR_METER_TYPE, MeterUtilies.METER_TEST_TYPE_SINGLE);
            Log.e("ssg", "mMeterType = " + mMeterType);
        }
        mMainHandler = new MainHandler(this, this);
        cmdCreater = new TerProtocolCreater();
//        initData();
        hideBottomLog();
        setActionBar(METER_TEST);
        getLoaderManager().initLoader(LOADER_ID_DEFAULT, null, this);
    }

    private void initData() {
        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
//        main_sort_log.setText("show the sort log");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showHomeView();
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.read_meter_control_button_list, null);
        mHomeController = (LinearLayout) inflate.findViewById(R.id.meter_test_control_home);
        mSelectMeterController = (LinearLayout) inflate.findViewById(R.id.meter_test_control_select_meter);
        analysisBtn = (DrawableCenterTextView) inflate.findViewById(R.id.analysis);
        readBtn = (DrawableCenterTextView) inflate.findViewById(R.id.read);
        deleteBtn = (DrawableCenterTextView) inflate.findViewById(R.id.delete_all);
        showLogBtn = (DrawableCenterTextView) inflate.findViewById(R.id.show_log);
        confirmBtn = (DrawableCenterTextView) inflate.findViewById(R.id.confirm);
        cancelBtn = (DrawableCenterTextView) inflate.findViewById(R.id.cancel);
        stopBtn = (DrawableCenterTextView) inflate.findViewById(R.id.stop);

        analysisBtn.setOnClickListener(this);
        readBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        showLogBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {
        View inflate = getLayoutInflater().inflate(R.layout.whmconnlist, null);
        conn = (Button) inflate.findViewById(R.id.conn);
        whm_ip = (EditText) inflate.findViewById(R.id.whm_ip);
        whm_port = (EditText) inflate.findViewById(R.id.whm_port);
        conn.setOnClickListener(this);
        main_layout_conn.addView(inflate);
    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.read_meter_center_layout, null);
        mEditMeterAddressView = (EditText) inflate.findViewById(R.id.meter_test_edit_text);
        mSelectChanel = (LinearLayout) inflate.findViewById(R.id.meter_test_select_channel);
        mChanelValue = (TextView) inflate.findViewById(R.id.meter_test_channel);
        mSelectItemValue = (TextView) inflate.findViewById(R.id.meter_test_item);
        mSelectMeter = (ImageView) inflate.findViewById(R.id.meter_test_other_input);

        mSelectChanel.setOnClickListener(this);
        mSelectMeter.setOnClickListener(this);

        mListView = (ListView) inflate.findViewById(R.id.list_view);
        mAdapter = new ReadMeterDataListAdapter(this, null);
        mListView.setAdapter(mAdapter);
        main_info.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                MeterSelectFragment fragment = (MeterSelectFragment) getFragmentManager().findFragmentByTag("MeterSelectFragment");
                if (fragment != null) {
                    HashMap<String, Meter> selectMeters = fragment.getSelectedMeters();
                    if (selectMeters != null && selectMeters.size() > 0) {
                        Log.e("ssg", "选中的电表数量 = " + selectMeters.size());
                        ArrayList<Meter> meters = new ArrayList<Meter>(selectMeters.values());
                        mMeter = meters.get(0);
                        ids = String.valueOf(mMeter.mId);
                        endTime = System.currentTimeMillis();
                        restartLoader(LOADER_ID_FILTER);
                        mEditMeterAddressView.setText(mMeter.mMeterAddress);
                    }
                } else {
                    Utility.showToast(this, "未选择任务电表");
                }
                showHomeView();
                break;
            case R.id.cancel:
                showHomeView();
                break;
            case R.id.meter_test_select_channel:
                showConnectTypeDialog();
                break;
            case R.id.meter_test_other_input:
                showWaySelectMeterDialog();
                break;
            case R.id.show_log://显示日志
                if (wholeIsShow()) {
                    showWholeLog(false);
                    showLogBtn.setText("显示日志");
                } else {
                    showWholeLog(true);
                    showLogBtn.setText("关闭日志");
                }
                break;
            case R.id.analysis:
                break;
            case R.id.read:
                break;
            case R.id.meter_address:
//                Intent intent = new Intent(SupplementReadMeterActivity.this, MSocketServer.class);
//                startService(intent);
                break;
            case R.id.conn_type:
//                intent = new Intent(SupplementReadMeterActivity.this, MSocketServer.class);
//                ConnectionService connSer = new ConnectionService(mMainHandler, myService);
//                bindService(intent, connSer, Context.BIND_AUTO_CREATE);
                break;
            case R.id.read_address:
//                mClientManager.clearClients();
//                mClientManager.createClient(null, -100);
                break;
            case R.id.select_meter:
//                mClientManager.clearClients();
//                mClientManager.createClient(null, -100);
                break;
            case R.id.conn:
//                mClient = ConnFactory.getInstance(6, new ref<String>(mEditMeterAddressView.getText().toString()),mMainHandler, whm_ip.getText().toString(),
//                        Integer.valueOf(whm_port.getText().toString()), BeanMark.METER_PROTOCOL);
                break;
            case R.id.stop:
//                try {
//                    nowChannel.close();
//                    Message msg = new Message();
//                    msg.obj = nowChannel.getConnInfo();
//                    msg.what = Const.CONN_CLOSE;
//                    mMainHandler.sendMessage(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
    public void setValue(BeanMark bean) {
        if (bean instanceof WhmBean) {
            commandLists.remove(((WhmBean) bean).tempCommand);
//            mAdapter.setmSelectedItemsValues((WhmBean) bean);
//            startTest();
        }
    }

    @Override
    public void pullWholeLog(SpannableString info) {
        main_whole_log.append(info);
    }

    @Override
    public void pullStatus(String info) {
        main_status_info.setText(info);
    }

    private void showWaySelectMeterDialog() {
        WaySelectMeterDialog waySelectMeterDialog = new WaySelectMeterDialog(SupplementReadMeterActivity.this);
        waySelectMeterDialog.show(getFragmentManager(), "way_select_meter");
    }

    private void showConnectTypeDialog() {
        ConnectTypeDialog connectTypeDialog = new ConnectTypeDialog(SupplementReadMeterActivity.this);
        connectTypeDialog.show(getFragmentManager(), "select_connect_type");
    }

    public static List<String> commandLists = new ArrayList<String>();

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
        String name = getResources().getStringArray(R.array.select_connect_type)[position];
        Log.e("ssg", "选择的通讯类型 ＝ " + name);
        Log.e("ssg", "选择的通讯类型 ＝ " + position);
        mChanelValue.setText(name);
        nowChannel = ConnFactory.getInstance(position, new ref<String>(mEditMeterAddressView.getText().toString()),mMainHandler, null, 0, BeanMark.METER_PROTOCOL);
        openComPort(nowChannel);
    }


    private void openComPort(ConnInter ComPort) {
        Message msg = new Message();
        msg.what = CONN_ERR;
        String conInfo = ComPort.getConnInfo();
        try {
            ComPort.open();
            msg.what = CONN_OK;
            msg.obj = conInfo;
            mMainHandler.sendMessage(msg);
        } catch (SecurityException e) {
            msg.obj = "打开串口失败:没有串口读/写权限!\n" + conInfo;
            mMainHandler.sendMessage(msg);
        } catch (IOException e) {
            msg.obj = "打开串口失败:未知错误!" + conInfo;
            mMainHandler.sendMessage(msg);
        } catch (InvalidParameterException e) {
            msg.obj = "打开串口失败:参数错误!" + conInfo;
            mMainHandler.sendMessage(msg);
        }

    }

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

    public void showHomeView() {
        showHomeController();
        removeFragment("MeterSelectFragment");
    }

    private void readMeterAddress() {
        Log.e("ssg", "从已连接的设备中读取电表地址");
    }

    private void scanMeterAddress() {
        Log.e("ssg", "扫描一维码获取电表地址");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder();
        switch (id) {
            case LOADER_ID_DEFAULT:
                selection.append(" 1=1 ");
                break;
            case LOADER_ID_FILTER:
                if (!TextUtils.isEmpty(ids)) {
                    selection.append(MeterData.METER_ID).append(" in ").append("(").append(ids).append(")").append(" AND ");
                }
                if (dataType != 0) {
                    selection.append(MeterData.DATA_TYPE).append(" = ").append("(").append(dataType).append(")").append(" AND ");
                }
                if (dataContent != 0) {
                    selection.append(MeterData.DATA_ID).append(" = ").append("(").append(dataContent).append(")").append(" AND ");
                }

                selection.append(MeterData.VALUE_TIME).append(" > ").append(startTime).append(" AND ");
                selection.append(MeterData.VALUE_TIME).append(" < ").append(endTime);
                break;
        }
        return new CursorLoader(this, MeterData.CONTENT_URI, MeterData.CONTENT_PROJECTION, selection.toString(),
                null, MeterData.ID + " " + Content.DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
        if (data != null) {
            Log.e("ssg", "cursor count = " + data.getCount());
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    public void restartLoader(final int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(id, null, SupplementReadMeterActivity.this);
            }
        });
    }

}
