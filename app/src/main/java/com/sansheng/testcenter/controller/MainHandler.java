package com.sansheng.testcenter.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.tools.log.LogUtils;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import static com.sansheng.testcenter.base.Const.*;

/**
 * Created by hua on 12/18/15.
 */
public class MainHandler extends Handler {
    private Context mContext;
    private IServiceHandlerCallback mMainUI;
    LogUtils logUtils;
    public MainHandler(Context ctx, IServiceHandlerCallback ui) {
        mContext = ctx;
        mMainUI = ui;
        logUtils = new LogUtils();
    }

    private SpannableString getErrSS(String content) {
        content = mContext.getResources().getString(R.string.conn_ser_err1) + ":" + content;
        SpannableString sserror = new SpannableString(content);
        sserror.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.err_msg_tip)),
                0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sserror;
    }
    private SpannableString getCloseSS(String content) {
        content = content+"  服务关闭！";
        SpannableString sserror = new SpannableString(content);
        sserror.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.err_msg_tip)),
                0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sserror;
    }

    private SpannableString getRecvCmdSS(String content) {
        String time = ProtocolUtils.getTimeStamp() + "\t接受指令=>>";
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(content).append("\n");
        SpannableString span = new SpannableString(time + logBuffer.toString());
        span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.stress_color)),
                0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public static SpannableString getSendCmdSS(Context mContext,String content) {
        String time = ProtocolUtils.getTimeStamp() + "\t发送指令=>>";
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(content).append("\n");
        SpannableString span = new SpannableString(time + logBuffer.toString());
        span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.contact_list_text_color_selected)),
                0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
    public SpannableString getSendCmdSS(String content) {
        String time = ProtocolUtils.getTimeStamp() + "\t发送指令=>>";
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(content).append("\n");
        SpannableString span = new SpannableString(time + logBuffer.toString());
        span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.contact_list_text_color_selected)),
                0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
    public SpannableString getSendValueSS(String content) {
        String time = ProtocolUtils.getTimeStamp() + "\t发送指令=>>";
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(content).append("\n");
        SpannableString span = new SpannableString(time + logBuffer.toString());
        span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.contact_list_text_color_selected)),
                0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String content = msg.obj.toString();
        SpannableString ssContent;
        switch (msg.what) {
            case SEND_MSG:
                SpannableString sendSS =new SpannableString(
                        getSendCmdSS(ProtocolUtils.addSpaceInCmd(content.toString())));
                mMainUI.pullShortLog(sendSS);
                mMainUI.pullWholeLog(sendSS);
                logUtils.saveLog(sendSS.toString());
                return;
            case RECV_MSG:
                SpannableString recvSS =new SpannableString(
                        getRecvCmdSS(ProtocolUtils.addSpaceInCmd(content.toString())));
                mMainUI.pullShortLog(recvSS);
                mMainUI.pullWholeLog(recvSS);

                if(msg.obj instanceof WhmBean){
                    WhmBean bean = (WhmBean) msg.obj;
                    double arr[] = bean.getUserDataArr(4);
                    mMainUI.setValue(bean);
                }
                logUtils.saveLog(recvSS.toString());
                return;
            case CONN_SER_CLS:
                break;
            case CONN_CLOSE:
                SpannableString ss = getCloseSS(content);
                mMainUI.pullWholeLog(ss);
                logUtils.saveLog(ss.toString());
                return;
//            case RECV_MSG:
//                if (content.contains(ERRCODE)) {
//                    content = mContext.getResources().getString(R.string.conn_ser_err2);
//                } else if (content.contains(CONN_SUCCESS)) {
//                    mMainUI.pullStatus(mContext.getResources().getString(R.string.server)
//                            + content.replace(CONN_SUCCESS, "").replace("\n", "") + "," +
//                            mContext.getResources().getString(R.string.ser_port) + PORT);
//
////                        btn_ser.setText(SocketDemo.this.getResources().getString(R.string.start_ser_close));
//                    return;
//                }
//                break;
            case CONN_OK:
                break;
            case CONN_ERR:
                if(TextUtils.isEmpty(msg.obj.toString())) {
                    content = mContext.getResources().getString(R.string.conn_ser_err1) + ":" + content + "\n";
                }
                SpannableString sserror = getErrSS(content);
                mMainUI.pullShortLog(sserror);
                mMainUI.pullWholeLog(sserror);
                logUtils.saveLog(sserror.toString());
                return;
            case OVER_TIME:
                content = "接受超时"+ "\n";
                SpannableString sserrot = getErrSS(content);
                mMainUI.pullShortLog(sserrot);
                mMainUI.pullWholeLog(sserrot);
            default:
        }
        logUtils.saveLog(content);
        mMainUI.pullWholeLog(content);

    }
}
