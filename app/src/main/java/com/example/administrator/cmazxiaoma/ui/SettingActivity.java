package com.example.administrator.cmazxiaoma.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.service.SmsService;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilSharePre;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import cn.bmob.v3.a.a.This;


/**
 * Created by Administrator on 2017/2/11.
 * 描述：设置页
 */

public class SettingActivity extends  BaseActivity implements View.OnClickListener {
    private LinearLayout user_safety,update_app;
    //语音播报开关
    private Switch butler_switch,sms_switch;
    private String versionName;
    private  int    versionCode;
    private String CurrentversionName,content,url;
    private int CurrentversionCode;
    private ImageView update_img;
    private TextView update_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initEvent();
    }

    private void initEvent() {
        getVersionCode();
        isCheckedUpdate();
    }

    private  Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==StaticClass.CHECK_UPDATE_MSG){
                update_img.setImageResource(R.drawable.ic_new);
                update_text.setText(getString(R.string.updateapp_true));
                UtilsLog.i("content="+content+",url="+url);
            }
        }
    };
    private void  isCheckedUpdate() {
        HttpParams params=new HttpParams();
        params.put("Charset","utf-8");
        RxVolley.get(StaticClass.CHECK_UPDATE_URL,params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                UtilsLog.i("app版本更新:"+t);
                try {
                    JSONObject object=new JSONObject(t);
                    versionName=object.getString("versionName");
                    versionCode=object.getInt("versionCode");
                    String messy_content=object.getString("content");
                    try {
                        //将乱码解码
                        content= URLDecoder.decode(messy_content,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    url=object.getString("url");
                    UtilsLog.i("versionName="+versionName+",versionCode="+versionCode);
                    if(versionCode>CurrentversionCode){
                        mHandler.sendEmptyMessage(StaticClass.CHECK_UPDATE_MSG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initView() {
        update_img= (ImageView) findViewById(R.id.update_app_img);
        update_text= (TextView) findViewById(R.id.update_app_text);
        update_app= (LinearLayout) findViewById(R.id.update_app);
        update_app.setOnClickListener(this);
        user_safety= (LinearLayout) findViewById(R.id.user_safety);
        user_safety.setOnClickListener(this);
        butler_switch= (Switch) findViewById(R.id.butler_broadcast_switch);
        butler_switch.setOnClickListener(this);
        sms_switch= (Switch) findViewById(R.id.butler_sms_switch);
        sms_switch.setOnClickListener(this);
        boolean isChecked=UtilSharePre.getBoolean(this,"butler_switch",false);
        butler_switch.setChecked(isChecked);
        boolean isChecked_sms=UtilSharePre.getBoolean(this,"sms_switch",false);
        sms_switch.setChecked(isChecked_sms);
        if(isChecked_sms){
            startService(new Intent(this,SmsService.class));
        }else{
            stopService(new Intent(this,SmsService.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_safety:
                startActivity(new Intent(SettingActivity.this,UserSafeActivity.class));
                break;
            case R.id.butler_broadcast_switch:
                //切换选项卡
                UtilsLog.i("机器人语音="+butler_switch.isSelected());
                butler_switch.setSelected(!butler_switch.isSelected());
                //保存状态
                UtilSharePre.putBoolean(this,"butler_switch",butler_switch.isChecked());
                break;
            case R.id.butler_sms_switch:
                //老规则，切换选项卡
                UtilsLog.i("短信选项卡:"+sms_switch.isSelected());
                sms_switch.setSelected(!sms_switch.isSelected());
                //保存状态
                UtilSharePre.putBoolean(this,"sms_switch",sms_switch.isChecked());
                //如果选中，就开始监听功能
                if(sms_switch.isChecked()){
                    startService(new Intent(this,SmsService.class));
                }//如果false,取消短信监听
                else{
                    stopService(new Intent(this,SmsService.class));
                }
                break;
            case R.id.update_app:
                /**
                 * 1、请求服务器的配置文件，拿出code
                 * 2.比较本地和服务器上面的版本号
                 * 3.dialog提示
                 * 4.跳转到界面，并且把url传递进去
                 *
                 */
                //说明要更新
                if(versionCode>CurrentversionCode){
                    showUpdateDialog();

                }else{
                    UtilTools.setAlertDialog(this,"当前已经是最新版本");
                }
                break;

        }
    }

    private void showUpdateDialog() {
        new AlertDialog.Builder(this)
                .setTitle("沉梦app有新版本")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SettingActivity.this,UpdateAppActivity.class).putExtra("url",url));
                    }
                })
                .setNegativeButton("取消",null)//什么也不做，也会执行dismiss();
                .create()
                .show();

    }

    //获取版本号code
    protected  void getVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo=pm.getPackageInfo(getPackageName(),0);
            CurrentversionName=packageInfo.versionName;
            CurrentversionCode=packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
