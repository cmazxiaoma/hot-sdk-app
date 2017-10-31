package com.example.administrator.cmazxiaoma.fragment.chatFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.cmazxiaoma.MainActivity;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.event.GroupConversation;
import com.example.administrator.cmazxiaoma.event.ReturnEvent;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;

/**
 * Created by Administrator on 2017/3/1.
 */

public class MoreFragment extends Fragment{
    private Callback mCallback;

    public void setmCallback(Callback mCallback){
        this.mCallback=mCallback;
    }
    //定义一个接口
    public interface Callback{
        void  sendMsg(View v);
    }


    @Subscribe
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chat_fragment_zone,null);
        UtilsLog.i("发现界面----onCreateView");
        EventBus.getDefault().post(new ReturnEvent(1000));
        Button test= (Button) view.findViewById(R.id.test);
        test.setText("我真的是小马啊");
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.sendMsg(v);
                UtilsLog.i("还有我");
            }
        });

        Button cloud= (Button) view.findViewById(R.id.cloud);
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestYun();
            }


        });
        return view;
    }

    private void onRequestYun() {
        // test对应你刚刚创建的云端代码名称
        String cloudCodeName = "Group";
        JSONObject params = new JSONObject();
        GroupConversation gc=new GroupConversation();
        gc.setMsg("我在测试");
        gc.setDate(UtilTools.DatetoTime(new Date()));
        //过滤属性,过滤_c_
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().contains("_c_")|f.getName().contains("increments");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        try {
            JSONObject jsonObject=new JSONObject(gson.toJson(gc));
            jsonObject.put("sender",new JSONObject(gson.toJson(BmobUser.getCurrentUser(MyUser.class))));
            UtilsLog.i("jsonObject="+jsonObject);
            // name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值
            params.put("tableName", "GroupChat");
            params.put("groupChatMsg",jsonObject);
        } catch (JSONException e) {

        }
        // 创建云端代码对象
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        // 异步调用云端代码
        cloudCode.callEndpoint(cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void done(Object o, BmobException e) {
                if(e==null){
                    UtilsLog.i("云端返回的结果="+o.toString());

                }else {
                    UtilsLog.i("云端发生错误,原因="+e.getMessage());
                }
            }
        });
    }


    public static MoreFragment newInstance(String params){
        MoreFragment moreFragment=new MoreFragment();
        Bundle data=new Bundle();
        data.putString("args",params);
        moreFragment.setArguments(data);
        return  moreFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        UtilsLog.i("发现界面----onPauese");
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilsLog.i("发现界面----onResume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UtilsLog.i("发现界面----onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        UtilsLog.i("发现界面----onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        UtilsLog.i("发现界面----onStop");
    }

    @Subscribe
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        UtilsLog.i("发现界面----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UtilsLog.i("发现界面----onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UtilsLog.i("发现界面----onDetach");
    }

    @Subscribe
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        UtilsLog.i("发现界面----onCreate");
    }
}
