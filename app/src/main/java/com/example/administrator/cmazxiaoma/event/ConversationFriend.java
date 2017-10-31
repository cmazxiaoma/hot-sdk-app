package com.example.administrator.cmazxiaoma.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrator.cmazxiaoma.entity.MyUser;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by Administrator on 2017/3/4.
 */

public class ConversationFriend implements Parcelable{
    private Long _id; //for cupborad
    private String user; //这是归登录者所有
    private String friendId; //也可以是群id
    private String friendHead; //也可以是群头像
    private String last_date;
    private String last_msg;
    private String  friendUsername;//朋友的用户名是唯一id
    public static final int FRIEND_CHAT=0;
    public static final int GROUP_CHAT=1;
    private int type; //根据type就能知道是单聊 还是群聊

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ConversationFriend(){

    }

    public void setFriendHead(String friendHead) {
        this.friendHead = friendHead;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }



    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }



    public ConversationFriend(String user,String friendId,String friendUsername, String friendHead, String last_msg, String last_date,int type){
        this.user=user;
        this.friendId=friendId;
        this.friendHead=friendHead;
        this.friendUsername=friendUsername;
        this.last_msg=last_msg;
        this.last_date=last_date;
        this.type=type;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFriendHead() {
        return friendHead;
    }

    public String getLast_date() {
        return last_date;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public ConversationFriend(Parcel dest){
        user=dest.readString();
        friendId=dest.readString();
        friendHead=dest.readString();
        last_date=dest.readString();
        last_msg=dest.readString();
        friendUsername=dest.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(friendId);
        dest.writeString(friendHead);
        dest.writeString(last_date);
        dest.writeString(last_msg);
        dest.writeString(friendUsername);
    }

    public static final Parcelable.Creator<ConversationFriend> CREATOR=new Creator<ConversationFriend>() {
        @Override
        public ConversationFriend createFromParcel(Parcel source) {
            return new ConversationFriend((source));
        }

        @Override
        public ConversationFriend[] newArray(int size) {
            return new ConversationFriend[size];
        }
    };

    @Override
    public String toString() {
        return "ConversationFriend{" +
                "_id=" + _id +
                ", user='" + user + '\'' +
                ", friendId='" + friendId + '\'' +
                ", friendHead='" + friendHead + '\'' +
                ", last_date='" + last_date + '\'' +
                ", last_msg='" + last_msg + '\'' +
                ", friendUsername='" + friendUsername + '\'' +
                ", type=" + type +
                '}';
    }
}

