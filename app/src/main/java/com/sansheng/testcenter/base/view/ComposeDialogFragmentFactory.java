package com.sansheng.testcenter.base.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.sansheng.testcenter.R;

public class ComposeDialogFragmentFactory {
	private static ComposeDialogFragmentFactory mInstance;
	private ComposeDialogFragmentFactory() {
	}
	public static ComposeDialogFragmentFactory getInstance() {
		if (mInstance == null) {
			mInstance = new ComposeDialogFragmentFactory();
		}
		return mInstance;
	}

	public DialogFragment createSendConfirmDlg(int titleId, int messageId, DlgCallback callback) {
		return  SendConfirmDialogFragment.newInstance(titleId, messageId, null,  callback);
	}

	public DialogFragment createAttachTitleSendConfirmDlg(int titleId, int messageId, int subMessageId, DlgCallback callback) {
		return SendConfirmDialogFragment.newInstance(titleId, messageId, subMessageId,  callback);
	}

//	public DialogFragment createSaveConfirmDlg(boolean save, boolean showToast,
//			DlgCallback callback) {
//		return new SaveConfirmDialogFragment(save, showToast, callback);
//	}
//
//	public DialogFragment createDiscardConfirmDlg(DlgCallback callback) {
//		return new DiscardConfirmDialogFragment(callback);
//	}
//
//	public DialogFragment createRecipientErrorDlg(String message,
//			DlgCallback callback) {
//		return new RecipientErrorDialogFragment(message, callback);
//	}
//
//	public DialogFragment createSelectAccountDlg(String[] emails, int[] icons,
//			DlgCallback callback) {
//		return new SelectAccountDialogFragment(emails, icons, callback);
//	}
//
//	public DialogFragment createDownloadAttConfirmDlg(DlgCallback callback) {
//		return new DownloadAttConfirmDialogFragment(callback);
//	}

//	public DialogFragment createPreAttConfirmDlg(ComposeActivity.PreAttCallback callback) {
//		return new PreAttConfirmDialogFragment(callback);
//	}

	public Dialog createSelectImageDialog(Context context,DlgCallback callback){
		return new SelectImageDialog(context,callback);
	}
//	public DialogFragment createComposeDownloadAttDlg(
//			int attNumToDownload, String attSizeToDownload, DlgCallback callback) {
//		return new ComposeDownloadAttDialogFragment(attNumToDownload, attSizeToDownload, callback);
//	}

	public interface DlgCallback {
		public void onPositiveCallback();

		public void onNegativeCallback();

		public void onItemSelected(int pos);
	}

	public interface ChooseAttachmentDialogCallback {
		public void onPositiveCallback();

		public void onNegativeCallback();

		public void onItemSelected(int loaderID);
	}

