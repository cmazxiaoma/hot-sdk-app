package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.WechatNewsData;
import com.example.administrator.cmazxiaoma.ui.BaseActivity;
import com.example.administrator.cmazxiaoma.utils.UtilPicasso;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 *
 */

public class WechatNewsAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context mcontext;
    private WechatNewsData data;
    private List<WechatNewsData> mlist;

    public WechatNewsAdapter(Context mcontext,List<WechatNewsData> mlist){
        this.mcontext=mcontext;
        this.mlist=mlist;
        inflater= (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.wechat_news_item,null);
            viewHolder.img= (ImageView) convertView.findViewById(R.id.wechat_news_img);
            viewHolder.title= (TextView) convertView.findViewById(R.id.wechat_news_title);
            viewHolder.source= (TextView) convertView.findViewById(R.id.wechat_news_source);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();

        }

        data=mlist.get(position);
        String imgurl=data.getFirstImg();
        if(imgurl!=null)
        {
            //加载图片,用picasso,Apdater的重用会被自动检测到，Picasso会取消上次的加载
            UtilPicasso.loadImageViewSize(mcontext,imgurl,260,120,viewHolder.img);
        }
        viewHolder.title.setText(data.getTitle());
        viewHolder.source.setText(data.getSource());


        return convertView;
    }

    private class ViewHolder{
        private ImageView img;
        private TextView title,source;

    }
}
