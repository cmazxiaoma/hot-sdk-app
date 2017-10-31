package com.example.administrator.cmazxiaoma.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ashokvarma.bottomnavigation.utils.Utils;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.MsgAdapter;
import com.example.administrator.cmazxiaoma.entity.ChatMsg;
import com.example.administrator.cmazxiaoma.entity.GroupChat;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.event.ConversationFriend;
import com.example.administrator.cmazxiaoma.event.GroupConversation;
import com.example.administrator.cmazxiaoma.event.Sender;
import com.example.administrator.cmazxiaoma.utils.GroupDB;
import com.example.administrator.cmazxiaoma.utils.MsgDatabaseHelper;
import com.example.administrator.cmazxiaoma.utils.SingleDB;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by Administrator on 2017/3/3.
 */

public class FriendChatActivity extends BaseActivity implements TextWatcher, View.OnTouchListener, View.OnClickListener,ObseverListener,MessageListHandler{
    private String friendUsername,friendHead,friendId;
    private Button send;
    private EditText content;
    private RecyclerView mRecyclerView;
    private MsgAdapter adapter;
    private List<ChatMsg> mlist;
    private List<GroupChat> mlist_1;
    private MyUser myUser;
    private MsgDatabaseHelper msgDatabaseHelper;
    //真正的会话实例
    private BmobIMConversation c;
    //不是真正的会话实例
    private BmobIMConversation conversation;
    //会话过期，怎么办？
    private int chatType;
    //判断群聊的flag
    private boolean chat_type_flag;
    //云端方法的名字
    private String CloudName="Group";
    private BmobRealTimeData brt;
    private String MyHead;
    @Subscribe
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_chat);
        EventBus.getDefault().register(this);
        UtilsLog.i("和好友聊天界面----onCreate");
        initData();
        initView();
        initEvent();

    }


    private void initEvent() {
        content.addTextChangedListener(this);
        mRecyclerView.setOnTouchListener(this);
        send.setOnClickListener(this);
    }

    private void initView() {
        send= (Button) findViewById(R.id.btn_friend_chat_send_msg);
        content=(EditText)findViewById(R.id.friend_chat_msg);
        mRecyclerView= (RecyclerView) findViewById(R.id.friend_chat_recyclerview);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        if(chatType==ConversationFriend.FRIEND_CHAT){
            adapter=new MsgAdapter(getApplicationContext(),mlist,null,MyHead,friendHead,ConversationFriend.FRIEND_CHAT);

        }else{
            adapter=new MsgAdapter(getApplicationContext(),null,mlist_1,MyHead,friendHead,ConversationFriend.GROUP_CHAT);
        }

        mRecyclerView.setAdapter(adapter);
        if(mlist!=null){
            mRecyclerView.scrollToPosition(mlist.size()-1);
        }

    }

    private void initData() {
        mlist=new ArrayList<>();
        mlist_1=new ArrayList<>();
        myUser= BmobUser.getCurrentUser(MyUser.class);
        chatType=getIntent().getIntExtra("chatType",-1);
        //是群聊，还是单聊
        ISChat();
        //然后根据群聊，还是单聊，然后就查询对应的记录
        ISRecord();
        //如果是群聊就监听连接,否则就不连接群聊监听。
        Connect();

    }

    private void Connect() {
        if(chatType==ConversationFriend.GROUP_CHAT){
            brt=new BmobRealTimeData();
            brt.start(new ValueEventListener() {
                @Override
                public void onConnectCompleted(Exception e) {
                    if(brt.isConnected()){
                        UtilsLog.i("开启监听数据实时同步成功!");
                        brt.subTableUpdate("GroupChat");
                    }

                }

                @Override
                public void onDataChange(JSONObject jsonObject) {
                    UtilsLog.i("改变的"+jsonObject.toString());
                    try {
                        if(BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.getString("action"))){
                            try {
                                JSONObject object= (JSONObject) jsonObject.get("data");
                                JSONObject sender= (JSONObject) object.get("sender");
                                String sender_name=sender.getString("username").trim();
                                //要过滤到自己发送的消息
                                UtilsLog.i("当前的自己:"+myUser.getUsername()+",发送者:"+sender_name);
                                if(!(myUser.getUsername().trim()).equals(sender_name)){
                                    String content=object.getString("msg");
                                    String date=object.getString("date");
                                    UtilsLog.i("sender="+sender);
                                    String sender_head=sender.getString("user_img");
                                    UtilsLog.i("收到的消息="+content+",date="+date+"发送者:"+sender);
                                    EventBus.getDefault().post(new Sender(sender_name,content,sender_head));
                                }else {
                                    UtilsLog.i("过滤到自己的消息");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }else{
            UtilsLog.i("你进入了是单聊，所以我不连接群聊服务器");
        }
    }

    //群聊接受到其他人发送的消息，然后更新UI
    @Subscribe
    public void onEventMainThread(Sender event){
        UtilsLog.i("绘制发送人的界面");
        if(event!=null){
            String content=event.getContent();
            String sender_head=event.getHead();
            String sender_name=event.getSender();
            UtilTools.Toast(getApplicationContext(),"sender="+event.getSender()+":content="+content);
            mlist_1.add(new GroupChat(UtilTools.DatetoTime(new Date()),sender_name,sender_head,content,GroupChat.TYPE_RECEIVE));
            //添加群聊
            GroupDB.getInstance(getApplicationContext()).insertData(myUser.getUsername(),sender_name,sender_head,content,GroupChat.TYPE_RECEIVE,UtilTools.DatetoTime(new Date()));
            adapter.notifyItemInserted(mlist_1.size()-1);
            mRecyclerView.scrollToPosition(mlist_1.size()-1);
            updateAdapter();
        }
    }


    private void ISRecord() {
        //是单聊
        if(chatType==ConversationFriend.FRIEND_CHAT){
            msgDatabaseHelper=new MsgDatabaseHelper(this,"msg_record.dp3",1);
            //查询单聊记录
            querySingleRecord(myUser.getUsername(),friendUsername);
            UtilsLog.i("查询单聊聊天");
        }else if(chatType==ConversationFriend.GROUP_CHAT){
            //查询群聊记录
            UtilsLog.i("查询群聊天记录");
            queryGroupRecord(myUser.getUsername());
        }

    }

    private void querySingleRecord(String username, String friendUsername) {
        Cursor cursor = SingleDB.getInstance(getApplicationContext()).query(username,friendUsername);
        if(cursor!=null){
            while(cursor.moveToNext()){
                mlist.add(new ChatMsg(cursor.getString(5),cursor.getInt(4),cursor.getString(3)));
            }
            cursor.close();
        }else{
            UtilsLog.i("curosr=null");
        }


    }

    private void queryGroupRecord(String username) {
            Cursor cursor = GroupDB.getInstance(this).query(username);
            if(cursor!=null){
                while(cursor.moveToNext()){
                    UtilsLog.i("内容="+cursor.getString(2)+",type="+cursor.getString(3));
                    mlist_1.add(new GroupChat(cursor.getString(6),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5)));
                }
                cursor.close();
            }else{
                UtilsLog.i("curosr=null");
            }


    }

    private void ISChat() {
        MyHead=myUser.getUser_img();
        if(chatType==ConversationFriend.FRIEND_CHAT){
            friendUsername=getIntent().getStringExtra("friendUsername");
            friendHead=getIntent().getStringExtra("friendHead");
            friendId=getIntent().getStringExtra("friendId");
            conversation=(BmobIMConversation)getIntent().getSerializableExtra("conversation");
            if(conversation!=null){
                c= BmobIMConversation.obtain(BmobIMClient.getInstance(),conversation);
            }
            UtilsLog.i("当前conversationId="+c.getConversationId()+",conversationTitle="+c.getConversationTitle());
            getSupportActionBar().setTitle(friendUsername);
            chat_type_flag=true;
        }else  if(chatType==ConversationFriend.GROUP_CHAT){
            getSupportActionBar().setTitle("群聊天");
            chat_type_flag=false;
        }else {
            UtilsLog.i("chatType为空");
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(content.getText().toString().trim())){
            send.setVisibility(View.VISIBLE);
            send.setEnabled(true);
        }else{
            send.setEnabled(false);
            send.setVisibility(View.GONE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        UtilTools.HideSoftInputViewGroup(this,mRecyclerView);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_friend_chat_send_msg:
                String send_msg=content.getText().toString().trim();
                //单聊
                if(chat_type_flag){
                    //处理要发送的消息
                    detailMessage(send_msg);
                    //查询所有会话
                    UtilsLog.i("查询所有会话"+BmobIM.getInstance().loadAllConversation());
                }else {
                    //群聊,true为单聊，false为群聊
                    String text=content.getText().toString().trim();
                    UtilsLog.i("发送群消息");
                    onRequestYun(text);
                    mlist_1.add(new GroupChat(UtilTools.DatetoTime(new Date()),null,null,text,GroupChat.TYPE_SEND));
                    updateAdapter();
                    String text1 = myUser.getUsername() + ":" + content.getText().toString().trim();
                    ConversationFriend event = new ConversationFriend(myUser.getUsername(), null, "群消息", null, text1, UtilTools.DatetoTime(new Date()), ConversationFriend.GROUP_CHAT);
                    EventBus.getDefault().post(event);
                    content.setText("");
                    GroupDB.getInstance(getApplicationContext()).insertData(myUser.getUsername(),null,null, text, ChatMsg.TYPE_SEND,UtilTools.DatetoTime(new Date()));
                }
                break;
        }
    }





    //把发送的消息 通过云端代码发送服务器
    private void onRequestYun(final String text) {
        // test对应你刚刚创建的云端代码名称

    JSONObject params = new JSONObject();
    GroupConversation gc = new GroupConversation();
    gc.setMsg(text);
    gc.setDate(UtilTools.DatetoTime(new Date()));
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
        JSONObject jsonObject = new JSONObject(gson.toJson(gc));
        jsonObject.put("sender", new JSONObject(gson.toJson(BmobUser.getCurrentUser(MyUser.class))));
        UtilsLog.i("jsonObject=" + jsonObject);
        // name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值
        params.put("tableName", "GroupChat");
        params.put("groupChatMsg", jsonObject);
    } catch (JSONException e) {

    }
    // 创建云端代码对象
    AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
    // 异步调用云端代码
    cloudCode.callEndpoint(CloudName, params, new CloudCodeListener() {
        @Override
        public void done(Object o, BmobException e) {
            if (e == null) {
                UtilsLog.i("云端返回的结果=" + o.toString());

            } else {
                UtilsLog.i("云端发生错误,原因=" + e.getMessage());
            }
        }
    });
}


    //发送消息
    private void detailMessage(final String text) {
        //记住是text
        UtilsLog.i("发送消息");
        BmobIMTextMessage message=new BmobIMTextMessage();
        message.setContent(text);
        c.sendMessage(message, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if(e==null){
                    UtilsLog.i("发送消息成功,BmobIMMessage="+bmobIMMessage);
                    mlist.add(new ChatMsg(UtilTools.DatetoTime(new Date()),ChatMsg.TYPE_SEND,text));
                    //也传递会话
                    //把朋友头像，朋友用户名，最后一句聊天内容，最后聊天时间，通过eventBus发给到--聊天界面列表
                    ConversationFriend event=new ConversationFriend(myUser.getUsername(),friendId,friendUsername,friendHead,text,UtilTools.DatetoTime(new Date()),ConversationFriend.FRIEND_CHAT);
                    EventBus.getDefault().post(event);
                    //插入单聊数据库
                    SingleDB.getInstance(getApplicationContext()).insert(myUser.getUsername(),friendUsername,text,ChatMsg.TYPE_SEND,UtilTools.DatetoTime(new Date()));
                    updateAdapter();
                    content.setText("");
                }else{
                    UtilsLog.i("发送消息失败,原因="+e.getMessage());
                }
            }

            @Override
            public void onStart(BmobIMMessage bmobIMMessage) {
                super.onStart(bmobIMMessage);
            }
        });
    }




    private void addMessage(MessageEvent event) {
        if(chat_type_flag){
            BmobIMMessage msg=event.getMessage();
            UtilsLog.i("接受到的conversationId="+event.getConversation().getConversationId());
            UtilsLog.i("当前的conversationId="+c.getConversationId());
            //如果当前会话信息
            if(c!=null && event!=null && c.getConversationId().equals(event.getConversation().getConversationId())&& !msg.isTransient()){ //如果是当前会话的消息//并且不为暂态消息
                UtilsLog.i("聊天窗口----在线接收到消息"+event.getMessage());
                String content=msg.getContent();
                mlist.add(new ChatMsg(UtilTools.DatetoTime(new Date()),ChatMsg.TYPE_RECEIVE,content));
                updateAdapter();
                //此处不再保存消息，已经消息已经由前端UI保存了
            }else{
                UtilsLog.i("不是当前聊天对象的消息");
            }
        }else {
            UtilsLog.i("这个消息不是群聊的消息");
        }

    }


    public void updateAdapter(){
        //通知适配器
        if(chat_type_flag){
            adapter.notifyItemInserted(mlist.size()-1);
            mRecyclerView.scrollToPosition(mlist.size()-1);
        }else{
            adapter.notifyItemInserted(mlist_1.size()-1);
            mRecyclerView.scrollToPosition(mlist_1.size()-1);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UtilsLog.i("和朋友聊天界面----onDestroy");
        EventBus.getDefault().unregister(this);
        //单聊的处理
        if(chat_type_flag){
            //消息设置为已读
            c.updateLocalCache();
            UtilsLog.i("我没有读的消息="+ BmobIM.getInstance().getUnReadCount(c.getConversationId()));
            //移除页面监听器
            BmobIM.getInstance().removeMessageListHandler(this);
            BmobNotificationManager.getInstance(getApplicationContext()).removeObserver(this);
        }else {
            //多聊的处理
            //取消监听
            brt.unsubTableUpdate("GroupChat");
        }
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        UtilsLog.i("和朋友聊天页面----接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i=0;i<list.size();i++){
            addMessage(list.get(i));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        UtilsLog.i("和朋友聊天界面---onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilsLog.i("和朋友聊天界面----onResume");
        //添加页面监听器
        BmobIM.getInstance().addMessageListHandler(this);
        BmobNotificationManager.getInstance(getApplicationContext()).addObserver(this);
    }


}
