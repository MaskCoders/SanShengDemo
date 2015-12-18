package com.sansheng.testcenter.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import com.sansheng.testcenter.R;

public class ProgressDailog extends Dialog {

    private static final int DEFAULT_MARG_DISMENS = 50;
    private int margDismens = DEFAULT_MARG_DISMENS;

    private Activity mActivity;

    private TextView messageTextView;

    public ProgressDailog(Context context, boolean cancelable,
                          OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public ProgressDailog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public ProgressDailog(Context context) {
        this(context, R.style.CustomDialog);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog_layout);
        messageTextView = (TextView) findViewById(R.id.message);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
        if (mActivity != null) {
            WindowManager windowManager = mActivity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = (int) (display.getWidth() - margDismens); //设置宽度
            getWindow().setAttributes(lp);
        }
    }

    public void setActivity(Activity ac) {
        mActivity = ac;
    }

    public void setMessage(String message) {
        messageTextView.setText(message);
    }
}
