package com.example.demo.app;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.demo.app.base.SimpleFooter;
import com.example.demo.app.base.SimpleHeader;
import com.example.demo.app.base.ZrcListView;
import com.example.demo.app.base.ZrcListView.OnStartListener;

import java.util.ArrayList;
/**
 * Created by sunshaogang on 12/9/15.
 */
public class DBOperationActivity extends Activity implements LoaderCallbacks<Cursor>{
    private TextView mQueryText;
    private TextView mInsertText;
    private TextView mUpdateText;
    private TextView mDeleteText;

    private ZrcListView listView;
    private Handler handler;
    private ArrayList<String> msgs;
    private int pageId = -1;
    private MeterListAdapter mAdapter;

    private static final int LOADER_ID_FILTER = 0;

    private static final String[][] names = new String[][]{
            {"加拿大", "瑞典", "澳大利亚", "瑞士", "新西兰", "挪威", "丹麦", "芬兰", "奥地利", "荷兰", "德国", "日本", "比利时", "意大利", "英国"},
            {"德国", "西班牙", "爱尔兰", "法国", "葡萄牙", "新加坡", "希腊", "巴西", "美国", "阿根廷", "波兰", "印度", "秘鲁", "阿联酋", "泰国"},
            {"智利", "波多黎各", "南非", "韩国", "墨西哥", "土耳其", "埃及", "委内瑞拉", "玻利维亚", "乌克兰"},
            {"以色列", "海地", "中国", "沙特阿拉伯", "俄罗斯", "哥伦比亚", "尼日利亚", "巴基斯坦", "伊朗", "伊拉克"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbshower_layout);
        initView();
        mAdapter = new MeterListAdapter(this, null);

        listView = (ZrcListView) findViewById(R.id.zListView);
        handler = new Handler();

        // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
        float density = getResources().getDisplayMetrics().density;
        listView.setFirstTopOffset((int) (50 * density));

        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(this);
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        listView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(this);
        footer.setCircleColor(0xff33bbee);
        listView.setFootable(footer);

        // 设置列表项出现动画（可选）
        listView.setItemAnimForTopIn(R.anim.topitem_in);
        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

        // 下拉刷新事件回调（可选）
        listView.setOnRefreshStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                refresh();
            }
        });

        // 加载更多事件回调（可选）
        listView.setOnLoadMoreStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                loadMore();
            }
        });

        listView.setAdapter(mAdapter);
        listView.refresh(); // 主动下拉刷新
        //加载
        getLoaderManager().initLoader(LOADER_ID_FILTER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Meter.CONTENT_URI, Meter.CONTENT_PROJECTION, null,
                null, Meter.ID + " " + Content.DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。

//        if (mMessageAdapter.getCount() != 0 && data != null && mMessageAdapter.getCount() == data.getCount() && mIsLoadingData) {
//            mMessageListView.setNoMoreData();
//            mStopLoading = true;
//            mMessageAdapter.swapCursor(data);
//            return;
//        }
//        mIsLoadingData = false;
//        int id = loader.getId();
//        if (id == LOADER_ID_FILTER_BY_EMAIL_MINE) {
//            if (loaMore) {
//                mMessageAdapter.allCount = true;
//            }
//            mMessageAdapter.swapCursor(data);
//            mMessageListView.onCompleteLoadMore(PullListView.LOAD_MORE_STATUS_USELESS);
//        } else if (id == LOADER_ID_FILTER_BY_EMAIL_OTHERS) {
//            mMessageAdapter.allCount = true;
//            if (isPullToRefreshing) {
//                if (data != null && data.getCount() != 0) {
//                    mMessageAdapter.swapCursor(data);
//                }
//            } else if (loaMore) {
//                if (data != null && data.getCount() != 0) {
//                    mMessageAdapter.swapCursor(data);
//                    mMessageListView.onCompleteLoadMore(PullListView.LOAD_MORE_STATUE_SUCCESS);
//                } else {
//                    if (todayTimeMillions - downSideTimeMillions >= 14 * CircleUtils.DAY) {
//                        if (!isSyncFinished) {
//                            startLoadMore(true);
//                        } else {
//                            isSyncFinished = false;
//                            mMessageListView.onCompleteLoadMore(PullListView.LOAD_MORE_STATUE_NO_MORE);
//                        }
//                        return;
//                    } else {
//                        downSideTimeMillions -= CircleUtils.DAY;
//                        getLoaderManager().restartLoader(LOADER_ID_FILTER_BY_EMAIL_OTHERS, null, DiscoveryPlateListFragment.this);
//                        return;
//                    }
//                }
//
//            } else {
//                if (data == null || data.getCount() == 0) {
//                    if (CircleCommonUtils.getCurrentDateMillions() - todayTimeMillions > CircleUtils.EXPIRED_PERIOD) {
//                        return;
//                    }
//                    //今日无数据，查询前一天的数据
//                    todayTimeMillions -= CircleUtils.DAY;
//                    getLoaderManager().restartLoader(LOADER_ID_FILTER_BY_EMAIL_OTHERS, null, DiscoveryPlateListFragment.this);
//                    return;
//                } else {
//                    mMessageAdapter.swapCursor(data);
//                }
//            }
//        }
//        int count = 1;
//        if (data != null && data.moveToFirst() && (isFirst || isPullToRefreshing)) {
//            ids = data.getLong(CircleContent.MessageVoteColumn.SERVER_ID_INDEX) + ",";
//            while (data.moveToNext()) {
//                ids += data.getLong(CircleContent.MessageVoteColumn.SERVER_ID_INDEX) + ",";
//                count++;
//                if (count >= data.getCount()) {
//                    break;
//                }
//            }
//        }
//        if (isFirst && data != null) {
//            totalCount = data.getCount();
//            isFirst = false;
//        }
//        isPullToRefreshing = false;
//        loaMore = false;
//        if ((data == null || !data.moveToFirst()) && mMessageListView != null) {
//            mMessageListView.setEmptyView(mEmptyView);
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void initView(){
        mQueryText = (TextView) findViewById(R.id.query);
        mInsertText = (TextView) findViewById(R.id.insert);
        mUpdateText = (TextView) findViewById(R.id.update);
        mDeleteText = (TextView) findViewById(R.id.delete);
        mQueryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mQueryText");
            }
        });
        mInsertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mInsertText");
            }
        });
        mUpdateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mUpdateText");
            }
        });
        mDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mDeleteText");
            }
        });
    }

    private void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int rand = (int) (Math.random() * 2); // 随机数模拟成功失败。这里从有数据开始。
                if (rand == 0 || pageId == -1) {
                    pageId = 0;
                    msgs = new ArrayList<String>();
                    for (String name : names[0]) {
                        msgs.add(name);
                    }
                    mAdapter.notifyDataSetChanged();
                    listView.setRefreshSuccess("加载成功"); // 通知加载成功
                    listView.startLoadMore(); // 开启LoadingMore功能
                } else {
                    listView.setRefreshFail("加载失败");
                }
            }
        }, 2 * 1000);
    }

    private void loadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageId++;
                if (pageId < names.length) {
                    for (String name : names[pageId]) {
                        msgs.add(name);
                    }
                    mAdapter.notifyDataSetChanged();
                    listView.setLoadMoreSuccess();
                } else {
                    listView.stopLoadMore();
                }
            }
        }, 2 * 1000);
    }

}

