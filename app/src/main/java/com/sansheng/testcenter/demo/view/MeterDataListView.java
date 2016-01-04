package com.sansheng.testcenter.demo.view;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.MeterData;

/**
 * Created by sunshaogang on 12/23/15.
 */
public class MeterDataListView extends LinearLayout implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {

    private View mRootView;
    private Context mContext;
    private Activity mActivity;
    private View mEmptyView;
    private PullListView mListView;
    private MeterDataViewListAdapter mAdapter;

    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量
    public MeterDataListView(Context context) {
        this(context, null);
    }

    public MeterDataListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeterDataListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    private void initView(){
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.meter_data_list_layout, this, true);
        mListView = (PullListView) findViewById(R.id.listview);
        mListView.setOnScrollListener(this);
        mEmptyView = findViewById(R.id.empty_view_group);
        mAdapter = new MeterDataViewListAdapter(mContext, null, this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder(" 1=1 ");
        return new CursorLoader(mContext, MeterData.CONTENT_URI, MeterData.CONTENT_PROJECTION, selection.toString(),
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
            mActivity.getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, this);
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
        mActivity.getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, this);
    }

    public void showMeterDataDetailFragment(MeterData meter) {
        MeterDataFragment fragment = new MeterDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, meter);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(mActivity.getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(meter.mId));
    }
}
