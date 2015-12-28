package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.sansheng.testcenter.R;

public class AnswerDialog extends BaseDialog {

    private Context mContext;
    private int mListItemGravity = Gravity.LEFT;

    public AnswerDialog(Context context) {
        super(context);
        mContext = context;
    }

    public AnswerDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public void setTitleText(String title) {
        super.setTitleText(title);
        setEditVisible(false);
    }

    public void setTitleText(CharSequence title) {
        if (title != null) {
            super.setTitleText(title.toString());
        }
        setEditVisible(false);
    }

    public void setTitleText(int title) {
        super.setTitleText(title);
        setEditVisible(false);
    }

    public void setMessageText(String msg) {
        setMessage(msg);
        setEditVisible(false);
    }

    public void setMessageText(CharSequence msg) {
        if (msg != null) {
            setMessage(msg);
        }
        setEditVisible(false);
    }

    public void setMessageText(int msg) {
        setMessage(msg);
        setEditVisible(false);
    }

    public void setListItemGravity(int mListItemGravity) {
        this.mListItemGravity = mListItemGravity;
    }

    public void setItems(String[] datas, int[] icons, final OnItemClickListener l) {
        setItems(datas, icons, l, -1);
    }

    public void setItems(String[] datas, int[] icons, final OnItemClickListener l, int paddingLeft) {
        ListView listView = createListView();
        if (datas != null && datas.length > 0 && icons != null && icons.length > 0) {
            listView.setAdapter(new ListAdapter(datas, icons, paddingLeft));
            if (l != null) {
                listView.setOnItemClickListener(l);
            }

            super.setEditVisible(false);
            super.setCustomView(listView);
        }
    }

    public void setItems(String[] datas, final OnItemClickListener l) {
        ListView listView = createListView();
        if (listView != null && datas != null && datas.length > 0) {
            listView.setAdapter(new ListAdapter(datas, null));
            if (l != null) {
                listView.setOnItemClickListener(l);
            }

            setEditVisible(false);
            setCustomView(listView);
        }
    }

    private ListView createListView() {
        ListView listView = new ListView(mContext);
        listView.setDivider(new ColorDrawable(mContext.getResources().getColor(R.color.divider_color)));
        listView.setDividerHeight((int) mContext.getResources().getDimension(R.dimen.account_settings_divider_height));
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int margin = getContext().getResources().getDimensionPixelSize(R.dimen.conversation_item_margin);
        mlp.setMargins(margin, 0, margin, 0);
        listView.setLayoutParams(mlp);
        listView.setPadding(margin, 0, margin, 0);
        listView.setSelector(R.drawable.dialog_item_selector);
        return listView;
    }

    class ListAdapter extends BaseAdapter {
        String[] mDatas;

        int[] mIcons;

        LayoutInflater mInflater;

        int mHeight = 0;
        int mPaddingLeft = -1;

        ListAdapter(String[] datas, int[] icons) {
            this(datas, icons, -1);
        }

        ListAdapter(String[] datas, int[] icons, int paddingLeft) {
            mDatas = datas;
            mIcons = icons;
            mPaddingLeft = paddingLeft;
            mInflater = LayoutInflater.from(mContext);
            mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, mContext
                    .getResources().getDisplayMetrics());
        }

        @Override
        public int getCount() {
            return mDatas.length;
        }

        @Override
        public Object getItem(int position) {
            return mDatas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) mInflater.inflate(R.layout.compose_dialog_list_item, null);
            if (mListItemGravity != Gravity.LEFT) {
                v.setGravity(mListItemGravity);
            }
            v.setText(getItem(position).toString());
            if (mIcons != null && mIcons.length > 0) {
                Drawable drawable = mContext.getResources().getDrawable(mIcons[position]);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                v.setCompoundDrawables(drawable, null, null, null);
            }
            v.setHeight(mHeight);
            if (mPaddingLeft != -1) {
                v.setPadding(mPaddingLeft, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
            }
            return v;
        }
    }

}
