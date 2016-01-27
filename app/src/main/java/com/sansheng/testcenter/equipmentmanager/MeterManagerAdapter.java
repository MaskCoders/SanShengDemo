package com.sansheng.testcenter.equipmentmanager;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.module.Meter;

import java.util.HashMap;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterManagerAdapter extends SimpleCursorAdapter {
    private Activity mActivity;
    private HashMap<String, Meter> mSelectedMeters = new HashMap<String, Meter>();

    public MeterManagerAdapter(MeterManagerActivity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Meter.CONTENT_PROJECTION,
                Meter.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
        this.mSelectedMeters.clear();
    }

    public MeterManagerAdapter(Activity context, Cursor cursor) {
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
        holder.infoLayout = (LinearLayout) view.findViewById(R.id.meter_item);
        holder.meterType = (ImageView) view.findViewById(R.id.meter_type);
        holder.meterName = (TextView) view.findViewById(R.id.meter_name);
        holder.meterId = (TextView) view.findViewById(R.id.meter_id);
        holder.meterCheckBox = (CheckBox) view.findViewById(R.id.meter_checkbox);
//        holder.dataType = (TextView) view.findViewById(R.id.meter_type);
//        holder.valueTime = (TextView) view.findViewById(R.id.meter_value_time);
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
        holder.meterId.setText(String.valueOf(meter.mMeterNum));
        holder.meterName.setText(String.valueOf(meter.mMeterName));
//        String type = meter.mDataType == 1 ? mActivity.getResources().getString(R.string.db_rdj) :
//                mActivity.getResources().getString(R.string.db_realdata);
//        holder.dataType.setText(mActivity.getResources().getString(R.string.db_type) + type);
//        holder.valueTime.setText(mActivity.getResources().getString(R.string.db_tip) + MeterUtilies.getSanShengDate(meter.mValueTime));
//        holder.readTime.setText(mActivity.getResources().getString(R.string.db_time) + MeterUtilies.getSanShengDate(meter.mReadTime));
//        holder.meterValue.setText(mActivity.getResources().getString(R.string.db_value) + String.valueOf(meter.mValz));
        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailFragment(meter);
            }
        });
        holder.meterCheckBox.setChecked(mSelectedMeters.containsKey(meter.mMeterAddress));
        holder.meterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do something
//                Log.e("ssg", "isChecked = " + isChecked);
                if (isChecked) {
                    mSelectedMeters.put(meter.mMeterAddress, meter);
                } else {
                    mSelectedMeters.remove(meter.mMeterAddress);
                }
//                Log.e("ssg", "mSelectCollects size = " + mSelectCollects.size());
            }
        });
    }

    public static class ViewHolder {
        public LinearLayout infoLayout;
        public ImageView meterType;
        public TextView meterName;
        public TextView meterId;
        public CheckBox meterCheckBox;
//        public TextView dataType;
//        public TextView valueTime;
//        public TextView readTime;
//        public TextView meterValue;
    }

    private void showDetailFragment(Meter meter) {
        MeterDetailFragment fragment = new MeterDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, meter);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(mActivity.getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(meter.mId));
    }

    public HashMap<String, Meter> getSelectedMeters() {
        return mSelectedMeters;
    }
}

