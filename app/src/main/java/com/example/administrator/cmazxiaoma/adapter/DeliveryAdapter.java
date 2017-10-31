package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.DeliveryData;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */

public class DeliveryAdapter extends BaseAdapter {

    private Context mcontext;
    private List<DeliveryData> mlist;
    //布局加载器
    private LayoutInflater inflater;
    private DeliveryData data;
    //构造方法
    public DeliveryAdapter(Context mcontext,List<DeliveryData> mlist){
        this.mcontext=mcontext;
        this.mlist=mlist;
        //获取系统服务
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
        //判断是否第一次加载
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.delivery_item,null);
            viewHolder.query_delivery_date= (TextView) convertView.findViewById(R.id.query_delivery_date);
            viewHolder.query_delivery_context= (TextView) convertView.findViewById(R.id.query_delivery_context);
            //设置缓存
            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //设置数据
        data=mlist.get(position);
        viewHolder.query_delivery_date.setText(data.getTime());
        viewHolder.query_delivery_context.setText(data.getContext());
        return convertView;
    }

    //内部类 缓存listView中的数据
    private class ViewHolder {
        private TextView query_delivery_date;
        private TextView query_delivery_context;

    }
}
