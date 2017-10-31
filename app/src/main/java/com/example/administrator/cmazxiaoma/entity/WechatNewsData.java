package com.example.administrator.cmazxiaoma.entity;

/**
 * Created by Administrator on 2017/2/20.
 * 微信精选文章实体类
 */

public class WechatNewsData {
    private String id; //id
    private String title; //标题
    private String  source; //出处
    private String firstImg; //图片
    private String  mark;
    private String  url;  //新闻地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {

        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFirstImg() {

        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

}
