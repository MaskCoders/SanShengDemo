package com.sansheng.testcenter.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;

/**
 * Created by sunshaogang on 12/10/15.
 */
public  abstract class BaseActivity extends Activity implements View.OnClickListener, IServiceHandlerCallback {
    private ActionBar mActionBar;
    private FrameLayout mActionBarView;
    public final static int DEFAULT_VIEW = -1;//默认
    public final static int GENERAL_VIEW = 0;//主页
    public final static int SOCKET_VIEW = 1;//通信
    public final static int METER_LIST_VIEW = 2;//电表
    public final static int FILE_MANAGER_VIEW = 3;//档案管理
    public final static int INSERT_DATABASE_VIEW = 4;//插入数据库
    public final static int DETAIL_VIEW = 5;//详情
    public final static int MODIFY_DETAIL_VIEW = 6;//修改详情a
    public final static int SETTINGS_VIEW = 7;//修改详情
    public final static int LOCATION_INFO = 8;//定位
    public final static int COMPOSE_LOCATION = 9;//新增现场信息
    public final static int METER_TEST = 10;//电表检测
    public final static int COLLECT_TEST = 11;//电表检测
    public final static int DATA_LIST_VIEW = 12;//数据浏览， 查，删，不允许修改
    public final static int STATE_GRID_MASTER_SATTION = 13;//国网主站
    private ActionBarCallback mActionBarCallback;
    protected StringBuffer logBuffer = new StringBuffer();
    protected DrawerLayout mDrawerLayout;
    protected EditText main_sort_log;
    protected EditText main_whole_log;
    protected ImageButton main_log_down_btn;
    protected LinearLayout main_info;
    protected TextView main_status_info;
//    protected TextView base_title;
//    protected TextView back_btn;
    protected LinearLayout main_button_list;
    protected LinearLayout main_layout_conn;
    protected LinearLayout main_whole_log_ll;

    @Override
    public void pullShortLog(String info) {

    }

    @Override
    public void pullShortLog(SpannableString info) {

    }

    @Override
    public void pullWholeLog(String info) {
        main_whole_log.append(info);
    }

    @Override
    public void pullWholeLog(SpannableString info) {
        main_whole_log.append(info);
    }

    @Override
    public void pullStatus(String info) {

    }

    public interface ActionBarCallback {
        void onSaveClick();

        void onCancleClick();
    }
    protected boolean wholeIsShow(){
        return main_whole_log_ll.getVisibility() == View.VISIBLE;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        customizeActionbar();
        initBaseViews();
    }

    protected abstract void initButtonList();

    protected abstract void initConnList();
    protected abstract void initCenter();

    private void initBaseViews() {
        setContentView(R.layout.base_layout);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        main_sort_log = (EditText) findViewById(R.id.main_sort_log);
        main_whole_log = (EditText) findViewById(R.id.main_whole_log);
        main_log_down_btn = (ImageButton) findViewById(R.id.main_log_down_btn);
        main_info = (LinearLayout) findViewById(R.id.main_info);
        main_status_info = (TextView) findViewById(R.id.main_status_info);
//        base_title = (TextView) findViewById(R.id.base_title);
//        back_btn = (TextView) findViewById(R.id.back_btn);
        main_button_list = (LinearLayout) findViewById(R.id.main_button_list);
        main_layout_conn = (LinearLayout) findViewById(R.id.main_layout_conn);
        main_whole_log_ll = (LinearLayout) findViewById(R.id.main_whole_log_ll);
//        back_btn.setOnClickListener(this);
        initButtonList();
        initConnList();
        initCenter();
    }

    protected void setDrawerDisable(){
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    protected void hideBottomLog (){
        main_status_info.setVisibility(View.GONE);
    }

    protected void showShortLog(boolean flag) {
//        if(main_whole_log.getVisibility() == View.VISIBLE){
//            main_sort_log.setVisibility(View.GONE);
//        }else {
            main_sort_log.setVisibility((main_whole_log.getVisibility() != View.VISIBLE && flag) ? View.VISIBLE : View.GONE);
        new Handler().postDelayed(new Runnable(){

            public void run() {
                main_sort_log.setVisibility(View.GONE);
            }

        }, 1000);
    }
    protected void setTitle(String title){
//        base_title.setText(title);
    }
    protected void showWholeLog(boolean flag) {
        main_whole_log_ll.setVisibility(flag ? View.VISIBLE : View.GONE);
        if(flag){
            main_sort_log.setVisibility(View.GONE);
        }
    }

    private void customizeActionbar() {
        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBarView = (FrameLayout) getLayoutInflater().inflate(
                    R.layout.base_actionbar_view, null);
            mActionBarView.findViewById(R.id.ic_back).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            onBackPressed();
                        }
                    });
            mActionBar.setCustomView(mActionBarView, new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            setActionBar(DEFAULT_VIEW);
            mActionBarView.findViewById(R.id.ab_modify_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionBarCallback != null) {
                        mActionBarCallback.onSaveClick();
                    }
                }
            });
            mActionBarView.findViewById(R.id.ab_modify_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionBarCallback != null) {
                        mActionBarCallback.onCancleClick();
                    }
                }
            });
        }
    }

    public void setActionBar(final int mode) {
        setActionBar(mode, null);
    }

    public void setActionBar(final int mode, ActionBarCallback callback) {
        mActionBarCallback = callback;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mode) {
                    case GENERAL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.index_page));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.VISIBLE);
                        break;
                    case SOCKET_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.connect_page));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case METER_LIST_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_meter_list));
                        mActionBarView.findViewById(R.id.ab_modify_view).setVisibility(View.GONE);
                        mActionBarView.findViewById(R.id.ab_view).setVisibility(View.VISIBLE);
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case FILE_MANAGER_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_file_manager));
                        mActionBarView.findViewById(R.id.ab_modify_view).setVisibility(View.GONE);
                        mActionBarView.findViewById(R.id.ab_view).setVisibility(View.VISIBLE);
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case INSERT_DATABASE_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.create_db));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case DETAIL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.db_info));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case MODIFY_DETAIL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_modify_title)).setText(getResources().getText(R.string.change_db));
                        mActionBarView.findViewById(R.id.ab_modify_view).setVisibility(View.VISIBLE);
                        mActionBarView.findViewById(R.id.ab_view).setVisibility(View.GONE);
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case SETTINGS_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_settings));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case LOCATION_INFO:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_gps));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case COMPOSE_LOCATION:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_compose_location));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case METER_TEST:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_socket));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case COLLECT_TEST:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_collect));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case DATA_LIST_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_data_list));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    case STATE_GRID_MASTER_SATTION:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.function_home));
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                    default:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(R.string.company_name);
                        (mActionBarView.findViewById(R.id.ab_time)).setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.back_btn:
//                finish();
//                break;
//        }
    }
}
