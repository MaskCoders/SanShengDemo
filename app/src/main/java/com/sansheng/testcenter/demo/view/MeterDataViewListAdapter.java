package com.sansheng.testcenter.demo.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.module.MeterData;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterDataViewListAdapter extends SimpleCursorAdapter {
    private Context mContext;
    private MeterDataListView mListView;

    public MeterDataViewListAdapter(Context context, Cursor cursor, MeterDataListView listView) {
        super(context, android.R.layout.simple_list_item_1, cursor, MeterData.CONTENT_PROJECTION,
                MeterData.ID_INDEX_PROJECTION, 0);
        this.mContext = context;
        this.mListView = listView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Cursor cursor = (Cursor)getItem(position);
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.meter_data_item_layout, null);
            viewHolder = new ViewHolder();
            initViewHolder(viewHolder, convertView);
            convertView.setTag(viewHolder);
        }
        fillDataToViewHolder(cursor, viewHolder, position);
        return convertView;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //do nothing
    }

    ViewHolder initViewHolder(final ViewHolder holder, View view) {
        holder.itemLayout = (LinearLayout) view.findViewById(R.id.meter_data_item);
        holder.meterNum = (TextView) view.findViewById(R.id.meter_num);
        holder.meterAddress = (TextView) view.findViewById(R.id.meter_address);
        holder.valueTime = (TextView) view.findViewById(R.id.meter_value_time);
        holder.readTime = (TextView) view.findViewById(R.id.meter_read_time);
        holder.meterValue = (TextView) view.findViewById(R.id.meter_value);
//        holder.meterNum.setWidth(mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item1_width));
//        holder.meterAddress.setWidth(mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item2_width));
//        holder.valueTime.setWidth(mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item3_width));
//        holder.readTime.setWidth(mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item4_width));
//        Log.e("ssg", "width1 = " + mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item1_width));
//        Log.e("ssg", "width2 = " + mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item2_width));
//        Log.e("ssg", "width3 = " + mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item3_width));
//        Log.e("ssg", "width4 = " + mActivity.getResources().getDimensionPixelSize(R.dimen.meter_data_item4_width));
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder, int position) {
        final MeterData meterData = new MeterData();
        meterData.restore(cursor);
        if (meterData.mId == 0) {//无此条数据
            return;
        }
        holder.meterNum.setText(String.valueOf(meterData.mMeter.mMeterNum));
        holder.meterAddress.setText(meterData.mMeter.mMeterAddress);
        holder.valueTime.setText(MeterUtilies.getSanShengDate(meterData.mValueTime));
        holder.readTime.setText(MeterUtilies.getSanShengDate(meterData.mReadTime));
        holder.meterValue.setText(String.valueOf(meterData.mValz));
//        int color = position % 2 == 0 ? R.color.color_white : R.color.sky_blue;
//        holder.itemLayout.setBackgroundColor(color);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.showMeterDataDetailFragment(meterData);
            }
        });
    }
    public static class ViewHolder {
        public LinearLayout itemLayout;
        public TextView meterNum;
        public TextView meterAddress;
        public TextView valueTime;
        public TextView readTime;
        public TextView meterValue;
    }
}

