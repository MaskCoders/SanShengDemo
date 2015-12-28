package com.sansheng.testcenter.collection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.DialogFragmentCallback;
import com.sansheng.testcenter.base.view.ComposeDialogFragmentFactory;
import com.sansheng.testcenter.utils.Utilities;
import com.sansheng.testcenter.utils.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunshaogang on 12/25/15.
 * collect scene information
 */
public class CollectionActivity extends Activity {

    public static String TAG = "CollectionActivity";
    private Uri photoUri;
    private File photoImage;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 201;
    public static final int SELECT_IMAGE_FROM_GALLERY = 202;

    public final static String SANSHENG_FOLDER = "SanSheng";
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
                selectPicture();
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setClass(CollectionActivity.this, TakePhotoActivity.class);
//                startActivity(intent);
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
            Uri uri = null;
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                uri = photoUri;
//                try {
//                    String a = MediaStore.Images.Media.insertImage(getContentResolver(), photoImage.getAbsolutePath(),uri.getLastPathSegment(),"");
//                    Log.e("ssg", "a = " + a);
//                    uri = Uri.parse(a);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                msc.connect();



//                String[] proj = {MediaStore.Images.Media.DATA};
//                Cursor cursor = managedQuery(uri, proj, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                String path = cursor.getString(column_index);
//                Log.e("ssg", "db path = " + path);
            } else if (requestCode == SELECT_IMAGE_FROM_GALLERY) {
                uri = data.getData();
            }
            //TODO:已经得到想要的URI和path，(ˇˍˇ） 想怎么处理都可以。
            String path = Utilities.getPath(this, uri);
            Log.e("ssg", "uri = " + uri);
            Log.e("ssg", "the path is :" + path);
            Log.e("ssg", "the photoImage is :" + photoImage);
            Log.e("ssg", "the photoUri is :" + photoUri);
//            String[] proj = {MediaStore.Images.Media.DATA};
//            Cursor cursor = managedQuery(uri, proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String path = cursor.getString(column_index);
////            Bitmap bmp = BitmapFactory.decodeFile(path);
//            Log.e("ssg", "the query path is :" + path);
        } else {
            Utility.showToast(this, "请重新选择图片");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 自定义文件名
     *
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

    private void selectPicture() {
        DialogFragmentCallback callback = new DialogFragmentCallback(this) {

            @Override
            public void onItemSelected(int pos) {
                Intent intent = null;
                if (pos == 0) {
                    //系统接口
                    Log.e("ssg", "版本 =  " + Build.VERSION.SDK_INT);
//                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
//                    } else {
//                        intent = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    }
                    startActivityForResult(intent, SELECT_IMAGE_FROM_GALLERY);

                } else if (pos == 1) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri = getImageFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else {
                    Log.w(TAG, "unknown position: " + pos);
                }
            }
        };
        Dialog df = ComposeDialogFragmentFactory.getInstance().createSelectImageDialog(this, callback);
        df.show();
    }

    private Uri getImageFileUri() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File mediaStorageDir = getSanShengPicFolder();
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            photoImage = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            photoUri = Uri.fromFile(photoImage);
            return photoUri;
        }
        return null;
    }
    final MediaScannerConnection msc = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {
        public void onMediaScannerConnected() {
            msc.scanFile(getSanShengPicFolder().getAbsolutePath(), "image/*");
        }
        public void onScanCompleted(String path, Uri uri) {

            msc.disconnect();
        }
    });

    public static File getSanShengPicFolder(){
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), SANSHENG_FOLDER);
    }
}
