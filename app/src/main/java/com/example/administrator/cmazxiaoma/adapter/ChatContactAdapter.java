package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.Friend;
import com.example.administrator.cmazxiaoma.event.FriendDataEvent;
import com.example.administrator.cmazxiaoma.ui.BaseActivity;
import com.example.administrator.cmazxiaoma.utils.UtilPicasso;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ChatContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<FriendDataEvent> mlist;
    private Context mcontext;
    public OnRecyclerViewLongItemClickListener onRecyclerViewLongItemClickListener=null;//长按
    public OnRecyclerViewItemClickListener onRecyclerViewItemClickListener=null;//点击

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewLongItemClickListener(OnRecyclerViewLongItemClickListener onRecyclerViewLongItemClickListener) {
        this.onRecyclerViewLongItemClickListener = onRecyclerViewLongItemClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_contact_item,null);
        //肯定是这个问题，绝对
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new FriendListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  FriendListHolder){
            UtilsLog.i("进行了通讯录适配");
            ((FriendListHolder)holder).frinedName.setText(mlist.get(position).getFriendusername());
            if(mlist.get(position).getFriend_head().equals("")){
                ((FriendListHolder)holder).friendHead.setImageResource(R.drawable.photoload);
            }else {
             UtilTools.setStringtoImage(mcontext, ((FriendListHolder)holder).friendHead,mlist.get(position).getFriend_head());
            }

        }

    }

    public ChatContactAdapter(Context mcontext,List<FriendDataEvent> mlist){
        this.mcontext=mcontext;
        this.mlist=mlist;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    private class FriendListHolder extends RecyclerView.ViewHolder{
        private TextView frinedName;
        private ImageView friendHead;

        public FriendListHolder(View itemView) {
            super(itemView);
            //这里出现过问题
            frinedName= (TextView) itemView.findViewById(R.id.chat_friend_username);
            friendHead= (ImageView) itemView.findViewById(R.id.chat_friend_head);
            //调用点击
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onRecyclerViewItemClickListener!=null){
                        onRecyclerViewItemClickListener.onItemClick(v,getAdapterPosition());
                    }
                }
            });
            //调用长按
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onRecyclerViewLongItemClickListener!=null){
                        onRecyclerViewLongItemClickListener.onLongItemClick(v,getAdapterPosition());
                    }

                    //返回true 就会忽略OnCreateContextMenuListener()函数,那么就不能弹出上下文菜单了
                    return false;
                }
            });
        }
    }


    public interface OnRecyclerViewLongItemClickListener{
        void onLongItemClick(View view,int position);
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,int position);
    }

}
