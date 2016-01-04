package com.sansheng.testcenter.base;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.demo.view.CollectListAdapter;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.demo.view.MeterDataFragment;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;

import java.util.HashMap;

/**
 * Created by sunshaogang on 12/30/15.
 */
public class CollectSelectDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {
    private View mRootView;
//    private View mEmptyView;
    private PullListView mListView;
    private CollectListDialogAdapter mAdapter;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量
    private AnswerDialog mDialog;
    private CollectCallback callback;

    public CollectSelectDialog() {
    }

    public CollectSelectDialog(CollectCallback callback) {
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
        mDialog.setTitleText("选择集中器");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.collect_list_layout, null);
        mListView = (PullListView) mRootView.findViewById(R.id.listview);
        mListView.setOnScrollListener(this);
        mListView.hideFooterView();
//        mEmptyView = mRootView.findViewById(R.id.empty_view_group);
        mAdapter = new CollectListDialogAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        mDialog.setCustomView(mRootView);
        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCollectNegativeClick();
                mDialog.dismiss();
            }
        });
        mDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                callback.onCollectPositiveClick(mAdapter.getSelectedCollects());
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
        StringBuilder selection = new StringBuilder(" 1=1 ");
        return new CursorLoader(getActivity(), Collect.CONTENT_URI, Collect.CONTENT_PROJECTION, selection.toString(),
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

    public interface CollectCallback {
        void onCollectNegativeClick();
        void onCollectPositiveClick(HashMap<String, Collect> collects);
    }
}
