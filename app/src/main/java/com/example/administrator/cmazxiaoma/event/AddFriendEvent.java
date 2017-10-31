package com.example.administrator.cmazxiaoma.event;

/**
 * Created by Administrator on 2017/3/14.
 */

public class AddFriendEvent {
    int addType; //1代表查询失败，2代表添加失败，3代表添加成功
    public AddFriendEvent(int Type){
        this.addType=Type;

    }

    public int getAddType() {
        return addType;
    }
}
