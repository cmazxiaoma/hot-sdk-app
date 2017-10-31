package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.MainActivity;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.XEditText;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by Administrator on 2017/2/12.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener, XEditText.DrawableRightListener, TextWatcher {
    private TextView register_app_name;
    private Button btn_register,btn_jumpto_login;
    private  EditText register_name,register_email;
    private XEditText register_password;
    private LinearLayout register_linearlayout;
    private long exitTime=0;
    private Timer edit_timer=new Timer();
    private Timer drawablerightTimer=new Timer();
    //通知mHandler来更改注册按钮的可用状态
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==StaticClass.HANDLER_REGISTER_NO_NULL_EDITTEXT){
                btn_register.setEnabled(true);
            }else{
                btn_register.setEnabled(false);
            }
        }
    };
    //当提交数据时候，提交成功，则清空输入框里面的内容
    private Handler editHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==StaticClass.HANDLER_REGISTER_NO_NULL_EDITTEXT){
                register_password.setText("");
                register_email.setText("");
                register_name.setText("");
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
    }

    private void initView(){
        register_app_name= (TextView) findViewById(R.id.register_app_name);
        btn_jumpto_login= (Button) findViewById(R.id.btn_jumpto_login);
        btn_register= (Button) findViewById(R.id.btn_register);
        register_name= (EditText) findViewById(R.id.register_name);
        register_password= (XEditText) findViewById(R.id.register_password);
        register_email= (EditText) findViewById(R.id.register_email);
        register_linearlayout= (LinearLayout) findViewById(R.id.register_linearlayout);
        //为3个editText绑定焦点变化监听事件
        register_name.setOnFocusChangeListener(this);
        register_password.setOnFocusChangeListener(this);
        register_email.setOnFocusChangeListener(this);
        register_linearlayout.setOnTouchListener(this);
        register_password.setDrawableRightListener(this);
        register_name.addTextChangedListener(this);
        register_email.addTextChangedListener(this);
        register_password.addTextChangedListener(this);
    }

    private void initData(){
        UtilTools.setFont(this,register_app_name);
        btn_jumpto_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_jumpto_login:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
                //点击注册
            case R.id.btn_register:
                //让editText失去光标
                register_linearlayout.requestFocus();
                final String username=register_name.getText().toString().trim();//trim()去空格
                final String password=register_password.getText().toString().trim();
                UtilsLog.i("password="+password);
                final String email=register_email.getText().toString().trim();
                if(username.length()>=6&&password.length()>=6&&UtilTools.isEmail(email)){
                    new Thread(){
                        public void run(){
                            //注册
                            MyUser myuser=new MyUser();
                            myuser.setUsername(username);
                            myuser.setPassword(password);
                            myuser.setEmail(email);
                            myuser.setAge(0);
                            myuser.setDesc("");
                            myuser.setSex(true);
                            myuser.setEnable_password(password);
                            myuser.signUp(new SaveListener<MyUser>() {
                                @Override
                                public void done(MyUser myUser, BmobException e) {
                                    if(e==null){
                                        try {
                                            Thread.sleep(200);

                                        }catch(InterruptedException ee){
                                            ee.printStackTrace();
                                        }
                                        //发送消息，清空3个输入框的内容
                                        editHandler.sendEmptyMessage(StaticClass.HANDLER_REGISTER_NO_NULL_EDITTEXT);
                                        Intent intent=new Intent(RegisterActivity.this,EmailTestActivity.class);
                                        Bundle data=new Bundle();
                                        data.putString("email",email);
                                        intent.putExtras(data);
                                        startActivity(intent);
                                    }else{
                                        UtilTools.Toast(RegisterActivity.this,"注册失败!\n"+e.getMessage());
                                    }
                                }

                            });
                        }
                    }.start();
                }else{

                    if(username.length()<6)
                    {
                        UtilTools.Toast(RegisterActivity.this,"用户名不能少于6位");
                        UtilsLog.i("用户名不能少于6位");
                    }

                    if(password.length()<6)
                    {
                        UtilTools.Toast(RegisterActivity.this,"密码不能少于6位");
                        UtilsLog.i("密码不能少于6位");
                    }

                    if(!UtilTools.isEmail(email)){
                        UtilTools.Toast(RegisterActivity.this,"邮箱格式错误");
                    }
                }
                break;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            btn_jumpto_login.setVisibility(View.GONE);
        }

        else{
                btn_jumpto_login.setVisibility(View.VISIBLE);
            }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        register_linearlayout.requestFocus();
        InputMethodManager im=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(register_linearlayout.getWindowToken(),0);
        return false;
    }

    //监听editText是否有内容
    public void listenEtIsempty(final EditText editText1,final EditText editText2,final EditText editText3){
        edit_timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if(!TextUtils.isEmpty(editText1.getText().toString().trim())&&!TextUtils.isEmpty(editText2.getText().toString().trim())&&!TextUtils.isEmpty(editText3.getText().toString().trim())){
                            mHandler.sendEmptyMessage(StaticClass.HANDLER_REGISTER_NO_NULL_EDITTEXT);
                        }else{
                            mHandler.sendEmptyMessage(StaticClass.HANDLER_REGISTER_NULL_EDITTEXT);
                        }

                    }
                }
        ,0,StaticClass.HANDLER_REGISTER_LOGIN_EDITTEXT_TIME);
    }
    //手机上按back键，提示再按一次就退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                UtilTools.Toast(this, "再按一次后退键退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //监听drawRight的点击事件
    @Override
    public void onDrawableRightClick(View view) {
        if (register_password.getInputType() == 129) {//如果按右边的图标，如果密码是密文的话，就让它显示出来，就是明文
            Drawable visiable = ContextCompat.getDrawable(RegisterActivity.this, R.drawable.visiable);
            //当你改变drawableRight的图片的时候，会让 drawableLeft锁的图标没有，所以要重新设置一下锁的图标
            Drawable drawableLeft = ContextCompat.getDrawable(RegisterActivity.this, R.drawable.pwd);
            visiable.setBounds(0, 0, visiable.getMinimumWidth(), visiable.getMinimumHeight());
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            register_password.setCompoundDrawables(drawableLeft, null, visiable, null);
            register_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            UtilsLog.i("显示密码的int=" + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //将光标移到文字末尾
            register_password.setSelection(register_password.getText().length());

        } else {
            Drawable novisiable = ContextCompat.getDrawable(RegisterActivity.this, R.drawable.novisi);
            novisiable.setBounds(0, 0, novisiable.getMinimumWidth(), novisiable.getMinimumHeight());
            Drawable drawableLeft = ContextCompat.getDrawable(RegisterActivity.this, R.drawable.pwd);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            register_password.setCompoundDrawables(drawableLeft, null, novisiable, null);
            register_password.setInputType(129);
            //将光标移到文字末尾
            register_password.setSelection(register_password.getText().length());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(register_name.getText().toString().trim())&&!TextUtils.isEmpty(register_password.getText().toString().trim())&&!TextUtils.isEmpty(register_email.getText().toString().trim())){
           btn_register.setEnabled(true);
        }else{
            btn_register.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
