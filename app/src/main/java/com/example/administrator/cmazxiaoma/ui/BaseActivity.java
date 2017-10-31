package com.example.administrator.cmazxiaoma.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

/**
 * Created by Administrator on 2017/2/10.
 * 描述：Activity基类
 * 统一的属性
 * 统一的方法
 * 统一的接口
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无阴影
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                UtilsLog.i("隐藏输入法");
                //返回统一隐藏键盘
                UtilTools.HideSoftInputView(this,getCurrentFocus());
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
