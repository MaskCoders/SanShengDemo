package com.sansheng.testcenter.collecttest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;

import java.util.HashMap;

/**
 * Created by sunshaogang on 1/4/16.
 */
public class CollectTestItemsDialog extends DialogFragment {
    private View mRootView;
    private ListView mListView;
    private AnswerDialog mDialog;
    private CollectTestCallback callback;
    private CollectTestAdapter mAdapter;

    public CollectTestItemsDialog() {
    }

    public CollectTestItemsDialog(CollectTestCallback callback) {
        this.callback = callback;
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
        mRootView = inflater.inflate(R.layout.collect_test_item_dialog_layout, null);
        mListView = (ListView) mRootView.findViewById(R.id.list_view);
        mAdapter = new CollectTestAdapter();
        mListView.setAdapter(mAdapter);
        mDialog.setCustomView(mRootView);
        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCollectItemNegativeClick();
                mDialog.dismiss();
            }
        });
        mDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                callback.onCollectItemPositiveClick(mAdapter.getSelectedCollects());
            }
        });
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface CollectTestCallback {
        void onCollectItemNegativeClick();
        void onCollectItemPositiveClick(HashMap<Integer, String> collects);
    }

    class CollectTestAdapter extends BaseAdapter {

        private String[] testItems;
        private HashMap<Integer, String> mSelectedItems = new HashMap<Integer, String>();

        public String[] getTestItems(){
            if (testItems == null || testItems.length == 0) {
                testItems = getResources().getStringArray(R.array.collect_test_select_items);
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.collect_test_dialog_item_layout, null);
                viewHolder = new ViewHolder();
                initViewHolder(viewHolder, convertView);
                convertView.setTag(viewHolder);
            }
            fillDataToViewHolder(position, viewHolder);
            return convertView;
        }

        ViewHolder initViewHolder(final ViewHolder holder, View view) {
            holder.itemLayout = (LinearLayout) view.findViewById(R.id.collect_test_item);
            holder.describeView = (TextView) view.findViewById(R.id.collect_test_item_describe);
            holder.mCheckBox = (CheckBox) view.findViewById(R.id.collect_test_checkbox);
            return holder;
        }

        private void fillDataToViewHolder(final int position, final ViewHolder holder) {
            if (getTestItems() == null || testItems.length == 0) {
                return;
            }
//            Log.e("ssg", "position = " + position);
            holder.describeView.setText(testItems[position]);
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mCheckBox.setChecked(!holder.mCheckBox.isChecked());
                    Log.e("ssg", "onClick position = " + position);
                    if (!mSelectedItems.containsKey(position)) {
                        mSelectedItems.put(position, testItems[position]);
                    } else {
                        mSelectedItems.remove(position);
                    }
                }
            });
            holder.mCheckBox.setChecked(mSelectedItems.containsKey(position));
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ssg", "onClick position = " + position);
                    if (!mSelectedItems.containsKey(position)) {
                        mSelectedItems.put(position, testItems[position]);
                    } else {
                        mSelectedItems.remove(position);
                    }
                }
            });
        }

        public HashMap<Integer, String> getSelectedCollects(){
            return mSelectedItems;
        }

        public class ViewHolder {
            public LinearLayout itemLayout;
            public TextView describeView;
            public CheckBox mCheckBox;
        }
    }

}
