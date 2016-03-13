package com.sansheng.testcenter.scann;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sansheng.testcenter.R;

/**
 * Created by hua on 3/9/16.
 */
public class ScannTestActivity extends Activity{
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scantestlayout);
        tv = (TextView) findViewById(R.id.code);
        Button click = (Button) findViewById(R.id.go_scan);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannTestActivity.this,CaptureActivity.class);
                startActivityForResult(intent,0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.print("\nss");
        if(resultCode!=-1)return;
        String backStr = "scan code error";
        try{
            tv.setText(data.getStringExtra("code"));
        }catch (Exception e){
            e.printStackTrace();
            tv.setText(backStr);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
