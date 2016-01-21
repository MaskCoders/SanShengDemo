package com.sansheng.testcenter.collecttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class Fragment4 extends BaseTabFragment{
    public static final String TAG = "Fragment4";
    private View mRootView;
    public Fragment4() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collect_test_fragment1_layout, container, false);
        return mRootView;
    }
    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
