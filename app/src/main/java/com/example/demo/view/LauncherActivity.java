package com.example.demo.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import com.example.demo.R;


public class LauncherActivity extends BaseActivity {
    private GridView mFunctionGridView;
    private FunctionsAdapter mFunctionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);
        mFunctionGridView = (GridView) findViewById(R.id.operation_panel);
        mFunctionAdapter = new FunctionsAdapter(this);
        mFunctionGridView.setAdapter(mFunctionAdapter);
        setActionBar(GENERAL_VIEW);
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
