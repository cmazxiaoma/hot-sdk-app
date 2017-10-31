package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/2/16.
 */

public class UserSafeActivity extends BaseActivity implements View.OnClickListener {
    private TextView edit_email;
    private LinearLayout edit_password_lin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_safety);
        initView();
        initData();
    }

    private void initData() {
        MyUser myuser= BmobUser.getCurrentUser(MyUser.class);
        edit_email.setText(myuser.getEmail());
    }

    private void initView() {
        edit_email= (TextView) findViewById(R.id.edit_email);
        edit_password_lin= (LinearLayout) findViewById(R.id.edit_password_lin);
        edit_password_lin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_password_lin:
                startActivity(new Intent(UserSafeActivity.this, EditPasswordActivity.class));
                break;
        }

    }
}
