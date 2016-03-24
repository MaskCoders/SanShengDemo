package com.sansheng.testcenter.center;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.ConnInter;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.base.MeterSelectDialog;
import com.sansheng.testcenter.base.view.DrawableCenterTextView;
import com.sansheng.testcenter.base.view.WaySelectMeterDialog;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.datamanager.MeterDataListActivity;
import com.sansheng.testcenter.equipmentmanager.CenterDataAdapter;
import com.sansheng.testcenter.metertest.CollectSelectDialog;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.CollectParam;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.server.ConnFactory;
import com.sansheng.testcenter.utils.Utilities;
import com.sansheng.testcenter.utils.Utility;
import hstt.data.DataItem;
import hstt.proto.upgw.GwTask;
import hstt.proto.upgw.UpGw;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/24/15.
 * 国网主站
 * //TODO:在选择集中器的时候应该增加进入集中器编辑界面的入口。
 */
public class CenterActivity extends BaseActivity implements WaySelectMeterDialog.WaySelectMeterCallback, CollectSelectDialog.CollectCallback,
        MeterSelectDialog.MeterCallback, BaseDialog.DialogCallback, IServiceHandlerCallback {
    public static final String PARAM_START_POINT = "start_point";
    public static final String PARAM_END_POINT = "end_point";

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("CenterActivity");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);

    private ExpandableListView mListView;
    private Spinner mDateSpinner;
    private Spinner mTimeSpinner;
    private ArrayAdapter mDateAdapter;
    private ArrayAdapter mTimeAdapter;
    private CenterDataAdapter mAdapter;
    private EditText mGWLog;
//    private CenterAdapter mAdapter;
    private List<RootProt> mGroupArray = new ArrayList<RootProt>();//组列表
    private ArrayList<Collect> mCollects;
    private ArrayList<Meter> mMeters;
    private CollectParam mCurrentCollectParam;

    private DrawableCenterTextView startTest;
    private DrawableCenterTextView selectCollect;
    private DrawableCenterTextView selectMeter;
    private DrawableCenterTextView dataManager;
    private DrawableCenterTextView eventCheck;
    private DrawableCenterTextView showLog;
    private DrawableCenterTextView showFile;
    private DrawableCenterTextView stopTest;

    private RootProt mCurrentRootProt;
    private Protocol mCurrentProtocol;
    private MainHandler mMainHandler;
    private ConnInter mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomLog();
        setActionBar(DATA_LIST_VIEW);
        setDrawerDisable();
        mMainHandler = new MainHandler(this, this);
//        setContentView(R.layout.center_center_layout);
//        mListView = (ExpandableListView) findViewById(R.id.center_list);
//        ParseTask task = new ParseTask();
//        task.executeOnExecutor(sThreadPool);
//        mListView.expandGroup( int groupPos):在分组列表视图中 展开一组，
//        mListView.setSelectedGroup( int groupPosition)：设置选择指定的组。
//        mListView.setSelectedChild( int groupPosition, int childPosition, boolean shouldExpandGroup)：设置选择指定的子项。
//        mListView.getPackedPositionGroup( long packedPosition)：返回所选择的组
//        mListView.getPackedPositionForChild( int groupPosition, int childPosition)：返回所选择的子项
//        mListView.getPackedPositionType( long packedPosition)：返回所选择项的类型（Child, Group）
//        mListView.isGroupExpanded( int groupPosition):判断此组是否展开
    }

    @Override
    protected void showShortLog(boolean flag) {
        super.showShortLog(flag);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.center_button_list, null);
        startTest = (DrawableCenterTextView) inflate.findViewById(R.id.start_test);
        selectCollect = (DrawableCenterTextView) inflate.findViewById(R.id.select_collect);
        selectMeter = (DrawableCenterTextView) inflate.findViewById(R.id.select_meter);
        dataManager = (DrawableCenterTextView) inflate.findViewById(R.id.data_manager);
        eventCheck = (DrawableCenterTextView) inflate.findViewById(R.id.check_event);
        showLog = (DrawableCenterTextView) inflate.findViewById(R.id.show_log);
        showFile = (DrawableCenterTextView) inflate.findViewById(R.id.show_detail);
        stopTest = (DrawableCenterTextView) inflate.findViewById(R.id.stop);

        startTest.setOnClickListener(this);
        selectCollect.setOnClickListener(this);
        selectMeter.setOnClickListener(this);
        eventCheck.setOnClickListener(this);
        dataManager.setOnClickListener(this);
        showLog.setOnClickListener(this);
        showFile.setOnClickListener(this);
        stopTest.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.center_center_layout, null);
