package com.sansheng.testcenter.metertest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunshaogang on 2016/1/5.
 * TODO:优化选中的测试项目的显示逻辑
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
//        if (convertView != null) {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        if (viewHolder == null) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.meter_test_center_item_layout, null);
        viewHolder = new ViewHolder();
        initViewHolder(viewHolder, convertView);
//            convertView.setTag(viewHolder);
//        }
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
        try {
            /**
             <item>"当前表码"</item>
             <item>"三相电压"</item>
             <item>"电表时间"</item>
             <item>"冻结时间"</item>
             <item>"剩余金额"</item>
             <item>"失压情况"</item>
             <item>"开盖次数"</item>
             <item>"跳闸次数"</item>
             */
            String values = "";
            String secType = bean.getSecType();
            System.out.println("secType is " + secType + "   by hua");
            switch (position) {
                case 0://当前表码
                    if (!secType.trim().equalsIgnoreCase("0001ff00")) break;
                    byte[] decodeData = bean.getUserData();
                    int len = (decodeData.length - bean.type.getLen()) / 4;
                    if (len <= 0) return;
                    int j = 0;
                    for (int i = bean.type.getLen(); i < decodeData.length; i = i + 4) {
                        double x = ProtocolUtils.getbcdDec4bytes2(decodeData[i], decodeData[i + 1]);
//                        if(!values.equals(""))
                        values = values + " ," + x;
                    }
                    break;
                case 1://三相电压
                    if (!secType.trim().equalsIgnoreCase("0201ff00")) break;
                    decodeData = bean.getUserData();
                    len = (decodeData.length - bean.type.getLen()) / 2;
                    if (len <= 0) return;
                    j = 0;
                    for (int i = bean.type.getLen(); i < decodeData.length; i = i + 2) {
                        double x = ProtocolUtils.getbcdDec4bytes2(decodeData[i], decodeData[i + 1]);
//                        if(values.length()>1)
                        values = values + " ," + x;
                    }
                    break;
                case 2://电表时间
                    if (!secType.trim().equalsIgnoreCase("04000101")) break;
                    decodeData = bean.getUserData();
                    for (int i = decodeData.length - 1; i > bean.type.getLen(); i--) {
                        System.out.println(ProtocolUtils.byte2hex(decodeData[i]));
//                         if(!values.equals(""))
                        values = values + "-" + ProtocolUtils.byte2hex(decodeData[i]);
                    }
                    break;
                case 3://冻结时间
                    if (!secType.trim().equalsIgnoreCase("05060001")) break;
                    decodeData = bean.getUserData();
                    for (int i = decodeData.length - 1; i >= bean.type.getLen(); i--) {
                        System.out.println(ProtocolUtils.byte2hex(decodeData[i]));
//                         if(!values.equals(""))
                        values = values + ":" + ProtocolUtils.byte2hex(decodeData[i]);
                    }
                    /* 时间需要2贞才可以，需要区别userdata前的数据
                    decodeData = bean.getUserData();
                    for(int i = decodeData.length-1;i >=bean.type.getLen();i--){
                        System.out.println(ProtocolUtils.byte2hex(decodeData[i]));
                         if(!values.equals(""))
                            values = values+":"+ProtocolUtils.byte2hex(decodeData[i]);
                    }
                    */
                    break;
                case 4://剩余金额
                    if (!secType.trim().equalsIgnoreCase("00900200")) break;
                    decodeData = bean.getUserData();
                    for (int i = decodeData.length - 1; i >= bean.type.getLen(); i--) {
                        //// 2015-10-9 0:00:00
                        values = values + ProtocolUtils.byte2hex(decodeData[i]);
                        System.out.println(ProtocolUtils.byte2hex(decodeData[i]));
                    }
                    values = String.valueOf(Double.valueOf(values) / 100);
                    System.out.println(values);
                    break;
                case 5://失压情况
                    if (!secType.trim().equalsIgnoreCase("0001ff00")) break;
                    break;
                case 6://开盖次数
                    if (!secType.trim().equalsIgnoreCase("03300d00")) break;
                    decodeData = bean.getUserData();
                    for (int i = decodeData.length - 1; i >= bean.type.getLen(); i--) {
                        //// 2015-10-9 0:00:00
                        values = values + ProtocolUtils.byte2hex(decodeData[i]);
                        System.out.println(ProtocolUtils.byte2hex(decodeData[i]));
                    }
                    values = String.valueOf(Double.valueOf(values));
                    System.out.println(values);
                    //break;  开盖和跳闸逻辑相同
                case 7://跳闸次数
                    if (!secType.trim().equalsIgnoreCase("1d000001")) break;
                    decodeData = bean.getUserData();
                    for (int i = decodeData.length - 1; i >= bean.type.getLen(); i--) {
                        //// 2015-10-9 0:00:00
                        values = values + ProtocolUtils.byte2hex(decodeData[i]);
                        System.out.println(ProtocolUtils.byte2hex(decodeData[i]));
                    }
                    values = String.valueOf(Double.valueOf(values));
                    System.out.println(values);
                    break;
            }
            if (holder.explainView.getText().toString().trim().equals(""))
                holder.explainView.setText(values);
        } catch (Exception e) {
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

    public void setmSelectedItemsValues(WhmBean bean) {
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

    public String[] getAllItems() {
        return allItems;
    }

}
