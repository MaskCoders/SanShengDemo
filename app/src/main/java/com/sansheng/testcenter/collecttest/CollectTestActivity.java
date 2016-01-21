package com.sansheng.testcenter.collecttest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.bean.WhmBean;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class CollectTestActivity extends BaseActivity {
    private TextView tab1Layout, tab2Layout, tab3Layout, tab4Layout;
    private int index = 1;
    private FragmentManager fragmentManager;
    private Fragment tab1Fragment, tab2Fragment, tab3Fragment, tab4Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initButtonList() {

    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.collect_test_center_layout, null);
        fragmentManager = getFragmentManager();
        tab1Layout = (TextView) inflate.findViewById(R.id.tab1);
        tab2Layout = (TextView) inflate.findViewById(R.id.tab2);
        tab3Layout = (TextView) inflate.findViewById(R.id.tab3);
        tab4Layout = (TextView) inflate.findViewById(R.id.tab4);

        tab1Layout.setOnClickListener(this);
        tab2Layout.setOnClickListener(this);
        tab3Layout.setOnClickListener(this);
        tab4Layout.setOnClickListener(this);
        setDefaultFragment();
        main_info.addView(inflate);
    }

    @Override
    public void setValue(WhmBean bean) {

    }

    @Override
    public void onClick(View v) {
        clearStatus();
        switch (v.getId()) {
            case R.id.tab1:
                if (tab1Fragment == null) {
                    tab1Fragment = new Fragment1();
                }
                replaceFragment(tab1Fragment);
                tab1Layout.setClickable(false);
                tab1Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                index = 1;
                break;
            case R.id.tab2:
                if (tab2Fragment == null) {
                    tab2Fragment = new Fragment2();
                }
                replaceFragment(tab2Fragment);
                tab2Layout.setClickable(false);
                tab2Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                index = 2;
                break;
            case R.id.tab3:
                if (tab3Fragment == null) {
                    tab3Fragment = new Fragment3();
                }
                replaceFragment(tab3Fragment);
                tab3Layout.setClickable(false);
                tab3Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                index = 3;
                break;
            case R.id.tab4:
                if (tab4Fragment == null) {
                    tab4Fragment = new Fragment4();
                }
                replaceFragment(tab4Fragment);
                tab4Layout.setClickable(false);
                tab4Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
                index = 4;
                break;
        }
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        tab2Fragment = new Fragment2();
        transaction.replace(R.id.content, tab2Fragment);
        transaction.commit();
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!newFragment.isAdded()) {
            transaction.replace(R.id.content, newFragment);
            transaction.commit();
        } else {
            transaction.show(newFragment);
        }
    }

    private void clearStatus() {
        tab1Layout.setClickable(true);
        tab2Layout.setClickable(true);
        tab3Layout.setClickable(true);
        tab4Layout.setClickable(true);

        if (index == 1) {
            tab1Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        } else if (index == 2) {
            tab2Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        } else if (index == 3) {
            tab3Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        } else if (index == 4) {
            tab4Layout.setBackground(getResources().getDrawable(R.drawable.btn_background_orange_to_green));
        } 
    }
}
