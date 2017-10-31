package com.example.administrator.cmazxiaoma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.example.administrator.cmazxiaoma.entity.ButlerChatData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 2017/2/11.
 */

public class UtilSharePre {

    public static final String NAME="config";

    public static void  putString(Context mcontext,String name,String value){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(name,value).apply();
    }

    //返回一个Value
    public static String getString(Context mcontext,String name,String defValue){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
         return  sp.getString(name,defValue);
    }

    public static void putInt(Context mcontext,String name,int value){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(name,value).apply();
    }

    public static Long getLong(Context mcontext,String name,Long defValue){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getLong(name,defValue);
    }


    public static void putLong(Context mcontext,String name,Long value){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putLong(name,value).apply();
    }

    public static int getInt(Context mcontext,String name,int defValue){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(name,defValue);
    }






    public static  void  putBoolean(Context mcontext,String name,Boolean value){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(name,value).apply();
    }

    public static Boolean getBoolean(Context mcontext,String name,Boolean defValue){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(name,defValue);
    }
    //删除单个键-值
    public static void del(Context mcontext,String name){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(name).apply();
    }
    //删除全部
    public static void delAll(Context mcontext){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }



    public static void  putSet(Context mcontext,String name,Set<String> mset){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putStringSet(name,mset).apply();
    }

    //返回一个Value
    public static Set<String> getSet(Context mcontext, String name, Set<String> mset){
        SharedPreferences sp=mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return  sp.getStringSet(name,mset);
    }

}
