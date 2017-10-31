package com.example.administrator.cmazxiaoma.fragment.chatFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.ChatContactAdapter;
import com.example.administrator.cmazxiaoma.adapter.ConversationAdapter;
import com.example.administrator.cmazxiaoma.entity.Friend;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.event.FriendDataEvent;
import com.example.administrator.cmazxiaoma.model.UserModel;
import com.example.administrator.cmazxiaoma.model.listener.QueryUserListener;
import com.example.administrator.cmazxiaoma.ui.FriendDataActivity;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.MyItemDecoration;
import com.koushikdutta.async.http.body.JSONArrayBody;
import com.yalantis.phoenix.PullToRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ContactFragment extends Fragment implements PullToRefreshView.OnRefreshListener, ChatContactAdapter.OnRecyclerViewItemClickListener, ChatContactAdapter.OnRecyclerViewLongItemClickListener
{
    private MyUser user;
    private ChatContactAdapter adapter;
    private List<FriendDataEvent> mlist;
    private PullToRefreshView mPullToRefreshView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager llm;
    //添加好友界面固定的4个列表,现在只写一个

    @Subscribe
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chat_fragment_contact,null);
        UtilsLog.i("联系人界面----onCreateView");
        EventBus.getDefault().register(this);
        initView(view);
        initEvent();
        initData();
        return view;
    }


    @Override
    public void onRefresh() {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.setRefreshing(false);
            }
        },2000);
    }


    private void initData() {
        user=BmobUser.getCurrentUser(MyUser.class);
        queryContactData();

    }

    private void queryContactData() {
        BmobQuery query=new BmobQuery("Friend");
        query.addWhereEqualTo("user",user.getUsername());
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if(e==null){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object=jsonArray.getJSONObject(i).getJSONObject("friend");
                            UtilsLog.i("object="+object);
                            UtilsLog.i(object.getString("sex"));
                            UtilsLog.i(object.getString("friendusername"));
                            UtilsLog.i("加载好友列表");
                            mlist.add(new FriendDataEvent(object.getString("id"),object.getString("friendusername"),object.getString("sex"),object.getString("age"),object.getString("desc"),object.getString("friend_head")));
                            EventBus.getDefault().post(true);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(Object event){
        UtilsLog.i("event="+event);
        if(event.equals(true)){
            mPullToRefreshView.setRefreshing(false);
            adapter.notifyItemInserted(mlist.size()-1);
            mRecyclerView.scrollToPosition(mlist.size()-1);
        }

    }

    private void initEvent() {
        mPullToRefreshView.setOnRefreshListener(this);
        mPullToRefreshView.setRefreshing(true);
        adapter.setOnRecyclerViewItemClickListener(this);
        adapter.setOnRecyclerViewLongItemClickListener(this);
    }

    private void initView(View view) {
        mlist=new ArrayList<>();
        mRecyclerView= (RecyclerView) view.findViewById(R.id.contact_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        adapter = new ChatContactAdapter(getActivity(),mlist);
        mRecyclerView.setAdapter(adapter);
        mPullToRefreshView= (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);

    }


    /**
     * 静态工厂方法需要一个string型的值来初始化fragment的参数，
          * 然后返回新的fragment到调用者
        */
         public static ContactFragment newInstance(String params) {
             ContactFragment f = new ContactFragment();
               Bundle args = new Bundle();
               args.putString("args",params);
               f.setArguments(args);
               return f;
         }


    @Override
    public void onPause() {
        super.onPause();
        UtilsLog.i("联系人界面----onPauese");

    }

    @Override
    public void onResume() {
        super.onResume();
        UtilsLog.i("联系人界面----onResume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UtilsLog.i("联系人界面----onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        UtilsLog.i("联系人界面----onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        UtilsLog.i("联系人界面----onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        UtilsLog.i("联系人界面----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UtilsLog.i("联系人界面---onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UtilsLog.i("联系人界面----onDetach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data=getArguments();
        String args=data.getString("args");
        UtilsLog.i("联系人界面----onCreate");
    }


    @Override
    public void onItemClick(View view, int position) {
        FriendDataEvent event=mlist.get(position);
        UtilsLog.i("position="+position);
        Intent intent=new Intent(getActivity(),FriendDataActivity.class);
        Bundle data=new Bundle();
        data.putParcelable("event", event);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(View view, int position) {
        UtilsLog.i("position="+position);
    }
}
