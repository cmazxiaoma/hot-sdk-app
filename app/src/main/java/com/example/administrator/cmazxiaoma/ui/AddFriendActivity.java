package com.example.administrator.cmazxiaoma.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.Friend;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.event.AddFriendEvent;
import com.example.administrator.cmazxiaoma.event.FriendDataEvent;
import com.example.administrator.cmazxiaoma.event.GroupConversation;
import com.example.administrator.cmazxiaoma.model.UserModel;
import com.example.administrator.cmazxiaoma.model.listener.QueryUserListener;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;

/**
 * Created by Administrator on 2017/3/14.
 */

public class AddFriendActivity extends  BaseActivity implements View.OnClickListener, TextWatcher {
    private Button btn_add;
    private EditText friendName;
    private LinearLayout add_Lin;

    @Subscribe
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend);
        EventBus.getDefault().register(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        btn_add.setOnClickListener(this);
        friendName.addTextChangedListener(this);
    }

    private void initView() {
        btn_add= (Button) findViewById(R.id.btn_add_friend);
        friendName= (EditText) findViewById(R.id.add_friendName);
        add_Lin= (LinearLayout) findViewById(R.id.add_linear);
    }

    @Subscribe
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_friend:
                String add_friendName=friendName.getText().toString().trim();
                UserModel.getInstance().queryUserInfo(null, add_friendName, new QueryUserListener() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if(e==null){
                            FriendDataEvent event=new FriendDataEvent();
                            event.setId(myUser.getObjectId());
                            event.setDesc(myUser.getDesc());
                            event.setFriend_head(myUser.getUser_img());
                            event.setSex(String.valueOf(myUser.isSex()));
                            event.setFriendusername(myUser.getUsername());
                            event.setAge(myUser.getAge()+"");
                            requestYun(event);
                        }else {
                            EventBus.getDefault().post(new AddFriendEvent(1));
                        }
                    }
                });
        }

    }

    private  void requestYun(FriendDataEvent event){
        JSONObject params = new JSONObject();
        Friend friend=new Friend();
        friend.setUser(BmobUser.getCurrentUser(MyUser.class).getUsername());
        //过滤属性,过滤_c_
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().contains("_c_") | f.getName().contains("increments");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(friend));
            jsonObject.put("friend", new JSONObject(gson.toJson(event)));
            UtilsLog.i("jsonObject=" + jsonObject);
            // name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值
            params.put("tableName", "Friend");
            params.put("groupChatMsg", jsonObject);
        } catch (JSONException e) {

        }
        // 创建云端代码对象
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        // 异步调用云端代码
        cloudCode.callEndpoint("insertFriend", params, new CloudCodeListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    UtilsLog.i("云端返回的结果=" + o.toString());
                    EventBus.getDefault().post(new AddFriendEvent(3));

                } else {
                    UtilsLog.i("云端发生错误,原因=" + e.getMessage());
                    EventBus.getDefault().post(new AddFriendEvent(2));
                }
            }
        });
    }


    @Subscribe
    public void onEventMainThread(AddFriendEvent event){
        switch (event.getAddType()){
            case 1:
                UtilTools.setAlertDialog(AddFriendActivity.this,"查询不到此人");
                break;
            case 2:
                UtilTools.setAlertDialog(AddFriendActivity.this,"添加好友失败");
                break;
            case 3:
                UtilTools.setAlertDialog(AddFriendActivity.this,"添加好友成功");
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(friendName.getText().toString().trim())){
            btn_add.setEnabled(true);
        }else {
            btn_add.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        add_Lin.requestFocus();
        UtilTools.HideSoftInputViewGroup(getApplicationContext(),add_Lin);
        return super.onTouchEvent(event);
    }

    @Subscribe
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
