package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.utils.Utilities;

/**
 * Created by sunshaogang on 12/18/15.
 */
public class SettingsListPreference extends ListPreference implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "SettingsListPreference";
    String mTitleText = null;
    String mSummaryText = null;
    View mRootView;

    public SettingsListPreference(Context context) {
        this(context, null);
    }

    public SettingsListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingsPreference);
        if (typedArray != null) {
            mTitleText = typedArray.getString(R.styleable.SettingsPreference_s_title);
            mSummaryText = typedArray.getString(R.styleable.SettingsPreference_s_summary);
            typedArray.recycle();
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView pTitleView = (TextView) view.findViewById(R.id.title_text);
        Log.e("ssg", TAG + " init title mTitleText = " + mTitleText);
        pTitleView.setText(mTitleText);
        TextView pTipView = (TextView) view.findViewById(R.id.content_text);
        Log.e("ssg", TAG + " init value = " + getValue());
        pTipView.setText(getEntry());
        setOnPreferenceChangeListener(this);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.changable_preference_layout,
                parent, false);
        return mRootView;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.e("ssg", TAG + " newValue = " + newValue.toString());
        Log.e("ssg", TAG + " old text = " + getEntry());
        int index = findIndexOfValue(newValue.toString());
        Log.e("ssg", TAG + " index = " + index);
        CharSequence value = (index >= 0 && getEntries() != null ? getEntries()[index] : "");
        Log.e("ssg", TAG + " text = " + value);
        if (!Utilities.equals(getEntry(), value)) {
            TextView contentView = (TextView) mRootView.findViewById(R.id.content_text);
            contentView.setText(value);
            return true;
        }
        return false;
    }

    public void setContent(String content) {
        TextView contentView = (TextView) mRootView.findViewById(R.id.content_text);
        contentView.setText(content);
    }
}
