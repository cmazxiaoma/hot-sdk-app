package com.example.administrator.cmazxiaoma.entity;

/**
 * Created by Administrator on 2017/3/11.
 */

public class GroupChat {
    public static final  int TYPE_RECEIVE=0;
    public static final int  TYPE_SEND=1;
    private String friendName; //一开始可以为空,当你给对方发送消息时，这里的friendName，friendHead可以为空。因为你不知道对方的friendName，friendHead
    private String friendHead; //一开始可以为空
    private String content;
    private String date;
    private int  type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public GroupChat(String date, String friendName, String friendHead, String content, int type){
        this.date=date;
        this.friendName=friendName;
        this.friendHead=friendHead;
        this.content=content;
        this.type=type;
    }
    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendHead() {
        return friendHead;
    }

    public void setFriendHead(String friendHead) {
        this.friendHead = friendHead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
