package com.example.administrator.cmazxiaoma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.event.ConversationFriend;
import com.example.administrator.cmazxiaoma.event.FriendDataEvent;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;


/**
 * Created by Administrator on 2017/3/2.
 */

public class FriendDataActivity extends BaseActivity implements View.OnClickListener {
    private TextView friend_username, friend_desc, friend_age;
    private ImageView friend_sex,friend_head;
    private Button btn_send_msg;
    private FriendDataEvent event;
    private String friendUsername,friendHead,friendId;


    @Subscribe
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_data);
        EventBus.getDefault().register(this);
        event=getIntent().getExtras().getParcelable("event");
        if(event!=null) {
            UtilsLog.i("这是" + event.getFriendusername());
        }
        initView();
        initData(event);
        initEvent();
    }

    private void initData(FriendDataEvent event) {
        if(event.getFriend_head()==null){
            friendHead=null;
            friend_head.setImageResource(R.drawable.add_pic);
        }else{
            friendHead=event.getFriend_head();
            UtilTools.setStringtoImage(this,friend_head,event.getFriend_head());
        }

        if(event.getSex().equals("true")){
            friend_sex.setImageResource(R.drawable.man);
        }else{
            friend_sex.setImageResource(R.drawable.woman);
        }

        friend_desc.setText(event.getDesc());
        friend_age.setText(event.getAge());
        friend_username.setText(event.getFriendusername());
        friendUsername=event.getFriendusername();
        friendId=event.getId();

    }


    @Subscribe
    public void onDestroy(){
        super.onDestroy();
        UtilsLog.i("查看朋友聊天资料界面---onDestroy");
        EventBus.getDefault().unregister(this);
    }


    private void initEvent() {
        btn_send_msg.setOnClickListener(this);
    }

    private void initView() {
        friend_username = (TextView) findViewById(R.id.friend_data_username);
        friend_age = (TextView) findViewById(R.id.friend_data_age);
        friend_desc = (TextView) findViewById(R.id.friend_data_desc);
        friend_sex = (ImageView) findViewById(R.id.friend_data_sex);
        friend_head= (ImageView) findViewById(R.id.friend_data_head);
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_msg:
                UtilsLog.i("跳到聊天界面");
                final BmobIMUserInfo info=new BmobIMUserInfo();
                info.setUserId(friendId);
                info.setName(friendUsername);
                info.setAvatar(friendHead);
                //A和B聊天，这个info则是B的info
                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                        if(e==null){
                            UtilsLog.i("建立会话成功!");
                            Intent intent=new Intent(getApplicationContext(),FriendChatActivity.class);
                            Bundle data=new Bundle();
                            data.putString("friendUsername",friendUsername);
                            data.putString("friendHead",friendHead);
                            data.putString("friendId",friendId);
                            data.putInt("chatType", ConversationFriend.FRIEND_CHAT);
                            data.putSerializable("conversation",bmobIMConversation);
                            intent.putExtras(data);
                            startActivity(intent);
                            finish();
                        }else{
                            UtilsLog.i("建立会话失败,原因="+e.getMessage());
                        }
                    }
                });
        }
    }


}
