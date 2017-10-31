package com.example.administrator.cmazxiaoma.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.cmazxiaoma.event.ConversationFriend;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

import nl.qbusict.cupboard.Cupboard;

/**
 * Created by Administrator on 2017/3/8.
 */

public class CupboardSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="conversation.db3";
    private static final int DATABAST_VERSION=1;

    static {
        cupboard().register(ConversationFriend.class);
    }

    public CupboardSQLiteOpenHelper(Context mcontext){
        super(mcontext,DATABASE_NAME,null,DATABAST_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
