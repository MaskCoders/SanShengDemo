package com.sansheng.testcenter.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.base.view.ProgressDailog;
import com.sansheng.testcenter.center.CenterActivity;
import com.sansheng.testcenter.demo.view.MeterDataListActivity;
import com.sansheng.testcenter.demo.view.MeterListActivity;
import com.sansheng.testcenter.demo.view.SocketDemo;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.settings.SettingsActivity;
import com.sansheng.testcenter.utils.Utility;
import com.sansheng.testcenter.view.TestBaseActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LaucherAdapter extends BaseAdapter {

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("EquipmentDBThread");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);
    private Activity mContext;
    private String[] textSource;
    private int[] iconSource = new int[]{R.drawable.socket_icon_selector,
            R.drawable.db_operation_icon_selector,
            R.drawable.db_operation_icon_selector,
            R.drawable.create_db_icon_selector,
            R.drawable.db_operation_icon_normal,
            R.drawable.db_operation_icon_normal,
            R.drawable.db_operation_icon_normal};
    private ProgressDailog mProgressDailog;

    public LaucherAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return iconSource.length;
    }

    @Override
    public Object getItem(int pos) {
        return null;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int postion, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.operation_item, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    switch (postion) {
                        case 0:
                            intent.setClass(mContext, SocketDemo.class);
                            mContext.startActivity(intent);
                            break;
                        case 1://电表列表
                            intent.setClass(mContext, MeterListActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 2://电表数据列表
                            intent.setClass(mContext, MeterDataListActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 3:
                            showProgressDialog();
                            InsertDataTask task = new InsertDataTask();
                            task.executeOnExecutor(sThreadPool);
                            break;
                        case 4:
                            intent.setClass(mContext, TestBaseActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 5://设置
                            intent.setClass(mContext, SettingsActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 6://主站
                            intent.setClass(mContext, CenterActivity.class);
                            mContext.startActivity(intent);
                            break;
                        default:
                            break;
                    }

                }
            });
            viewHolder.imageView = (ImageView) view.findViewById(R.id.chat_bottom_option_image);
            viewHolder.textView = (TextView) view.findViewById(R.id.chat_bottom_option_text);
            convertView = view;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(iconSource[postion]);
        viewHolder.textView.setText(getAttSource()[postion]);
        return convertView;
    }

    public interface OptionCallback {
        public void onOptionSelected(int postion);
    }

    private String[] getAttSource() {
        if (textSource == null || textSource.length == 0) {
            textSource = mContext.getResources().getStringArray(R.array.launcher_icon_array);
        }
        return textSource;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    private class InsertDataTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            for (int i = 1; i < 51; i++) {
                Meter meter = new Meter(true, i);
                meter.save(mContext);
                MeterData meterData = new MeterData(true, i);
                meterData.save(mContext);
                MeterData meterData1 = new MeterData(true, i);
                meterData1.save(mContext);
            }
            hideProgressDialog();
            return null;
        }
    }

    private void showProgressDialog() {
        mProgressDailog = new ProgressDailog(mContext);
        mProgressDailog.setCanceledOnTouchOutside(false);
        mProgressDailog.show();
        mProgressDailog.setActivity(mContext);
        mProgressDailog.setMessage(mContext.getResources().getString(R.string.create_db_info));
    }

    private void hideProgressDialog() {
        if (mProgressDailog != null && mProgressDailog.isShowing()) {
            mProgressDailog.dismiss();
            mProgressDailog = null;
        }
        Utility.showToast(mContext, mContext.getResources().getString(R.string.has_create_db));
    }
}