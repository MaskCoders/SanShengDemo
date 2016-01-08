package com.sansheng.testcenter.location;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.module.LocationInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class LocationInfoAdapter extends SimpleCursorAdapter {
    private LocationInfoActivity mActivity;
    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("LocationInfoAdapter");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);
    private int mThumbnailWidth;
    private int mThumbnailHeight;

    public LocationInfoAdapter(LocationInfoActivity context, Cursor cursor) {
        super(context, android.R.layout.simple_list_item_1, cursor, LocationInfo.CONTENT_PROJECTION,
                LocationInfo.ID_INDEX_PROJECTION, 0);
        this.mActivity = context;
        mThumbnailWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.thumbnail_width);
        mThumbnailHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.thumbnail_height);

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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.location_info_item_layout, null);
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
        holder.itemLayout = (LinearLayout) view.findViewById(R.id.location_item);
        holder.theme = (ImageView) view.findViewById(R.id.theme_image);
        holder.address = (TextView) view.findViewById(R.id.location_address);
        holder.poi = (TextView) view.findViewById(R.id.location_poi);
        holder.updateTime = (TextView) view.findViewById(R.id.location_update_time);
        return holder;
    }

    private void fillDataToViewHolder(final Cursor cursor, final ViewHolder holder) {
        final LocationInfo location = new LocationInfo();
        location.restore(cursor);
        Log.e("ssg", "id = " + location.mId);
        if (location.mId == 0) {//无此条数据
            return;
        }

        holder.theme.setScaleType(ImageView.ScaleType.FIT_XY);
        if (location.mUriList == null || location.mUriList.size() == 0) {
            holder.theme.setImageResource(R.drawable.circle_message_photo_unload);
        } else {
            Log.e("ssg", "photo path = " + location.mUriList.get(0));
            ThumbnailUtility.loadBitmap(sThreadPool, location.mUriList.get(0) != null ? location.mUriList.get(0) : null,
                    holder.theme, mThumbnailWidth, mThumbnailHeight, R.drawable.circle_message_photo_unload, mActivity);
        }
        holder.address.setText(location.mAddress);
        holder.poi.setText(location.mPoi);
        holder.updateTime.setText(location.mUpdateTime);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mActivity.showDetailFragment(location);
            }
        });
    }

    public static class ViewHolder {
        public LinearLayout itemLayout;
        public ImageView theme;
        public TextView address;
        public TextView poi;
        public TextView updateTime;
    }
}

