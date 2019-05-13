package com.example.lenovo.simplemusicplayer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import com.example.lenovo.simplemusicplayer.util.constant.FileColumn;

import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * 数据库名
     */
    private static final String DATABASE_NAME="MyMusic.db";

    /**
     * 数据库版本号
     */
    private static final int DATABASE_VERSION=1;

    public static final String TABLES_TABLE_NAME="FILE_TABLE";
    private static final String DATABASE_CREATE="CREATE TABLE "+ FileColumn.TABLE+"("
            +FileColumn.ID+" integer primary key autoincrement,"
            +FileColumn.NAME+" text,"
            +FileColumn.PATH+" text,"
            +FileColumn.SORT+" integer,"
            +FileColumn.TYPE+" text)";

    /**
     * 构造方法
     * @param context
     */
    public DBHelper(Context context) {
        //创建数据库
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Locale l=new Locale("zh","CN");
//        db.setLocale(l);
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+FileColumn.TABLE);
        onCreate(db);
    }
}
