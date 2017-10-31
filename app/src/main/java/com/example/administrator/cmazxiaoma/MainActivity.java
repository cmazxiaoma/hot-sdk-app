package com.example.administrator.cmazxiaoma;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.adapter.MyFragmentPagerAdapter;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.fragment.ButlerFragment;
import com.example.administrator.cmazxiaoma.fragment.GrilsFragment;
import com.example.administrator.cmazxiaoma.fragment.LifeToolsFragment;
import com.example.administrator.cmazxiaoma.fragment.UserFragment;
import com.example.administrator.cmazxiaoma.fragment.WechatFragment;
import com.example.administrator.cmazxiaoma.ui.SettingActivity;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {
    private TabLayout mTablayout;
    private ViewPager mViewPage;
    private List<String> actionBar_title_list=new ArrayList<>();
    //titile
    private List<String> mtitle=new ArrayList<>();
    //fragment
    private List<Fragment> mfragments=new ArrayList<>();
    //FloatingAcitonButton 悬浮窗
    private FloatingActionButton softwareSetting;
    //自己封装的MyFragmentPagerAdapter
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    //5个未选中时的Tab的图标
    private int[] mIconNormal=new int[]{
            R.drawable.butler_false,
            R.drawable.wechat_false,
            R.drawable.girl_false,
            R.drawable.life_false,
            R.drawable.user_false
    };
    //5个选中时的tab图标
    private int[] mIconSelected=new int[]{
            R.drawable.butler_true,
            R.drawable.wechat_true,
            R.drawable.girl_true,
            R.drawable.life_true,
            R.drawable.user_true
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();
        //去掉阴影
        getSupportActionBar().setElevation(0);
        initData();
        initView();
        //连接IM服务器
       // ConnectServer();
        /**
         * *测试腾讯Bugly
         */
        //CrashReport.testJavaCrash();
    }

    @Override
    protected void onStart() {
        super.onStart();
        UtilsLog.i("主页----onStart");
        //连接IM服务器
        //ConnectServer();
    }

    private void ConnectServer() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    UtilsLog.i("Connect Success");

                } else {
                    UtilsLog.i("当前用户连接服务器失败,原因=" + e.getMessage());
                }
            }
        });
    }


    //初始化数据
    private void initData() {
        mtitle.add(getResources().getString(R.string.butler));
        mtitle.add(getResources().getString(R.string.wechatnews));
        mtitle.add(getResources().getString(R.string.girlalbum));
        mtitle.add(getResources().getString(R.string.life));
        mtitle.add(getResources().getString(R.string.user));
        UtilsLog.i("mtitle="+mtitle);
        mfragments.add(new ButlerFragment());
        mfragments.add(new WechatFragment());
        mfragments.add(new GrilsFragment());
        mfragments.add(new LifeToolsFragment());
        mfragments.add(new UserFragment());
        //acitonBar标题集合
        actionBar_title_list.add(getResources().getString(R.string.acitonBar0));
        actionBar_title_list.add(getResources().getString(R.string.acitonBar1));
        actionBar_title_list.add(getResources().getString(R.string.acitonBar2));
        actionBar_title_list.add(getResources().getString(R.string.acitonBar3));
        actionBar_title_list.add(getResources().getString(R.string.acitonBar4));
    }

    //初始化View
    private void initView() {
        mTablayout = (TabLayout) findViewById(R.id.mTablayout);
        mViewPage = (ViewPager) findViewById(R.id.mViewPage);
        //预加载
        mViewPage.setOffscreenPageLimit(mfragments.size());
        softwareSetting = (FloatingActionButton) findViewById(R.id.software_setting);
        softwareSetting.setOnClickListener(this);
        //悬浮窗设置
        softwareSetting.setVisibility(View.INVISIBLE);
        //ViewPage的滑动监听
        mViewPage.addOnPageChangeListener(this);
        //实例化
        myFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),mfragments,mtitle);
        //别忘记设置适配器了
        mViewPage.setAdapter(myFragmentPagerAdapter);
        //绑定
         mTablayout.setupWithViewPager(mViewPage);
        mTablayout.setOnTabSelectedListener(this);
        //先设置tab的一个图标和text
         setupTabIconText();

    }

    //这个id就是0为未选中， 1为选中
    private void setupTabIconText() {
        mTablayout.getTabAt(0).setCustomView(getTabView(0));
        mTablayout.getTabAt(1).setCustomView(getTabView(1));
        mTablayout.getTabAt(2).setCustomView(getTabView(2));
        mTablayout.getTabAt(3).setCustomView(getTabView(3));
        mTablayout.getTabAt(4).setCustomView(getTabView(4));
    }

    //当mIcon，colorId为0是就是未选中的状态， 为1时为选中。
    private View getTabView(int position) {
        View view=getLayoutInflater().inflate(R.layout.tab_item,null);
        TextView tab_text= (TextView) view.findViewById(R.id.tab_text);
        ImageView tab_icon= (ImageView) view.findViewById(R.id.tab_icon);
        tab_text.setText(mtitle.get(position));
        if(position==0){
            tab_text.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
            tab_icon.setImageResource(mIconSelected[position]);
        }else{
            tab_text.setTextColor(ContextCompat.getColor(this,R.color.babygray));
            tab_icon.setImageResource(mIconNormal[position]);
        }

        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.software_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }

    }
    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setTitle(actionBar_title_list.get(position));
        UtilsLog.i("position="+position);
        switch (position){
            case 0:
                softwareSetting.setVisibility(View.GONE);
                break;
            case 1:
                softwareSetting.setVisibility(View.VISIBLE);
                break;
            case 2:
                softwareSetting.setVisibility(View.VISIBLE);
                break;
            case 3:
                softwareSetting.setVisibility(View.VISIBLE);
                break;
            case 4:
                softwareSetting.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        changeTabSelected(tab);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        changeTabNormal(tab);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void changeTabSelected(TabLayout.Tab tab) {
        View view=tab.getCustomView();
        ImageView tab_icon= (ImageView) view.findViewById(R.id.tab_icon);
        TextView text= (TextView) view.findViewById(R.id.tab_text);
        text.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        String tab_text=text.getText().toString().trim();
        if(tab_text.equals(getResources().getString(R.string.butler)))
        {
            tab_icon.setImageResource(mIconSelected[0]);
            mViewPage.setCurrentItem(0);
        }else if(tab_text.equals(getResources().getString(R.string.wechatnews))){
            tab_icon.setImageResource(mIconSelected[1]);
            mViewPage.setCurrentItem(1);
        }else if(tab_text.equals(getResources().getString(R.string.girlalbum))){
            tab_icon.setImageResource(mIconSelected[2]);
            mViewPage.setCurrentItem(2);
        }else if(tab_text.equals(getResources().getString(R.string.life))){
            tab_icon.setImageResource(mIconSelected[3]);
            mViewPage.setCurrentItem(3);
        }else {
            tab_icon.setImageResource(mIconSelected[4]);
            mViewPage.setCurrentItem(4);
        }

    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view=tab.getCustomView();
        ImageView tab_icon= (ImageView) view.findViewById(R.id.tab_icon);
        TextView text= (TextView) view.findViewById(R.id.tab_text);
        text.setTextColor(ContextCompat.getColor(this,R.color.babygray));
        String tab_text=text.getText().toString().trim();
        if(tab_text.equals(getResources().getString(R.string.butler))) {
            tab_icon.setImageResource(mIconNormal[0]);
        }else if(tab_text.equals(getResources().getString(R.string.wechatnews))){
            tab_icon.setImageResource(mIconNormal[1]);
        }else if(tab_text.equals(getResources().getString(R.string.girlalbum))){
            tab_icon.setImageResource(mIconNormal[2]);
        }else if(tab_text.equals(getResources().getString(R.string.life))){
            tab_icon.setImageResource(mIconNormal[3]);
        }else {
            tab_icon.setImageResource(mIconNormal[4]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UtilsLog.i("主页----onDestroy");
        //注销该用户的会话服务器  当强制从后台关闭即时聊天时候，那么我们就断开连接就行了。反正可以收到离线记录
        //如果我们不断开连接的话，我们可以收到在线信息，但是我们postEvent无法订阅到还没有启动的Activity里面
        //也就是说聊天信息无法更新到聊天界面上
         BmobIM.getInstance().disConnect();
        //清除观察者
        //BmobNotificationManager.getInstance(getApplicationContext()).clearObserver();
    }


}
