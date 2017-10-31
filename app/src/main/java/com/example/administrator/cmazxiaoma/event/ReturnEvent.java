package com.example.administrator.cmazxiaoma.event;

/**
 * Created by Administrator on 2017/3/4.
 */

public class ReturnEvent  {
    private int code;

    public int getCode() {
        return code;
    }

    public ReturnEvent(int requestcode){
        code=requestcode;
    }

    @Override
    public String toString() {
        return "ReturnEvent{" +
                "code=" + code +
                '}';
    }
}
