package com.sansheng.testcenter.collecttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class Fragment3 extends BaseTabFragment {
    public static final String TAG = "Fragment3";
    private View mRootView;
    private Spinner mTimeSpinner;

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
        return mRootView;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
