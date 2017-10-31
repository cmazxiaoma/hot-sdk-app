package com.example.administrator.cmazxiaoma.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/3/10.
 * 静态内部类实现单例
 */

public class GroupDB {
    private static  final String DB_NAME="GroupRecord.db3";
    private static  final int VERSION=1;
    private SQLiteDatabase db;
    private static Context  context;


    private  GroupDB(){
        db=new GroupDatabaseHelper(context,DB_NAME,VERSION).getReadableDatabase();
    }

    public static GroupDB getInstance(Context mcontext) {
        context=mcontext;
        return  GroupDBHolder.instance;
    }

    //静态内部类确保了首次调用getInstance()的时候才会初始化GroupDBHolder,从而导致实例被创建
    //并且由JVM保证了线程的安全
    private static  class GroupDBHolder{
        private static final GroupDB instance=new GroupDB();
    }


    public void insertData(String user,String senderName,String senderHead,String text, int type,String date) {
        UtilsLog.i("数据库状态:"+db.isOpen());
        ContentValues values=new ContentValues();
        values.put("user",user);
        values.put("senderName",senderName);
        values.put("senderHead",senderHead);
        values.put("text",text);
        values.put("type",type);
        values.put("date",date);
        long rowId=db.insert("group_record",null,values);
        if(rowId!=-1)
        {
            UtilsLog.i("插入群聊天记录数据="+rowId);
        }else{
            UtilsLog.i("插入群聊天记录数据失败");
        }

    }

    public Cursor query(String username){
        UtilsLog.i("数据库状态:"+db.isOpen());
        Cursor cursor=db.rawQuery("select * from group_record where user= ?",new String[]{username});
        UtilsLog.i("curosr="+cursor);
        return  cursor;
    }

    public void close(){
        db.close();
    }
}
