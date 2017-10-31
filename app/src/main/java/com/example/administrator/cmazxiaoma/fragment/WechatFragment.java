package com.example.administrator.cmazxiaoma.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.WechatNewsAdapter;
import com.example.administrator.cmazxiaoma.entity.WechatNewsData;
import com.example.administrator.cmazxiaoma.ui.WechatNewDetailActivity;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/2/11.
 */

public class WechatFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView wechat_listview;
    private  WechatNewsAdapter adapter;
    private List<WechatNewsData> mlist=new ArrayList<>();
    //储存文章标题
    private List<String> title_list=new ArrayList<>();
    //存储文章地址
    private List<String> url_list=new ArrayList<>();
    private SwipeRefreshLayout mSwipe;
    //记录刷新的次数
    private int REFRESH_TIME;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.REFRESH_COMPLETE:
                    REFRESH_TIME++;
                    parsingWechatNews(REFRESH_TIME,20);
                    UtilsLog.i("通知新闻更新");
                    mSwipe.setRefreshing(false);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_wechat,null);
        findView(view);
        return view;
    }
    private void findView(View view) {
        wechat_listview= (ListView) view.findViewById(R.id.wechat_mlistView);
        wechat_listview.setOnItemClickListener(this);
        mSwipe= (SwipeRefreshLayout) view.findViewById(R.id.Wechat_mSwipeRefreshLayout);
        mSwipe.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        mSwipe.setOnRefreshListener(this);
        //先设置一个空的适配器
        adapter=new WechatNewsAdapter(getActivity(),mlist);
        wechat_listview.setAdapter(adapter);
        parsingWechatNews(1,100);

    }

    //page代表当前页数，ps代表每页返回的条数
    private void parsingWechatNews(int page,int ps) {
        HttpParams params=new HttpParams();
        params.put("pno",page);
        params.put("ps",ps);
        params.put("key",StaticClass.WECHAT_NEWS_APP_KEY);
        String url= StaticClass.wechat_news_api;
        RxVolley.get(url,params,new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject object=new JSONObject(t);
                    String reason=object.getString("reason");
                    UtilsLog.i("微信精选返回的reason="+reason);
                    if(reason.equals(getResources().getString(R.string.wechat_news_parse_success)))
                    {
                        JSONObject result=object.getJSONObject("result");
                        JSONArray list=result.getJSONArray("list");
                        for(int i=0;i<list.length();i++){
                            WechatNewsData  data=new WechatNewsData();
                            String id=list.getJSONObject(i).getString("id");
                            String firstImg=list.getJSONObject(i).getString("firstImg");
                            String source=list.getJSONObject(i).getString("source");
                            String url=list.getJSONObject(i).getString("url");
                            String title=list.getJSONObject(i).getString("title");
                            if(!TextUtils.isEmpty(firstImg)&&!TextUtils.isEmpty(source)&&!TextUtils.isEmpty(url)&&!TextUtils.isEmpty(title))
                            {
                                data.setId(id);
                                data.setFirstImg(firstImg);
                                data.setSource(source);
                                data.setTitle(title);
                                data.setUrl(url);
                                title_list.add(title);
                                url_list.add(url);
                                mlist.add(data);
                                Collections.reverse(title_list);
                                Collections.reverse(url_list);
                                Collections.reverse(mlist);
                            }else{
                                UtilsLog.i("微信精选文章返回的数据其中一项为空");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        UtilsLog.i("解析新闻失败,错误码:"+reason);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UtilsLog.i("文章的position="+position);
       startActivity(new Intent(getActivity(), WechatNewDetailActivity.class).putExtra("title",title_list.get(position)).putExtra("url",url_list.get(position)));
    }

    @Override
    public void onRefresh() {
        if(UtilTools.isNetworkAvailable(getActivity())) {
            mHandler.sendEmptyMessageDelayed(StaticClass.REFRESH_COMPLETE, 2000);
        }
        else{
            UtilsLog.i("网络连接失败");
            mSwipe.setRefreshing(false);
            UtilTools.Toast(getActivity(),"网络连接失败");
        }
    }


}
