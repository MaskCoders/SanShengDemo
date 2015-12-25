package com.sansheng.testcenter.collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 12/25/15.
 * collect scene information
 */
public class CollectionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_activity_layout);
        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(CollectionActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(CollectionActivity.this, TakePhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
