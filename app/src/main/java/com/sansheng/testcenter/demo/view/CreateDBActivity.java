package com.sansheng.testcenter.demo.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.module.MeterData;
import hstt.data.DataItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class CreateDBActivity extends BaseActivity {
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
            }
        });
        mInsertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertDataTask task = new InsertDataTask();
                task.executeOnExecutor(sThreadPool);
            }
        });
        mUpdateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void initButtonList() {

    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setValue(DataItem bean) {

    }

    private class InsertDataTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            for (int i = 0; i< 50; i++) {
                MeterData meter = new MeterData(true, i);
                meter.save(CreateDBActivity.this);
            }
            return null;
        }
    }
}
