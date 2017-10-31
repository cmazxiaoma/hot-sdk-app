package com.example.administrator.cmazxiaoma.event;

import com.example.administrator.cmazxiaoma.entity.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/9.
 */

public class GroupConversation extends BmobObject {
    private MyUser sender;
    private String  msg;
    private String date;

    public  GroupConversation(){

    }


    public  GroupConversation(MyUser sender,String msg,String date){
        this.sender=sender;
        this.msg=msg;
        this.date=date;

    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public MyUser getSender() {
        return sender;
    }

    public void setSender(MyUser sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GroupConversation{" +
                "sender=" + sender +
                ", msg='" + msg + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
