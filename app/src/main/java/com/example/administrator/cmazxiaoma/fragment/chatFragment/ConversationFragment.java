package com.example.administrator.cmazxiaoma.fragment.chatFragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.ConversationAdapter;
import com.example.administrator.cmazxiaoma.entity.ChatMsg;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.event.BaseEvent;
import com.example.administrator.cmazxiaoma.event.ConversationFriend;
import com.example.administrator.cmazxiaoma.event.ReturnEvent;
import com.example.administrator.cmazxiaoma.ui.FriendChatActivity;
import com.example.administrator.cmazxiaoma.utils.CupboardSQLiteOpenHelper;
import com.example.administrator.cmazxiaoma.utils.SingleDB;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.MyItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import nl.qbusict.cupboard.QueryResultIterable;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ConversationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ConversationAdapter.OnRecyclerViewItemClickListener, ConversationAdapter.OnRecyclerViewLongItemClickListener {
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private int lastVisiableItem;
    private LinearLayoutManager llm;
    private ConversationAdapter adapter;
    private List<ConversationFriend> mlist;
    private MyHandler mHandler;
    private int position;
    private MyUser myUser;
    //如果在聊天主页上，没有进入聊天窗口，那么就是false，如果已经进入了聊天窗口，那么就是true
    //flase,就是在recyclerview界面 收到的消息不插入数据库
    //true,就是再recylerview 收到的消息插入数据库
    //避免插入2次，收到的消息
    private Boolean flag=true;
    private CupboardSQLiteOpenHelper cupboardHelper;

    @Override
    public void onItemClick(View view, int position) {
        if(mlist.get(position).getType()==ConversationFriend.GROUP_CHAT){
            UtilsLog.i("进入群聊,position="+position);
            final String group_msg=mlist.get(position).getLast_msg();
            final String group_date=mlist.get(position).getLast_date();
            int  group_chat=mlist.get(position).getType();
            startActivity(new Intent(getApplicationContext(),FriendChatActivity.class).putExtra("group_msg",group_msg).putExtra("group_date",group_date).putExtra("chatType",group_chat));
        }else{
            final String friendHead=mlist.get(position).getFriendHead();
            final  String friendUsername=mlist.get(position).getFriendUsername();
            final  String friendId=mlist.get(position).getFriendId();
            final BmobIMUserInfo info=new BmobIMUserInfo();
            final int  chatType=mlist.get(position).getType();
            info.setUserId(friendId);
            info.setName(friendUsername);
            info.setAvatar(friendHead);
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
                        data.putInt("chatType",chatType);
                        data.putSerializable("conversation",bmobIMConversation);
                        intent.putExtras(data);
                        startActivity(intent);
                    }else{
                        UtilsLog.i("建立会话失败,原因="+e.getMessage());
                    }
                }
            });
        }
    }


    @Subscribe
    @Override
    public void onLongItemClick(View view, int position) {
        UtilsLog.i("你长按了recyclerview,position="+position);
        EventBus.getDefault().post(new BaseEvent("position",position,null));
    }

    static class MyHandler extends Handler {
        ConversationFragment fragment_1;

        MyHandler(ConversationFragment fragment) {
            fragment_1 = (new WeakReference<ConversationFragment>(fragment)).get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UtilsLog.i("fragment_1=" + fragment_1 + ",msg.what=" + msg.what);
            if (fragment_1 != null) {
                if (msg.what == 0x123) {
                    UtilsLog.i("准备更新了");
                    EventBus.getDefault().post(new ReturnEvent(100));
                }
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_conversation, null);
        UtilsLog.i("聊天列表界面----onCreateView");
        initView(view);
        return view;
    }



    @Subscribe
    public void onEventMainThread(BaseEvent event){
        if(event.getText().equals("position")){
            UtilsLog.i("收到了长按的 position="+event.getInt_msg());
            position=event.getInt_msg();
        }
    }
    @Subscribe
    public void onEventMainThread(ReturnEvent event) {
        if(event.getCode()==100){
            UtilsLog.i("code=" + event.getCode());
            mSwipeLayout.setRefreshing(false);
        }
        //mlist.add("我不怕千万人阻挡,只怕自己投降!");
        //adapter.notifyItemInserted(mlist.size() - 1);
        //mRecyclerView.scrollToPosition(mlist.size() - 1);
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event){
        String content=event.getMessage().getContent();
        String updateTime=UtilTools.DatetoTime(new Date(event.getMessage().getUpdateTime()));
        BmobIMUserInfo info=event.getFromUserInfo();
        String friendHead=info.getAvatar();
        String friendId=info.getUserId();
        String friendName=info.getName();
        UtilsLog.i("username1="+event.getConversation().getConversationTitle()+",username1="+friendName);
        ConversationFriend con=new ConversationFriend(myUser.getUsername(),friendId,friendName,friendHead,content,updateTime,ConversationFriend.FRIEND_CHAT);
        for(int position=0;position<mlist.size();position++){
            if(friendName.equals(mlist.get(position).getFriendUsername())){
                //去掉旧数据所在的项
                mlist.remove(position);
                adapter.notifyItemRemoved(position);
                mRecyclerView.scrollToPosition(mlist.size() - 1);

            }
        }
        UtilsLog.i("聊天界面----收到消息,发送者:"+friendName+":"+content+"消息的id="+event.getMessage());
        mlist.add(con);
        adapter.notifyItemInserted(mlist.size()-1); //自己犯错的地方
        mRecyclerView.scrollToPosition(mlist.size()-1);
        UtilsLog.i("flag="+flag);
        //将消息插入数据内
        UtilsLog.i("插入单聊数据");
        if (event.getMessage().getId() != null) {
            SingleDB.getInstance(getApplicationContext()).insert(myUser.getUsername(), friendName, content, ChatMsg.TYPE_RECEIVE,UtilTools.DatetoTime(new Date()));
        }else{
                UtilsLog.i("离线消息抽风了，id=null");
        }

    }

    //实际开发中,数据很大，耗时的操作应该放到子线程完成， UI线程只操作结果，更新UI就行
    @Subscribe
    public void onEventMainThread(ConversationFriend event) {
        UtilsLog.i("收到了更新聊天列表的消息,mlist长度=" + mlist.size());
        //避免重复添加一个朋友会话的聊天列表
        for (int position = 0; position < mlist.size(); position++) {
               UtilsLog.i("收到的的朋友用户名=" + event.getFriendUsername() + ",原有集合里面的朋友用户名=" + mlist.get(position).getFriendUsername() + ",position=" + position);
               if ((event.getFriendUsername()).equals(mlist.get(position).getFriendUsername())) {
                   UtilsLog.i("找到了名字相同的数据");
                   //删除重复数据
                   mlist.remove(position);
                   adapter.notifyItemRemoved(position);
                   mRecyclerView.scrollToPosition(mlist.size() - 1);
                   UtilsLog.i("通知删除第" + position + "项列表");
               }
        }
        mlist.add(event);
        adapter.notifyItemInserted(mlist.size() - 1);
        mRecyclerView.scrollToPosition(mlist.size() - 1);
    }/*
    /*反射
                        Class c=mlist.get(position).getClass();
                        Field f0=c.getDeclaredField("conversation");
                        f0.setAccessible(true);
                        f0.set("conversation",event.getConversation());**/


    private void initView(View view) {
        myUser= BmobUser.getCurrentUser(MyUser.class);
        ConversationFragment fragment = new ConversationFragment();
        mHandler = new MyHandler(fragment);
        mlist = new ArrayList<>();
        loadData();
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.conversation_swipeRefreshlayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.conversation_recyclerview);
        mSwipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.babygreen));
        //这句话为了第一次进入页面的时候显示加载进度条
        mSwipeLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwipeLayout.setOnRefreshListener(this);
        //保持mRecyclerView的尺寸，避免重复的增删造成额外的资源浪费
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        adapter = new ConversationAdapter(getActivity(),mlist);
        mRecyclerView.setAdapter(adapter);
        initEvent();
        loadGroup();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == recyclerView.SCROLL_STATE_IDLE && lastVisiableItem + 1 == adapter.getItemCount()) {
                    mSwipeLayout.setRefreshing(true);
                    //请求网络数据
                    mHandler.sendEmptyMessageDelayed(0x123,2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem = llm.findLastVisibleItemPosition();
                UtilsLog.i("lastViesiableItem=" + lastVisiableItem);
            }
        });
    }

    private void loadGroup() {
        mlist.add(new ConversationFriend(myUser.getUsername(),"group","群消息",null,"","",ConversationFriend.GROUP_CHAT));
        adapter.notifyItemInserted(mlist.size()-1);
        mRecyclerView.scrollToPosition(mlist.size()-1);
    }

    private void loadData() {
        cupboardHelper=new CupboardSQLiteOpenHelper(getApplicationContext());
        try{
            Cursor cursor=cupboard().withDatabase(cupboardHelper.getReadableDatabase()).query(ConversationFriend.class).withSelection("user=?",myUser.getUsername()).getCursor();
            if(cursor!=null){
                QueryResultIterable<ConversationFriend>itr=cupboard().withCursor(cursor).iterate(ConversationFriend.class);
                for(ConversationFriend it:itr){
                    mlist.add(it);
                }

                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initEvent() {
        adapter.setOnRecyclerViewItemClickListener(this);
        adapter.setOnRecyclerViewLongItemClickListener(this);
        //注册上下文菜单,有2种方法
        //registerForContextMenu(mRecyclerView);
        mRecyclerView.setOnCreateContextMenuListener(this);
    }


    public static ConversationFragment newInstance(String params) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle data = new Bundle();
        data.putString("args", params);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onRefresh() {
        if (UtilTools.isNetworkAvailable(getActivity())) {
            mHandler.sendEmptyMessageDelayed(0x123, 2000);
            UtilsLog.i("进行到这里了");
        } else {
            mSwipeLayout.setRefreshing(false);
            UtilTools.Toast(getActivity(), "网络连接失败");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilsLog.i("聊天列表界面----onDestroy");
        EventBus.getDefault().unregister(this);
        //删除以前的列表数据
        try {
            UtilsLog.i("要删除的列表长度="+mlist.size());
            for(int position=0;position<mlist.size();position++){
                UtilsLog.i("删除以前和"+mlist.get(position).getFriendUsername()+"的列表");
                cupboard().withDatabase(cupboardHelper.getReadableDatabase()).delete(ConversationFriend.class,"user=?",myUser.getUsername());
            }
        }catch (Exception e){
            e.getMessage();
            UtilsLog.i("删除失败!,原因="+e.getMessage());
        }

        try {
            UtilsLog.i("现在的mlist.size=" + mlist.size());
            for (ConversationFriend con : mlist) {
                if (con.getType() == ConversationFriend.FRIEND_CHAT) {
                    long rowId = cupboard().withDatabase(cupboardHelper.getReadableDatabase()).put(con);
                    if (rowId != -1) {
                        UtilsLog.i("保存" + con.getFriendUsername() + "的聊天列表成功");
                    } else {
                        UtilsLog.i("保存" + con.getFriendUsername() + "的聊天列表失败");
                    }
                } else {
                    UtilsLog.i("群组，我不负责");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Subscribe
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsLog.i("聊天列表界面----onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UtilsLog.i("聊天列表界面----onPause");
        //退出 即时聊天界面的时候,保存数据

    }

    @Override
    public void onStart() {
        super.onStart();
        UtilsLog.i("聊天列表界面----onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilsLog.i("聊天列表界面-----onResume");
    }


    //保存数据
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStop() {
        super.onStop();
        UtilsLog.i("聊天列表界面-----onStop");
    }

    //恢复数据
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    //上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater=new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.conversation_menu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //只适合listView
       // AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int position=info.position;
        switch (item.getItemId()){
            case R.id.conversation_del:
                UtilsLog.i("长按,position="+position);
                //删除item
                mlist.remove(position);
                UtilsLog.i("删除后mlist.size="+mlist.size());
                adapter.notifyItemRemoved(position);
                break;
            case R.id. conversation_stick:
                //置顶
                break;
        }

        return super.onContextItemSelected(item);
    }

}
