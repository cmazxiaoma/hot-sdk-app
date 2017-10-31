package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.MainActivity;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.GuideApdater;
import com.example.administrator.cmazxiaoma.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/11.
 * 描述：引导页
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mviewPager;
    //容器，放View
    private List<View> mlist=new ArrayList<>();
    private View view1,view2,view3;
    //View里面的textView;
    private TextView textView1,textView2,textView3;
    private ImageView point1,point2,point3;
    //进入主页的Button
    private Button start;
    //引导页跳过的ImageView
    private ImageView skip;
    private GuideApdater adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView(){
        //初始化引导页的3个point
        point1= (ImageView) findViewById(R.id.guide_one_point);
        point2= (ImageView) findViewById(R.id.guide_two_point);
        point3= (ImageView) findViewById(R.id.guide_three_point);
        //
        skip= (ImageView) findViewById(R.id.skip);
        skip.setVisibility(View.VISIBLE);
        skip.setOnClickListener(this);
        //
        view1=View.inflate(this,R.layout.guide_page_one,null);
        view2=View.inflate(this,R.layout.guide_page_two,null);
        view3=View.inflate(this,R.layout.guide_page_three,null);
        textView1= (TextView)view1.findViewById(R.id.page_one_text);
        textView2= (TextView)view2. findViewById(R.id.page_two_text);
        textView3= (TextView)view3.findViewById(R.id.page_three_text);

        //进入主页的Button是guide_page_three的里面的控件
        start= (Button)view3.findViewById(R.id.btn_to_MainActivity);
        start.setOnClickListener(this);
        //为guide_page里面的textView设置文字个性字体 typeface
        UtilTools.setFont(GuideActivity.this,textView1);
        UtilTools.setFont(GuideActivity.this,textView2);
        UtilTools.setFont(GuideActivity.this,textView3);
        mlist.add(view1);
        mlist.add(view2);
        mlist.add(view3);

        //设置适配器
        mviewPager= (ViewPager) findViewById(R.id.guide_mViewPage);
        mviewPager.addOnPageChangeListener(this);
        adapter=new GuideApdater(this,mlist);
        mviewPager.setAdapter(adapter);

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_MainActivity:
            case R.id.skip:
                startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                finish();
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                setPointImg(true,false,false);
                skip.setVisibility(View.VISIBLE);
                break;
            case 1:
                setPointImg(false,true,false);
                skip.setVisibility(View.VISIBLE);
                break;
            case 2:
                setPointImg(false,false,true);
                skip.setVisibility(View.INVISIBLE);
                break;
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //分别判断3个point的true|false状态,（也就是2个图片切换），然后设置图片属性
    private void setPointImg(boolean ischecked1,boolean ischecked2,boolean ischecked3){
        if(ischecked1){
            point1.setImageResource(R.drawable.point_on);
        }else{
            point1.setImageResource(R.drawable.point_off);
        }

        if(ischecked2){
            point2.setImageResource(R.drawable.point_on);
        }else{
            point2.setImageResource(R.drawable.point_off);
        }

        if(ischecked3){
            point3.setImageResource(R.drawable.point_on);
        }else{
            point3.setImageResource(R.drawable.point_off);
        }
    }

}
