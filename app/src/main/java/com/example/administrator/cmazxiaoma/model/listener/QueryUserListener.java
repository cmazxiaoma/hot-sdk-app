package com.example.administrator.cmazxiaoma.model.listener;

import com.example.administrator.cmazxiaoma.entity.Friend;
import com.example.administrator.cmazxiaoma.entity.MyUser;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2017/3/1.
 */

public abstract class QueryUserListener extends BmobListener1<MyUser>{
    public abstract void done(MyUser myUser,BmobException e);

    @Override
    protected void postDone(MyUser myUser, BmobException e) {
        done(myUser,e);
    }
}