package com.example.demo.app;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterListAdapter extends SimpleCursorAdapter {
    private Activity mActivity;
    private Context mContext;
    private ArrayList<String> msgs;

    public MeterListAdapter(Context context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Meter.CONTENT_PROJECTION,
                Meter.ID_INDEX_PROJECTION, 0);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return msgs == null ? 0 : msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = (TextView) mActivity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(msgs.get(position));
        return textView;
    }
}

