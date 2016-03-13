package com.huaonline.poitest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    Button writeBtn;
    Button readBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        writeBtn = (Button) findViewById(R.id.writebtn);
        readBtn = (Button) findViewById(R.id.readbtn);
        writeBtn.setOnClickListener(this);
        readBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writebtn:
//                ExcelWriter.WriterTest();
                break;
            case R.id.readbtn:
                ExcelReader.ReaderTet();
                break;
        }

    }
}
