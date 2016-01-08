package com.sansheng.testcenter.base.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.MeterSelectDialog;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.utils.Utility;

import java.util.HashMap;

/**
 * Created by sunshaogang on 2016/1/4.
 */
public class WaySelectMeterDialog extends DialogFragment implements MeterSelectDialog.MeterCallback{

    private View mRootView;
    private ListView mListView;
    private AnswerDialog mDialog;
    private MeterTestAdapter mAdapter;
    private WaySelectMeterCallback mCallback;

    public WaySelectMeterDialog() {
    }

    public WaySelectMeterDialog(WaySelectMeterCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AnswerDialog(getActivity());
        mDialog.show();
        mDialog.setTitleText("选择添加电表方式");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.way_select_meter_dialog_layout, null);
        mListView = (ListView) mRootView.findViewById(R.id.listview);
        mAdapter = new MeterTestAdapter();
        mListView.setAdapter(mAdapter);
        mDialog.setCustomView(mRootView);
        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setPositiveButtonDismiss();
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onMeterPositiveClick(HashMap<String, Meter> meters) {
        Log.e("ssg", "WaySelectMeterDialog selected meters size = " + meters.size());
        mCallback.onSelectMeterPositiveClick(meters);
    }

    class MeterTestAdapter extends BaseAdapter {

        HashMap<String, Meter> selectMeters = new HashMap<String, Meter>();
        private String[] testItems;

        public String[] getTestItems(){
            if (testItems == null || testItems.length == 0) {
                testItems = getResources().getStringArray(R.array.way_select_meter);
            }
            return testItems;
        }

        @Override
        public int getCount() {
            if (getTestItems() == null) {
                return 0;
            } else {
                return testItems.length;
            }
        }

        @Override
        public Object getItem(int position) {
            return getTestItems()[position];
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.way_select_meter_item_layout, null);
                viewHolder = new ViewHolder();
                initViewHolder(viewHolder, convertView);
                convertView.setTag(viewHolder);
            }
            fillDataToViewHolder(position, viewHolder);
            return convertView;
        }

        ViewHolder initViewHolder(final ViewHolder holder, View view) {
            holder.itemLayout = (LinearLayout) view.findViewById(R.id.meter_test_item);
            holder.describeView = (TextView) view.findViewById(R.id.meter_test_item_describe);
            return holder;
        }

        private void fillDataToViewHolder(final int position, final ViewHolder holder) {
            if (getTestItems() == null || testItems.length == 0) {
                return;
            }
            holder.describeView.setText(testItems[position]);
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0://手动输入
                            composeMeterAddress();
                            break;
                        case 1://选择已有电表
                            MeterSelectDialog meterDialog = new MeterSelectDialog(WaySelectMeterDialog.this);
                            meterDialog.show(getActivity().getFragmentManager(), "select_meter");
                            break;
                        case 2://读地址
                            readMeterAddress();
                            break;
                        default:
                            break;
                    }
                    mDialog.dismiss();
                }
            });
        }

        public HashMap<String, Meter> getSelectedCollects(){
            return selectMeters;
        }

        public class ViewHolder {
            public LinearLayout itemLayout;
            public TextView describeView;
        }

        private void readMeterAddress(){
            Log.e("ssg", "从已连接的设备中读取电表地址");
        }

        private void composeMeterAddress() {
            final BaseDialog dialog = new BaseDialog(getActivity(), R.style.CustomDialog);
            dialog.setTitleText(R.string.edit_meter_adders);
            dialog.setOnPositiveBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.getEditText().getText().toString().length() != 10) {//TODO:电表地址的长度可能不对
                        //未修改，无需更新
                        Utility.showToast(getActivity(), "电表地址的长度可能不对");
                    } else {//修改
                        Meter meter = new Meter();
                        meter.mMeterAddress = dialog.getEditText().getText().toString();
                        selectMeters.put(meter.mMeterAddress, meter);
                        Log.e("ssg", "address = " + meter.mMeterAddress);
                        mCallback.onSelectMeterPositiveClick(selectMeters);
                        dialog.dismiss();
                    }
                }
            });
            dialog.setOnNegativeBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public interface WaySelectMeterCallback {
        void onSelectMeterPositiveClick(HashMap<String, Meter> meters);
    }

}
