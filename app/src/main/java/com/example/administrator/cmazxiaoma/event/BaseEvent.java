package com.example.administrator.cmazxiaoma.event;

/**
 * Created by Administrator on 2017/3/5.
 */

public class BaseEvent {
    //唯一的标识符
    private String text;

    //int类型的的msg
    private int int_msg;


    //String类型的 msg
    private String string_msg;


    public BaseEvent(String text, int int_msg, String string_msg){
        this.text=text;
        this.int_msg=int_msg;
        this.string_msg=string_msg;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getInt_msg() {
        return int_msg;
    }

    public void setInt_msg(int int_msg) {
        this.int_msg = int_msg;
    }

    public String getString_msg() {
        return string_msg;
    }

    public void setString_msg(String string_msg) {
        this.string_msg = string_msg;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "text='" + text + '\'' +
                ", int_msg=" + int_msg +
                ", string_msg='" + string_msg + '\'' +
                '}';
    }
}
