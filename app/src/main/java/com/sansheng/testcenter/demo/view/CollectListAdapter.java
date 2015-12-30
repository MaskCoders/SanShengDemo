package com.sansheng.testcenter.demo.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.Collect;

import java.util.HashMap;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class CollectListAdapter extends SimpleCursorAdapter {
    private Activity mActivity;
    private HashMap<String, Collect> mSelectedCollects = new HashMap<String, Collect>();

    public CollectListAdapter(CollectListActivity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Collect.CONTENT_PROJECTION,
                Collect.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
        this.mSelectedCollects.clear();
    }

    public CollectListAdapter(Activity context, Cursor cursor) {
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.collect_item_layout, null);
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
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show detail
//                mActivity.showDetailFragment(collect);
            }
        });
        holder.mCheckBox.setChecked(mSelectedCollects.containsKey(collect.mCommonAddress));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do something
//                Log.e("ssg", "isChecked = " + isChecked);
                if (isChecked) {
                    mSelectedCollects.put(collect.mCommonAddress, collect);
                } else {
                    mSelectedCollects.remove(collect.mCommonAddress);
                }
//                Log.e("ssg", "mSelectCollects size = " + mSelectCollects.size());
            }
        });
    }

    public HashMap<String, Collect> getSelectedCollects(){
        return mSelectedCollects;
    }

    public static class ViewHolder {
        public LinearLayout itemLayout;
        public TextView collectName;
        public TextView collectAddress;
        public CheckBox mCheckBox;
    }
}