//        mListView = (ExpandableListView) inflate.findViewById(R.id.center_list);
        mGWLog = (EditText) inflate.findViewById(R.id.gw_log);
        mDateSpinner = (Spinner) inflate.findViewById(R.id.date_flag);
        mTimeSpinner = (Spinner) inflate.findViewById(R.id.time_range);
        mDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentRootProt = mGroupArray.get(position);
                renderList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentRootProt != null) {
                    mCurrentProtocol = mCurrentRootProt.mChildArray.get(position);
                    Log.e("ssg", "mCurrentProtocol = " + mCurrentProtocol.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ParseTask task = new ParseTask();
        task.executeOnExecutor(sThreadPool);
        main_info.addView(inflate);
    }

    @Override
    protected void initConnList() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_test:
                if (mCollects == null || mCollects.isEmpty()) {
                    Utility.showToast(CenterActivity.this, "请先选择集中器");
                    return;
                }
                startTest();
                break;
            case R.id.select_collect:
                selectCollect();
                break;
            case R.id.select_meter:
                selectMeter();
                break;
            case R.id.data_manager://数据浏览
                Intent intent = new Intent();
                intent.setClass(this, MeterDataListActivity.class);
                startActivity(intent);
                break;
            case R.id.check_event://事件查看？
                showReadDialog();
                break;
            case R.id.show_log:
                if (wholeIsShow()) {
                    showWholeLog(false);
                    showLog.setText("显示日志");
                } else {
                    showWholeLog(true);
                    showLog.setText("关闭日志");
                }
                break;
            case R.id.show_detail:
                if (wholeIsShow()) {
                    showLog.setText("显示报文");
                } else {
                    showLog.setText("关闭报文");
                }
                break;
            case R.id.stop:
                reset();
//                try{
//                    nowChannel.stopSend();
//                    nowChannel.close();
//                    Message msg = new Message();
//                    msg.obj = nowChannel.getPort();
//                    msg.what = Const.CONN_CLOSE;
//                    mMainHandler.sendMessage(msg);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                break;
        }
        super.onClick(v);
    }

    public String mParam = "\n" +
            "{\n" +
            "  \"Fn\":3,\n" +
            "  \"Paras\":[\n" +
            "  {\"n\":\"主站IP地址\",\"v\":\"10.130.124.219\",\"f\":0,\"u\":1},\n" +
            "  {\"n\":\"主站IP地址1段\",\"v\":\"10\",\"f\":101,\"u\":0},\n" +
            "  {\n" +
            "    \"n\":\"主站IP地址2段\",\n" +
            "    \"v\":\"130\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"主站IP地址3段\",\n" +
            "    \"v\":\"124\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"主站IP地址4段\",\n" +
            "    \"v\":\"219\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"主站端口\",\n" +
            "    \"v\":\"6006\",\n" +
            "    \"f\":102,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址\",\n" +
            "    \"v\":\"192.169.0.3\",\n" +
            "    \"f\":0,\n" +
            "    \"u\":1\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址1段\",\n" +
            "    \"v\":\"192\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址2段\",\n" +
            "    \"v\":\"169\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址3段\",\n" +
            "    \"v\":\"0\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址4段\",\n" +
            "    \"v\":\"3\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用端口\",\n" +
            "    \"v\":\"8001\",\n" +
            "    \"f\":102,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"APN\",\n" +
            "    \"v\":\"BNDQ-DDN.BJ\",\n" +
            "    \"f\":50,\n" +
            "    \"u\":0\n" +
            "  }]\n" +
            "}";//json
    private List<ProtoParam> params = new ArrayList<ProtoParam>();

    private void reset() {
        try {
            JSONObject root = new JSONObject(mParam);
            JSONArray array = root.getJSONArray("Paras");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                ProtoParam param = new ProtoParam(object);
                params.add(param);
                Log.e("ssg", param.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("ssg", params.toString());
    }

    @Override
    public void setValue(DataItem dataItem) {
        //TODO:继续执行.
        mAdapter.addItemsValues(dataItem);
        if (mCollects.size() > 0) {
            mCollects.remove(0);
            sendProtocol();
        }
    }

    private class TestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            sendProtocol();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean param) {

        }
    }

    private void modifyDate() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
