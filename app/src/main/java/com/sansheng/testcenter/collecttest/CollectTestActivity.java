package com.sansheng.testcenter.collecttest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.ConnInter;
import com.sansheng.testcenter.base.view.DrawableCenterTextView;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.ModuleUtilites;
import com.sansheng.testcenter.provider.EquipmentPreference;
import com.sansheng.testcenter.server.ConnFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class CollectTestActivity extends BaseActivity implements CollectTestItemsDialog.CollectTestCallback {
    private TextView tab1Layout, tab2Layout, tab3Layout, tab4Layout;
    private int currentIndex = 1;
    private FragmentManager fragmentManager;
    private BaseTabFragment tab1Fragment, tab2Fragment, tab3Fragment, tab4Fragment;

    private LinearLayout mButtonList1;
    private LinearLayout mButtonList2;
    private LinearLayout mButtonList3;
    private LinearLayout mButtonList4;
    private MainHandler mMainHandler;
    private ConnInter mClient;
    private TextView collectAddress;
    private DrawableCenterTextView paraCompare;
    private DrawableCenterTextView readStandard;
    private DrawableCenterTextView selectItem;
    private DrawableCenterTextView eventsCompare;
    private DrawableCenterTextView deleteAll;
    private DrawableCenterTextView readMeters;
//    private Spinner dateRegion;
    private DrawableCenterTextView analysis;
    private DrawableCenterTextView read_events;
    private DrawableCenterTextView events_manager;
    private ArrayList<TextView> tabList = new ArrayList<TextView>();
    private ArrayList<DrawableCenterTextView> btnList = new ArrayList<DrawableCenterTextView>();
    private DrawableCenterTextView showLog;
    private DrawableCenterTextView closeConn;

    private Collect mCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            mCollect = b.getParcelable(CollectTestUtils.PARAM_COLLECT);
        }
        if (mCollect == null) {
            onBackPressed();
        }
        super.onCreate(savedInstanceState);
        hideBottomLog();
        mMainHandler = new MainHandler(this, this);
        String ip = "192.168.134.1";
        int port = 8001;
        mClient = ConnFactory.getInstance(6,mMainHandler,this,ip,8001,BeanMark.GW_PROTOCOL);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.onDestroy();
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.collect_test_controll_layout, null);
        mButtonList1 = (LinearLayout) inflate.findViewById(R.id.collect_test_control_1);
        mButtonList2 = (LinearLayout) inflate.findViewById(R.id.collect_test_control_2);
        mButtonList3 = (LinearLayout) inflate.findViewById(R.id.collect_test_control_3);
        mButtonList4 = (LinearLayout) inflate.findViewById(R.id.collect_test_control_4);
        //whole
        collectAddress = (TextView) inflate.findViewById(R.id.collect_address);
        showLog = (DrawableCenterTextView) inflate.findViewById(R.id.show_log);
        closeConn = (DrawableCenterTextView) inflate.findViewById(R.id.cancel);
        //mButtonList1
        paraCompare = (DrawableCenterTextView) inflate.findViewById(R.id.para_compare);
        readStandard = (DrawableCenterTextView) inflate.findViewById(R.id.read_standard);
        selectItem = (DrawableCenterTextView) inflate.findViewById(R.id.select_item);
        //mButtonList2
        eventsCompare = (DrawableCenterTextView) inflate.findViewById(R.id.events_compare);
        deleteAll = (DrawableCenterTextView) inflate.findViewById(R.id.delete_all);
        //mButtonList3
        readMeters = (DrawableCenterTextView) inflate.findViewById(R.id.read_meters);
//        dateRegion = (Spinner) inflate.findViewById(R.id.date_region);
        analysis = (DrawableCenterTextView) inflate.findViewById(R.id.analysis);
        //mButtonList4
        read_events = (DrawableCenterTextView) inflate.findViewById(R.id.read_events);
        events_manager = (DrawableCenterTextView) inflate.findViewById(R.id.events_manager);
        tabList.add(collectAddress);
        tabList.add(paraCompare);
        tabList.add(readStandard);
        tabList.add(selectItem);
        tabList.add(eventsCompare);
        tabList.add(deleteAll);
        tabList.add(readMeters);
        tabList.add(analysis);
        tabList.add(read_events);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.collect_time_range, android.R.layout.simple_spinner_item);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dateRegion.setAdapter(adapter);
//        dateRegion.setSelection(0, true);
        collectAddress.setText(mCollect.mTerminalIp);
        showLog.setOnClickListener(this);
        closeConn.setOnClickListener(this);
        paraCompare.setOnClickListener(this);
        readStandard.setOnClickListener(this);
        selectItem.setOnClickListener(this);
        eventsCompare.setOnClickListener(this);
        deleteAll.setOnClickListener(this);
        readMeters.setOnClickListener(this);
