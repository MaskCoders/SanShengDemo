package com.example.demo.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class CreateDBActivity extends Activity{
    private TextView mQueryText;
    private TextView mInsertText;
    private TextView mUpdateText;
    private TextView mDeleteText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_db_layout);mQueryText = (TextView) findViewById(R.id.query);
        mInsertText = (TextView) findViewById(R.id.insert);
        mUpdateText = (TextView) findViewById(R.id.update);
        mDeleteText = (TextView) findViewById(R.id.delete);
        mQueryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mQueryText");
            }
        });
        mInsertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mInsertText");
                for (int i = 0; i< 50; i++) {
                    Meter meter = new Meter(true, i);
                    meter.save(CreateDBActivity.this);
                }
            }
        });
        mUpdateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mUpdateText");
            }
        });
        mDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ssg", "mDeleteText");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