//                mDateSpinner.setText(MeterUtilies.getSanShengDate(calendar.getTimeInMillis()));
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        final DatePickerDialog datePickDialog = new DatePickerDialog(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickDialog.show();
    }

    private void showFourteenToThree() {
        CollectParam param = new CollectParam(1, 14, 3, mParam);
        Dialog14to3 dialog = new Dialog14to3(CenterActivity.this, param);
        dialog.show(getFragmentManager(), "14-3");
    }

    private void showFourToOne() {
        CollectParam param = new CollectParam(1, 4, 1, mParam);
        Dialog4to1 dialog = new Dialog4to1(CenterActivity.this, param);
        dialog.show(getFragmentManager(), "4-1");
    }

    private void showFourToThree() {
        CollectParam param = new CollectParam(1, 4, 3, mParam);
//        mParam.mCollectId = 1;
//        mParam.mAFn = 4;
//        mParam.mFn = 3;
//        mParam.mParam = mParam;
        Dialog4to3 dialog = new Dialog4to3(CenterActivity.this, param);
        dialog.show(getFragmentManager(), "4-3");
    }

    private void showReadDialog() {
        CollectParam param = new CollectParam(1, 4, 1, mParam);
        DataReadDialog dialog = new DataReadDialog(CenterActivity.this, null);
        dialog.show(getFragmentManager(), "data_read");
    }

    private void showWaySelectMeterDialog() {
        WaySelectMeterDialog dialog = new WaySelectMeterDialog(CenterActivity.this);
        dialog.show(getFragmentManager(), "way_select_meter");
    }

    private void selectMeter() {
        MeterSelectDialog meterSelectDialog = new MeterSelectDialog(this);
        meterSelectDialog.show(getFragmentManager(), "select_meter");
    }

    private void selectCollect() {
        CollectSelectDialog collectDialog = new CollectSelectDialog(this);
        collectDialog.show(getFragmentManager(), "select_collects");
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
                selectMeter();
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

    private void readMeterAddress() {
        Log.e("ssg", "从已连接的设备中读取电表地址");
    }

    private void scanMeterAddress() {
        Log.e("ssg", "扫描一维码获取电表地址");
    }

    @Override
    public void onCollectNegativeClick() {

    }

    @Override
    public void onCollectPositiveClick(HashMap<String, Collect> collects) {
        if (collects == null || collects.size() == 0) {
            return;
        }
        this.mCollects = new ArrayList<Collect>(collects.values());
        if (collects.size() == 1) {
            selectCollect.setText(mCollects.get(0).mCollectName);
        } else {
            selectCollect.setText(mCollects.get(0).mCollectName + "[" + mCollects.size() + "]");
        }
    }

    @Override
    public void onMeterPositiveClick(HashMap<String, Meter> meters) {
        if (meters == null || meters.size() == 0) {
            return;
        }
        this.mMeters = new ArrayList<Meter>(meters.values());
        if (meters.size() == 1) {
            selectMeter.setText(mMeters.get(0).mMeterName);
        } else {
            selectMeter.setText(mMeters.get(0).mMeterName + "[" + mMeters.size() + "]");
        }
    }

    @Override
    public void onPositiveClick(CollectParam param) {//click confirm button on dialog
        if (param != null) {
            mCurrentCollectParam = param;
            Log.e("ssg", "json value = " + param.toJson());
            TestTask task = new TestTask();
            task.executeOnExecutor(sThreadPool);
        }
    }

    private class ParseTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return parseXml();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            renderList(0);
        }
    }

    private void renderList(int parent) {
        if (mGroupArray == null) return;
        if (mDateAdapter == null) {
            //simple_spinner_item small type
            mDateAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item/*, R.id.date_flag*/, buildParentList(mGroupArray));
            mDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDateSpinner.setAdapter(mDateAdapter);
            mDateSpinner.setSelection(parent);
            if (parent < mGroupArray.size()) {
                mTimeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item/*, R.id.time_range*/, buildProtocolList(mGroupArray.get(parent).mChildArray));
                mTimeSpinner.setAdapter(mTimeAdapter);
                mTimeSpinner.setSelection(0, true);
            }
        } else {
            mTimeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item/*, R.id.time_range*/, buildProtocolList(mGroupArray.get(parent).mChildArray));
            mTimeSpinner.setAdapter(mTimeAdapter);
            mTimeSpinner.setSelection(0, true);
        }

    }

    private void startTest() {
        Log.e("ssg", "the position that clicked is " + mCurrentProtocol.n);
        showParamDialog(mCurrentProtocol.afn, mCurrentProtocol.fn);
    }

    /**
     * 根据指令集弹出相应的对话框
     *
     * @param afn
     * @param fn
     */
    private void showParamDialog(char afn, int fn) {
        Log.e("ssg", "afn = " + afn);
        Log.e("ssg", "fn = " + fn);
        switch (afn) {
            case '4':
                switch (fn) {
                    case 1:
                        showFourToOne();
                        return;
                    case 3:
                        showFourToThree();
                        return;
                }
                break;
            case 'E':
                switch (fn) {
                    case 3:
                        showFourteenToThree();
                        return;
                }
                break;

        }
        sendProtocolWithoutParam(afn, fn);
    }

    private void sendProtocolWithoutParam(int afn, int fn) {
        CollectParam param = new CollectParam();
        param.mAFn = afn;
        param.mFn = fn;
        mCurrentCollectParam = param;
        TestTask task = new TestTask();
        task.executeOnExecutor(sThreadPool);
    }

    /**
     * 把所需要的参数向下传递。
     */
    private void sendProtocol() {
        final int afn = mCurrentCollectParam.mAFn;
        final int fn = mCurrentCollectParam.mFn;
        new Thread(new Runnable() {
            @Override
            public void run() {
//                for (Collect collect : mCollects) {
                Collect collect = mCollects.get(0);
                if (mCollects.size() > 0) {
                    collect = mCollects.get(0);
                }
                if (collect == null) return;
                    UpGw p = new UpGw();
                    GwTask task = new GwTask(collect.mCommonAddress, afn, fn,
                            Utilities.list2Array(mCurrentCollectParam.getDataList()), null);
//                    byte[] buffer = p.BuildPacket(task);
                    //这里需要根据集中器的配置设置client的配置
                    mClient = ConnFactory.getInstance(ConnFactory.RS485_1_TYPE, mMainHandler,
                            collect.mTerminalIp, Integer.valueOf(collect.mTerminalPort), BeanMark.GW_PROTOCOL);
                    mClient.sendMessage(task);//TODO:crush msg 放到client方法中实现.
//                    Message msg = new Message();
//                    msg.obj = Util.ByteArrayToString(buffer);
//                    msg.what = Const.SEND_MSG;
//                    mMainHandler.sendMessage(msg);
//                }
            }
        }).start();

//        Logger.d("发送组包：" + Util.ByteArrayToString(buffer));
//        buffer = Util.StringToByteArray("11 22 68 4A 00 4A 00 68 88 00 00 01 00 02 0C 60 00 00 02 00 49 48 11 15 52 15 17 16 33 44");
//        byte[][] validPackets = p.SearchValid(buffer, buffer.length);
//        Logger.d("validPackets length= " + validPackets.length);
//        Logger.d("validPackets[0] length= " + validPackets[0].length);
//        Logger.d("返回数组：" + Util.ByteArrayToString(validPackets[0]));
//        List<DataItem> results = p.ParsePacket(task, validPackets[0]);
//
//        for (DataItem di : results) {
//            Logger.d("解析结果：" + di);
//        }
    }

    /**
     * 解析协议列表
     *
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private boolean parseXml() throws XmlPullParserException, IOException {
        XmlResourceParser parser = getResources().getXml(R.xml.afn);
        List<Protocol> childArray = new ArrayList<Protocol>();
        RootProt rootProt = null;
        Protocol protocol = null;
        char afn = 0;
        String pn = null;
        int pt = -1, po = -1;
        int fn = -1, t = -1, o = -1;
        String n = null;
        int event = parser.getEventType();
        while (event != XmlResourceParser.END_DOCUMENT) {
            switch (event) {
                case XmlResourceParser.START_DOCUMENT://判断当前事件是否是文档开始事件
                    Log.e("ssg", "start parse");
                    break;
                case XmlResourceParser.START_TAG://判断当前事件是否是标签元素开始事件
                    if (TextUtils.equals("afn", parser.getName())) {//判断开始标签元素是否是
                        afn = parser.getAttributeValue(0).charAt(0);
                        int count = parser.getAttributeCount();
                        if (count == 2) {
                            pn = parser.getAttributeValue(1);
                        } else if (count == 4) {
                            pt = Integer.parseInt(parser.getAttributeValue(1));
                            po = Integer.parseInt(parser.getAttributeValue(2));
                            pn = parser.getAttributeValue(3);
                        }
                        rootProt = new RootProt(afn, pn, pt, po);
//                        Log.e("ssg", "start afn = " + afn);
                    } else if (TextUtils.equals("fn", parser.getName())) {
                        fn = Integer.parseInt(parser.getAttributeValue(0));
                        t = Integer.parseInt(parser.getAttributeValue(1));
                        o = Integer.parseInt(parser.getAttributeValue(2));
                        n = parser.getAttributeValue(3);
//                        Log.e("ssg", "start fn = " + fn);
                    }
                    protocol = new Protocol(afn, pn, fn, t, o, n);
                    break;
                case XmlResourceParser.END_TAG://判断当前事件是否是标签元素结束事件
                    if (TextUtils.equals("afn", parser.getName())) {//判断开始标签元素是否是
//                        Log.e("ssg", "end afn");
                        rootProt.setChildArray(childArray);
                        childArray = new ArrayList<Protocol>();
                        mGroupArray.add(rootProt);
                    } else if (TextUtils.equals("fn", parser.getName())) {
//                        Log.e("ssg", "end fn");
                        childArray.add(protocol);
                    }
                    break;
                case XmlResourceParser.END_DOCUMENT:
                    Log.e("ssg", "end parse");
                    break;
            }
            event = parser.next();
        }
        if (mGroupArray.size() > 0) {
            return true;
        }
        return false;
    }

    private ArrayList<String> buildParentList(List<RootProt> objects) {
        ArrayList<String> list = new ArrayList<String>();
        for (RootProt object : objects) {
            list.add(object.afn + ". " + object.n);
        }
        return list;
    }

    private ArrayList<String> buildProtocolList(List<Protocol> objects) {
        ArrayList<String> list = new ArrayList<String>();
        for (Protocol object : objects) {
            list.add(object.fn + ". " + object.n);
        }
        return list;
    }
}
