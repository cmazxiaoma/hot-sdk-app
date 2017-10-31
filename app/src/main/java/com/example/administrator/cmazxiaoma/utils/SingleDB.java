package com.example.administrator.cmazxiaoma.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/3/11.
 *
 */

public class SingleDB {
    //使用volatile关键字,它能确保instance变量每次都直接从主内存（而不是寄存器)中加载最新赋值
    private volatile  static SingleDB instance=null;
    private static final int VERSION=1;
    private SQLiteDatabase db;
    private static  final String DB_NAME="msg_record.dp3";

    private SingleDB(Context mcontext){
        db=new MsgDatabaseHelper(mcontext,DB_NAME,VERSION).getReadableDatabase();
    }

    public static SingleDB getInstance(Context mocntext) {
        if(instance==null){
            synchronized(SingleDB.class){
                if(instance==null){
                    instance=new SingleDB(mocntext);
                }
            }
        }
        return instance;
    }

    public void insert(String username,String friendName,String content,int type,String date){
        ContentValues values=new ContentValues();
        values.put("username",username);
        values.put("friendname",friendName);
        values.put("content",content);
        values.put("type",type);
        values.put("date",date);
        long rowId=db.insert("msg_record",null,values);
        if(rowId!=-1)
        {
            UtilsLog.i("插入聊天记录数据="+rowId);
        }else{
            UtilsLog.i("插入聊天记录数据失败");
        }
    }


    public Cursor query(String username,String friendName){
        Cursor cursor=db.rawQuery("select * from msg_record where username= ? and friendname= ? ",new String[]{username,friendName});
        return cursor;
    }

    public void close(){
        db.close();
    }
}
