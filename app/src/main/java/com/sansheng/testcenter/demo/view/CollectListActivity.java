package com.sansheng.testcenter.demo.view;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class CollectListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {
//    private View mEmptyView;
    private PullListView mListView;
    private CollectListAdapter mAdapter;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_list_layout);
        initView();
        mAdapter = new CollectListAdapter(this, null);
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
        return new CursorLoader(this, Collect.CONTENT_URI, Collect.CONTENT_PROJECTION, selection.toString(),
                null, Collect.ID + " " + Content.DESC + " LIMIT " + mOriginLength);
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
            getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, CollectListActivity.this);
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
        getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, CollectListActivity.this);
    }

    private void initView() {
        mListView = (PullListView) findViewById(R.id.listview);
        mListView.setOnScrollListener(this);
//        mEmptyView = findViewById(R.id.empty_view_group);
    }

    public void showDetailFragment(Collect collect) {
        MeterDataFragment fragment = new MeterDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, collect);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(collect.mId));
    }

    public void restartLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, CollectListActivity.this);
            }
        });
    }

    @Override
    public void setValue(WhmBean bean) {

    }
}
