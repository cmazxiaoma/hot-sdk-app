package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.GirlData;
import com.example.administrator.cmazxiaoma.utils.UtilPicasso;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class GirlAdapter extends BaseAdapter {
    private List<GirlData> mlist;
    private Context mcontext;
    private LayoutInflater inflater;
    private GirlData data;

    public GirlAdapter(Context mcontext,List<GirlData> mlist){
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
            convertView=inflater.inflate(R.layout.girl_item,null);
            viewHolder.girl_img= (ImageView) convertView.findViewById(R.id.girl_img);
            convertView.setTag(viewHolder);

        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        data=mlist.get(position);
        UtilPicasso.loadImageViewHolder(mcontext,data.getUrl(),R.drawable.photoload,R.drawable.photoload,340,280,viewHolder.girl_img);
        return convertView;
    }

    private class ViewHolder{
        private ImageView girl_img;
    }
}
