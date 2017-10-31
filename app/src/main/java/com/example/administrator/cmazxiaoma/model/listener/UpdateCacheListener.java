package com.example.administrator.cmazxiaoma.model.listener;

import com.example.administrator.cmazxiaoma.entity.MyUser;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2017/3/1.
 * 更新缓冲监听器
 */

public abstract class UpdateCacheListener extends BmobListener1<MyUser>{

    public abstract  void done(BmobException e);
    @Override
    protected void postDone(MyUser myUser, BmobException e) {
        done(e);

    }
}
