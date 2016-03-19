package com.sansheng.testcenter.datamanager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterDataListAdapter extends SimpleCursorAdapter {
    private MeterDataCallback mCallback;
    private Context mContext;

    public MeterDataListAdapter(Context context, Cursor cursor, MeterDataCallback callback) {
        super(context, android.R.layout.simple_list_item_1, cursor, MeterData.CONTENT_PROJECTION,
                MeterData.ID_INDEX_PROJECTION, 0);
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Cursor cursor = (Cursor) getItem(position);
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.meter_data_item_layout, null);
            viewHolder = new ViewHolder();
            initViewHolder(viewHolder, convertView);
            convertView.setTag(viewHolder);
        }
        fillDataToViewHolder(cursor, viewHolder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //do nothing
    }

    ViewHolder initViewHolder(final ViewHolder holder, View view) {
        holder.itemLayout = (LinearLayout) view.findViewById(R.id.meter_data_item);
        holder.meterName = (TextView) view.findViewById(R.id.meter_name);
        holder.meterAddress = (TextView) view.findViewById(R.id.meter_address);
        holder.valueTime = (TextView) view.findViewById(R.id.meter_value_time);
        holder.meterValue = (TextView) view.findViewById(R.id.meter_value);
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
        final MeterData meterData = new MeterData();
        meterData.restore(cursor);
        if (meterData.mId == 0) {//无此条数据
            return;
        }
        holder.meterName.setText(String.valueOf(meterData.mMeter.mMeterName));
        holder.meterAddress.setText(meterData.mMeter.mMeterAddress);
        holder.valueTime.setText(MeterUtilies.getSanShengDate(meterData.mValueTime));
        holder.meterValue.setText(String.valueOf(meterData.mValz));

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.setViewMode(MeterDataListActivity.VIEW_MODE_DETIAL);
                mCallback.showDetailFragment(meterData);
                mCallback.setCurrentData(meterData);
            }
        });
    }

    public static class ViewHolder {
        public LinearLayout itemLayout;
        public TextView meterName;
        public TextView meterAddress;
        public TextView valueTime;
        public TextView meterValue;
    }

    public static interface MeterDataCallback {
        public void setViewMode(int viewMode);

        public void showDetailFragment(MeterData data);

        public void setCurrentData(MeterData data);
    }
}

