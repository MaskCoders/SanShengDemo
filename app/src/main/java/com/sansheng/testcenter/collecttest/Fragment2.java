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
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Meter;

/**
 * Created by sunshaogang on 1/20/16.
 * show meter list when compare files
 */
public class Fragment2 extends BaseTabFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "Fragment2";
    private View mRootView;
    private TextView mCollectName;
    private TextView mCollectAddress;
    private TextView mCollectChannel;
    private TextView mCollectIp;
    private TextView mCollectState;
    private ListView mListView;
    private MeterCompareListAdapter mAdapter;
    private CollectTestActivity mActivity;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;

    public Fragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (CollectTestActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.collect_test_fragment2_layout, container, false);
//        mCollectName = (TextView) mRootView.findViewById(R.id.collect_name);
//        mCollectAddress = (TextView) mRootView.findViewById(R.id.collect_address);
//        mCollectChannel = (TextView) mRootView.findViewById(R.id.collect_channel);
//        mCollectIp = (TextView) mRootView.findViewById(R.id.collect_ip);
//        mCollectState = (TextView) mRootView.findViewById(R.id.collect_state);
//        mCollectName.setText(mCollect.mCollectName);
//        mCollectAddress.setText(mCollect.mCommonAddress);
//        mCollectChannel.setText(CollectTestUtils.channelItems(mActivity)[mCollect.mChannelType]);
//        mCollectIp.setText(mCollect.mTerminalIp);
//        mCollectState.setText("");
        mRootView = inflater.inflate(R.layout.meter_manager_list_layout, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.list_view);
        mAdapter = new MeterCompareListAdapter(mActivity, null);
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
        StringBuilder selection = new StringBuilder(Content.MeterColumns.COLLECT_ID + " = " + collect.mId);
        return new CursorLoader(mActivity, Meter.CONTENT_URI, Meter.CONTENT_PROJECTION, selection.toString(),
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

    public void restartLoader() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, Fragment2.this);
            }
        });
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
