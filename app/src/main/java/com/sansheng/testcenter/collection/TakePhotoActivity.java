package com.sansheng.testcenter.collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.utils.Utility;

import java.io.File;

/**
 * Created by sunshaogang on 12/25/15.
 * take picture
 */
public class TakePhotoActivity extends Activity {
    public Uri photoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo_layout);
        findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CollectionUtilies.START_CAMERA);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CollectionUtilies.START_CAMERA) {
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
//            Bitmap bmp = BitmapFactory.decodeFile(path);
                Log.e("ssg", "the path is :" + path);
            }

        } else {
            Utility.showToast(this, "请重新选择图片");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 自定义文件名
     * @param context
     */
    private void takePhoto(Context context) {
        String fileName = CollectionUtilies.generateRandomString(CollectionUtilies.RANDOM_FILE_NAME_LENGTH) + ".jpg";
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath());
        if (dir != null) {
            File imageFile = new File(dir.getAbsolutePath(), fileName);
            Uri imageFileUri = Uri.fromFile(imageFile);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
            photoUri = imageFileUri;
            startActivityForResult(intent, CollectionUtilies.START_CAMERA);
        }
    }
}
