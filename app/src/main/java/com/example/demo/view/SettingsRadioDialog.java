package com.example.demo.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import com.example.demo.R;

public class SettingsRadioDialog extends BaseDialog {
	private RadioGroup mRadioGroup;
	public SettingsRadioDialog(Context context, boolean cancelable,
							   OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public SettingsRadioDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public SettingsRadioDialog(Context context) {
		super(context);
		init();
	}

	private void init(){
		View v = getLayoutInflater().inflate(R.layout.settings_radio_dialog_layout, null);
		setCustomView(v);
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		getPositiveButton().setVisibility(View.GONE);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public void setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener
			 listener){

		setOnNegativeBtnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		View.OnClickListener radioItemClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(SettingsRadioDialog.this, v.getId());
			}
		};
		int index = 0;
		mRadioGroup.removeAllViews();
		for (CharSequence item : items) {
			RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.settings_radio_item, null);
			radioButton.setText(item);
			RadioGroup.LayoutParams rl = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.single_line_height));
			radioButton.setLayoutParams(rl);
			radioButton.setId(index);
			radioButton.setOnClickListener(radioItemClickListener);
			mRadioGroup.addView(radioButton);
			if (index == checkedItem)
				radioButton.setChecked(true);
			if (index != (items.length - 1)) {
				View v = getLayoutInflater().inflate(R.layout.divider, null);
				rl = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT,
						getContext().getResources().getDimensionPixelSize(R.dimen.custom_divider_height));
				int margin = getContext().getResources().getDimensionPixelSize(R.dimen.conversation_item_margin);
				rl.setMargins(margin, 0, margin, 0);
				v.setLayoutParams(rl);
				mRadioGroup.addView(v);
			}
			index++;
		}
	}
}
