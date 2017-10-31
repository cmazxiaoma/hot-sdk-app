package com.example.administrator.cmazxiaoma.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.DispatchLinearLayout;

/**
 * Created by Administrator on 2017/2/27.
 */

public class SmsService extends Service{
    private SmsReceiver smsReceiver;
    //短信手机号，内容
    private String smsPhone,smsContent;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutparams;
    private DispatchLinearLayout mView;
    private LinearLayout sms_reply;
    private TextView sms_phone,sms_content;
    public static final String SYSTEM_DIALOGS_REASON_KEY="reason";
    public static final String SYSTEM_DIALOGS_REASON_HOME_KEY="homekey";
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilsLog.i("Service onDestroy");
        //注销注册
        unregisterReceiver(smsReceiver);
    }


    //初始化
    @Override
    public void onCreate() {
        super.onCreate();
        UtilsLog.i("SmsService onCreate");
        init();
    }

    private void init() {
        //动态广播注册
        smsReceiver=new SmsReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(StaticClass.SMS_ACTION);
        //设置优先级
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        registerReceiver(smsReceiver,intentFilter);
        //给home键注册广播
        HomeWatchReceiver homeWatchReceiver=new HomeWatchReceiver();
        IntentFilter intentFilter1=new IntentFilter();
        intentFilter1.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); //关闭系统的dialog
        registerReceiver(homeWatchReceiver,intentFilter1);

    }


    /**短信广播*/
    public class SmsReceiver extends BroadcastReceiver implements View.OnClickListener, DispatchLinearLayout.DispatchKeyEventListener {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            //说明来短信了
            if(action.equals(StaticClass.SMS_ACTION)){
                UtilsLog.i("来短信了");
                //获取短信内容返回的是一个Object数组
               Object[] obj= (Object[]) intent.getExtras().get("pdus");
                //遍历数组
                for(Object object:obj){
                    //把数组元素转换成短信对象
                    SmsMessage sms=SmsMessage.createFromPdu((byte[]) object);
                    smsPhone=sms.getOriginatingAddress();
                    smsContent=sms.getMessageBody();
                    UtilsLog.i("smsPhone="+smsPhone+",smsContent="+smsContent);
                    //将手机号，短信内容以窗体的形式来提示
                    showWindow();
                }
            }

        }

        private void showWindow() {
            try {
                //获取WINDOW_SERVICE
                wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                //获取布局参数
                layoutparams = new WindowManager.LayoutParams();
                //定义宽和高
                layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutparams.height = WindowManager.LayoutParams.MATCH_PARENT;
                //定义标记
                layoutparams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
                //定义格式
                layoutparams.format = PixelFormat.TRANSLUCENT;
                //定义类型
                layoutparams.type = WindowManager.LayoutParams.TYPE_PHONE;
                //加载布局
                mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);
                sms_reply = (LinearLayout) mView.findViewById(R.id.sms_reply);
                sms_phone = (TextView) mView.findViewById(R.id.sms_phone);
                sms_content = (TextView) mView.findViewById(R.id.sms_content);
                sms_reply.setOnClickListener(this);
                sms_phone.setText(smsPhone);
                sms_content.setText(smsContent);
                //添加mView到窗口
                wm.addView(mView, layoutparams);
                mView.setDispatchKeyEventListener(this);

            }catch (Exception e){
                e.printStackTrace();
                UtilsLog.i("短信提示窗口发送异常");
            }
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.sms_reply:
                    //回复
                    sendSms();
                    break;
            }
        }

        private void sendSms() {
            UtilsLog.i("回复");
            Uri uri=Uri.parse("smsto:"+ smsPhone);
            Intent intent=new Intent(Intent.ACTION_SENDTO, uri);//记得是sendto，不是send
            //设置启动模式
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("sms_body","");
            startActivity(intent);
            if(mView.getParent()!=null){
                wm.removeView(mView);
            }
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否按返回键
            if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
                UtilsLog.i("我按了back键");
                if(mView.getParent()!=null){
                    wm.removeView(mView);
                }
                return true;
            }
            return false;
        }
    }


    //监听home键的广播
    private class HomeWatchReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason=intent.getStringExtra(SYSTEM_DIALOGS_REASON_KEY);
                if(reason.equals(SYSTEM_DIALOGS_REASON_HOME_KEY)){
                    UtilsLog.i("点击了HOME键盘");
                    if(mView.getParent()!=null){
                        wm.removeView(mView);
                    }
                }

            }

        }
    }
}
