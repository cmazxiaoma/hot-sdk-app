package com.example.administrator.cmazxiaoma.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.ChatAdapter;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.event.ReturnEvent;
import com.example.administrator.cmazxiaoma.fragment.chatFragment.ContactFragment;
import com.example.administrator.cmazxiaoma.fragment.chatFragment.ConversationFragment;
import com.example.administrator.cmazxiaoma.fragment.chatFragment.MoreFragment;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2017/2/28.
 */

public class ChatActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener,MoreFragment.Callback{
    private List<Fragment> mlist;
    private BottomNavigationBar bottom_navigation_bar;
    private ViewPager viewpager;
    private ChatAdapter adapter;
    //设置小红点
    private BadgeItem badgeImtem=new BadgeItem()
            .setBorderWidth(2)//Badge的Border(边界)宽度
            .setBorderColor("#FF0000")//Badge的Border颜色
            .setGravity(Gravity.RIGHT| Gravity.TOP)//位置，默认右上角
            .setText("2")//显示的文本
            .setTextColor("#F0F8FF")//文本颜色
            .setAnimationDuration(2000)
            .setHideOnSelect(true);//当选中状态时消失，非选中状态显示

    @Subscribe
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsLog.i("即时聊天主页----onCreate");
        setContentView(R.layout.activity_chat);
        EventBus.getDefault().register(this);
        initView();
        //连接IM服务器
        ConnectServer();
        //监听连接
        //ListenConnect();
        //实现接口
        MoreFragment moreFragment= (MoreFragment) adapter.getItem(2);
        moreFragment.setmCallback(this);
    }



    @Subscribe
    public void onDestroy(){
        super.onDestroy();
        UtilsLog.i("即时聊天主页----onDestroy");
        EventBus.getDefault().unregister(this);
        //清除观察者
        BmobNotificationManager.getInstance(getApplicationContext()).clearObserver();
        //注销IM服务器
        BmobIM.getInstance().disConnect();
        UtilsLog.i("注销后自己未读的消息="+BmobIM.getInstance().getAllUnReadCount()+",注销后连接状态="+BmobIMClient.getInstance().getCurrentConnectStatus());
    }

    @Override
    protected void onStart() {
        super.onStart();
        UtilsLog.i("即时聊天主页----onstart");
        ConnectionStatus c=BmobIMClient.getInstance().getCurrentConnectStatus();
        UtilsLog.i("与服务器连接状态="+c);


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


    @Override
    protected void onPause() {
        super.onPause();
        UtilsLog.i("即时聊天主页----onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UtilsLog.i("即时聊天主页----onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilsLog.i("即时聊天主页----onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        UtilsLog.i("即时聊天主页----onStop");
    }

    /**private void ListenConnect() {
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus connectionStatus) {
                UtilsLog.i("监听连接状态="+connectionStatus.getMsg());
            }

        });
    }
     */


    private void initView() {
        initBottomNavigationBar();
        initViewpager();
    }

    private void initViewpager() {
        viewpager= (ViewPager) findViewById(R.id.chat_mViewpager);
        adapter=new ChatAdapter(this,getSupportFragmentManager(),mlist);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(this);
        viewpager.setCurrentItem(0);
    }

    private void initBottomNavigationBar() {
        bottom_navigation_bar= (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_SHIFTING);
        //点击有水波纹
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        addItem();
        mlist=getFragments();
        bottom_navigation_bar.setTabSelectedListener(this);
    }


    private void addItem() {
        bottom_navigation_bar.addItem(new BottomNavigationItem(R.drawable.ic_chat_con_false,R.string.login_app_name).setActiveColorResource(R.color.colorPrimary))
        .addItem(new BottomNavigationItem(R.drawable.ic_chat_contact_false,R.string.contact).setActiveColor(R.color.littlegray))
        .addItem(new BottomNavigationItem(R.drawable.ic_chat_more_false,R.string.find).setActiveColorResource(R.color.babyred))
                .setFirstSelectedPosition(0)
                .initialise();

    }

    public List<Fragment> getFragments() {
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(ConversationFragment.newInstance(getResources().getString(R.string.login_app_name)));
        fragments.add(ContactFragment.newInstance(getResources().getString(R.string.contact)));
        fragments.add(MoreFragment.newInstance(getResources().getString(R.string.find)));
        return  fragments;

    }

    @Override
    public void onTabSelected(int position) {
        viewpager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottom_navigation_bar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Subscribe
    //注册  在线消息和离线接收事件
    public void onEventMainThread(MessageEvent event){
        UtilsLog.i("即时聊天主页----接收到消息");

    }

    public void onEventMainThread(OfflineMessageEvent event){
        UtilsLog.i("即时聊天主页----离线接收到消息");
    }



    //防止横竖屏切换Activity重建
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UtilsLog.i("防止横竖屏----聊天主页activity被重建");
    }


    @Subscribe
    //接受从MoreFragment传来的消息
    public void onEventMainThread(ReturnEvent event){
        if(event.getCode()==1000){
            UtilsLog.i("接受到来MoreFragment的消息----媒介是EventBus");
        }
    }


    @Override
    public void sendMsg(View v) {
        Button button=(Button)v;
        String text=button.getText().toString();
        UtilsLog.i("收到了来自MoreFragment的消息----"+text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //勾选此菜单
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.add_friend:
                startActivity(new Intent(getApplicationContext(),AddFriendActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


