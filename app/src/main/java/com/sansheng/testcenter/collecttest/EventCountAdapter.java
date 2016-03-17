package com.sansheng.testcenter.collecttest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.Event;

/**
 * Created by sunshaogang on 1/20/16.
 * 时间管理列表的adapter
 */
public class EventCountAdapter extends SimpleCursorAdapter {
    private Context mContext;

    public EventCountAdapter(Context context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, Event.CONTENT_PROJECTION,
                Event.ID_INDEX_PROJECTION, 0);
        this.mContext = context;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.event_count_item_layout, null);
            viewHolder = new ViewHolder();
            initViewHolder(viewHolder, convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.count = cursor.getCount();
        fillDataToViewHolder(cursor, viewHolder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //do nothing
    }

    ViewHolder initViewHolder(final ViewHolder holder, View view) {
        holder.itemLayout = (LinearLayout) view.findViewById(R.id.event_item);
        holder.eventType = (TextView) view.findViewById(R.id.event_type);
        holder.eventTime = (TextView) view.findViewById(R.id.event_time);
        holder.eventCount = (TextView) view.findViewById(R.id.event_count);
        holder.eventExplain = (TextView) view.findViewById(R.id.event_explain);
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
        final Event event = new Event();
        event.restore(cursor);
        if (event.mId == 0) {//无此条数据
            return;
        }
        holder.eventType.setText(String.valueOf(event.mType));
        holder.eventTime.setText(event.mHappenTime);
        holder.eventCount.setText(String.valueOf(event.mCount));
        holder.eventExplain.setText(event.mNote);
    }

    public static class ViewHolder {
        public LinearLayout itemLayout;
        public TextView eventType;
        public TextView eventTime;
        public TextView eventCount;
        public TextView eventExplain;
        int count;
    }

}
