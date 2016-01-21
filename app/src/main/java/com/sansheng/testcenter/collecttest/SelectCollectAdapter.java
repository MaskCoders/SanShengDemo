package com.sansheng.testcenter.collecttest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.Collect;

import java.util.ArrayList;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class SelectCollectAdapter extends SimpleCursorAdapter {
    private SelectCollectActivity mActivity;
//    private HashMap<String, Collect> mSelectedCollects = new HashMap<String, Collect>();
    private ArrayList<Collect> mSelectedCollects = new ArrayList<Collect>();

    public SelectCollectAdapter(SelectCollectActivity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Collect.CONTENT_PROJECTION,
                Collect.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
        this.mSelectedCollects.clear();
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.collect_select_item_layout, null);
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
        holder.itemLayout = (LinearLayout) view.findViewById(R.id.collect_item);
        holder.collectName = (TextView) view.findViewById(R.id.collect_name);
        holder.collectAddress = (TextView) view.findViewById(R.id.collect_address);
        holder.collectChannel = (TextView) view.findViewById(R.id.collect_channel);
        holder.connectState = (TextView) view.findViewById(R.id.connect_state);
        holder.mCheckBox = (CheckBox) view.findViewById(R.id.collect_checkbox);
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
        final Collect collect = new Collect();
        collect.restore(cursor);
        if (collect.mId == 0) {//无此条数据
            return;
        }
        holder.collectName.setText(String.valueOf(collect.mCollectName));
        holder.collectAddress.setText(collect.mCommonAddress);
        holder.collectChannel.setText(CollectTestUtils.channelItems(mActivity)[collect.mChannelType]);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show detail
                mActivity.showDetailFragment(collect);
            }
        });
        holder.mCheckBox.setChecked(mSelectedCollects.contains(collect));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do something
//                Log.e("ssg", "isChecked = " + isChecked);
                if (isChecked) {
                    mSelectedCollects.add(collect);
                } else {
                    mSelectedCollects.remove(collect);
                }
//                Log.e("ssg", "mSelectCollects size = " + mSelectCollects.size());
            }
        });
    }

    public ArrayList<Collect> getSelectedCollects(){
        return mSelectedCollects;
    }

    public static class ViewHolder {
        public LinearLayout itemLayout;
        public TextView collectName;
        public TextView collectAddress;
        public TextView collectChannel;
        public TextView connectState;
        public CheckBox mCheckBox;
    }
}

