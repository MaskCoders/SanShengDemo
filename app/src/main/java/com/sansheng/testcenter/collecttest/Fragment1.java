package com.sansheng.testcenter.collecttest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class Fragment1 extends Fragment{

    private View mRootView;
    public Fragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collect_test_layout, container, false);
        mRootView.setBackgroundColor(getResources().getColor(R.color.new_color3));
        return mRootView;
    }

}
