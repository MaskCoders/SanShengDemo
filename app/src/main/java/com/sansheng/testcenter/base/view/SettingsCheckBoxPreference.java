package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 12/18/15.
 */
public class SettingsCheckBoxPreference extends CheckBoxPreference {

    private static final String TAG = "SettingsCheckBoxPreference";
//    TypedValue.COMPLEX_UNIT_PX : Pixels
//    TypedValue.COMPLEX_UNIT_SP : Scaled Pixels
//    TypedValue.COMPLEX_UNIT_DIP : Device Independent Pixels
    int mTitleColorRes;
    int mTitleSizeRes;
    int mSummaryColorRes;
    int mSummarySizeRes;

    public SettingsCheckBoxPreference(Context context) {
        this(context, null);
    }

    public SettingsCheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("checkBoxPreferenceStyle", "attr", "android"));
    }

    public SettingsCheckBoxPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingsCheckBoxPreference);
        if (typedArray != null) {
            mTitleColorRes = typedArray.getColor(R.styleable.SettingsCheckBoxPreference_titlecolor, R.color.title_body_color);
            mTitleSizeRes = typedArray.getDimensionPixelSize(R.styleable.SettingsCheckBoxPreference_titlesize, R.dimen.list_body_text_size_big);
            mSummaryColorRes = typedArray.getColor(R.styleable.SettingsCheckBoxPreference_summerycolor, R.color.conversation_subject_color);
            mSummarySizeRes = typedArray.getDimensionPixelSize(R.styleable.SettingsCheckBoxPreference_summerysize, R.dimen.list_hint_text_size);
            typedArray.recycle();
        }
        Log.e("ssg", "SettingsCheckBoxPreference mTitleColor = " + getContext().getResources().getColor(mTitleColorRes));
        Log.e("ssg", "SettingsCheckBoxPreference mTitleSize = " + getContext().getResources().getDimension(mTitleSizeRes));
        Log.e("ssg", "SettingsCheckBoxPreference mSummaryColor = " + getContext().getResources().getColor(mSummaryColorRes));
        Log.e("ssg", "SettingsCheckBoxPreference mSummarySize = " + getContext().getResources().getDimension(mSummarySizeRes));
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        int summaryId = Resources.getSystem().getIdentifier("summary", "id", "android");
        int titleId = Resources.getSystem().getIdentifier("title", "id", "android");
        TextView summaryView = (TextView) view.findViewById(summaryId);
        TextView titleView = (TextView) view.findViewById(titleId);
        if (titleView != null) {
            if (mTitleColorRes != 0) {
                titleView.setTextColor(getContext().getResources().getColor(mTitleColorRes));
            }
            if (mTitleSizeRes != 0) {
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(mTitleSizeRes));
            }
        }
        if (summaryView != null) {
            if (mSummaryColorRes != 0) {
                summaryView.setTextColor(getContext().getResources().getColor(mSummaryColorRes));
            }
            if (mSummarySizeRes != 0) {
                summaryView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(mSummarySizeRes));
            }
        }
//        titleView.setTextColor(getContext().getResources().getColor(R.color.title_body_color));
//        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.list_body_text_size_big));
//        summaryView.setTextColor(getContext().getResources().getColor(R.color.conversation_subject_color));
//        summaryView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.list_body_text_size_big));
    }
}
