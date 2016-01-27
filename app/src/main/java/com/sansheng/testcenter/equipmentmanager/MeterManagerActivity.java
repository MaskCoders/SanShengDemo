package com.sansheng.testcenter.equipmentmanager;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Meter;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterManagerActivity extends BaseActivity implements LoaderCallbacks<Cursor>{

    private View mEmptyView;
    private ListView mListView;
    private MeterManagerAdapter mAdapter;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_list_layout);
        initView();
        mAdapter = new MeterManagerAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        setActionBar(METER_LIST_VIEW);
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
        return new CursorLoader(this, Meter.CONTENT_URI, Meter.CONTENT_PROJECTION, selection.toString(),
                null, Meter.ID + " " + Content.DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
        Log.e("ssg", "meter cursor count = " + data.getCount());
        int id = loader.getId();
        if (id == LOADER_ID_FILTER_DEFAULT) {
            if (data != null) {
                mAdapter.swapCursor(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mEmptyView = findViewById(R.id.empty_view_group);
    }

    public void restartLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, MeterManagerActivity.this);
            }
        });
    }

    @Override
    public void pullShortLog(String info) {

    }

    @Override
    public void pullShortLog(SpannableString info) {

    }

    @Override
    public void pullWholeLog(String info) {

    }

    @Override
    public void setValue(WhmBean bean) {

    }

    @Override
    public void pullWholeLog(SpannableString info) {

    }

    @Override
    public void pullStatus(String info) {

    }
}

