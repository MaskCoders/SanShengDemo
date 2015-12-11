package com.example.demo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.demo.R;


/**
 * @author huzhi
 * @date  2014.06.16
 *
 * 完善 zhuzhenhua
 * @since 2015.05.14
 */
public class BaseDialog extends Dialog {


	private static final int DEFAULT_MARIN = 50;
	protected int margin = DEFAULT_MARIN;

	private TextView mTitleText;
	private EditText mEditText;
	private Button mPositive;
	private Button mNegative;
	private TextView mMessage;
	private TextView mSubMessage;
	private View mDialogButtonPanel;
    private ViewGroup mContainer;
    private View mCenterLine;
	private View mMessageDivider;
	private View mButtonPanel;
	private View mContainerTailDivider;
	private View mContainerHeadDivider;
	private View root;
	private View mTitleLayout;

	public BaseDialog(Context context) {
		this(context, R.style.CustomDialog);
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	protected void init(Context context) {
		setContentView(R.layout.base_dialog_layout);
		mTitleLayout = findViewById(R.id.title_layout);
		mContainer = (ViewGroup) findViewById(R.id.container);
		mTitleText = (TextView) findViewById(R.id.title);
		mEditText = (EditText) findViewById(R.id.edit);
		mPositive = (Button) findViewById(R.id.dialog_button_ok);
		mNegative = (Button) findViewById(R.id.dialog_button_cancel);
		mMessage = (TextView) findViewById(R.id.mention_message);
		mSubMessage = (TextView) findViewById (R.id.sub_mention_message);
		mEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		mCenterLine = findViewById(R.id.center_line);
//		mMessageDivider = findViewById(R.id.dialog_message_divider);
		mContainerHeadDivider = findViewById(R.id.dialog_body_head_divider);
		mContainerTailDivider = findViewById(R.id.dialog_container_divider);
		mButtonPanel = findViewById(R.id.dialog_button_panel);
		mDialogButtonPanel = findViewById(R.id.dialog_button_panel);

		margin = (int) context.getResources().getDimensionPixelSize(R.dimen.conversation_item_margin);

		root = findViewById(R.id.root);
	}

	public void setCustomView(View view) {
		mContainer.addView(view);
		mContainer.setVisibility(View.VISIBLE);
		mEditText.setVisibility(View.GONE);
        mMessage.setVisibility(View.GONE);
        mSubMessage.setVisibility(View.GONE);
	}

	public void setMessage(int resId) {
		if (mMessage != null) {
			mMessage.setText(resId);
			mMessage.setVisibility(View.VISIBLE);
		}
	}
	
	public void setMessage(String str) {
		if (mMessage != null) {
			mMessage.setText(str);
			mMessage.setVisibility(View.VISIBLE);
		}
	}

	public void setMessage(CharSequence str) {
		if (mMessage != null) {
			mMessage.setText(str);
			mMessage.setVisibility(View.VISIBLE);
		}
	}
	public void setSubMessage(int resId) {
		if (mSubMessage != null) {
			mSubMessage.setText(resId);
			mSubMessage.setVisibility(View.VISIBLE);
		}
	}

	public void setSubMessage(String str) {
		if (mSubMessage != null) {
			mSubMessage.setText(str);
			mSubMessage.setVisibility(View.VISIBLE);
		}
	}

	public void setSubMessage(CharSequence str) {
		if (mSubMessage != null) {
			mSubMessage.setText(str);
			mSubMessage.setVisibility(View.VISIBLE);
		}
	}
	public void setTitleText(int resId) {
		if (mTitleText != null) {
			mTitleText.setText(resId);
			setTitleVisibility(true);
		}
	}

	public void setTitleText(String title) {
		if (!TextUtils.isEmpty(title)) {
			mTitleText.setText(title);
			setTitleVisibility(true);
		}else
			setTitleVisibility(false);
	}

	public void setEditText(String edit) {
		if (!TextUtils.isEmpty(edit)) {
			mEditText.setText(edit);
            mEditText.setSelection(mEditText.getText().length());
            mEditText.setVisibility(View.VISIBLE);
		}
	}

	public void setEditVisible(boolean visible) {
        if(visible){
            mEditText.setVisibility(View.VISIBLE);
        }
        else{
            mEditText.setVisibility(View.GONE);
        }
	}
    public void setEditRestriction(TextWatcher textWatcher,boolean isSet){
        if(textWatcher!=null){
            if(isSet){
                if(mEditText!=null) mEditText.addTextChangedListener(textWatcher);
            } else {
                if(mEditText!=null) mEditText.removeTextChangedListener(textWatcher);
            }
        }
    }
	public void setOnPositiveBtnClickListener(View.OnClickListener listener) {

		mPositive.setOnClickListener(listener);
	}

	public void setPositiveButton(int resId, View.OnClickListener listener) {
		if (mPositive != null) {
			mPositive.setText(resId);
			mPositive.setOnClickListener(listener);
		}
	}

	public void setPositiveButton(String msg, View.OnClickListener listener) {
		if (mPositive != null) {
			mPositive.setText(msg);
			mPositive.setOnClickListener(listener);
		}
	}
	
	public void setOnNegativeBtnClickListener(View.OnClickListener listener){
		mNegative.setOnClickListener(listener);
	}

	public void setNegativeButton(int resId, View.OnClickListener listener) {
		if (mNegative != null) {
			mNegative.setText(resId);
			mNegative.setOnClickListener(listener);
		}
	}

	public void setNegativeButton(String msg, View.OnClickListener listener) {
		if (mNegative != null) {
			mNegative.setText(msg);
			mNegative.setOnClickListener(listener);
		}
	}
	
	public void setNegativeButtonDismiss() {
		if (mNegative != null) {
			mNegative.setVisibility(View.GONE);
		}
		if (mCenterLine != null) {
			mCenterLine.setVisibility(View.GONE);
		}
		if (mPositive != null) {
			mPositive.setBackgroundResource(R.drawable.settings_button_single_drawable);
		}
	}

	public void setPositiveButtonDismiss() {
		if (mPositive != null) {
			mPositive.setVisibility(View.GONE);
		}
		if (mCenterLine != null) {
			mCenterLine.setVisibility(View.GONE);
		}
		if (mNegative != null) {
			mNegative.setBackgroundResource(R.drawable.settings_button_single_drawable);
		}
	}

	public void setButtonPanelDismiss() {
		if (mButtonPanel != null) {
			mButtonPanel.setVisibility(View.GONE);
		}
		if (mMessageDivider != null) {
			mMessageDivider.setVisibility(View.GONE);
		}
		if (mCenterLine != null) {
			mCenterLine.setVisibility(View.GONE);
		}
		if (mContainerTailDivider != null) {
			mContainerTailDivider.setVisibility(View.GONE);
		}
	}

	/**
	 * 隐藏正文下面的线
	 */
	public void setContainDividerDismiss() {
		if (mContainerTailDivider != null) {
			mContainerHeadDivider.setVisibility(View.GONE);
			mContainerTailDivider.setVisibility(View.GONE);
		}
	}

	@Override
	public void show() {
		super.show();
		onShow();
	}

	public void onShow() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (Math.min(dm.widthPixels, dm.heightPixels) - margin); //设置宽度
		getWindow().setAttributes(lp);
	}

	public EditText getEditText() {
		return mEditText;
	}

	public TextView getMessageTextView() {
		return mMessage;
	}

	public Button getPositiveButton() {
		return mPositive;
	}

	public Button getNegativeButton() {
		return mNegative;
	}

	public void setPositiveButtonText(int resid) {
		mPositive.setText(resid);
	}

	public void setNegativeButtonText(int resid) {
		mNegative.setText(resid);
	}

	public void setNoButton() {
		mDialogButtonPanel.setVisibility(View.GONE);
		mContainerTailDivider.setVisibility(View.GONE);
	}

	public void setTitleVisibility(boolean visible) {
		mTitleLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	public void setTitleDismiss() {
		mTitleLayout.setVisibility(View.GONE);
	}

	public static boolean isUnableToShow(Activity mActivity) {
		boolean canNotShow = mActivity == null || mActivity.isFinishing();
		if (!canNotShow && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			canNotShow = canNotShow || mActivity.isDestroyed();
		}
		return canNotShow;
	}

	public void setMargDimension(int dimension) {
		margin = dimension;
	}
}
