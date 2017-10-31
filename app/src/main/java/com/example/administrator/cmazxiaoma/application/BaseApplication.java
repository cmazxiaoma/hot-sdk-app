package com.example.administrator.cmazxiaoma.application;

import android.app.Application;

import com.example.administrator.cmazxiaoma.utils.DemoMessageHandler;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/2/10.
 */

public class BaseApplication extends Application{
    private static BaseApplication INSTANCE;


    public static BaseApplication INSTANCE() {
        return INSTANCE;
    }

    private static void setInstance(BaseApplication INSTANCE) {
        setBaseApplication(INSTANCE);
    }

     private static void setBaseApplication(BaseApplication a){
        BaseApplication.INSTANCE=a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.TENXUN_BUGLY_APPID, true);//第3个参数是开关
        //初始化Bmob
        Bmob.initialize(this,StaticClass.BMOB_APPLICATION_ID);
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+StaticClass.XUNFEI_APP_ID);
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
