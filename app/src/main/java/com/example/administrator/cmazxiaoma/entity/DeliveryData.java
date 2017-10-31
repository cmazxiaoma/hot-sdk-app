package com.example.administrator.cmazxiaoma.entity;

/**
 * Created by Administrator on 2017/2/18.
 */

public class DeliveryData {
    private String time;
    private String context;
    private String location;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLoction() {
        return location;
    }

    public void setLoction(String loction) {
        this.location = loction;
    }

    @Override
    public String toString() {
        return "DeliveryData{" +
                "time='" + time + '\'' +
                ", context='" + context + '\'' +
                ", loction='" + location + '\'' +
                '}';
    }
}

