package com.example.administrator.cmazxiaoma.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/2/13.
 * 用户属性
 */

public class MyUser extends BmobUser{
    private int age;
    private boolean sex;//true为男，false为女
    private String desc;
    private String enable_password;
    private String user_img;
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnable_password() {
        return enable_password;
    }

    public void setEnable_password(String enable_password) {
        this.enable_password = enable_password;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }
}
