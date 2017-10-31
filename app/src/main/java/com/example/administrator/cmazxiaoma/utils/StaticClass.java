package com.example.administrator.cmazxiaoma.utils;

/**
 * Created by Administrator on 2017/2/10.
 * 1.存放数组和常量
 */

public class StaticClass {
    public static final String IS_FIRST_START="config";
    //闪屏页的msg
    public static final int HANDLER_SPLASH=0x123;
    //闪屏页的延迟
    public static final long HANDLER_SPALSH_SLEEPTIME=2000;
    //腾讯bugly的AppID
    public static  final String TENXUN_BUGLY_APPID="987e8bc945";
    //Bmob的ID
    public static final String BMOB_APPLICATION_ID="7e7086247264ba3492bf0a568b8a786a";
    //Bmob的KEY
    public static final String BMOB_RESET_API_KEY="615019745f840c0e1015a7b7e8c46588";
    //注册界面的3个editText 如果3个editText都不为空的MSG
    public static final int HANDLER_REGISTER_NO_NULL_EDITTEXT=0x234;
    //注册界面的3个editText 如果3个editText有一个为空的MSG
    public static final int HANDLER_REGISTER_NULL_EDITTEXT=0x345;
    //注册和登录界面的editText的监听的频率
    public static final long HANDLER_REGISTER_LOGIN_EDITTEXT_TIME=500;
    //登录界面的editText监听的msg 如果2个editText都不为空的MSG
    public static final int HANDLER_LOGIN_NO_NULL_EDITTEXT=0x456;
    //注册界面的3个editText 如果3个editText有一个为空的MSG
    public static final int HANDLER_LOGIN_NULL_EDITTEXT=0x567;
    //邮箱验证界面判断用户是否激活了邮箱，查询数据库的时间频率
    public static final long HANDLER_EMAIL_TEST_TIME=2000;
    //邮箱验证界面成功的msg
    public static final int HANDLER_EMAIL_TEST_MSG=0x678;
    //登录页，主页页 drawableright点击事件线程监听的频率
    public static final int LOGIN_REGISER_DRAWABLELEFT_TIME=500;
    public static final String  QUERY_DELIVERY_APP_KEY="d77b6eb3700653ffc08960a090b160c4";
    /**快递100的物流接口
     * "https://www.kuaidi100.com/query?type=" + comCode + "&postid=" + code + "&id=1&valicode=&temp=0.581272836541757"
     * 快递100的查询快递名字接口
     * "https://www.kuaidi100.com/autonumber/autoComNum?text="+code
     * 聚合数据的物流接口
     * *http://v.juhe.cn/exp/index?key=d77b6eb3700653ffc08960a090b160c4&com=sto&no=3323171299829*/
    public static final String QUERY_PHONE_LAND="d9923c0b96e44bf0b76b1c916331bcb5";
    /**实例 http://apis.haoservice.com/mobile?phone=13569768914&key=d9923c0b96e44bf0b76b1c916331bcb5
     * **/
    //图灵机器人APP_KEY,记住不是聚合数据的那个接口
    public static final String  TULING_APP_KEY="af49e42680eb475495438473fa133b85";
    //图灵机器人api地址
    public static final String tuling_api="http://www.tuling123.com/openapi/api";
    //聚合接口 微信精选文章 APP_KEY
    public static final String WECHAT_NEWS_APP_KEY="6ba2ac60f227341a6b49143b4774ef08";
    //聚合接口 微信精选文章 api
    public static final String wechat_news_api="http://v.juhe.cn/weixin/query";
    //科大讯飞
    public static final String XUNFEI_APP_ID=" 58abc697";
    //下拉刷新
    public static final int REFRESH_COMPLETE=0x789;
    //短信Action
    public static final String SMS_ACTION="android.provider.Telephony.SMS_RECEIVED";
    //版本更新
    public static final String CHECK_UPDATE_URL="http://192.168.155.1:8080/updatecmapp/config.json";
    //
    public static final int CHECK_UPDATE_MSG=0x8910;
    //云端连接
    public static final String  CLOURD="http://cloud.bmob.cn/e061fba3d5274736/";
}
