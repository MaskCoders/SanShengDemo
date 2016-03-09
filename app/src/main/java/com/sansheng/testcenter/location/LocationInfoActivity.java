package com.sansheng.testcenter.location;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
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
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.datamanager.MeterDataFragment;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.LocationInfo;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 1/7/16.
 * 显示信息列表
 */
public class LocationInfoActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener{
    private PullListView mListView;
    private LocationInfoAdapter mAdapter;
    private Button mComposeBtn;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量
    //start activity for result
    private static final int COMPOSE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_info_list_layout);
        initView();
        setActionBar(LOCATION_INFO);
        mAdapter = new LocationInfoAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == COMPOSE_LOCATION) {
                Log.e("ssg", "添加定位成功");
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                LocationInfo locationInfo = bundle.getParcelable(LocationUtilies.COMPOSE_LOCATION_LOCATIONINFO);
                Log.e("ssg", "insert uri = " + locationInfo.save(this));
                Log.e("ssg", "insert uri = " + locationInfo.toString());
                restartLoader();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder(" 1=1 ");
        return new CursorLoader(this, LocationInfo.CONTENT_URI, LocationInfo.CONTENT_PROJECTION, selection.toString(),
                null, LocationInfo.ID + " " + Content.DESC + " LIMIT " + mOriginLength);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
        if (data == null) {
            return;
        }
        if (mAdapter.getCount() != 0 && mAdapter.getCount() == data.getCount()) {
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
            getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, LocationInfoActivity.this);
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
        getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, LocationInfoActivity.this);
    }

    private void initView() {
        mListView = (PullListView) findViewById(R.id.list_view);
        mListView.setOnScrollListener(this);
        mComposeBtn = (Button) findViewById(R.id.compose_button);
        mComposeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "ocClick Compose Button");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(LocationInfoActivity.this, ComposeLocationActivity.class);
                startActivityForResult(intent, COMPOSE_LOCATION);
            }
        });
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
                getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, LocationInfoActivity.this);
            }
        });
    }


    @Override
    public void setValue(BeanMark bean) {

    }
}