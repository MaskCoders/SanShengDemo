package com.sansheng.testcenter.base.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.location.Poi;
import com.sansheng.testcenter.R;

import java.util.List;

/**
 * Created by sunshaogang on 2016/1/4.
 */
public class PoiSelectDialog extends DialogFragment {

    private View mRootView;
    private ListView mListView;
    private AnswerDialog mDialog;
    private PoiListAdapter mAdapter;
    private PoiSelectCallback mCallback;
    private List<Poi> mPoiList;

    public PoiSelectDialog() {
    }

    public PoiSelectDialog(PoiSelectCallback callback, List<Poi> list) {
        this.mCallback = callback;
        this.mPoiList = list;
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
        mAdapter = new PoiListAdapter();
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

    class PoiListAdapter extends BaseAdapter {


        public PoiListAdapter() {
        }

        @Override
        public int getCount() {
            if (mPoiList == null) {
                return 0;
            } else {
                return mPoiList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return mPoiList.get(position);
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
            if (mPoiList == null || mPoiList.size() == 0) {
                return;
            }
            holder.describeView.setText(mPoiList.get(position).getName());
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onPoiSelectClick(position);
                    mDialog.dismiss();
                }
            });
        }

        public class ViewHolder {
            public LinearLayout itemLayout;
            public TextView describeView;
        }

    }

    public interface PoiSelectCallback {
        void onPoiSelectClick(int position);
    }

}
