package com.example.administrator.cmazxiaoma.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.utils.UtilSharePre;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.internal.Util;

/**
 * Created by Administrator on 2017/2/17.
 */

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText reset_phone_email;
    private Button btn_reset_send_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_reset);
        initView();
    }


    private void initView() {
        reset_phone_email= (EditText) findViewById(R.id.reset_phone_email);
        reset_phone_email.addTextChangedListener(this);
        btn_reset_send_code= (Button) findViewById(R.id.btn_reset_send_code);
        btn_reset_send_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reset_send_code:
                final String phone_email=reset_phone_email.getText().toString().trim();
                //先判断输入的 是手机号，还是邮箱
                if(UtilTools.isEmail(phone_email)){
                    UtilsLog.i("输入的是邮箱");
                    //如果没有频繁发送验证码的话，就让它发送验证邮件,is_ofen_send_code=false
                        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                        query.addWhereEqualTo("email",phone_email);
                        query.findObjects(new FindListener<MyUser>() {
                            @Override
                            public void done(List<MyUser> list, BmobException e) {
                                if(e==null){
                                    //没有抛出异常，证明查询到了含有emial的数据
                                    UtilsLog.i("查询到了");
                                    MyUser.resetPasswordByEmail(phone_email, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                UtilTools.setAlertDialog(ResetPasswordActivity.this,"重置密码请求成功,请到"+phone_email+"邮箱进行密码重置操作");
                                                UtilsLog.i("重置密码请求成功,请到"+phone_email+"邮箱进行密码重置操作");
                                                btn_reset_send_code.setEnabled(false);
                                            }else{
                                                UtilsLog.i("重置密码请求失败"+e.getMessage());
                                                UtilTools.setAlertDialog(ResetPasswordActivity.this,"操作失败"+e.getMessage());
                                            }

                                        }
                                    });
                                }else{
                                    UtilsLog.i("未找到对应用户");
                                    UtilTools.setAlertDialog(ResetPasswordActivity.this,"未找到对应用户");
                                }

                            }
                        });

                }else if(UtilTools.isMobile(phone_email)){
                    UtilsLog.i("输入的是手机号");
                }else{
                    UtilsLog.i("输入的既不是手机号也不是邮箱");
                    UtilTools.Toast(ResetPasswordActivity.this,"请输入正确的手机号或者邮箱");
                }

                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(reset_phone_email.getText().toString().trim())){
            btn_reset_send_code.setEnabled(true);
        }else {
            btn_reset_send_code.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
