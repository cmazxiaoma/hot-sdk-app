package com.example.administrator.cmazxiaoma.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.GirlPhotoAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/23.
 */

public class GirlPhotoActivity extends AppCompatActivity {
    private ArrayList<String> girl_photo;
    private int position;
    private ViewPager girl_mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girl_photo);
        initData();
        initView();
    }

    //沉浸模式
 public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initView() {
        girl_mViewPager= (ViewPager) findViewById(R.id.girl_mViewpager);
        //设置预加载
        girl_mViewPager.setOffscreenPageLimit(2);
        GirlPhotoAdapter adapter=new GirlPhotoAdapter(this,girl_photo);
        girl_mViewPager.setAdapter(adapter);
        girl_mViewPager.setCurrentItem(position);

    }

    private void initData() {
        girl_photo=getIntent().getStringArrayListExtra("girl_photo");
        position=getIntent().getIntExtra("position",0);
    }
}
