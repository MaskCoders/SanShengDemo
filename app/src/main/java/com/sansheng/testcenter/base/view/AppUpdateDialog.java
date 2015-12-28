package com.sansheng.testcenter.base.view;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class AppUpdateDialog extends BaseDialog {
	
	public AppUpdateDialog(Context context, boolean cancelable,
						   DialogInterface.OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public AppUpdateDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public AppUpdateDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onShow() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if(dm.widthPixels >  dm.heightPixels){ 
			lp.width = dm.widthPixels/2;
		}else{
			lp.width = (int)(Math.min(dm.widthPixels, dm.heightPixels)- margin);
		}
		getWindow().setAttributes(lp);
	}
	
}
