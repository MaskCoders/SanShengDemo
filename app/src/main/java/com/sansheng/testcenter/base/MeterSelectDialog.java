package com.sansheng.testcenter.base;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.datamanager.MeterDataFragment;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.HashMap;

/**
 * Created by sunshaogang on 12/30/15.
 */
public class MeterSelectDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {
    private View mRootView;
//    private View mEmptyView;
    private TextView mSelectCollectView;
    private EditText mFilterView;
    private CheckBox mSelectAllView;
    private PullListView mListView;
    private MeterListDialogAdapter mAdapter;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = Integer.MAX_VALUE;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量
    private AnswerDialog mDialog;
    private MeterCallback callback;
    private String collectIds;

    public MeterSelectDialog(MeterCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AnswerDialog(getActivity());
        mDialog.show();
        mDialog.setTitleText("选择电表");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.meter_dialog_list_layout, null);
        mSelectCollectView = (TextView) mRootView.findViewById(R.id.meter_select_collect);
        mFilterView = (EditText) mRootView.findViewById(R.id.meter_select_filter);
        mSelectAllView = (CheckBox) mRootView.findViewById(R.id.meter_select_all);
        mSelectAllView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAdapter.addAllMeters();
                } else {
                    mAdapter.getSelectedMeters().clear();
                }
            }
        });
        mListView = (PullListView) mRootView.findViewById(R.id.listview);
        mListView.setOnScrollListener(this);
        mListView.hideFooterView();
//        mEmptyView = mRootView.findViewById(R.id.empty_view_group);
        mAdapter = new MeterListDialogAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        mDialog.setCustomView(mRootView);
        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                callback.onMeterPositiveClick(mAdapter.getSelectedMeters());
            }
        });
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        collectIds = String .valueOf(0);
        StringBuilder selection = new StringBuilder();
        if (TextUtils.isEmpty(collectIds)) {
            selection.append(" 1=1 ");
        } else {//如必要,根据选中的集中器显示相应的电表以供选择
            selection.append(Meter.COLLECT_ID).append(" in ").append("(").append(collectIds).append(")");
        }
        return new CursorLoader(getActivity(), Meter.CONTENT_URI, Meter.CONTENT_PROJECTION, selection.toString(),
                null, Meter.ID + " " + Content.DESC + " LIMIT " + mOriginLength);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
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
            getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, this);
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

    public interface MeterCallback {
        void onMeterPositiveClick(HashMap<String, Meter> meters);
    }
}
