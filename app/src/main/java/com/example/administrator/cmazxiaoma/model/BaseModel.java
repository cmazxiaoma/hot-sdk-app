package com.example.administrator.cmazxiaoma.model;

import android.content.Context;

import com.example.administrator.cmazxiaoma.application.BaseApplication;

/**
 * Created by Administrator on 2017/3/1.
 */

public abstract class BaseModel {

    public Context getContext(){
        return BaseApplication.INSTANCE();
    }
}
