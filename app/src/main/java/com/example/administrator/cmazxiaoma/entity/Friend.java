package com.example.administrator.cmazxiaoma.entity;

import com.example.administrator.cmazxiaoma.event.ConversationFriend;
import com.example.administrator.cmazxiaoma.event.FriendDataEvent;

import java.util.List;

import cn.bmob.newim.db.dao.UserDao;
import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/1.
 *
 * 好友的数据库表
 */

public class Friend extends BmobObject {
    private String user;
    private FriendDataEvent frined;

    public FriendDataEvent getFrined() {
        return frined;
    }

    public void setFrined(FriendDataEvent frined) {
        this.frined = frined;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
