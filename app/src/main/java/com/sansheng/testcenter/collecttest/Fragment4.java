package com.sansheng.testcenter.collecttest;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Event;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class Fragment4 extends BaseTabFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG = "Fragment4";
    private View mRootView;
    private ListView mListView;
    private EventCountAdapter mAdapter;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private static final int LOADER_ID_FILTER_COLLECT = 1;
    private static final int LOADER_ID_FILTER_EVENT_TYPE = 2;
    private CollectTestActivity mActivity;
    private int mCurrentType = -1;

    public Fragment4() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (CollectTestActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collect_test_fragment4_layout, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.list_view);
        mAdapter = new EventCountAdapter(mActivity, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        return mRootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Collect collect = mActivity.getCollect();
        if (collect == null) {
            return null;
        }
        StringBuilder selection = new StringBuilder();
        selection.append(Content.MeterColumns.COLLECT_ID + " = " + collect.mId);
        selection.append(" GROUP BY ").append(Content.EventColumns.TYPE);
        Log.e("ssg", " event selection = " + selection);
        return new CursorLoader(mActivity,
                Event.CONTENT_URI,
                Event.CONTENT_PROJECTION,
                selection.toString(),
                null, Content.EventColumns.HEPPEN_TIME + " " + Content.DESC);
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

    @Override
    public String getFragmentTag() {
        return TAG;
    }

}
