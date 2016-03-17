package com.sansheng.testcenter.collecttest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.equipmentmanager.MeterManagerActivity;
import com.sansheng.testcenter.module.Meter;

import java.util.ArrayList;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterCompareListAdapter extends SimpleCursorAdapter {
    private Activity mActivity;
    private ArrayList<Meter> mSelectedMeters = new ArrayList<Meter>();

    public MeterCompareListAdapter(MeterManagerActivity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Meter.CONTENT_PROJECTION,
                Meter.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
        this.mSelectedMeters.clear();
    }

    public MeterCompareListAdapter(Activity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Meter.CONTENT_PROJECTION,
                Meter.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
        this.mSelectedMeters.clear();
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.meter_compare_item_layout, null);
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
        holder.infoLayout = (LinearLayout) view.findViewById(R.id.meter_item);
        holder.meterType = (ImageView) view.findViewById(R.id.meter_type);
        holder.meterNum = (TextView) view.findViewById(R.id.meter_num);
        holder.meterAddress = (TextView) view.findViewById(R.id.meter_address);
//        holder.meterCheckBox = (CheckBox) view.findViewById(R.id.meter_checkbox);
        holder.meterIp = (TextView) view.findViewById(R.id.meter_ip);
        holder.meterNote = (TextView) view.findViewById(R.id.meter_note);
//        holder.readTime = (TextView) view.findViewById(R.id.meter_read_time);
//        holder.meterValue = (TextView) view.findViewById(R.id.meter_value);
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
        final Meter meter = new Meter();
        meter.restore(cursor);
        if (meter.mId == 0) {//无此条数据
            return;
        }
//        if (meter.isImportant == 0) {
//            holder.vip.setVisibility(View.VISIBLE);
//        } else {
//            holder.vip.setVisibility(View.GONE);
//        }
//        holder.meterName.setText(mActivity.getResources().getString(R.string.db_name) + meter.mMeterName);
        holder.meterType.setImageResource(meter.mDa == 0 ? R.drawable.single_meter : R.drawable.three_meter);//单相 v 三相
        holder.meterNum.setText(String.valueOf(meter.mDa));
        holder.meterAddress.setText(meter.mUserAddress);
//        String type = meter.mDataType == 1 ? mActivity.getResources().getString(R.string.db_rdj) :
//                mActivity.getResources().getString(R.string.db_realdata);
        holder.meterIp.setText(meter.mMeterAddress);
        holder.meterNote.setText(meter.mNote);
//        holder.readTime.setText(mActivity.getResources().getString(R.string.db_time) + MeterUtilies.getSanShengDate(meter.mReadTime));
//        holder.meterValue.setText(mActivity.getResources().getString(R.string.db_value) + String.valueOf(meter.mValz));
//        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MeterManagerActivity)mActivity).showDetailFragment(meter);
//            }
//        });
//        holder.meterCheckBox.setChecked(mSelectedMeters.contains(meter));
//        holder.meterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //do something
////                Log.e("ssg", "isChecked = " + isChecked);
//                if (isChecked) {
//                    mSelectedMeters.add(meter);
//                } else {
//                    mSelectedMeters.remove(meter);
//                }
////                Log.e("ssg", "mSelectCollects size = " + mSelectCollects.size());
//            }
//        });
    }

    public static class ViewHolder {
        public LinearLayout infoLayout;
        public ImageView meterType;
        public TextView meterNum;
        public TextView meterAddress;
//        public CheckBox meterCheckBox;
        public TextView meterIp;
        public TextView meterNote;
//        public TextView readTime;
//        public TextView meterValue;
    }

    public ArrayList<Meter> getSelectedMeters() {
        return mSelectedMeters;
    }
}

