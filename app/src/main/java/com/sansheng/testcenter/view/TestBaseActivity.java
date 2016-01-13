package com.sansheng.testcenter.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.controller.ConnectionService;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.server.ClientManager;
import com.sansheng.testcenter.server.MSocketServer;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;

/**
 * Created by hua on 12/17/15.
 */
public class TestBaseActivity extends BaseActivity  {
    Button text1;
    Button text2;
    Button text4;
    Button text3;
    Button conn1;
    Button conn2;
    Button conn4;
    Button conn3;
    private MainHandler mMainHandler;
    private MSocketServer myService;  //我们自己的service
    private ClientManager mClientManager;
    private TerProtocolCreater cmdCreater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        mMainHandler = new MainHandler(this, this);
        mClientManager = ClientManager.getInstance(this, mMainHandler);
        cmdCreater = new TerProtocolCreater();
        initData();
        setTitle("电表检测");
    }

    private void initData() {
        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
        main_sort_log.setText("show the sort log");
        main_whole_log.setText("show long test\nshow long test\nshow long test\nshow long test\nshow long test\n");
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.testbuttonlist, null);
        text1 = (Button) inflate.findViewById(R.id.text1);
        text2 = (Button) inflate.findViewById(R.id.text2);
        text3 = (Button) inflate.findViewById(R.id.text3);
        text4 = (Button) inflate.findViewById(R.id.text4);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        main_button_list.addView(inflate);


    }

    @Override
    protected void initConnList() {
        View inflate = getLayoutInflater().inflate(R.layout.connbuttonlist, null);
        conn1 = (Button) inflate.findViewById(R.id.conn1);
        conn2 = (Button) inflate.findViewById(R.id.conn2);
        conn3 = (Button) inflate.findViewById(R.id.conn3);
        conn4 = (Button) inflate.findViewById(R.id.conn4);
        conn1.setOnClickListener(this);
        conn2.setOnClickListener(this);
        conn3.setOnClickListener(this);
        conn4.setOnClickListener(this);
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
            case R.id.text1:
                showShortLog(true);
                break;
            case R.id.text2:
                showWholeLog(true);
                break;
            case R.id.text3:
                showShortLog(false);
                break;
            case R.id.text4:
                showWholeLog(false);
                break;
            case R.id.conn1:
                Intent intent = new Intent(TestBaseActivity.this, MSocketServer.class);
                startService(intent);
                break;
            case R.id.conn2:
                intent = new Intent(TestBaseActivity.this, MSocketServer.class);
                ConnectionService connSer = new ConnectionService(mMainHandler, myService);
                bindService(intent, connSer, Context.BIND_AUTO_CREATE);
                break;
            case R.id.conn3:
                mClientManager.createClient(null,-100);
                break;
            case R.id.conn4:
                mClientManager.sendMessage(null, cmdCreater.makeCommand(null,null,null));
                break;
        }
        super.onClick(v);
    }


    @Override
    public void pullShortLog(String info) {

    }

    @Override
    public void pullShortLog(SpannableString info) {

    }

    @Override
    public void pullWholeLog(String info) {
        SpannableString ss = new SpannableString(main_whole_log.getText().toString() + "\n " +
                getResources().getString(R.string.server) + info);
//            ss.setSpan(new ForegroundColorSpan(SocketDemo.this.getResources().getColor(R.color.download_text_color)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_whole_log.setText(ss);
        main_whole_log.setSelection(main_whole_log.getText().length() - 1);
    }

    @Override
    public void setValue(WhmBean bean) {

    }

    @Override
    public void pullWholeLog(SpannableString info) {

    }

    @Override
    public void pullStatus(String info) {
        main_status_info.setText(info);
    }

}
