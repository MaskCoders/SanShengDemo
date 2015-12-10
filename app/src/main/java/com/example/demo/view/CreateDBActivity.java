package com.example.demo.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.demo.util.CustomThreadPoolFactory;
import com.example.demo.mode.Meter;
import com.example.demo.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class CreateDBActivity extends BaseActivity{
    private TextView mQueryText;
    private TextView mInsertText;
    private TextView mUpdateText;
    private TextView mDeleteText;
    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("EquipmentDBThread");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar(INSERT_DATABASE_VIEW);
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
                InsertDataTask task = new InsertDataTask();
                task.executeOnExecutor(sThreadPool);
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

    private class InsertDataTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            for (int i = 0; i< 50; i++) {
                Meter meter = new Meter(true, i);
                meter.save(CreateDBActivity.this);
            }
            return null;
        }
    }
}