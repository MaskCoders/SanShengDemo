package com.sansheng.testcenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 2016/1/5.
 */
public class MeterTestCenterListAdapter extends BaseAdapter {
    private HashMap<Integer, String> mSelectedItems = new HashMap<Integer, String>();
    private ArrayList<Integer> testItemList = new ArrayList<Integer>();
    private WhmBean bean;
    private String[] allItems;
    private Context mContext;

    public MeterTestCenterListAdapter(Context context) {
        this.mContext = context;
        allItems = mContext.getResources().getStringArray(R.array.meter_test_items);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.meter_test_center_item_layout, null);
            viewHolder = new ViewHolder();
            initViewHolder(viewHolder, convertView);
            convertView.setTag(viewHolder);
        }
        fillDataToViewHolder(position, viewHolder);
        return convertView;
    }

    ViewHolder initViewHolder(final ViewHolder holder, View view) {
        holder.itemLayout = (LinearLayout) view.findViewById(R.id.meter_test_center_item);
        holder.describeView = (TextView) view.findViewById(R.id.test_item_describe);
        holder.resultView = (TextView) view.findViewById(R.id.test_item_result);
        holder.explainView = (TextView) view.findViewById(R.id.test_item_explain);
        return holder;
    }

    private void fillDataToViewHolder(final int position, final ViewHolder holder) {
        if (getTestItems() == null || testItemList.size() == 0) {
            return;
        }
        holder.describeView.setText(allItems[testItemList.get(position)]);
        try{
            String values = "";
            switch (position){
                case 0:
                    byte[] decodeData = bean.getUserData();
                    int len = (decodeData.length -bean.type.getLen())/4;
                    if(len <=0) return ;
                    int j = 0;
                    for(int i=bean.type.getLen();i<decodeData.length;i=i+4){
                        double x= ProtocolUtils.getbcdDec4bytes2(decodeData[i],decodeData[i+1]);
                        if(values.length()>1)
                            values = values+" ,"+x;
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
            }
            holder.explainView.setText(values);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public HashMap<Integer, String> getSelectedItems() {
        return mSelectedItems;
    }

    public void setSelectedItemts(HashMap<Integer, String> itemMap) {
        if (itemMap == null || itemMap.size() == 0) {
            return;
        }
        mSelectedItems = itemMap;
        testItemList.clear();
        for (int index : mSelectedItems.keySet()) {
            testItemList.add(index);
        }
    }
    public void setmSelectedItemsValues(WhmBean bean){
        this.bean = bean;
        notifyDataSetChanged();
    }
    public class ViewHolder {
        public LinearLayout itemLayout;
        public TextView describeView;
        public TextView resultView;
        public TextView explainView;
    }

    private void readMeterAddress() {
        Log.e("ssg", "从已连接的设备中读取电表地址");
    }

    public String[] getAllItems(){
        return allItems;
    }

}
