package com.example.administrator.cmazxiaoma.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/4.
 */

public class MsgDatabaseHelper extends SQLiteOpenHelper {

    private final String CREATE_TABLE_SQL = "create table msg_record(_id integer primary " +
            " key autoincrement , username , friendname , content , type ,date)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    public MsgDatabaseHelper(Context context, String name, int Version) {
        super(context, name, null, Version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UtilsLog.i("onUpgrade called oldVersion=" + oldVersion + "---->" + newVersion);
    }

}
