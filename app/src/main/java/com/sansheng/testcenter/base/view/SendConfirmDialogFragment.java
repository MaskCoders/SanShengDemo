package com.sansheng.testcenter.base.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import com.sansheng.testcenter.R;

public class SendConfirmDialogFragment extends DialogFragment {
    private int titleId;
    private int messageId;
    private int subMessageId = -1;//default no sub message info;
    private ComposeDialogFragmentFactory.DlgCallback callback;

    public SendConfirmDialogFragment(int titleId, int messageId, Integer subMessageId, ComposeDialogFragmentFactory.DlgCallback callback) {
        this.titleId = titleId;
        this.messageId = messageId;
        this.callback = callback;
        if(subMessageId != null)
            this.subMessageId = subMessageId;
    }

    public SendConfirmDialogFragment(){

    }
    public static SendConfirmDialogFragment newInstance(int titleId, int messageId, Integer subMessageId, ComposeDialogFragmentFactory.DlgCallback callback){
        SendConfirmDialogFragment sendConfirmDialogFragment=new SendConfirmDialogFragment();
        sendConfirmDialogFragment.titleId=titleId;
        sendConfirmDialogFragment.callback=callback;
        sendConfirmDialogFragment.messageId=messageId;
        if(subMessageId!=null){
            sendConfirmDialogFragment.subMessageId=subMessageId;
        }
        return sendConfirmDialogFragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AnswerDialog dialog = new AnswerDialog(getActivity());

        if(titleId==0&&messageId==0){
            return null;
        }
        dialog.show();
        dialog.setTitleText(titleId);
        dialog.setMessageText(messageId);
        if(this.subMessageId != -1)
            dialog.setSubMessage(subMessageId);
        dialog.setPositiveButton(R.string.send,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onPositiveCallback();
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton(R.string.cancel,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onNegativeCallback();
                        dialog.dismiss();
                    }
                });
        return dialog;
    }
}
