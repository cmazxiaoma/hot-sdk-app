package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.MainActivity;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilSharePre;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.CustomDialog;
import com.example.administrator.cmazxiaoma.view.XEditText;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.internal.Util;

/**
 * Created by Administrator on 2017/2/12.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener, XEditText.DrawableRightListener, TextWatcher {
    private Button btn_jumpto_resiger,btn_login,btn_forget_password;
    private EditText login_name;
    private EditText login_password;
    private TextView app_name,login_other_text;
    private CheckBox rem_password;
    private LinearLayout login_linearlayout;
    private long exitTime=0;
    private Timer drawablerightTimer=new Timer();
    //自定义dialog,一个登录的，一个忘记密码的
    private  CustomDialog customDialog,dialog_forget_password;
    private  TextInputLayout usernameWrapper,passwordWrapper;
    private Button dialog_forget_password_reset,dialog_forget_password_phone_login,dialog_forget_password_cancel;
    private Timer edit_timer=new Timer();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==StaticClass.HANDLER_LOGIN_NO_NULL_EDITTEXT){
                btn_login.setEnabled(true);
            }else{
                btn_login.setEnabled(false);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }
    private  void initView(){
        usernameWrapper= (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper= (TextInputLayout) findViewById(R.id.passwordWrapper);
        usernameWrapper.setHint(getResources().getString(R.string.loginName));
        passwordWrapper.setHint(getResources().getString(R.string.password));
        passwordWrapper.setPasswordVisibilityToggleDrawable(R.drawable.visiable);
        dialog_forget_password=new CustomDialog(this, WindowManager.LayoutParams.MATCH_PARENT,345,R.layout.dialog_forget_password,R.style.dialog_Theme,Gravity.BOTTOM,R.style.pop_anim_style);
        dialog_forget_password.setCanceledOnTouchOutside(true);
        dialog_forget_password_reset= (Button)dialog_forget_password.findViewById(R.id.forget_password_reset);
        dialog_forget_password_phone_login= (Button)dialog_forget_password. findViewById(R.id.forget_password_phone_login);
        dialog_forget_password_cancel= (Button)dialog_forget_password.findViewById(R.id.forget_password_cancel);
        dialog_forget_password_reset.setOnClickListener(this);
        dialog_forget_password_phone_login.setOnClickListener(this);
        dialog_forget_password_cancel.setOnClickListener(this);
        btn_jumpto_resiger= (Button) findViewById(R.id.btn_jumpto_register);
        btn_login= (Button) findViewById(R.id.btn_login);
        btn_forget_password= (Button) findViewById(R.id.forget_password);
        login_name= (EditText) findViewById(R.id.login_name);
        login_password= (EditText) findViewById(R.id.login_password);
        app_name= (TextView) findViewById(R.id.app_name);
        rem_password= (CheckBox) findViewById(R.id.rem_password);
        login_other_text= (TextView) findViewById(R.id.login_other_text);
        login_linearlayout= (LinearLayout) findViewById(R.id.login_relativelayout);
        //为2个editText绑定焦点变化监听事件
        login_name.setOnFocusChangeListener(this);
        login_password.setOnFocusChangeListener(this);
        login_linearlayout.setOnTouchListener(this);
        rem_password= (CheckBox) findViewById(R.id.rem_password);
        //监听drawableRight的点击事件
        //login_password.setDrawableRightListener(this);
        //监听EditText的内容改变事件
        login_name.addTextChangedListener(this);
        login_password.addTextChangedListener(this);
        btn_forget_password.setVisibility(View.GONE);

    }


    private void initData(){
        UtilTools.setFont(this,app_name);
        btn_jumpto_resiger.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_forget_password.setOnClickListener(this);
        //获取本地存储的账号密码， 就是勾选了记住密码的选中框，然后登录的用户的账号密码
        rem_password.setChecked(UtilSharePre.getBoolean(LoginActivity.this,"rem_password",false));
        login_name.setText(UtilSharePre.getString(LoginActivity.this,"username",""));
        login_password.setText(UtilSharePre.getString(LoginActivity.this,"password",""));
        customDialog=new CustomDialog(this,360,240,R.layout.dialog_loding,R.style.dialog_Theme, Gravity.CENTER,R.style.pop_anim_style);
        //如果登录不成功，就可以点击屏幕外可以按返回键取消登录
        customDialog.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jumpto_register:
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;
            case R.id.btn_login:
                final String username=login_name.getText().toString().trim();
                final String password=login_password.getText().toString().trim();
                MyUser user=new MyUser();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmailVerified(true);
                customDialog.show();
                user.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if(e==null){
                          if(myUser.getEmailVerified()){
                              customDialog.dismiss();
                              //登录成功后，如果记住密码勾选，那么保存正确的账号密码，否则不保存密码
                             // 保存勾选框的状态
                              UtilSharePre.putBoolean(LoginActivity.this,"rem_password",rem_password.isChecked());
                              if(rem_password.isChecked()){
                                  //保存账号，密码
                                  UtilSharePre.putString(LoginActivity.this,username+"username",username);
                                  UtilSharePre.putString(LoginActivity.this,username+"password",password);
                                  UtilSharePre.putString(LoginActivity.this,"username",username);
                                  UtilSharePre.putString(LoginActivity.this,"password",password);

                              }else{
                                  //清除账号，密码
                                  UtilSharePre.del(LoginActivity.this,"password");
                                  UtilSharePre.del(LoginActivity.this,"username"+password);

                              }
                              UtilTools.Toast(LoginActivity.this,"登录成功");
                              startActivity(new Intent(LoginActivity.this, MainActivity.class));
                          }else{
                              customDialog.dismiss();
                              UtilTools.Toast(LoginActivity.this,"该用户未认证邮箱,请尽快验证");
                              final String email=myUser.getEmail();
                              //然后再发送邮箱认证邮件，再认证一次
                              MyUser.requestEmailVerify(email, new UpdateListener() {
                                  @Override
                                  public void done(BmobException e) {
                                      if(e==null){
                                          //发送验证邮件成功
                                          UtilTools.Toast(LoginActivity.this,"发送激活邮件成功");
                                          startActivity(new Intent(LoginActivity.this,EmailTestActivity.class).putExtra("email",email));

                                      }else{
                                          UtilTools.Toast(LoginActivity.this,"发送激活邮件失败");
                                          UtilsLog.i("发送验证邮件失败="+e.getMessage());
                                      }
                                  }
                              });
                              new Intent(LoginActivity.this,EmailTestActivity.class).putExtra("email",email);

                          }
                        }else{
                            customDialog.dismiss();
                            UtilTools.Toast(LoginActivity.this,"登录失败:"+e.getMessage());
                            UtilsLog.i("登录失败,原因="+e.getMessage());

                        }
                    }
                });
                break;
            case R.id.forget_password:
                app_name.setVisibility(View.VISIBLE);
                dialog_forget_password.show();
                break;
            case R.id.forget_password_cancel:
                dialog_forget_password.dismiss();
                break;
            case R.id.forget_password_reset:
                dialog_forget_password.dismiss();
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                break;
        }
    }
    //监听editText的焦点状态改变，如果获取焦点，就隐藏下面的布局
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            btn_jumpto_resiger.setVisibility(View.GONE);
            login_other_text.setVisibility(View.GONE);
            btn_forget_password.setVisibility(View.VISIBLE);
            app_name.setVisibility(View.GONE);
        }else{
            btn_jumpto_resiger.setVisibility(View.VISIBLE);
            login_other_text.setVisibility(View.VISIBLE);
            btn_forget_password.setVisibility(View.GONE);
            app_name.setVisibility(View.VISIBLE);
            //app_name.setAnimation(AnimationUtils.loadAnimation(this,R.anim.text_in));
        }
    }
    //点屏幕的其他地方，让editText失去焦点，并且隐藏输入法键盘
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //让editText所属于的父类控件的其他位置获取焦点
        login_linearlayout.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        /**隐藏键盘，如果当前没有显示出来则显示出来，如果显示出来，那么就隐藏
         *imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
         * 这个方法不符合要求，当我们点击其他地方，会弹出键盘。不符合实际开发
         *  */
        imm.hideSoftInputFromWindow(login_linearlayout.getWindowToken(), 0);
        return false;
    }

    //监听editText是否有内容
    public void listenEtIsempty(final EditText editText1,final EditText editText2){
        edit_timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if(!TextUtils.isEmpty(editText1.getText().toString().trim())&&!TextUtils.isEmpty(editText2.getText().toString().trim())){
                            mHandler.sendEmptyMessage(StaticClass.HANDLER_LOGIN_NO_NULL_EDITTEXT);
                        }else{
                            mHandler.sendEmptyMessage(StaticClass.HANDLER_LOGIN_NULL_EDITTEXT);
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

    //drawableLeft和drawableRight的点击事件
    @Override
    public void onDrawableRightClick(View view) {
        if(login_password.getInputType()== 129) {//如果按右边的图标，如果密码是密文的话，就让它显示出来，就是明文
            Drawable visiable = ContextCompat.getDrawable(LoginActivity.this, R.drawable.visiable);
            //当你改变drawableRight的图片的时候，会让 drawableLeft锁的图标没有，所以要重新设置一下锁的图标
            Drawable drawableLeft=ContextCompat.getDrawable(LoginActivity.this,R.drawable.pwd);
            visiable.setBounds(0, 0, visiable.getMinimumWidth(), visiable.getMinimumHeight());
            drawableLeft.setBounds(0,0,drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            login_password.setCompoundDrawables(drawableLeft, null, visiable, null);//Compound合成的意思
            login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //将光标移到文字末尾
            login_password.setSelection(login_password.getText().length());
            UtilsLog.i("显示密码的int="+InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else{
            Drawable novisiable=ContextCompat.getDrawable(LoginActivity.this,R.drawable.novisi);
            novisiable.setBounds(0,0,novisiable.getMinimumWidth(),novisiable.getMinimumHeight());
            Drawable drawableLeft=ContextCompat.getDrawable(LoginActivity.this,R.drawable.pwd);
            drawableLeft.setBounds(0,0,drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            login_password.setCompoundDrawables(drawableLeft,null,novisiable,null);
            login_password.setInputType(129);
            login_password.setSelection(login_password.getText().length());
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(login_name.getText().toString().trim())&&!TextUtils.isEmpty(login_password.getText().toString().trim())){
            btn_login.setEnabled(true);
        }else{
            btn_login.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
