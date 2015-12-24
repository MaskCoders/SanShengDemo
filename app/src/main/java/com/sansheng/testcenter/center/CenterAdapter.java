package com.sansheng.testcenter.center;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sansheng.testcenter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class CenterAdapter extends BaseExpandableListAdapter {

    private Activity mActivity;
    private List<RootProt> mGroupArray = new ArrayList<RootProt>();

    public CenterAdapter(Activity mActivity, List<RootProt> mGroupArray) {
        this.mActivity = mActivity;
        this.mGroupArray = mGroupArray;
    }

    @Override
    public int getGroupCount() {
        return mGroupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroupArray.get(groupPosition).getChildCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroupArray.get(groupPosition).mChildArray.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.center_group_layout, parent, false);
        String title = mGroupArray.get(groupPosition).n;
        TextView textView = (TextView) ll.findViewById(R.id.center_group_title);
        textView.setText(title);
        return ll;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.center_child_layout, parent, false);
        String title = mGroupArray.get(groupPosition).mChildArray.get(childPosition).n;
        TextView textView = (TextView) ll.findViewById(R.id.center_child_title);
        textView.setText(title);
        return ll;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