//        dateRegion.setOnClickListener(this);
        analysis.setOnClickListener(this);
        read_events.setOnClickListener(this);
        events_manager.setOnClickListener(this);

        main_button_list.addView(inflate);

    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.collect_test_center_layout, null);
        fragmentManager = getFragmentManager();
        tab1Layout = (TextView) inflate.findViewById(R.id.tab1);
        tab2Layout = (TextView) inflate.findViewById(R.id.tab2);
        tab3Layout = (TextView) inflate.findViewById(R.id.tab3);
        tab4Layout = (TextView) inflate.findViewById(R.id.tab4);

        tab1Layout.setOnClickListener(this);
        tab2Layout.setOnClickListener(this);
        tab3Layout.setOnClickListener(this);
        tab4Layout.setOnClickListener(this);
        tabList.add(tab1Layout);
        tabList.add(tab2Layout);
        tabList.add(tab3Layout);
        tabList.add(tab4Layout);
        setDefaultFragment();
        main_info.addView(inflate);
    }

    @Override
    protected void initConnList() {

    }

    @Override
    public void setValue(BeanMark bean) {
        //在这里处理回传数据
        resumeAllButton();
    }
    public void stopAllButton(){
        for(TextView tv:tabList){
                tv.setClickable(false);
        }
        for(TextView tv:btnList){
            tv.setClickable(false);
        }
    }
    public void resumeAllButton(){
        for(TextView tv:tabList){
            if(tv.getTag()!=null){
                tv.setClickable(false);
            }else {
                tv.setClickable(true);
            }
        }
        for(TextView tv:btnList){
            tv.setClickable(true);
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tab1:
                if (tab1Fragment == null) {
                    tab1Fragment = new Fragment1();
                }
                replaceFragment(tab1Fragment);
                tab1Layout.setClickable(false);
                tab1Layout.setTag("1");
                tab1Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                currentIndex = 1;
                showControllView(currentIndex);
                break;
            case R.id.tab2:
                if (tab2Fragment == null) {
                    tab2Fragment = new Fragment2();
                }
                replaceFragment(tab2Fragment);
                tab2Layout.setClickable(false);
                tab2Layout.setTag("1");
                tab2Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                currentIndex = 2;
                showControllView(currentIndex);
                break;
            case R.id.tab3:
                if (tab3Fragment == null) {
                    tab3Fragment = new Fragment3();
                }
                replaceFragment(tab3Fragment);
                tab3Layout.setClickable(false);
                tab3Layout.setTag("1");
                tab3Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                currentIndex = 3;
                showControllView(currentIndex);
                break;
            case R.id.tab4:
                if (tab4Fragment == null) {
                    tab4Fragment = new Fragment4();
                }
                replaceFragment(tab4Fragment);
                tab4Layout.setClickable(false);
                tab4Layout.setTag("1");
                tab4Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                currentIndex = 4;
                showControllView(currentIndex);
                break;
            case R.id.show_log:
                showOrHideLog();
                break;
            //1
            case R.id.para_compare://参数对比
                comparePara();
                break;
            case R.id.read_standard:
                readStandard();
                break;
            case R.id.select_item:
                showTestItemsDialog();
                break;
            //2
            case R.id.events_compare:
                eventsCompare();
                break;
            case R.id.delete_all:
                deleteAll();
                break;
            //3
            case R.id.read_meters:
                readMeters();
                break;
            case R.id.date_region:
                dateRegion();
                break;
            case R.id.analysis:
                analysisMiss();
                break;
            //4
            case R.id.read_events:
                readEvents();
                break;
            case R.id.events_manager:
                eventsManager();
                break;
            case R.id.cancel:
                try{
                    mClient.close();
                }catch (Exception e){

                    e.printStackTrace();
                }
                break;
        }
    }

    private void setDefaultFragment() {
        tab1Layout.setClickable(false);
        tab1Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        tab1Fragment = new Fragment1();
        transaction.replace(R.id.content, tab1Fragment, tab1Fragment.getFragmentTag());
        transaction.commit();
        currentIndex = 1;
        showControllView(currentIndex);
    }

    private void replaceFragment(BaseTabFragment newFragment) {
        clearStatus();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!newFragment.isAdded()) {
            transaction.replace(R.id.content, newFragment, newFragment.getFragmentTag());
            transaction.commit();
        } else {
            transaction.show(newFragment);
        }
    }

    private void clearStatus() {
        tab1Layout.setClickable(true);
        tab2Layout.setClickable(true);
        tab3Layout.setClickable(true);
        tab4Layout.setClickable(true);
        tab1Layout.setTag(null);
        tab2Layout.setTag(null);
        tab3Layout.setTag(null);
        tab4Layout.setTag(null);

        if (currentIndex == 1) {
            tab1Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        } else if (currentIndex == 2) {
            tab2Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        } else if (currentIndex == 3) {
            tab3Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        } else if (currentIndex == 4) {
            tab4Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        }
    }

    private void showControllView(int index) {
        switch (index) {
            case 1:
                mButtonList1.setVisibility(View.VISIBLE);
                mButtonList2.setVisibility(View.GONE);
                mButtonList3.setVisibility(View.GONE);
                mButtonList4.setVisibility(View.GONE);
                break;
            case 2:
                mButtonList1.setVisibility(View.GONE);
                mButtonList2.setVisibility(View.VISIBLE);
                mButtonList3.setVisibility(View.GONE);
                mButtonList4.setVisibility(View.GONE);
                break;
            case 3:
                mButtonList1.setVisibility(View.GONE);
                mButtonList2.setVisibility(View.GONE);
                mButtonList3.setVisibility(View.VISIBLE);
                mButtonList4.setVisibility(View.GONE);
                break;
            case 4:
                mButtonList1.setVisibility(View.GONE);
                mButtonList2.setVisibility(View.GONE);
                mButtonList3.setVisibility(View.GONE);
                mButtonList4.setVisibility(View.VISIBLE);
                break;
            default:
                mButtonList1.setVisibility(View.VISIBLE);
                mButtonList2.setVisibility(View.GONE);
                mButtonList3.setVisibility(View.GONE);
                mButtonList4.setVisibility(View.GONE);
                break;
        }
    }

    private void showOrHideLog() {
        if (wholeIsShow()) {
            showWholeLog(false);
            showLog.setText("显示日志");
        } else {
            showWholeLog(true);
            showLog.setText("关闭日志");
        }
    }

    public Collect getCollect(){
        return mCollect;
    }

    @Override
    public void onCollectItemNegativeClick() {

    }

    @Override
    public void onCollectItemPositiveClick(HashMap<Integer, String> collects) {//此处为选定测试项的回调函数
        if (collects != null) {
            Log.e("ssg", "选择测试项目的数量 ＝ " + collects.size());
            Fragment1 fragment = (Fragment1) getFragmentManager().findFragmentByTag(Fragment1.TAG);
            if (fragment != null) {
                if (fragment.getAdapter() != null) {
                    fragment.getAdapter().setSelectedItemts(collects);
                }
                ArrayList<String> list = new ArrayList<String>();
                for (int index : collects.keySet()) {
                    list.add(String.valueOf(index));
                }
                EquipmentPreference.getPreferences(this).setSelectedCollectTest(ModuleUtilites.listToJson(list));
                Log.e("ssg", "onCollectItemPositiveClick json = " + ModuleUtilites.listToJson(list));
            }
        }
    }

    private void showTestItemsDialog() {
        CollectTestItemsDialog testItemsDialog = new CollectTestItemsDialog(this);
        testItemsDialog.show(getFragmentManager(), "select_items");
    }

    private void comparePara() {
        Log.e("ssg", "参数对比");
        try {
            mClient.open();
            mClient.sendMessage("68 32 00 32 00 68 4B 00 50 02 00 02 09 70 00 00 01 00 19 16".replace(" ",""));
            stopAllButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readStandard() {
        Log.e("ssg", "读标准值");
        try {
            mClient.open();
            mClient.sendMessage("68 32 00 32 00 68 4A 00 50 02 00 02 0C 70 00 00 02 00 1C 16".replace(" ",""));
            stopAllButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eventsCompare() {
        Log.e("ssg", "档案对比");
        try {
            mClient.open();
            mClient.sendMessage("68 8A 00 8A 00 68 4B 00 50 02 00 02 0A 70 00 00 02 01 0a 00 01 00 02 00 03 00 04 00 05 00 06 00 07 00 08 00 09 00 0a 00 5d FF 1E 16".replace(" ",""));
            stopAllButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAll() {
        Log.e("ssg", "全部删除");
    }

    private void readMeters() {
        Log.e("ssg", "抄读表码");
        try {
            mClient.open();
            mClient.sendMessage("68 8A 00 8A 00 68 4B 00 50 02 00 02 0A 70 00 00 02 01 0a 00 01 00 02 00 03 00 04 00 05 00 06 00 07 00 08 00 09 00 0a 00 5d FF 1E 16".replace(" ",""));
            stopAllButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dateRegion() {
        Log.e("ssg", "时间区域");
    }

    private void analysisMiss() {
        Log.e("ssg", "分析漏抄");
    }

    private void readEvents() {
        Log.e("ssg", "读取事件");
        try {
            mClient.open();
            mClient.sendMessage("68 3A 00 3A 00 68 4B 00 50 02 00 02 0E 70 00 00 02 00 00 FF 1E 16".replace(" ",""));
            stopAllButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eventsManager() {
        Log.e("ssg", "事件查看");
        Intent intent = new Intent();
        intent.setClass(this, EventManagerActivity.class);
        startActivity(intent);
    }

}