	public interface DlgWithCheckboxCallback {
		public void onChecked(boolean checked);
	}


//	private class SaveConfirmDialogFragment extends DialogFragment {
//		private boolean save;
//		private boolean showToast;
//		private DlgCallback callback;
//
//		public SaveConfirmDialogFragment(boolean save, boolean showToast,
//				DlgCallback callback) {
//			this.save = save;
//			this.showToast = showToast;
//			this.callback = callback;
//		}
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			final AnswerDialog dialog = new AnswerDialog(getActivity());
//			dialog.show();
//			dialog.setTitleText(R.string.confirm_save_message_title);
//			dialog.setNegativeButton(R.string.confirm_save_message_no,
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							callback.onNegativeCallback();
//							dialog.dismiss();
//						}
//					});
//			dialog.setPositiveButton(R.string.confirm_save_message_yes,
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							callback.onPositiveCallback();
//							dialog.dismiss();
//						}
//					});
//			dialog.setContainDividerDismiss();
//			return dialog;
//		}
//	}

//	private class DiscardConfirmDialogFragment extends DialogFragment {
//		private DlgCallback callback;
//
//		public DiscardConfirmDialogFragment(DlgCallback callback) {
//			this.callback = callback;
//		}
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			final AnswerDialog dialog = new AnswerDialog(getActivity());
//			dialog.show();
//			dialog.setTitleText(R.string.confirm_discard_text);
//			dialog.setNegativeButton(R.string.cancel,
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							dialog.dismiss();
//						}
//					});
//			dialog.setPositiveButton(R.string.discard,
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							callback.onPositiveCallback();
//						}
//					});
//			return dialog;
//		}
//	}
//
//	private class RecipientErrorDialogFragment extends DialogFragment {
//		private String message;
//		private DlgCallback callback;
//
//		// Public no-args constructor needed for fragment re-instantiation
//		public RecipientErrorDialogFragment(String message, DlgCallback callback) {
//			this.message = message;
//			this.callback = callback;
//		}
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			final AnswerDialog dialog = new AnswerDialog(getActivity());
//			dialog.show();
//			dialog.setTitleText(R.string.recipient_error_dialog_title);
//			dialog.setMessageText(message);
//			dialog.setPositiveButton(R.string.ok,
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							callback.onPositiveCallback();
//							dialog.dismiss();
//						}
//					});
//			dialog.setNegativeButtonDismiss();
//			return dialog;
//		}
//	}

//	private class DownloadAttConfirmDialogFragment extends DialogFragment {
//		private DlgCallback callback;
//
//		public DownloadAttConfirmDialogFragment(DlgCallback callback) {
//			this.callback = callback;
//		}
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			final AnswerDialog dialog = new AnswerDialog(getActivity());
//			dialog.show();
//			dialog.setTitleText(R.string.send_att_title);
//			dialog.setMessageText(R.string.send_att_message);
//			dialog.setNegativeButton(R.string.cancel_sending,
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							dialog.dismiss();
//						}
//					});
//			dialog.setPositiveButton(R.string.download_and_send,
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							callback.onPositiveCallback();
//							dialog.dismiss();
//						}
//					});
//			return dialog;
//		}
//	}

//	private class SelectAccountDialogFragment extends DialogFragment {
//		private String[] emails;
//		private int[] icons;
//		private DlgCallback callback;
//
//		public SelectAccountDialogFragment(String[] emails, int[] icons,
//				DlgCallback callback) {
//			this.emails = emails;
//			this.icons = icons;
//			this.callback = callback;
//		}
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			final AnswerDialog dialog = new AnswerDialog(getActivity());
//			dialog.show();
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//				@Override
//				public boolean onKey(DialogInterface dialog, int keyCode,
//						KeyEvent event) {
//					switch (keyCode) {
//					case KeyEvent.KEYCODE_BACK:
//						callback.onNegativeCallback();
//						return true;
//					}
//					return false;
//				}
//			});
//			dialog.setTitleText(R.string.merged_mailbox_compose_select_account);
//			dialog.setItems(emails, icons, new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//										int position, long id) {
//					callback.onItemSelected(position);
//					dialog.dismiss();
//				}
//			});
//			dialog.setButtonPanelDismiss();
//			return dialog;
//		}
//	}

//	private class PreAttConfirmDialogFragment extends DialogFragment {
//		private ComposeActivity.PreAttCallback callback;
//
//		public PreAttConfirmDialogFragment (ComposeActivity.PreAttCallback callback) {
//			this.callback = callback;
//		}
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			final AnswerDialog dialog = new AnswerDialog(getActivity());
//			dialog.show();
//			dialog.setTitleText(R.string.send_att_title);
//			LayoutInflater inflater = LayoutInflater.from(getActivity());
//			View view = inflater.inflate(R.layout.compose_change_account_with_attachment, null);
//			final CheckBox cb = (CheckBox)view.findViewById(R.id.compose_change_account_checkbox);
//			dialog.setCustomView(view);
//			dialog.setNegativeButton(R.string.no, new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (cb.isChecked()) {
//						callback.onChecked(false);
//					}
//					dialog.dismiss();
//				}
//			});
//			dialog.setPositiveButton(R.string.yes, new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//					callback.onPositiveCallback();
//					if (cb.isChecked()) {
//						callback.onChecked(true);
//					}
//				}
//			});
//			return dialog;
//		}
//	}



	private class SelectImageDialog extends AnswerDialog {
		private DlgCallback callback;

		private Context context;

		public SelectImageDialog(Context context,DlgCallback callback) {
			super(context);
			this.context=context;
			this.callback = callback;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			String[] attResource = context.getResources().getStringArray(R.array.crop_image_from);
			int[] icons = new int[] {
					R.drawable.dialog_select_picture,
					R.drawable.dialog_take_photo,
					};

			setTitleText(R.string.select_photo_type);
			getContainer().setBackground(context.getResources().getDrawable(R.drawable.dialog_back_down));
			setItems(attResource, icons, new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					if (callback != null) {
						callback.onItemSelected(position);
					}
					dismiss();
				}
			}, context.getResources().getDimensionPixelSize(R.dimen.choose_attachment_from_paddingLeft));
			setButtonPanelDismiss();
		}
	}

//	private class ComposeDownloadAttDialogFragment extends DialogFragment {
//		private DlgCallback callback;
//		int mAttNumToDownload;
//		String mAttSizeToDownload;
//		public ComposeDownloadAttDialogFragment(int attNumToDownload, String attSizeToDownload, DlgCallback callback) {
//			this.callback = callback;
//			mAttNumToDownload = attNumToDownload;
//			mAttSizeToDownload = attSizeToDownload;
//		}
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//			final AnswerDialog dialog = new AnswerDialog(getActivity());
//			dialog.show();
//			dialog.setTitleText(R.string.send_att_title);
//			dialog.setMessageText(getString(R.string.compose_att_download_message, mAttNumToDownload, mAttSizeToDownload));
//			dialog.setNegativeButton(R.string.downlaod_confirm_not_download, new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					KingsoftAgent.onEventHappened(EventID.MAIL_EDIT.CLICK_GIVE_UP_DOWNLOAD);
//					dialog.dismiss();
//				}
//			});
//			dialog.setPositiveButton(R.string.downlaod_confirm_download, new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					KingsoftAgent.onEventHappened(EventID.MAIL_EDIT.CLICK_DOWNLOAD);
//					dialog.dismiss();
//					callback.onPositiveCallback();
//				}
//			});
//			return dialog;
//		}
//	}
}
