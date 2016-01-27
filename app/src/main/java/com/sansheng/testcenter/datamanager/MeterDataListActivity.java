package com.sansheng.testcenter.datamanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterDataListActivity extends BaseActivity implements LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {

    private PullListView mListView;
    private MeterDataListAdapter mAdapter;
    private Button text1;
    private Button text2;
    private Button text3;
    private Button text4;
    private Button text5;
//    private Button text6;
    private int mLastVisibleItem;
    public static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量

    public static final int VIEW_MODE_LIST = 0;
    public static final int VIEW_MODE_DETIAL = 1;
    public static final int VIEW_MODE_FILTER = 2;
    private static int mViewMode = VIEW_MODE_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        setActionBar(DATA_LIST_VIEW);
        setDrawerDisable();
        hideBottomLog();
        setViewMode(mViewMode);
        setTitle("档案管理");
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.meter_data_control_button_list, null);
        text1 = (Button) inflate.findViewById(R.id.text1);
        text2 = (Button) inflate.findViewById(R.id.text2);
        text3 = (Button) inflate.findViewById(R.id.text3);
        text4 = (Button) inflate.findViewById(R.id.text4);
        text5 = (Button) inflate.findViewById(R.id.text5);
//        text6 = (Button) inflate.findViewById(R.id.text6);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        text5.setOnClickListener(this);
//        text6.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.meter_data_center_list_layout, null);
        mListView = (PullListView) inflate.findViewById(R.id.list_view);
        mAdapter = new MeterDataListAdapter(this, null);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        main_info.addView(inflate);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder(" 1=1 ");
        return new CursorLoader(this, MeterData.CONTENT_URI, MeterData.CONTENT_PROJECTION, selection.toString(),
                null, MeterData.ID + " " + Content.DESC + " LIMIT " + mOriginLength);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
        Log.e("ssg", "cursor count = " + data.getCount());
        if (mAdapter.getCount() != 0 && data != null && mAdapter.getCount() == data.getCount()) {
            mListView.setNoMoreData();
            mAdapter.swapCursor(data);
            return;
        }
        int id = loader.getId();
        if (id == LOADER_ID_FILTER_DEFAULT) {
            if (data != null && data.moveToFirst()) {
                mAdapter.swapCursor(data);
                mListView.onCompleteLoadMore(PullListView.LOAD_MORE_STATUS_USELESS);
            }
        }
        if ((data == null || !data.moveToFirst()) && mListView != null) {
//            mListView.setEmptyView(mEmptyView);
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mLastVisibleItem >= mListView.getCount() - DOWNSIDE_INCREASE_COUNT / 2 && scrollState == SCROLL_STATE_IDLE) {
            mOriginLength += DOWNSIDE_INCREASE_COUNT;
            getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, MeterDataListActivity.this);
            mListView.setFooterViewStatic();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLastVisibleItem = firstVisibleItem + visibleItemCount - 1;
    }

    @Override
    public void onLoadMore(PullListView refreshView) {
        refreshView.onCompleteLoadMore(PullListView.LOAD_MORE_STATUE_SUCCESS);
        mOriginLength += DOWNSIDE_INCREASE_COUNT;
        getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, MeterDataListActivity.this);
    }

    public void showDetailFragment(MeterData meter) {
        MeterDataFragment fragment = new MeterDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, meter);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, "MeterDataFragment");
    }

    public void showFilterFragment(){
        MeterDataFilterFragment fragment = new MeterDataFilterFragment();
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, "MeterDataFilterFragment");
    }

    public void removeFragment(String tag){
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void restartLoader(final int id){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(id, null, MeterDataListActivity.this);
            }
        });
    }

    @Override
    public void setValue(WhmBean bean) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1://查询条件
                showFilterFragment();
                setViewMode(VIEW_MODE_FILTER);
                break;
            case R.id.text2://全部删除
                break;
            case R.id.text3://删除当前数据
                showHomeView();
                removeFragment("MeterDataFragment");
                break;
            case R.id.text4://确认
                showHomeView();
                removeFragment("MeterDataFilterFragment");
                break;
            case R.id.text5://取消
                showHomeView();
                removeFragment("MeterDataFilterFragment");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showHomeView();
        super.onBackPressed();
    }

    public void showHomeView() {
        setViewMode(VIEW_MODE_LIST);
        restartLoader(LOADER_ID_FILTER_DEFAULT);
    }

    public void setViewMode(int mode){
        switch (mode) {
            case VIEW_MODE_LIST:
                mViewMode = mode;
                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                text3.setVisibility(View.GONE);
                text4.setVisibility(View.GONE);
                text5.setVisibility(View.GONE);
                break;
            case VIEW_MODE_DETIAL:
                mViewMode = mode;
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.VISIBLE);
                text4.setVisibility(View.GONE);
                text5.setVisibility(View.GONE);
                break;
            case VIEW_MODE_FILTER:
                mViewMode = mode;
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.GONE);
                text4.setVisibility(View.VISIBLE);
                text5.setVisibility(View.VISIBLE);
                break;
        }
    }

}

