package com.example.administrator.cmazxiaoma.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.MainActivity;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilSharePre;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/2/11.
 * 描述：闪屏页
 * 步骤：1.先延迟2000ms
 * 2.判断程序时候第一次运行
 * 3.自定义字体
 * 4.SplashActivity设置全屏主题
 */

public class SplashActivity extends AppCompatActivity {
    private TextView spalsh_text;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否第一次运行
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                    }else{
                        //如果本地有缓冲用户
                        MyUser user=BmobUser.getCurrentUser(MyUser.class);
                        if(user != null){
                            // 允许用户使用应用
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                            UtilsLog.i("currentUser="+user+",id="+user.getObjectId()+",username="+user.getUsername());
                        }else{
                            //缓存用户对象为空时， 可打开用户注册界面…
                            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        }
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();

    }

     //初始化View
    private void initView(){
        spalsh_text= (TextView) findViewById(R.id.splash_text);
        //调用工具包设置字体的方法
        UtilTools.setFont(this,spalsh_text);
        /**延迟, 记得用sendEmptyMessageDelayed

         */
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,StaticClass.HANDLER_SPALSH_SLEEPTIME);
    }

    //是否第一次运行程序
    public Boolean isFirst(){
        Boolean isFirst= UtilSharePre.getBoolean(this, StaticClass.IS_FIRST_START,true);
        if(isFirst){
            UtilSharePre.putBoolean(this, StaticClass.IS_FIRST_START,false);
            return true;
        }
        else{
            return false;
        }
    }
    //禁止返回键的方法
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

}
