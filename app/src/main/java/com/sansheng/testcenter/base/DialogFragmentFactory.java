package com.sansheng.testcenter.base;

import android.app.*;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AppUpdateDialog;
import com.sansheng.testcenter.upgrade.AppInfo;
import com.sansheng.testcenter.upgrade.AppUpgrade;

public class DialogFragmentFactory {
	
	public static final int FRAGEMNT_TYPE_INSTALL = 1;
	
	public static final int FRAGEMNT_TYPE_CHECKVERSION = 2;
	
	public static final int FRAGEMENT_TYPE_UPDATE = 3;
	
	public static DialogFragment createDialogFragment(int fragmentType,Object... objects){
		switch (fragmentType) {
		case FRAGEMNT_TYPE_INSTALL:
			return InstallAlertDialogFragment.newInstance((AppUpgrade) objects[0], (AppUpgrade.UpgradeInfo) objects[1]);
		case FRAGEMNT_TYPE_CHECKVERSION:
			return CheckVersionDialogFragment.newInstance();	
		case FRAGEMENT_TYPE_UPDATE:
			return UpgradeDialogFragment.newInstance((AppUpgrade) objects[0], (AppUpgrade.UpgradeInfo) objects[1]);
		default:
			break;
		}
		return null;
	}
	
	public static class CheckVersionDialogFragment extends DialogFragment {
		
		public final static String TAG = "CheckVersionDialog";

		public CheckVersionDialogFragment() {
		}

		public static CheckVersionDialogFragment newInstance() {
			final CheckVersionDialogFragment f = new CheckVersionDialogFragment();
			return f;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Context context = getActivity();
			final ProgressDialog dialog = new ProgressDialog(context);
			dialog.setIndeterminate(true);
			dialog.setMessage(context.getString(R.string.checking_message));
			dialog.setCancelable(false);
			return dialog;
		}
	}

	public static class InstallAlertDialogFragment extends DialogFragment {

		public final static String TAG = "InstallAlertDialogFragment";

        AppUpgrade mUpgrade;
        AppUpgrade.UpgradeInfo mInfo;
        
        public InstallAlertDialogFragment(){
        	
        }

		public InstallAlertDialogFragment(AppUpgrade p,AppUpgrade.UpgradeInfo info) {
			mUpgrade = p;
            mInfo = info;
		}

		public static DialogFragment newInstance(AppUpgrade p,AppUpgrade.UpgradeInfo info) {
			final InstallAlertDialogFragment f = new InstallAlertDialogFragment(p,info);
			return f;
		}
		
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
            final BaseDialog dialog =  new BaseDialog(getActivity(),R.style.CustomDialog);
            dialog.show();
            dialog.setTitleText(getString(R.string.upgrade_title));
            dialog.setMessage(R.string.download_complete_message);
            dialog.setEditVisible(false);
            dialog.setNegativeButton(R.string.donot_upgrade, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (mUpgrade.isAuto() && mInfo.isForce()) {
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
			});
            dialog.setPositiveButton(R.string.upgrade_now, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mUpgrade.installNow();
					dialog.dismiss();
				}
			});
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
		}
	}

	public static class UpgradeDialogFragment extends DialogFragment {
		public final static String TAG = "UpgradeDialogFragment";

        AppUpgrade mUpgrade;
        AppUpgrade.UpgradeInfo mInfo;

        public UpgradeDialogFragment(){

        }

		public UpgradeDialogFragment(AppUpgrade p,AppUpgrade.UpgradeInfo info) {
			mUpgrade = p;
            mInfo = info;
		}

		public static DialogFragment newInstance(AppUpgrade p,AppUpgrade.UpgradeInfo info) {
			final UpgradeDialogFragment f = new UpgradeDialogFragment(p,info);
			return f;
		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			// TODO Auto-generated method stub
			super.onConfigurationChanged(newConfig);
			Handler h = new Handler();
			final AppUpgrade k = mUpgrade;
			final AppUpgrade.UpgradeInfo i = mInfo;
			final FragmentManager f = getFragmentManager();
			h.post(new Runnable() {
				@Override
				public void run() {
					dismiss();
					newInstance(k, i).show(f, TAG);
				}
			});

		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			if(mUpgrade==null) // when conifg of system changed, Fragment will init agin ; the mUpgtrade will be null.
				return null;
			 final Activity context = mUpgrade.getActivity();
		        LayoutInflater layoutInflater = LayoutInflater
		                .from(context);
		        View view = layoutInflater.inflate(
		                R.layout.find_new_version, null);
		        TextView releaseView = (TextView) view
		                .findViewById(R.id.now_version);
		        StringBuilder sb = new StringBuilder();
		        sb.append(context.getString(R.string.now_version))
		                .append(AppInfo.getTheAppInfo(context).getAppVersionName())
		                .append("\n")
		                .append("\n")
		                .append(context
		                        .getString(R.string.new_version))
		                .append(mInfo.versionName)
		                .append(" ("
		                        + context.getString(R.string.file_size)
		                        + " " + mInfo.getSizeUseMB() + "MB)")
		                .append("\n")
		                .append(mInfo.releaseNote)
		                .append("\n");
		        releaseView.setText(sb.toString());

		        TextView promptView = (TextView) view
		                .findViewById(R.id.warm_prompt);
		        sb = new StringBuilder();
		        sb.append(context.getString(R.string.new_down_desc2))
				         .append("\n")
				         .append(context.getString(R.string.new_down_desc3)).append("\n");
		        promptView.setText(sb.toString());

		        final CheckBox checkbox = (CheckBox) view
		                .findViewById(R.id.CheckBox);
		        boolean isNotify = mUpgrade.isNotify();
		        checkbox.setChecked(!isNotify);
		        final RelativeLayout rlCheckBox = (RelativeLayout) view
		                .findViewById(R.id.rlCheckBox);
		        rlCheckBox.setOnClickListener(new OnClickListener() {
		            public void onClick(View v) {
		                if (checkbox.isChecked()) {
		                    checkbox.setChecked(false);
		                    mUpgrade.setNotify(true);
		                } else {
		                    checkbox.setChecked(true);
		                    mUpgrade.setNotify(false);
		                    Toast.makeText(context, R.string.auto_notify_toast, Toast.LENGTH_LONG).show();
		                }
		            }

		        });

		        if(!mUpgrade.isAuto() || mInfo.isForce()){
		            rlCheckBox.setVisibility(View.GONE);
		        }
		        if(mUpgrade.isAuto() && mInfo.isForce()){
		            view.findViewById(R.id.mandatoryalert).setVisibility(View.VISIBLE);
		        }

		        final AppUpdateDialog dialog =  new AppUpdateDialog(context,R.style.CustomDialog);
		        dialog.show();
		        dialog.setTitleText(getString(R.string.upgrade_title));
		        dialog.setCustomView(view);
		        dialog.setNegativeButton(R.string.donot_download, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (mInfo.isForce() && mUpgrade.isAuto()) {
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					}
				});
		        dialog.setPositiveButton(R.string.download_now, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							mUpgrade.startDowndLoad(Uri.parse(mInfo.url));
						} catch (IllegalArgumentException e) {
							Toast.makeText(mUpgrade.getActivity(), R.string.check_download_app, Toast.LENGTH_LONG).show();
						}
						dialog.dismiss();
					}
				});
		        dialog.setCanceledOnTouchOutside(false);
		        return dialog;
		}
	}
	
}
