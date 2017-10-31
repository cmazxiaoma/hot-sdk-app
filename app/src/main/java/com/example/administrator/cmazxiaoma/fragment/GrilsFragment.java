package com.example.administrator.cmazxiaoma.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.GirlAdapter;
import com.example.administrator.cmazxiaoma.entity.GirlData;
import com.example.administrator.cmazxiaoma.ui.GirlPhotoActivity;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilPicasso;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/2/11.
 */

public class GrilsFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private GridView mGridView;
    private List<GirlData> mlist=new ArrayList<>();
    private ArrayList<String> girl_url_list=new ArrayList<>();
    private CustomDialog dialog_girl;
    private ImageView girl_photo;
    private PhotoViewAttacher photoViewAttacher;
    private SwipeRefreshLayout mSwipeRefresh;
    private GirlAdapter adapter;
    private int REFRESH_TIME;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==StaticClass.REFRESH_COMPLETE){
                REFRESH_TIME++;
                if(REFRESH_TIME==5){
                    REFRESH_TIME=1;
                }
                parsingImg(REFRESH_TIME);
                mSwipeRefresh.setRefreshing(false);

            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_girl,null);
        findView(view);
        return  view;
    }

    private void findView(View view) {
        mSwipeRefresh= (SwipeRefreshLayout) view.findViewById(R.id.girl_mSwipeRefreshLayout);
        mSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        mSwipeRefresh.setOnRefreshListener(this);
        mGridView= (GridView) view.findViewById(R.id.girl_gridview);
        mGridView.setOnItemClickListener(this);
       // dialog_girl=new CustomDialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,R.layout.dialog_girl,R.style.dialog_girl_Theme, Gravity.CENTER,R.style.pop_anim_style);
        //点击其他地方，dialog就消失了
        //dialog_girl.setCanceledOnTouchOutside(true);
        //girl_photo= (ImageView) dialog_girl.findViewById(R.id.photo_girl);
        adapter=new GirlAdapter(getActivity(),mlist);
        mGridView.setAdapter(adapter);
        parsingImg(1);
    }

    private void parsingImg(int page) {
        try {
            String welfare= URLEncoder.encode(getResources().getString(R.string.welfare),"UTF-8");
            String wefare_url="http://gank.io/api/data/"+welfare+"/50/"+page;
            RxVolley.get(wefare_url, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    try {
                        JSONObject object=new JSONObject(t);
                        //UtilsLog.i("美女图片 t="+t);
                        String  error=object.getString("error");
                        if(error.equals("false")){
                            JSONArray results=object.getJSONArray("results");
                            for(int i=0;i<results.length();i++){
                                String url=results.getJSONObject(i).getString("url");
                                GirlData data=new GirlData();
                                data.setUrl(url);
                                girl_url_list.add(url);
                                mlist.add(data);
                                //倒序,让最新的数据在上面
                                Collections.reverse(mlist);
                                Collections.reverse(girl_url_list);
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            UtilsLog.i("美女相册解析失败,error="+error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        UtilPicasso.loadImageView(getActivity(),girl_url_list.get(position),girl_photo);
        photoViewAttacher=new PhotoViewAttacher(girl_photo);
        //刷新
        photoViewAttacher.update();
        dialog_girl.show();*/
        startActivity(new Intent(getActivity(), GirlPhotoActivity.class).putStringArrayListExtra("girl_photo",girl_url_list).putExtra("position",position));

    }

    @Override
    public void onRefresh() {
        if(UtilTools.isNetworkAvailable(getActivity())) {
            mHandler.sendEmptyMessageDelayed(StaticClass.REFRESH_COMPLETE, 2000);
        }
        else{
            UtilsLog.i("网络连接失败");
            mSwipeRefresh.setRefreshing(false);
            UtilTools.Toast(getActivity(),"网络连接失败");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilsLog.i("girlFragment----onDestroy");
    }
}
