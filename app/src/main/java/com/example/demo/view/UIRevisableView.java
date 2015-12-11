package com.example.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.demo.R;

/**
 * Created by sunshaogang on 7/27/15.
 */
public class UIRevisableView extends RelativeLayout {

    private Context mContext;
    private TextView mTitleView;
    private TextView mContentView;

    private String mTitle;
    private String mContent;

    public UIRevisableView(Context context) {
        this(context, null);
    }

    public UIRevisableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIRevisableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ModifyView);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ModifyView_title:
                    mTitle = context.getResources().getText(typedArray.getResourceId(R.styleable.ModifyView_title, 0)).toString();
                    break;
                case R.styleable.ModifyView_content:
                    mContent = context.getResources().getText(typedArray.getResourceId(R.styleable.ModifyView_content, 0)).toString();
                    break;
            }
        }
        init();
    }

    private void init() {
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.changable_view, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleView = (TextView) findViewById(R.id.title_text);
        mContentView = (TextView) findViewById(R.id.content_text);
        mTitleView.setText(mTitle);
        mContentView.setText(mContent);
    }

    public void setTitle(String text) {
        if (mTitleView == null) {
            mTitleView = (TextView) findViewById(R.id.title_text);
        }
        mTitle = text;
        mTitleView.setText(mTitle);
    }

    /**
     * @param text content
     */
    public void setContent(String text) {
        if (mContentView == null) {
            mContentView = (TextView) findViewById(R.id.content_text);
        }
        mContent = text;
        mContentView.setText(mContent);
    }

}
