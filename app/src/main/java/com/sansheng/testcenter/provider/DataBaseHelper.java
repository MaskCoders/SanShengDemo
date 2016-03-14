package com.sansheng.testcenter.provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.sansheng.testcenter.utils.Utilities;

import java.io.*;


public class DataBaseHelper extends SQLiteOpenHelper {
    private String DB_PATH = "/data/data/com.sansheng.testcenter/databases/" + EquipmentProvider.DATABASE_NAME;
    private static final String TAG = "DataBaseHelper";
    private static final int VERSION = 1;
    private final Context mContext;

    private SQLiteDatabase myDataBase;
    private int current_versionCode = 0;

    public DataBaseHelper(Context context) {
        super(context, EquipmentProvider.DATABASE_NAME, null, VERSION);
        this.mContext = context;
//        DB_PATH = context.getDatabasePath(EquipmentProvider.DATABASE_NAME).getPath();
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void copyDB(String from, String to) throws IOException {
//        String toName = new File(to, EquipmentProvider.DATABASE_NAME).getAbsolutePath();
        String fromName = from;
//        String fromName = new File(from, EquipmentProvider.DATABASE_NAME).getAbsolutePath();
        String toName = to;
        Log.e("ssg", "backup toName = " + toName);
        Log.e("ssg", "backup fromName = " + fromName);
        File file = new File(toName);
        if (file.exists()) {
            Log.d("", "数据库文件存在");
            if (file.delete()) {
                Log.d("", "删除文件成功");
            } else {
                Log.d("", "删除文件失败");
            }
        }
        InputStream myInput = new FileInputStream(fromName);
        OutputStream myOutput = new FileOutputStream(toName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    //save db to sdcard
    public void backup() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            copyDB(DB_PATH, new File(Utilities.getOutDBPath(), EquipmentProvider.DATABASE_NAME).getAbsolutePath());
            openDataBase();
            close();
        }
    }

    //update db to app
    public void update() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            copyDB(new File(Utilities.getOutDBPath(), EquipmentProvider.DATABASE_NAME).getAbsolutePath(), DB_PATH);
            openDataBase();
            close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private boolean checkDataBase() {
        boolean exist = true;
        SQLiteDatabase checkDB = null;
        checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS
                        | SQLiteDatabase.OPEN_READONLY);
        current_versionCode = checkDB.getVersion();
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null && exist;
    }

    public void openDataBase() throws SQLException {
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS
                        | SQLiteDatabase.OPEN_READWRITE);
        myDataBase.setVersion(VERSION);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }
}

