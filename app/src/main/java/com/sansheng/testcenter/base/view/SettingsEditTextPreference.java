package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
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
public class SettingsEditTextPreference extends EditTextPreference implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "SettingsEditTextPreference";
    String mTitleText = null;
    String mSummaryText = null;
    View mRootView;

    public SettingsEditTextPreference(Context context) {
        this(context, null);
    }

    public SettingsEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("editTextPreferenceStyle", "attr", "android"));
    }

    public SettingsEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        pTitleView.setText(mTitleText);
        TextView pTipView = (TextView) view.findViewById(R.id.content_text);
        Log.e("ssg", TAG + " init value = " + getText());
        pTipView.setText(getText());
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
        Log.e("ssg", TAG + " new Value = " + newValue.toString());
        Log.e("ssg", TAG + " old text = " + getText());
        if (!Utilities.equals(getText(), newValue.toString())) {
            TextView contentView = (TextView) mRootView.findViewById(R.id.content_text);
            contentView.setText(newValue.toString());
            return true;
        }
        return false;
    }

    public void setContent(String content) {
        TextView contentView = (TextView) mRootView.findViewById(R.id.content_text);
        contentView.setText(content);
    }
}
