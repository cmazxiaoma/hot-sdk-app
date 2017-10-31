package com.example.administrator.cmazxiaoma.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;

/**
 * Created by Administrator on 2017/2/28.
 */

public class UpdateAppActivity extends  BaseActivity {
    private TextView update_size;
    private String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_app);
        initData();
        initView();
    }

    private void initData() {
        url=getIntent().getStringExtra("url");
    }

    private void initView() {
        update_size= (TextView) findViewById(R.id.update_size);
    }
}
