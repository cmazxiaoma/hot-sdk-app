package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.XEditText;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/2/16.
 */

public class EditPasswordActivity extends  BaseActivity implements View.OnClickListener, TextWatcher, XEditText.DrawableRightListener, View.OnTouchListener {
    Button btn_edit_password;
    XEditText new_pasword,new_confirm_password;
    EditText  old_password;
    LinearLayout edit_passoword_lin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersafety_editpassword);
        initView();
    }

    private void initView() {
        edit_passoword_lin= (LinearLayout) findViewById(R.id.edit_password_lin);
        btn_edit_password= (Button) findViewById(R.id.btn_edit_password);
        new_pasword= (XEditText) findViewById(R.id.new_password);
        new_confirm_password=(XEditText)findViewById(R.id.new_confirm_password);
        old_password= (EditText) findViewById(R.id.old_password);
        btn_edit_password.setOnClickListener(this);
        new_pasword.addTextChangedListener(this);
        new_confirm_password.addTextChangedListener(this);
        old_password.addTextChangedListener(this);
        btn_edit_password.setEnabled(false);
        new_pasword.setDrawableRightListener(this);
        edit_passoword_lin.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_password:
                btn_edit_password.requestFocus();
                String old_pasword=old_password.getText().toString().trim();
                String new_password=new_pasword.getText().toString().trim();
                String confirm_password=new_confirm_password.getText().toString().trim();
                if(new_password.equals(confirm_password)&&new_password.length()>=6){
                    MyUser user= BmobUser.getCurrentUser(MyUser.class);
                    MyUser.updateCurrentUserPassword(old_pasword, new_password, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                UtilTools.Toast(EditPasswordActivity.this,"修改成功");
                                startActivity(new Intent(EditPasswordActivity.this,UserSafeActivity.class));
                                finish();

                            }else{
                                UtilTools.Toast(EditPasswordActivity.this,"修改失败+"+e.getMessage());
                                UtilsLog.i("修改密码失败="+e.getMessage());
                            }
                        }
                    });


                }else{
                    if(!new_password.equals(confirm_password)){
                        UtilTools.Toast(EditPasswordActivity.this,"2次输入密码不一致!");
                    }

                    if(new_password.length()<6){
                        UtilTools.Toast(EditPasswordActivity.this,"新密码长度不能少于6位");
                    }
                }

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(old_password.getText().toString().trim())&&!TextUtils.isEmpty(new_pasword.getText().toString().trim())&&!TextUtils.isEmpty(new_confirm_password.getText().toString().trim()))
        {
            btn_edit_password.setEnabled(true);
        }else{
            btn_edit_password.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDrawableRightClick(View view) {
        if(new_pasword.getInputType()==129){
            Drawable visiable= ContextCompat.getDrawable(EditPasswordActivity.this,R.drawable.visiable);
            visiable.setBounds(0,0,visiable.getMinimumWidth(),visiable.getMinimumHeight());
            new_pasword.setCompoundDrawables(null,null,visiable,null);
            new_pasword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            old_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            new_confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            old_password.setSelection(old_password.length());
            new_pasword.setSelection(new_pasword.length());
            new_confirm_password.setSelection(new_confirm_password.length());
        }else{
            Drawable novisiable= ContextCompat.getDrawable(EditPasswordActivity.this,R.drawable.novisi);
            novisiable.setBounds(0,0,novisiable.getMinimumWidth(),novisiable.getMinimumHeight());
            new_pasword.setCompoundDrawables(null,null,novisiable,null);
            new_pasword.setInputType(129);
            old_password.setInputType(129);
            new_confirm_password.setInputType(129);
            old_password.setSelection(old_password.length());
            new_pasword.setSelection(new_pasword.length());
            new_confirm_password.setSelection(new_confirm_password.length());
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        edit_passoword_lin.requestFocus();
        InputMethodManager im=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(edit_passoword_lin.getWindowToken(),0);
        UtilsLog.i("修改密码界面被触摸");
        return false;
    }
}
