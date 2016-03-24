package com.sansheng.testcenter.equipmentmanager;

import android.content.Context;
import android.widget.SimpleAdapter;
import hstt.data.DataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshaogang on 16/3/23.
 */
public class CenterDataAdapter extends SimpleAdapter{

    private ArrayList<DataItem> dataList = new ArrayList<DataItem>();
    public CenterDataAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }
    public void addItemsValues(DataItem dataItem) {
        dataList.add(dataItem);
        notifyDataSetChanged();
    }
}
