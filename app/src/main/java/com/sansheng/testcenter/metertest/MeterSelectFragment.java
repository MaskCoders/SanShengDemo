package com.sansheng.testcenter.metertest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.datamanager.MeterDataFragment;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 1/18/16.
 */
public class MeterSelectFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ListView.OnScrollListener, PullListView.OnLoadMoreListener, CollectSelectDialog.CollectCallback {
    private View mRootView;
    private LinearLayout mSelectCollectView;
    private TextView mSelectedCollect;
    private EditText mFilterView;
    private ImageView mSearchView;
    private LinearLayout mSelectAllView;
    private CheckBox mSelectAllCheckBox;
    private ListView mListView;
    private MeterListAdapter mAdapter;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = Integer.MAX_VALUE;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量
    private String collectIds;
    private ArrayList<Collect> collectList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.meter_select_fragment_list_layout, null);
        mSelectCollectView = (LinearLayout) mRootView.findViewById(R.id.meter_select_collect);
        mSelectedCollect = (TextView) mRootView.findViewById(R.id.meter_select_collect_value);
        mFilterView = (EditText) mRootView.findViewById(R.id.meter_select_filter);
        mSelectAllView = (LinearLayout) mRootView.findViewById(R.id.meter_select_all);
        mSelectAllCheckBox = (CheckBox) mRootView.findViewById(R.id.meter_select_all_checkbox);
        mSearchView = (ImageView) mRootView.findViewById(R.id.meter_select_filter_search);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", " 开始查询");
            }
        });
        mSelectCollectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCollectDialog();
            }
        });
        mSelectAllCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectAllCheckBox.isChecked()) {
                    mAdapter.sellectAll(true);
                } else {
                    mAdapter.sellectAll(false);
                }
            }
        });
        mSelectAllView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSelectAllCheckBox.isChecked()) {
                    mSelectAllCheckBox.setChecked(true);
                    mAdapter.sellectAll(true);
                } else {
                    mSelectAllCheckBox.setChecked(false);
                    mAdapter.sellectAll(false);
                }
            }
        });
        mListView = (ListView) mRootView.findViewById(R.id.list_view);
        mAdapter = new MeterListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        return mRootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int type = ((MeterTestActivity)getActivity()).getTestMeterType();
        Log.e("ssg", "type = " + type);
        StringBuilder selection = new StringBuilder();
        if (TextUtils.isEmpty(collectIds)) {
            selection.append(" 1=1 ");
        } else {//如必要,根据选中的集中器显示相应的电表以供选择
            selection.append(Meter.COLLECT_ID).append(" in ").append("(").append(collectIds).append(")");
        }
        selection.append(" AND ").append(Meter.METER_TYPE).append("=").append(type);
        return new CursorLoader(getActivity(), Meter.CONTENT_URI, Meter.CONTENT_PROJECTION, selection.toString(),
                null, Meter.ID + " " + Content.DESC + " LIMIT " + mOriginLength);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mLastVisibleItem >= mListView.getCount() - DOWNSIDE_INCREASE_COUNT / 2 && scrollState == SCROLL_STATE_IDLE) {
            mOriginLength += DOWNSIDE_INCREASE_COUNT;
            getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, this);
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
        getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, this);
    }

    public void showDetailFragment(Collect collect) {
        MeterDataFragment fragment = new MeterDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, collect);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(collect.mId));
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, this);
    }

    private void showCollectDialog() {
        CollectSelectDialog collectDialog = new CollectSelectDialog(this);
        collectDialog.show(getActivity().getFragmentManager(), "select_collects");
    }

    @Override
    public void onCollectNegativeClick() {

    }

    @Override
    public void onCollectPositiveClick(HashMap<String, Collect> collects) {
        //get collect list
        Log.e("ssg", "selected collects size = " + collects.size());
        if (collects != null && collects.size() > 0) {
            collectIds = "";
            collectList = new ArrayList<Collect>(collects.values());
            mSelectedCollect.setText(collectList.get(0).mCollectName
                    + "[" + collectList.size() + "]");
            for (Collect collect : collectList) {
                if (!TextUtils.isEmpty(collectIds)) {
                    collectIds += ",";
                }
                collectIds += String.valueOf(collect.mId);
            }
            Log.e("ssg", "collectIds = " + collectIds);
            restartLoader();
        }
    }

    public interface MeterCallback {
        void onMeterPositiveClick(HashMap<String, Meter> meters);
    }

    public HashMap<String, Meter> getSelectedMeters() {
        if (mAdapter != null) {
            return mAdapter.getSelectedMeters();
        }
        return null;
    }

}

