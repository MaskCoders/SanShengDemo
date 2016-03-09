package com.sansheng.testcenter.view;

import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.server.MSocketServer;
import com.sansheng.testcenter.server.SocketClient;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;

/**
 * Created by hua on 12/17/15.
 */
public class WhmActivity extends BaseActivity  {
    Button whm_bl_read_address;
    Button whm_bl_choose_db;
    Button whm_bl_start;
    Button whm_bl_choose_items;
    Button whm_bl_show_log;
    Button conn;
    EditText whm_ip ;
    EditText whm_port;
    private MainHandler mMainHandler;
    private MSocketServer myService;  //我们自己的service
//    private ClientManager mClientManager;
    private SocketClient mClient;
    private TerProtocolCreater cmdCreater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        mMainHandler = new MainHandler(this, this);
//        mClientManager = ClientManager.getInstance(this, mMainHandler);
        cmdCreater = new TerProtocolCreater();
        initData();
        setTitle("电表检测");
    }

    private void initData() {
        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
        main_sort_log.setText("show the sort log");
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.whmbuttonlist, null);
        whm_bl_read_address = (Button) inflate.findViewById(R.id.whm_bl_read_address);
        whm_bl_choose_db = (Button) inflate.findViewById(R.id.whm_bl_choose_db);
        whm_bl_start = (Button) inflate.findViewById(R.id.whm_bl_start);
        whm_bl_choose_items = (Button) inflate.findViewById(R.id.whm_bl_choose_items);
        whm_bl_show_log = (Button) inflate.findViewById(R.id.whm_bl_show_log);

        whm_bl_read_address.setOnClickListener(this);
        whm_bl_choose_db.setOnClickListener(this);
        whm_bl_start.setOnClickListener(this);
        whm_bl_choose_items.setOnClickListener(this);
        whm_bl_show_log.setOnClickListener(this);
        main_button_list.addView(inflate);


    }

    @Override
    protected void initConnList() {
        View inflate = getLayoutInflater().inflate(R.layout.whmconnlist, null);
        conn = (Button) inflate.findViewById(R.id.conn);
        whm_ip = (EditText) inflate.findViewById(R.id.whm_ip);
        whm_port = (EditText) inflate.findViewById(R.id.whm_port);
//        conn4 = (Button) inflate.findViewById(R.id.conn4);
//        conn1.setOnClickListener(this);
//        conn2.setOnClickListener(this);
//        conn3.setOnClickListener(this);
        conn.setOnClickListener(this);
        main_layout_conn.addView(inflate);
    }

    @Override
    protected void initCenter() {
//        View inflate = getLayoutInflater().inflate(R.layout.connbuttonlist, null);
//        main_info.addView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whm_bl_read_address:
//                String time = getTimeStamp()+"\t发送指令=>>";
//                logBuffer = new StringBuffer();
//                logBuffer.append("68 49 00 49 00 68 4A 10 12 64 00 02 0C F0 00 00 01 00 00 35 24 09 25 00 56 16".replace(" ",""));
                String address = "02 00 00 00 10 20".replace(" ","");
                Const.WhmConst.C type = Const.WhmConst.C.MAIN_REQUEST_READ_DATA;
                String data = "33 32 34 33 ".replace(" ","");
                WhmBean bean =  WhmBean.create(type,data,address);
                        //.append("\n");
//                SpannableString span = new SpannableString(time+logBuffer.toString());
//                span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.contact_list_text_color_selected)),
//                        0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                main_whole_log.append(span);
//                main_sort_log.setText(span);
//                showShortLog(true);
                ;
                Message msg = new Message();
                msg.obj = bean.toString();
                msg.what = Const.SEND_MSG;
                mMainHandler.sendMessage(msg);
                System.out.println("by hua : "+bean.toString());
//                mClientManager.sendMessage(mClient,ProtocolUtils.hexStringToBytes(bean.toString()));
                break;
            case R.id.whm_bl_choose_db:
                break;
            case R.id.whm_bl_start:
                break;
            case R.id.whm_bl_choose_items:
                break;
            case R.id.whm_bl_show_log:
                if(wholeIsShow()){
                    showWholeLog(false);
                    ((Button)findViewById(R.id.whm_bl_show_log)).setText(R.string.whm_bl_show_log);
                }else{
                    showWholeLog(true);
                    ((Button)findViewById(R.id.whm_bl_show_log)).setText(R.string.whm_bl_close_log);
                }
                break;
            case R.id.conn:
//                mClient = mClientManager.createClient(whm_ip.getText().toString(),Integer.valueOf(whm_port.getText().toString()));
                break;
        }
        super.onClick(v);
    }


    @Override
    public void pullShortLog(String info) {

    }

    @Override
    public void pullShortLog(SpannableString info) {
        main_sort_log.append(info);
        showShortLog(true);
    }

    @Override
    public void pullWholeLog(String info) {
        SpannableString ss = new SpannableString(main_whole_log.getText().toString() + "\n " +
                getResources().getString(R.string.server) + info);
        main_whole_log.append(ss);
        main_whole_log.setSelection(main_whole_log.getText().length() - 1);
    }

    @Override
    public void setValue(BeanMark bean) {

    }

    @Override
    public void pullWholeLog(SpannableString info) {
        main_whole_log.append(info);
    }

    @Override
    public void pullStatus(String info) {
        main_status_info.setText(info);
    }

}
