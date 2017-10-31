package com.example.administrator.cmazxiaoma.utils;

import com.example.administrator.cmazxiaoma.entity.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/3/14.
 */

public class CurrentUser {

    private CurrentUser(){}


    public static CurrentUser getInstance(){
        return CurrentUserHolder.instance;
    }

   private  static class  CurrentUserHolder{
       private static final CurrentUser instance=new CurrentUser();
    }

    public BmobUser getCurrentUser(){
        return BmobUser.getCurrentUser(MyUser.class);
    }

    public String  getCurrentName(){
        return getCurrentUser().getUsername();
    }


}
