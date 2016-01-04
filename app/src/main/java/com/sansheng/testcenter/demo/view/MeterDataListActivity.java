package com.sansheng.testcenter.demo.view;

import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.MeterData;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterDataListActivity extends BaseActivity implements LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {

    private View mEmptyView;
    private PullListView mListView;
    private Handler mHandler = new Handler();
    private MeterDataListAdapter mAdapter;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_data_list_layout);
        initView();
        mAdapter = new MeterDataListAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        setActionBar(METERDATA_LIST_VIEW);
    }

    @Override
    protected void initButtonList() {

    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {

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
            mListView.setEmptyView(mEmptyView);
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

    private void initView() {
//        initHeaderView();
        mListView = (PullListView) findViewById(R.id.listview);
        mListView.setOnScrollListener(this);
        mEmptyView = findViewById(R.id.empty_view_group);
    }

    public void showDetailFragment(MeterData meter) {
        MeterDataFragment fragment = new MeterDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, meter);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(meter.mId));
    }

    public void restartLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, MeterDataListActivity.this);
            }
        });
    }

//    private void initHeaderView(){
//        TextView textView1 = (TextView) findViewById(R.id.header_id1);
//        TextView textView2 = (TextView) findViewById(R.id.header_id2);
//        TextView textView3 = (TextView) findViewById(R.id.header_id3);
//        TextView textView4 = (TextView) findViewById(R.id.header_id4);
//        textView1.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item1_width));
//        textView2.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item2_width));
//        textView3.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item3_width));
//        textView4.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item4_width));
//    }

}

