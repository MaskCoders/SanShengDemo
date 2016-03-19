package com.sansheng.testcenter.equipmentmanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.view.DrawableCenterTextView;
import com.sansheng.testcenter.collecttest.CollectTestUtils;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.utils.Utility;
import hstt.data.DataItem;

import java.util.ArrayList;

/**
 * Created by sunshaogang on 1/20/16.
 * 管理集中器
 */
public class CollectManagerActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private LinearLayout mHomeController;
    private LinearLayout mCollectDetailController;

    private DrawableCenterTextView deleteAll;
    private DrawableCenterTextView compose;
    private DrawableCenterTextView delete;
    private DrawableCenterTextView confirm;
    private DrawableCenterTextView cancel;
    private ListView mListView;
    private CollectManagerAdapter mAdapter;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;
    private String collectAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar(COLLECT_TEST);
        hideBottomLog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.collect_manager_control_layout, null);
        mHomeController = (LinearLayout) inflate.findViewById(R.id.collect_test_control_home);
        mCollectDetailController = (LinearLayout) inflate.findViewById(R.id.collect_test_control_select_meter);
        deleteAll = (DrawableCenterTextView) inflate.findViewById(R.id.delete_all);
        compose = (DrawableCenterTextView) inflate.findViewById(R.id.new_collect);
        delete = (DrawableCenterTextView) inflate.findViewById(R.id.delete);
        confirm = (DrawableCenterTextView) inflate.findViewById(R.id.confirm);
        cancel = (DrawableCenterTextView) inflate.findViewById(R.id.cancel);

        mHomeController.setOnClickListener(this);
        mCollectDetailController.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        deleteAll.setOnClickListener(this);
        compose.setOnClickListener(this);
        delete.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.collect_manager_list_layout, null);
        mListView = (ListView) inflate.findViewById(R.id.list_view);
        mAdapter = new CollectManagerAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        main_info.addView(inflate);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (saveCurrentCollect()) {
                    onBackPressed();
                }
                break;
            case R.id.cancel:
                onBackPressed();
                break;
            case R.id.delete_all://删除全部
                deleteAll();
                break;
            case R.id.new_collect:
                //新建终端
                showNewCollect();
                break;
            case R.id.delete:
                //删除终端
                deleteCollect();
                break;
        }
        super.onClick(v);

    }

    @Override
    public void setValue(DataItem bean) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        StringBuilder selection = new StringBuilder(" 1=1 ");
        return new CursorLoader(this, Collect.CONTENT_URI, Collect.CONTENT_PROJECTION, selection.toString(),
                null, Collect.ID + " " + Content.DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            Log.e("ssg", "cursor count = " + data.getCount());
            mAdapter.swapCursor(data);
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        showHomeView();
        int count = getFragmentManager().getBackStackEntryCount();
        Log.e("ssg", "count = " + count);
        super.onBackPressed();
    }

    public void showDetailFragment(Collect collect) {
        showDetailController();
        CollectDetailFragment fragment = new CollectDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CollectTestUtils.PARAM_COLLECT, collect);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.content,
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN, "CollectDetailFragment");
    }

    public void removeFragment(String tag) {
        int count = getFragmentManager().getBackStackEntryCount();
        Log.e("ssg", "removeFragment count = " + count);
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void showHomeView() {
        showHomeController();
        removeFragment("CollectDetailFragment");
        restartLoader(LOADER_ID_FILTER_DEFAULT);
    }

    public void restartLoader(int id) {
        getLoaderManager().restartLoader(id, null, this);
    }

    private void showHomeController() {
        mHomeController.setVisibility(View.VISIBLE);
        mCollectDetailController.setVisibility(View.GONE);
    }

    private void showDetailController() {
        mHomeController.setVisibility(View.GONE);
        mCollectDetailController.setVisibility(View.VISIBLE);
    }

    private void showNewCollect() {
        Log.e("ssg", "新建终端");
        showDetailFragment(null);
    }

    private void deleteAll() {
        Log.e("ssg", "全部删除");
        getContentResolver().delete(Collect.CONTENT_URI, null, null);
        restartLoader(LOADER_ID_FILTER_DEFAULT);
    }

    private void deleteCollect() {
        Log.e("ssg", "删除终端");
        ArrayList<Collect> collects = mAdapter.getSelectedCollects();
        if (collects == null || collects.size() == 0) {
            Utility.showToast(this, "未选择集中器");
            return;
        }
        String ids = "";
        for (Collect collect:collects) {
            if (!TextUtils.isEmpty(ids)) {
                ids += ",";
            }
            ids += collect.mId;
        }
        Log.e("ssg", "ids = " + ids);
        StringBuilder sb = new StringBuilder();
        sb.append("_id in(");
        sb.append(ids);
        sb.append(") ");
//        if (selection != null) {
//            sb.append(" AND (");
//            sb.append(selection);
//            sb.append(')');
//        }
        getContentResolver().delete(Collect.CONTENT_URI, sb.toString(), null);
        restartLoader(LOADER_ID_FILTER_DEFAULT);
    }

    private boolean saveCurrentCollect() {
        Collect collect = null;
        CollectDetailFragment fragment = (CollectDetailFragment) getFragmentManager().findFragmentByTag("CollectDetailFragment");
        if (fragment != null) {
            collect = fragment.getCollect();
        }
        if (collect != null && collect.isEffective()) {
            if (collect.isSaved()) {
                Log.e("ssg", "collect update = " + collect.mChannelType);
                collect.update(this);
            } else {
                Log.e("ssg", "collect save = " + collect.mChannelType);
                collect.save(this);
            }
            return true;
        } else {
            Utility.showToast(this, "集中器信息不完整");
            return false;
        }
    }

}
