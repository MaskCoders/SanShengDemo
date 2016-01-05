package com.sansheng.testcenter.base;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.demo.view.MeterFragment;
import com.sansheng.testcenter.demo.view.MeterListActivity;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.HashMap;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterListDialogAdapter extends SimpleCursorAdapter {
    private Activity mActivity;
    private HashMap<String, Meter> mSelectedMeters = new HashMap<String, Meter>();

    public MeterListDialogAdapter(MeterListActivity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Meter.CONTENT_PROJECTION,
                Meter.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
        this.mSelectedMeters.clear();
    }

    public MeterListDialogAdapter(Activity context, Cursor cursor) {
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
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
        final Meter meter = new Meter();
        meter.restore(cursor);
        if (meter.mId == 0) {//无此条数据
            return;
        }
        holder.meterType.setImageResource(meter.mDa == 0 ? R.drawable.single_meter : R.drawable.three_meter);//单项 v 三项
        holder.meterId.setText(String.valueOf(meter.mMeterNum));
        holder.meterName.setText(String.valueOf(meter.mMeterName));
//        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDetailFragment(meter);
//            }
//        });
        holder.meterCheckBox.setChecked(mSelectedMeters.containsKey(meter.mMeterAddress));
        holder.meterCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "onClick position = " + meter.mMeterAddress);
                if (!mSelectedMeters.containsKey(meter.mMeterAddress)) {
                    mSelectedMeters.put(meter.mMeterAddress, meter);
                } else {
                    mSelectedMeters.remove(meter.mMeterAddress);
                }
            }
        });
    }

    public static class ViewHolder {
        public LinearLayout infoLayout;
        public ImageView meterType;
        public TextView meterName;
        public TextView meterId;
        public CheckBox meterCheckBox;
    }

    private void showDetailFragment(Meter meter) {
        MeterFragment fragment = new MeterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, meter);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(mActivity.getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(meter.mId));
    }

    public HashMap<String, Meter> getSelectedMeters() {
        return mSelectedMeters;
    }


    public void addAllMeters() {

    }
}

