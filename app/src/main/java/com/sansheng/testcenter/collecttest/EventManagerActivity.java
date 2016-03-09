package com.sansheng.testcenter.collecttest;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Event;

/**
 * Created by sunshaogang on 1/21/16.
 */
public class EventManagerActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mListView;
    private EventManagerAdapter mAdapter;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private static final int LOADER_ID_FILTER_COLLECT = 1;
    private static final int LOADER_ID_FILTER_EVENT_TYPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_manager_activity_layout);
        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new EventManagerAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void setValue(BeanMark bean) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder(" 1=1 ");
        return new CursorLoader(this, Event.CONTENT_URI, Event.CONTENT_PROJECTION, selection.toString(),
                null, Event.ID + " " + Content.DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            Log.e("ssg", "cursor count = " + data.getCount());
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void restartLoader(int id) {
        getLoaderManager().restartLoader(id, null, this);
    }
}
