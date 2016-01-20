package com.sansheng.testcenter.demo.view;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.datamanager.MeterDataFragment;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class CollectListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private ListView mListView;
    private CollectListAdapter mAdapter;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_list_layout);
        initView();
        mAdapter = new CollectListAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        setActionBar(COLLECT_TEST);
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
        return new CursorLoader(this, Collect.CONTENT_URI, Collect.CONTENT_PROJECTION, selection.toString(),
                null, Collect.ID + " " + Content.DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {//加载更多。
        Log.e("ssg", "cursor count = " + data.getCount());
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
        mListView = (ListView) findViewById(R.id.list_view);
    }

    public void showDetailFragment(Collect collect) {
        MeterDataFragment fragment = new MeterDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, collect);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(collect.mId));
    }

    public void restartLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(LOADER_ID_FILTER_DEFAULT, null, CollectListActivity.this);
            }
        });
    }

    @Override
    public void setValue(WhmBean bean) {

    }
}
