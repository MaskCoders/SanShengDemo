package com.example.demo.view;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.demo.R;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class BaseActivity extends Activity {
    private ActionBar mActionBar;
    private RelativeLayout mActionBarView;
    public final static int DEFAULT_VIEW = -1;//默认
    public final static int GENERAL_VIEW = 0;//主页
    public final static int SOCKET_VIEW = 1;//通信
    public final static int DATABASE_VIEW = 2;//数据库
    public final static int INSERT_DATABASE_VIEW = 3;//插入数据库
    public final static int DETAIL_VIEW = 4;//详情
    public final static int MODIFY_DETAIL_VIEW = 5;//修改详情
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customizeActionbar();
    }
    private void customizeActionbar() {
        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBarView = (RelativeLayout) getLayoutInflater().inflate(
                    R.layout.base_actionbar_view, null);
            mActionBarView.findViewById(R.id.ic_back).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            onBackPressed();
                        }
                    });
            mActionBar.setCustomView(mActionBarView, new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            setActionBar(DEFAULT_VIEW);
        }
    }

    protected void setActionBar(final int mode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mode) {
                    case GENERAL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText("主页");
                        break;
                    case SOCKET_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText("通讯");
                        break;
                    case DATABASE_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText("数据库展示");
                        break;
                    case INSERT_DATABASE_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText("生成测试数据");
                        break;
                    case DETAIL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText("电表数据详情");
                        break;
                    case MODIFY_DETAIL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText("修改电表数据");
                        break;
                    default:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(R.string.company_name);
                }
            }
        });
    }
}
