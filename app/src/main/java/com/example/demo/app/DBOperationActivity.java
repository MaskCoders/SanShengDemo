package com.example.demo.app;

import android.app.Activity;
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

/**
 * Created by sunshaogang on 12/9/15.
 */
public class DBOperationActivity extends Activity implements LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {

    private View mEmptyView;
    private PullListView mListView;
    private Handler mHandler = new Handler();
    private MeterListAdapter mAdapter;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_layout);
        initView();
        mAdapter = new MeterListAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder(" 1=1 ");
        return new CursorLoader(this, Meter.CONTENT_URI, Meter.CONTENT_PROJECTION, selection.toString(),
                null, Meter.ID + " " + Content.DESC + " LIMIT " + mOriginLength);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
        Log.e("ssg", "onLoadFinished cursor.getCount() = " + data.getCount());
        if ((data == null || !data.moveToFirst()) && mListView != null) {
            mListView.setEmptyView(mEmptyView);
            return;
        }
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mLastVisibleItem >= mListView.getCount() - DOWNSIDE_INCREASE_COUNT / 2 && scrollState == SCROLL_STATE_IDLE) {
            mOriginLength += DOWNSIDE_INCREASE_COUNT;
            getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, DBOperationActivity.this);
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
        getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, DBOperationActivity.this);
    }

    private void initView() {
        mListView = (PullListView) findViewById(R.id.listview);
        mListView.setOnScrollListener(this);
        mEmptyView = findViewById(R.id.empty_view_group);
    }
}

