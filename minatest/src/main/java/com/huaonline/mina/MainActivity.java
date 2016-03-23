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
    Button clientsend;
    Button serversend;
    MinaTimeClient client;
    MinaTimeServer server;
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
        clientsend = (Button) findViewById(R.id.clientsend);
        serversend = (Button) findViewById(R.id.serversend);
        stopserver.setOnClickListener(this);
        stopclient.setOnClickListener(this);
        clientsend.setOnClickListener(this);
        serversend.setOnClickListener(this);
        client = new MinaTimeClient();
        server = new MinaTimeServer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.runserver:
                server.runServer();
                break;
            case R.id.runclient:
                client.runClient();
                break;
            case R.id.stopserver:
                server.close();
                break;
            case R.id.stopclient:
                client.closeClient();
                break;
            case R.id.clientsend:
                client.sendMessage();
                break;
            case R.id.serversend:
                server.sendMessage("xxxx");
                break;
        }

    }
}
