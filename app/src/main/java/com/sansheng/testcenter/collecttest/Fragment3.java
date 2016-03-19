package com.sansheng.testcenter.collecttest;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.datamanager.MeterDataFilterFragment;
import com.sansheng.testcenter.datamanager.MeterDataListAdapter;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class Fragment3 extends BaseTabFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        MeterDataListAdapter.MeterDataCallback{
    public static final String TAG = "Fragment3";
    private View mRootView;
    private Spinner mTimeSpinner;
    private ListView mListView;
    private MeterDataListAdapter mAdapter;
//    private TextView mCollectName;
//    private TextView mCollectAddress;
//    private TextView mCollectChannel;
//    private TextView mCollectIp;
//    private TextView mCollectMiss;

    public static final int LOADER_ID_DEFAULT = 0;
    public static final int LOADER_ID_FILTER = 1;

    private static long startTime;
    private static long endTime;
    private static String ids;
    private static int dataType = 0;
    private static int dataContent = 0;

    public Fragment3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collect_test_fragment3_layout, container, false);
        mTimeSpinner = (Spinner) mRootView.findViewById(R.id.date_region);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.collect_time_range, android.R.layout.simple_spinner_item);
//      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(adapter);
        mTimeSpinner.setSelection(0, true);
        mListView = (ListView) mRootView.findViewById(R.id.list_view);
        mAdapter = new MeterDataListAdapter(getActivity(), null, this);
        mListView.setAdapter(mAdapter);
//        mCollectName = (TextView) mRootView.findViewById(R.id.collect_name);
//        mCollectAddress = (TextView) mRootView.findViewById(R.id.collect_address);
//        mCollectChannel = (TextView) mRootView.findViewById(R.id.collect_channel);
//        mCollectIp = (TextView) mRootView.findViewById(R.id.collect_ip);
//        mCollectMiss = (TextView) mRootView.findViewById(R.id.collect_miss);
//        mCollectName.setText(mCollect.mCollectName);
//        mCollectAddress.setText(mCollect.mCommonAddress);
//        mCollectChannel.setText(CollectTestUtils.channelItems(getActivity())[mCollect.mChannelType]);
//        mCollectIp.setText(mCollect.mTerminalIp);
//        mCollectMiss.setText("");
        getLoaderManager().initLoader(LOADER_ID_DEFAULT, null, this);
        return mRootView;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder();
        switch (id) {
            case LOADER_ID_DEFAULT:
                selection.append(" 1=1 ");
                break;
            case LOADER_ID_FILTER:
                if (!TextUtils.isEmpty(ids)){
                    selection.append(MeterData.METER_ID).append(" in ").append("(").append(ids).append(")").append(" AND ");
                }
                if (dataType != 0) {
                    selection.append(MeterData.DATA_TYPE).append(" = ").append("(").append(dataType).append(")").append(" AND ");
                }
                if (dataContent != 0) {
                    selection.append(MeterData.DATA_ID).append(" = ").append("(").append(dataContent).append(")").append(" AND ");
                }
                selection.append(MeterData.VALUE_TIME).append(" > ").append(startTime).append(" AND ");
                selection.append(MeterData.VALUE_TIME).append(" < ").append(endTime);
                break;
        }
        return new CursorLoader(getActivity(), MeterData.CONTENT_URI, MeterData.CONTENT_PROJECTION, selection.toString(),
                null, MeterData.ID + " " + Content.DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
        if (data != null) {
            Log.e("ssg", "cursor count = " + data.getCount());
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    public void restartLoader(final int id) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(id, null, Fragment3.this);
            }
        });
    }

    private void restartQuery() {
        Bundle bundle = null;
        MeterDataFilterFragment fragment = (MeterDataFilterFragment) getFragmentManager().findFragmentByTag("MeterDataFilterFragment");
        if (fragment != null) {
            bundle = fragment.getFilter();
        }
        if (bundle == null) {
            restartLoader(LOADER_ID_DEFAULT);
            return;
        }
        ids = bundle.getString(MeterUtilies.PARAM_METER_ID);
        startTime = bundle.getLong(MeterUtilies.PARAM_START_TIME);
        endTime = bundle.getLong(MeterUtilies.PARAM_END_TIME);
        dataType = bundle.getInt(MeterUtilies.PARAM_DATA_TYPE);
        dataContent = bundle.getInt(MeterUtilies.PARAM_DATA_CONTENT);
        restartLoader(LOADER_ID_FILTER);
    }

    @Override
    public void setViewMode(int viewMode) {

    }

    @Override
    public void showDetailFragment(MeterData data) {

    }

    @Override
    public void setCurrentData(MeterData data) {

    }
}
