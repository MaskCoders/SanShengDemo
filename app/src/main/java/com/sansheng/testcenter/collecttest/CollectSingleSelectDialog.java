package com.sansheng.testcenter.collecttest;

import android.app.*;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;
import com.sansheng.testcenter.base.view.PullListView;
import com.sansheng.testcenter.datamanager.MeterDataFragment;
import com.sansheng.testcenter.equipmentmanager.CollectManagerActivity;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.HashMap;

/**
 * Created by sunshaogang on 12/30/15.
 */
public class CollectSingleSelectDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnScrollListener, PullListView.OnLoadMoreListener {
    private View mRootView;
    private ListView mListView;
    private CollectSingleDialogAdapter mAdapter;
    private int mLastVisibleItem;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private int mOriginLength = 10;//默认初始显示数量
    private final static int DOWNSIDE_INCREASE_COUNT = 10;//每次增加数量
    private AnswerDialog mDialog;
    public static final int COMEPOSE_COLLECT_RESULT = 1;

    public CollectSingleSelectDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AnswerDialog(getActivity());
        mDialog.show();
        mDialog.setTitleText("选择要检测的集中器");
        mDialog.setPositiveButtonDismiss();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.collect_single_select_layout, null);
        mListView = (ListView) mRootView.findViewById(R.id.list_view);
        mListView.setOnScrollListener(this);
        mAdapter = new CollectSingleDialogAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        mDialog.setCustomView(mRootView);
        mDialog.getNegativeButton().setText("进入集中器管理界面");
        mDialog.getNegativeButton().setTextColor(getActivity().getResources().getColor(R.color.color_white));
        mDialog.getNegativeButton().setBackground(getActivity().getResources().getDrawable(R.drawable.btn_background_orange_no_circle));
        mDialog.setOnNegativeBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入集中器管理界面
                Intent intent = new Intent();
                intent.setClass(getActivity(), CollectManagerActivity.class);
                startActivityForResult(intent, COMEPOSE_COLLECT_RESULT);
            }
        });

        return mDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("ssg", "requestCode = " + requestCode);
        Log.e("ssg", "resultCode = " + resultCode);
        if (requestCode == COMEPOSE_COLLECT_RESULT) {
            restartLoader();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        int id = loader.getId();
        if (id == LOADER_ID_FILTER_DEFAULT) {
            if (data != null && data.moveToFirst()) {
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

    class CollectSingleDialogAdapter extends SimpleCursorAdapter {
        private Activity mActivity;
        private HashMap<String, Collect> mSelectedCollects = new HashMap<String, Collect>();

        public CollectSingleDialogAdapter(Activity context, Cursor cursor) {
            super(context, android.R.layout.simple_list_item_1, cursor, Collect.CONTENT_PROJECTION,
                    Collect.ID_INDEX_PROJECTION, 0);
            this.mActivity = context;
            this.mSelectedCollects.clear();
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            Cursor cursor = (Cursor) getItem(position);
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (viewHolder == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.collect_single_item_layout, null);
                viewHolder = new ViewHolder();
                initViewHolder(viewHolder, convertView);
                convertView.setTag(viewHolder);
            }
            fillDataToViewHolder(cursor, viewHolder);
            return convertView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            //do nothing
        }

        ViewHolder initViewHolder(final ViewHolder holder, View view) {
            holder.itemLayout = (LinearLayout) view.findViewById(R.id.collect_item);
            holder.collectName = (TextView) view.findViewById(R.id.collect_name);
            holder.collectAddress = (TextView) view.findViewById(R.id.collect_address);
            return holder;
        }

        private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
            final Collect collect = new Collect();
            collect.restore(cursor);
            if (collect.mId == 0) {//无此条数据
                return;
            }
            holder.collectName.setText(String.valueOf(collect.mCollectName));
            holder.collectAddress.setText(collect.mTerminalIp);
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    startTest(collect);
                }
            });

        }

        public class ViewHolder {
            public LinearLayout itemLayout;
            public TextView collectName;
            public TextView collectAddress;
        }

        private void startTest(Collect collect) {
            Log.e("ssg", "开始检测");
            if (collect == null) {
                return;
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(CollectTestUtils.PARAM_COLLECT, collect);
            intent.putExtras(bundle);
            intent.setClass(getActivity(), CollectTestActivity.class);
            startActivity(intent);
        }
    }
}
