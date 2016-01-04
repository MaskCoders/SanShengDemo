package com.sansheng.testcenter.base.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 2016/1/4.
 */
public class ConnectTypeDialog extends DialogFragment {
    private View mRootView;
    private ListView mListView;
    private AnswerDialog mDialog;
    private ConnectTypeCallback mCallback;
    private MeterTestAdapter mAdapter;

    public ConnectTypeDialog() {
    }

    public ConnectTypeDialog(ConnectTypeCallback callback) {
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
        mDialog.setTitleText("选择测试项目");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.connect_type_dialog_layout, null);
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

    public interface ConnectTypeCallback {
        void onItemClick(int position);
    }

    class MeterTestAdapter extends BaseAdapter {

        private String[] testItems;

        public String[] getTestItems(){
            if (testItems == null || testItems.length == 0) {
                testItems = getResources().getStringArray(R.array.select_connect_type);
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
                    mDialog.dismiss();
                    mCallback.onItemClick(position);
                }
            });
        }

        public class ViewHolder {
            public LinearLayout itemLayout;
            public TextView describeView;
        }
    }
}
