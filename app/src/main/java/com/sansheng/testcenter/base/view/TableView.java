package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 12/23/15.
 */
public class TableView extends LinearLayout{

    private View mRootView;
    private LinearLayout mHeaderView;
    private PullListView mListView;
    private Context mContext;

    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView(context);
    }

    void initView(Context context){
        mRootView = LayoutInflater.from(context).inflate(R.layout.table_layout, this, true);
        mHeaderView = (LinearLayout) mRootView.findViewById(R.id.meter_data_header);
        mListView = (PullListView) mRootView.findViewById(R.id.listview);
    }


}
