package com.example.administrator.cmazxiaoma.event;

/**
 * Created by Administrator on 2017/3/11.
 */

public class Sender {
    private String sender;
    private String content;
    private String  head;

    public Sender(String sender,String content,String head){
        this.sender=sender;
        this.content=content;
        this.head=head;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
