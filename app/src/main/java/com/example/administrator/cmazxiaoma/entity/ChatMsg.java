package com.example.administrator.cmazxiaoma.entity;

/**
 * Created by Administrator on 2017/3/3.
 * 单聊的消息实体类
 */

public class ChatMsg {
    public static final  int TYPE_RECEIVE=0;
    public static final int  TYPE_SEND=1;
    private String date;
    private String content;
    private int type;


    public ChatMsg(String date,int type, String content){
        this.date=date;
        this.type=type;
        this.content=content;
    }



    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }
}
