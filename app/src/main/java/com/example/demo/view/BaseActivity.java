package com.example.demo.view;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.demo.R;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class BaseActivity extends Activity {
    private ActionBar mActionBar;
    private FrameLayout mActionBarView;
    public final static int DEFAULT_VIEW = -1;//默认
    public final static int GENERAL_VIEW = 0;//主页
    public final static int SOCKET_VIEW = 1;//通信
    public final static int METER_LIST_VIEW = 2;//数据库
    public final static int INSERT_DATABASE_VIEW = 3;//插入数据库
    public final static int DETAIL_VIEW = 4;//详情
    public final static int MODIFY_DETAIL_VIEW = 5;//修改详情
    private ActionBarCallback mActionBarCallback;

    public interface ActionBarCallback {
        void onSaveClick();

        void onCancleClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customizeActionbar();
    }

    private void customizeActionbar() {
        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBarView = (FrameLayout) getLayoutInflater().inflate(
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
            mActionBarView.findViewById(R.id.ab_modify_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionBarCallback != null) {
                        mActionBarCallback.onSaveClick();
                    }
                }
            });
            mActionBarView.findViewById(R.id.ab_modify_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionBarCallback != null) {
                        mActionBarCallback.onCancleClick();
                    }
                }
            });
        }
    }

    protected void setActionBar(final int mode) {
        setActionBar(mode, null);
    }

    protected void setActionBar(final int mode, ActionBarCallback callback) {
        mActionBarCallback = callback;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mode) {
                    case GENERAL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.index_page));
                        break;
                    case SOCKET_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.connect_page));
                        break;
                    case METER_LIST_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.db_show));
                        mActionBarView.findViewById(R.id.ab_modify_view).setVisibility(View.GONE);
                        mActionBarView.findViewById(R.id.ab_view).setVisibility(View.VISIBLE);
                        break;
                    case INSERT_DATABASE_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.create_db));
                        break;
                    case DETAIL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(getResources().getText(R.string.db_info));
                        break;
                    case MODIFY_DETAIL_VIEW:
                        ((TextView) mActionBarView.findViewById(R.id.ab_modify_title)).setText(getResources().getText(R.string.change_db));
                        mActionBarView.findViewById(R.id.ab_modify_view).setVisibility(View.VISIBLE);
                        mActionBarView.findViewById(R.id.ab_view).setVisibility(View.GONE);
                        break;

                    default:
                        ((TextView) mActionBarView.findViewById(R.id.ab_title)).setText(R.string.company_name);
                        break;
                }
            }
        });
    }

}
