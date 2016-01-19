package com.sansheng.testcenter.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sansheng.testcenter.metertest.MeterTestActivity;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.CollectSelectDialog;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.base.MeterSelectDialog;
import com.sansheng.testcenter.base.view.ProgressDailog;
import com.sansheng.testcenter.center.CenterActivity;
import com.sansheng.testcenter.datamanager.MeterDataListActivity;
import com.sansheng.testcenter.location.LocationInfoActivity;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.settings.SettingsActivity;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.utils.Utility;
import com.sansheng.testcenter.view.TestBaseActivity;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LauncherAdapter extends BaseAdapter implements CollectSelectDialog.CollectCallback, MeterSelectDialog.MeterCallback {

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("EquipmentDBThread");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);
    private Activity mContext;
    private String[] textSource;
    private int[] iconSource = new int[]{R.drawable.dxb,
            R.drawable.sxb,
            R.drawable.jzq,
            R.drawable.bdmk,
            R.drawable.ycmkjc,
            R.drawable.tqsb,
            R.drawable.gps,
            R.drawable.gw3761,
            R.drawable.dagl,
            R.drawable.sjbcjsc,
            R.drawable.tqsb,
            R.drawable.gyjx,
            R.drawable.xcsd,
            R.drawable.settings,
    };
    private ProgressDailog mProgressDailog;

    public LauncherAdapter(Activity mContext) {
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
                    Bundle bundle = new Bundle();
                    switch (postion) {
                        case 0://单项表检测
                            bundle.putInt(MeterUtilies.PAMAR_METER_TYPE, MeterUtilies.METER_TEST_TYPE_SINGLE);
                            intent.putExtras(bundle);
                            intent.setClass(mContext, MeterTestActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 1://三相表检测
                            bundle.putInt(MeterUtilies.PAMAR_METER_TYPE, MeterUtilies.METER_TEST_TYPE_TRIPLE);
                            intent.putExtras(bundle);
                            intent.setClass(mContext, MeterTestActivity.class);
                            mContext.startActivity(intent);
//                            MeterSelectDialog meterDialog = new MeterSelectDialog(LaucherAdapter.this);
//                            meterDialog.show(mContext.getFragmentManager(), "select_meter");
//                            intent.setClass(mContext, MeterListActivity.class);
//                            mContext.startActivity(intent);
                            break;
                        case 2://集中器检测
                            CollectSelectDialog collectDialog = new CollectSelectDialog(LauncherAdapter.this);
                            collectDialog.show(mContext.getFragmentManager(), "select_collects");
                            break;
                        case 3://本地模块检测
                            intent.setClass(mContext, TestBaseActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 4://远程模块检测
                            intent.setClass(mContext, TestBaseActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 5://台区识别
                            break;
                        case 6://GPS定位
                            intent.setClass(mContext, LocationInfoActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 7://国网376.1主站
                            intent.setClass(mContext, CenterActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 8://档案管理
                            intent.setClass(mContext, MeterDataListActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 9://数据补抄
                            showProgressDialog();
                            InsertDataTask task = new InsertDataTask();
                            task.executeOnExecutor(sThreadPool);
                            break;
                        case 10://组网拓扑
                            intent.setClass(mContext, TestBaseActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 11://规约解析
                            intent.setClass(mContext, TestBaseActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 12://现场售电
                            MeterSelectDialog meterDialog = new MeterSelectDialog(LauncherAdapter.this);
                            meterDialog.show(mContext.getFragmentManager(), "select_meter");
                            break;
                        case 13://系统设置
                            intent.setClass(mContext, SettingsActivity.class);
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

    @Override
    public void onCollectNegativeClick() {
        //do nothing
    }

    @Override
    public void onCollectPositiveClick(HashMap<String, Collect> collects) {
        //get collect list
        Log.e("ssg", "selected collects size = " + collects.size());
    }

    @Override
    public void onMeterPositiveClick(HashMap<String, Meter> meters) {
        Log.e("ssg", "selected meters size = " + meters.size());
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
            for (int i = 1; i < 11; i++) {
                Collect collect = new Collect(true, i);
                collect.save(mContext);
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
