package com.huaonline.mina;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {
    Button runserver;
    Button runclient;
    Button stopserver;
    Button stopclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runserver = (Button) findViewById(R.id.runserver);
        runclient = (Button) findViewById(R.id.runclient);
        runserver.setOnClickListener(this);
        runclient.setOnClickListener(this);
        stopserver = (Button) findViewById(R.id.stopserver);
        stopclient = (Button) findViewById(R.id.stopclient);
        stopserver.setOnClickListener(this);
        stopclient.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.runserver:
                MinaTimeServer.runServer();
                break;
            case R.id.runclient:
                MinaTimeClient.runClient();
                break;
            case R.id.stopserver:

                break;
            case R.id.stopclient:
                break;
        }

    }
}
