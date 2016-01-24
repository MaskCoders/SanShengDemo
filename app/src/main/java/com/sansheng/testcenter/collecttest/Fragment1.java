package com.sansheng.testcenter.collecttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.ModuleUtilites;
import com.sansheng.testcenter.provider.EquipmentPreference;

import java.util.HashMap;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class Fragment1 extends BaseTabFragment{
    public static final String TAG = "Fragment1";

    private View mRootView;
    private ListView mListView;
    private CollectTestItemAdapter mAdapter;
    public Fragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collect_test_fragment1_layout, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.list_view);
        mAdapter = new CollectTestItemAdapter(getActivity());
        mAdapter.setSelectedItemts(getSelectProject());
        mListView.setAdapter(mAdapter);
        return mRootView;
    }
    private HashMap<Integer, String> getSelectProject(){
        String result = EquipmentPreference.getPreferences(getActivity()).getSelectedCollectTest();
        Log.e("ssg", "result = " + result);
        if (TextUtils.isEmpty(result)) {
            result = "[\"0\",\"1\",\"2\"]";
        }
       return ModuleUtilites.jsonToMapForMeterTest(result, getResources().getStringArray(R.array.meter_test_items));

    }
    @Override
    public String getFragmentTag() {
        return TAG;
    }

    public CollectTestItemAdapter getAdapter(){
        return mAdapter;
    }
}
