package com.test.app;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sansheng.testcenter.base.Const;
import com.sansheng.testcenter.bean.ChannelFactory;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.callback.IServiceHandlerCallback;
import com.sansheng.testcenter.controller.MainHandler;
import com.sansheng.testcenter.server.ClientManager;
import com.sansheng.testcenter.server.SocketClient;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;
import com.sansheng.testcenter.tools.serial.SerialHelper;

import java.io.IOException;
import java.security.InvalidParameterException;

import static com.sansheng.testcenter.base.Const.CONN_ERR;
import static com.sansheng.testcenter.base.Const.CONN_OK;

public class MainActivity extends ActionBarActivity implements View.OnClickListener,IServiceHandlerCallback
{
    TextView log;
    private ClientManager mClientManager;
    private SocketClient mClient;
    private MainHandler mMainHandler;
    private SerialHelper nowChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log = (TextView) findViewById(R.id.log);
        log.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button SocketConn = (Button) findViewById(R.id.SocketConn);
        Button PortConn = (Button) findViewById(R.id.PortConn);
        Button CloseConn = (Button) findViewById(R.id.CloseConn);
        Button start = (Button) findViewById(R.id.start);
        SocketConn.setOnClickListener(this);
        PortConn.setOnClickListener(this);
        CloseConn.setOnClickListener(this);
        start.setOnClickListener(this);
        mClientManager = ClientManager.getInstance(this, mMainHandler);
        mMainHandler = new MainHandler(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private  String getCmd(){
        String data = "33 32 34 33 ".replace(" ", "");//正向有功电能s
        data = ProtocolUtils.bytes2hex(ProtocolUtils.hexStringToBytesDecode(data));
        String address = "12 00 00 00 10 20".replace(" ", "");
        Const.WhmConst.C type = Const.WhmConst.C.MAIN_REQUEST_READ_DATA;
        WhmBean bean = WhmBean.create(type, data, address);
        Message msg = new Message();
        msg.obj = bean.toString();
        msg.what = Const.SEND_MSG;
        mMainHandler.sendMessage(msg);
        return bean.toString();
    }
    int i = 0;
    int isSocket = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SocketConn:
                mClient = mClientManager.createClient("192.168.134.1",8001);
                isSocket = 1;
                break;
            case R.id.PortConn:
                nowChannel = ChannelFactory.getInstance(1, true,mMainHandler,this);
                openComPort(nowChannel);
                isSocket = 2;
                break;
            case R.id.CloseConn:
                try{
                    mClient.stop();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    nowChannel.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.start:
                switch (isSocket){
                    case 0:

                        break;
                    case 1:
                        mClientManager.sendMessage(mClient, ProtocolUtils.hexStringToBytes(getCmd()));
                        break;
                    case 2:
                        nowChannel.sendHex(getCmd());
                        break;
                }
                break;
        }

    }
    private void openComPort(SerialHelper ComPort) {
        Message msg = new Message();
        msg.what = CONN_ERR;
        try {
            ComPort.open();
            msg.what = CONN_OK;
            msg.obj = ComPort.getPort();
            mMainHandler.sendMessage(msg);
        } catch (SecurityException e) {
            msg.obj = "打开串口失败:没有串口读/写权限!";
            mMainHandler.sendMessage(msg);
        } catch (IOException e) {
            msg.obj = "打开串口失败:未知错误!";
            mMainHandler.sendMessage(msg);
            ;
        } catch (InvalidParameterException e) {
            msg.obj = "打开串口失败:参数错误!";
            mMainHandler.sendMessage(msg);
        }

    }

    @Override
    public void pullShortLog(String s) {

    }

    @Override
    public void pullShortLog(SpannableString spannableString) {

    }

    @Override
    public void pullWholeLog(String s) {
        log.setText(s);
    }

    @Override
    public void setValue(WhmBean whmBean) {

    }

    @Override
    public void pullWholeLog(SpannableString spannableString) {

    }

    @Override
    public void pullStatus(String s) {

    }
}
