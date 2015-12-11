package com.example.demo.view;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.demo.R;
import com.example.demo.mode.Meter;
import com.example.demo.util.MeterUtilies;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterListAdapter extends SimpleCursorAdapter {
    private MeterListActivity mActivity;

    public MeterListAdapter(MeterListActivity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Meter.CONTENT_PROJECTION,
                Meter.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.meter_item_layout, null);
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
        holder.infoLayout = (RelativeLayout) view.findViewById(R.id.meter_item_info);
        holder.vip = (ImageView) view.findViewById(R.id.meter_vip);
        holder.meterName = (TextView) view.findViewById(R.id.meter_name);
        holder.meterId = (TextView) view.findViewById(R.id.meter_id);
        holder.dataType = (TextView) view.findViewById(R.id.meter_type);
        holder.valueTime = (TextView) view.findViewById(R.id.meter_value_time);
        holder.readTime = (TextView) view.findViewById(R.id.meter_read_time);
        holder.meterValue = (TextView) view.findViewById(R.id.meter_value);
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
        final Meter meter = new Meter();
        meter.restore(cursor);
        if (meter.mId == 0) {//无此条数据
            return;
        }
        if (meter.isImportant == 0) {
            holder.vip.setVisibility(View.VISIBLE);
        } else {
            holder.vip.setVisibility(View.GONE);
        }
        holder.meterName.setText("电表名称:" + meter.mMeterName);
        holder.meterId.setText("电表ID:" + String.valueOf(meter.mMeterID));
        String type = meter.mDataType == 1 ? "日冻结" : "实时数据";
        holder.dataType.setText("数据类型:" + type);
        holder.valueTime.setText("数据时标:" + MeterUtilies.getSanShengDate(meter.mValueTime));
        holder.readTime.setText("读取时间:" + MeterUtilies.getSanShengDate(meter.mReadTime));
        holder.meterValue.setText("电表值:" + String.valueOf(meter.mValz));
        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showDetailFragment(meter);
            }
        });
    }
    public static class ViewHolder {
        public RelativeLayout infoLayout;
        public ImageView vip;
        public TextView meterName;
        public TextView meterId;
        public TextView dataType;
        public TextView valueTime;
        public TextView readTime;
        public TextView meterValue;
    }
}

