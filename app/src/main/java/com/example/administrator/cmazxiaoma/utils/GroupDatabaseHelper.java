package com.example.administrator.cmazxiaoma.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/10.
 */

public class GroupDatabaseHelper extends SQLiteOpenHelper {
    private final String  CREATE_TABLE_SQL = "create table group_record(_id integer primary" +
            " key autoincrement , user , senderName, senderHead, text , type ,date)";

    public GroupDatabaseHelper(Context mcontext,String name,int version){
        super(mcontext,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UtilsLog.i("onUpgrade oldversion="+oldVersion+"--->newVersion="+newVersion);
    }
}
