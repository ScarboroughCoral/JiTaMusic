package com.example.lenovo.simplemusicplayer.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lenovo.simplemusicplayer.util.constant.FileColumn;

public class DBProvider extends ContentProvider {

    private DBHelper dbOpenHelper;
    public static final String AUTHORITY="MUSIC";
    public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+ FileColumn.TABLE);

    @Override
    public boolean onCreate() {
        dbOpenHelper=new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cur = db.query(FileColumn.TABLE,projection,selection,selectionArgs,null,null,sortOrder);
        return cur;
    }

    /**
     * 待实现
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long count=0;
        try {
            count=db.insert(FileColumn.TABLE,null,values);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBProvider","insert error");
        }

        if (count>0)
            return uri;

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        try {
            db.delete(FileColumn.TABLE,selection,selectionArgs);
            Log.i("DBProvider","delete");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBProvider","delete error");
        }
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int i=0;
        try {
            i=db.update(FileColumn.TABLE,values,selection,null);
            return i;
        }catch (Exception e){
            Log.e("DBProvider","update error");
        }
        return 0;
    }
}
