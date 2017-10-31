package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.MainActivity;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.ValueEventListener;
import okhttp3.internal.Util;

/**
 * Created by Administrator on 2017/2/13.
 */

public class EmailTestActivity extends BaseActivity implements View.OnClickListener {
    private ImageView email_test_image;
    private TextView email_test_text,email_test_clock;
    private String email;
    private Button btn_welcome_cm;
    private Boolean emailVerified;
    private   BmobRealTimeData rtd;
    //设置邮箱认证的时间为120s
    private int time_test=120;
    private Timer timer;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==StaticClass.HANDLER_EMAIL_TEST_MSG){
                //关闭倒计时
                timeHandler.removeCallbacks(runnable);
                email_test_image.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_48dp);
                email_test_clock.setText("邮箱"+email+"完成激活");
                email_test_text.setText("注册成功");
                btn_welcome_cm.setVisibility(View.VISIBLE);
            }
        }
    };
    Handler timeHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time_test--;
            email_test_clock.setText("请在" + time_test + "秒内,到"+email+"邮箱完成激活");
            if(time_test>0) {
                timeHandler.postDelayed(this, 1000);
            }else if(time_test<=0){
                email_test_clock.setText("邮箱"+email+"激活失败");
                email_test_image.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_black_48dp);
                email_test_text.setText("注册失败");
               //关闭查询提交数据的用户时候验证邮箱的线程
                timer.cancel();
                //关闭倒计时
                timeHandler.removeCallbacks(runnable);
            }
    }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_test);
        initView();
        initData();
    }

    private void initView() {
        email_test_image= (ImageView) findViewById(R.id.email_test_image);
        email_test_text= (TextView) findViewById(R.id.email_test_text);
        email_test_clock= (TextView) findViewById(R.id.email_test_clock);
        btn_welcome_cm= (Button) findViewById(R.id.btn_welcome_cm);
        btn_welcome_cm.setVisibility(View.INVISIBLE);
        btn_welcome_cm.setOnClickListener(this);
    }

    private void initData() {
        email=getIntent().getExtras().getString("email");
        isEmailVerified();
        timeHandler.postDelayed(runnable,1000);
    }

    //判断用户是否激活邮箱，实际上是循环查询_User表 wherer id=email这一行数据的emailVerified的值
    public void  isEmailVerified(){
        timer=new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        String bql="select emailVerified from _User where email=?";
                        //查询
                        new BmobQuery<MyUser>().doSQLQuery(bql, new SQLQueryListener<MyUser>() {
                            @Override
                            public void done(BmobQueryResult<MyUser> bmobQueryResult, BmobException e) {
                                if(e==null){
                                    List<MyUser> list=(List<MyUser>)bmobQueryResult.getResults();
                                    if(list!=null&&list.size()>0) {
                                        for (MyUser user : list) {
                                            emailVerified = user.getEmailVerified();
                                        }
                                        if(emailVerified){
                                            mHandler.sendEmptyMessage(StaticClass.HANDLER_EMAIL_TEST_MSG);
                                            timer.cancel();
                                        }
                                    }else{
                                        UtilsLog.i("查询无数据!");
                                    }


                                }else{
                                    e.printStackTrace();
                                }
                            }
                        },email);
                    }
                }
        ,0, StaticClass.HANDLER_EMAIL_TEST_TIME);
    }

    //静止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_welcome_cm:
                startActivity(new Intent(EmailTestActivity.this, MainActivity.class));
                finish();
                break;
        }
    }
}
