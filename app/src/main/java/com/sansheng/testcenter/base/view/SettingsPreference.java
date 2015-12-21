package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 12/18/15.
 */
public class SettingsPreference extends Preference {

    private static final String TAG = "SettingsPreference";
    int mTitleColorRes;
    int mTitleSizeRes;

    public SettingsPreference(Context context) {
        this(context, null);
    }

    public SettingsPreference(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("preferenceStyle", "attr", "android"));
    }

    public SettingsPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingsCheckBoxPreference);
        if (typedArray != null) {
            mTitleColorRes = typedArray.getColor(R.styleable.SettingsCheckBoxPreference_titlecolor, R.color.title_body_color);
            mTitleSizeRes = typedArray.getDimensionPixelSize(R.styleable.SettingsCheckBoxPreference_titlesize, R.dimen.list_body_text_size_big);
            typedArray.recycle();
        }
        Log.e("ssg", "SettingsPreference mTitleColor = " + getContext().getResources().getColor(mTitleColorRes));
        Log.e("ssg", "SettingsPreference mTitleSize = " + getContext().getResources().getDimension(mTitleSizeRes));

    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        int titleId = Resources.getSystem().getIdentifier("title", "id", "android");
        TextView titleView = (TextView) view.findViewById(titleId);
        if (titleView != null) {
            if (mTitleColorRes != 0) {
                titleView.setTextColor(getContext().getResources().getColor(mTitleColorRes));
            }
            if (mTitleSizeRes != 0) {
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(mTitleSizeRes));
            }
        }
    }

}
