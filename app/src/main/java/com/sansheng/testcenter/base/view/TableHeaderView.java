package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 12/23/15.
 */
public class TableHeaderView extends LinearLayout{

    private LinearLayout mRootView;
    private Context mContext;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;

    public TableHeaderView(Context context) {
        this(context, null);
    }

    public TableHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView(mContext);
    }

    void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) inflater.inflate(R.layout.meter_data_list_header, null);
        textView1 = (TextView) mRootView.findViewById(R.id.header_id1);
        textView2 = (TextView) mRootView.findViewById(R.id.header_id2);
        textView3 = (TextView) mRootView.findViewById(R.id.header_id3);
        textView4 = (TextView) mRootView.findViewById(R.id.header_id4);
        textView5 = (TextView) mRootView.findViewById(R.id.header_id5);
        textView1.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item1_width));
        textView2.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item2_width));
        textView3.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item3_width));
        textView4.setWidth(getResources().getDimensionPixelSize(R.dimen.meter_data_item4_width));
    }
}
