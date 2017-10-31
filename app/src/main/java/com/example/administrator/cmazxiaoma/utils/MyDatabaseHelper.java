package com.example.administrator.cmazxiaoma.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final String CREATE_TABLE_SQL = "create table user_butler_chat_record(_id integer primary" +
            " key autoincrement , user, text , type)";


    public MyDatabaseHelper(Context mcontext, String name, int version) {
        super(mcontext, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //第一次使用数据库时自动创建表
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("-----onUpdate Called----" + oldVersion + "---->" + newVersion);
    }


}
