package com.sansheng.testcenter.collecttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sansheng.testcenter.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 2016/1/5.
 */
public class CollectTestItemAdapter extends BaseAdapter {
    private HashMap<Integer, String> mSelectedItems = new HashMap<Integer, String>();
    private ArrayList<Integer> testItemList = new ArrayList<Integer>();
    private String[] allItems;
    private Context mContext;

    public CollectTestItemAdapter(Context context) {
        this.mContext = context;
        allItems = mContext.getResources().getStringArray(R.array.collect_test_items);

    }

    public ArrayList<Integer> getTestItems() {
        if (testItemList == null || testItemList.size() == 0) {
            for (int index : mSelectedItems.keySet()) {
                testItemList.add(index);
            }
        }
        return testItemList;
    }

    @Override
    public int getCount() {
        if (getTestItems() == null) {
            return 0;
        } else {
            return testItemList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return getTestItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.collect_test_item_layout, null);
            viewHolder = new ViewHolder();
            initViewHolder(viewHolder, convertView);
            convertView.setTag(viewHolder);
        }
        fillDataToViewHolder(position, viewHolder);
        return convertView;
    }

    ViewHolder initViewHolder(final ViewHolder holder, View view) {
        holder.itemLayout = (LinearLayout) view.findViewById(R.id.collect_test_center_item);
        holder.describeView = (TextView) view.findViewById(R.id.collect_test_item);
        holder.resultView = (TextView) view.findViewById(R.id.collect_test_result);
        holder.explainView = (TextView) view.findViewById(R.id.collect_test_explain);
        return holder;
    }

    private void fillDataToViewHolder(final int position, final ViewHolder holder) {
        if (getTestItems() == null || testItemList.size() == 0) {
            return;
        }
        holder.describeView.setText(allItems[testItemList.get(position)]);
    }

    public void setSelectedItemts(HashMap<Integer, String> itemMap) {
        if (itemMap == null || itemMap.size() == 0) {
            return;
        }
        mSelectedItems = itemMap;
        testItemList.clear();
        for (int index : mSelectedItems.keySet()) {//TODO:规则需要确认
            switch (index) {
                case 0:
                    testItemList.add(0);
                    testItemList.add(1);
                    break;
                case 1:
                    testItemList.add(2);
                    testItemList.add(3);
                    testItemList.add(4);
                    break;
                case 2:
                    testItemList.add(5);
                    testItemList.add(6);
                    break;
                case 3:
                    testItemList.add(7);
                    break;
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder {
        public LinearLayout itemLayout;
        public TextView describeView;
        public TextView resultView;
        public TextView explainView;
    }

    public String[] getAllItems() {
        return allItems;
    }

}
