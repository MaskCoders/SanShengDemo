package com.sansheng.testcenter.collecttest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.view.DrawableCenterTextView;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.metertest.MeterSelectFragment;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Content;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.HashMap;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class SelectCollectActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private LinearLayout mHomeController;
    private LinearLayout mCollectDetailController;

    private DrawableCenterTextView text1;
    private DrawableCenterTextView text3;
    private DrawableCenterTextView text4;
    private DrawableCenterTextView confirm;
    private DrawableCenterTextView cancel;

    private ListView mListView;
    private SelectCollectAdapter mAdapter;
    private static final int LOADER_ID_FILTER_DEFAULT = 0;


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
        View inflate = getLayoutInflater().inflate(R.layout.collect_select_control_button_list, null);
        mHomeController = (LinearLayout) inflate.findViewById(R.id.meter_test_control_home);
        mCollectDetailController = (LinearLayout) inflate.findViewById(R.id.meter_test_control_select_meter);
        text1 = (DrawableCenterTextView) inflate.findViewById(R.id.start_test);
        text3 = (DrawableCenterTextView) inflate.findViewById(R.id.new_collect);
        text4 = (DrawableCenterTextView) inflate.findViewById(R.id.delete);
        confirm = (DrawableCenterTextView) inflate.findViewById(R.id.confirm);
        cancel = (DrawableCenterTextView) inflate.findViewById(R.id.cancel);

        mHomeController.setOnClickListener(this);
        mCollectDetailController.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        text1.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.collect_select_list_layout, null);
        mListView = (ListView) inflate.findViewById(R.id.list_view);
        mAdapter = new SelectCollectAdapter(this, null);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID_FILTER_DEFAULT, null, this);
        main_info.addView(inflate);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                MeterSelectFragment fragment = (MeterSelectFragment) getFragmentManager().findFragmentByTag("MeterSelectFragment");
                if (fragment != null) {
                    HashMap<String, Meter> selectMeters = fragment.getSelectedMeters();
                    if (selectMeters != null) {
                        Log.e("ssg", "选中的电表数量 = " + selectMeters.size());
//                        ArrayList<Meter> meters = new ArrayList<Meter>(selectMeters.values());
//                        mMeter = meters.get(0);
//                        mEditMeterAddressView.setText(mMeter.mMeterAddress);
                    }
                }
                showHomeView();
                break;
            case R.id.cancel:
                showHomeView();
                break;
            case R.id.start_test://开始按钮
                startTest();
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
    public void setValue(WhmBean bean) {

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

    private void startTest() {
        Log.e("ssg", "开始检测");
        Intent intent = new Intent();
        intent.setClass(this, CollectTestActivity.class);
        startActivity(intent);
    }

    private void deleteCollect() {
        Log.e("ssg", "删除终端");
    }

}
