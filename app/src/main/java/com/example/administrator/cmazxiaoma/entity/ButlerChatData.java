package com.example.administrator.cmazxiaoma.entity;

/**
 * Created by Administrator on 2017/2/19.
 * 机器人聊天界面对话的实体类
 */

public class ButlerChatData {
    //type,区分是左边，还是右边'
    private int type;
    //聊天文本
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ButlerChatData{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
