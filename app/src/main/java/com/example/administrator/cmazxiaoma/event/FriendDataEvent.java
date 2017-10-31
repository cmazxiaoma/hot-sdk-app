package com.example.administrator.cmazxiaoma.event;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/1.
 */

public class FriendDataEvent implements Parcelable, Serializable {
    //对应着数据的objectId
    private String id;
    private String friendusername;
    private String sex;
    private String age;
    private String desc;
    private String friend_head;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FriendDataEvent(){}

    public FriendDataEvent(String id,String friendusername,String sex,String age,String desc,String friend_head){
        this.id=id;
        this.friendusername=friendusername;
        this.sex=sex;
        this.age=age;
        this.desc=desc;
        this.friend_head=friend_head;
    }

    public String getFriendusername() {
        return friendusername;
    }


    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getDesc() {
        return desc;
    }


    public String getFriend_head() {
        return friend_head;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(friendusername);
        dest.writeString(sex);
        dest.writeString(age);
        dest.writeString(desc);
        dest.writeString(friend_head);

    }

    public FriendDataEvent(Parcel dest){
        id=dest.readString();
        friendusername=dest.readString();
        sex=dest.readString();
        age=dest.readString();
        desc=dest.readString();
        friend_head=dest.readString();
    }



    public void setFriendusername(String friendusername) {
        this.friendusername = friendusername;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFriend_head(String friend_head) {
        this.friend_head = friend_head;
    }


    public static  final  Parcelable.Creator<FriendDataEvent> CREATOR=new Creator<FriendDataEvent>() {
        @Override
        public FriendDataEvent createFromParcel(Parcel source) {
            return new FriendDataEvent(source);
        }

        @Override
        public FriendDataEvent[] newArray(int size) {
            return new FriendDataEvent[size];
        }
    };

    @Override
    public String toString() {
        return "FriendDataEvent{" +
                "id='" + id + '\'' +
                ", friendusername='" + friendusername + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", desc='" + desc + '\'' +
                ", friend_head='" + friend_head + '\'' +
                '}';
    }
}
