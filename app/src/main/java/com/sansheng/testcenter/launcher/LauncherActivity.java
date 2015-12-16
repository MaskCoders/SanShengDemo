package com.sansheng.testcenter.launcher;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;


public class LauncherActivity extends BaseActivity {
    private GridView mFunctionGridView;
    private LaucherAdapter mFunctionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);
        mFunctionGridView = (GridView) findViewById(R.id.operation_panel);
        mFunctionAdapter = new LaucherAdapter(this);
        mFunctionGridView.setAdapter(mFunctionAdapter);
        setActionBar(GENERAL_VIEW);
//        Groundy.create(ExampleTask.class)
//                .callback(this)        // required if you want to get notified of your task lifecycle
//                .arg("arg_name", "foo")       // optional
//                .queueUsing(this);
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
        return super.onOptionsItemSelected(item);
    }

}
