package com.example.administrator.cmazxiaoma.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.event.ConversationFriend;
import com.example.administrator.cmazxiaoma.event.FriendDataEvent;
import com.example.administrator.cmazxiaoma.event.GroupConversation;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import java.util.List;

/**
 * Created by Administrator on 2017/3/4.
 */

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ConversationFriend> mlist;
    private final int TYPE_ITEM=0;
    private final int TYPE_FOOTER=1;
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
        if(viewType==TYPE_ITEM){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item,null);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ItemViewHolder(view);
        }else{
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_footerview,null);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FootViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //instanceof是双目运算符,左边的操作是一个对象，右边是一个类，当左边的对象是右面的类创建的对象时，该结果是true
        if(holder instanceof ItemViewHolder){
                //单聊
              if(mlist.get(position).getType()==ConversationFriend.FRIEND_CHAT){
                  UtilsLog.i("ConversationApdater,position=" + position);
                  ConversationFriend event = mlist.get(position);
                  //去掉以前第position 缓存的朋友图像
                  ((ItemViewHolder) holder).friendHead.setImageBitmap(null);
                  ((ItemViewHolder) holder).friendUsername.setText(event.getFriendUsername());
                  if (event.getFriendHead() == null) {
                      ((ItemViewHolder) holder).friendHead.setImageResource(R.drawable.photoload);
                  }
                  UtilTools.setStringtoImage(mcontext, ((ItemViewHolder) holder).friendHead, event.getFriendHead());
                  ((ItemViewHolder) holder).last_msg.setText(event.getLast_msg());
                  ((ItemViewHolder) holder).last_date.setText(event.getLast_date());

                  //群聊
              }else  if(mlist.get(position).getType()==ConversationFriend.GROUP_CHAT){
                  ConversationFriend event = mlist.get(position);
                  UtilsLog.i("apater接收到群聊"+mlist.get(position).toString());
                  ((ItemViewHolder) holder).last_msg.setText(event.getLast_msg());
                  ((ItemViewHolder) holder).last_date.setText(event.getLast_date());
                  ((ItemViewHolder)holder).friendUsername.setText(event.getFriendUsername());
                  //先清空图片资源
                  ((ItemViewHolder)holder).friendHead.setImageBitmap(null);
                  ((ItemViewHolder)holder).friendHead.setImageResource(R.drawable.ic_group);
              }


        }

    }


    public ConversationAdapter(Context mcontext,List<ConversationFriend> mlist){
        this.mcontext=mcontext;
        this.mlist=mlist;
    }



    //RecyclerView的count设置为数据的总条数+1，因为还有footerView
    @Override
    public int getItemCount() {
        return mlist.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        //最后一个item设置为footerView
        if(position+1==getItemCount()){
            return  TYPE_FOOTER;
        }else {
            return  TYPE_ITEM;
        }

    }

     private class FootViewHolder extends RecyclerView.ViewHolder{
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

     private   class ItemViewHolder extends RecyclerView.ViewHolder{
         //这是单聊的界面,
        TextView last_date,last_msg,friendUsername;
        ImageView friendHead;
         LinearLayout single_chat;


        public ItemViewHolder(View itemView) {
            super(itemView);
            //单聊和群聊
            single_chat= (LinearLayout) itemView.findViewById(R.id.single_chat);
            friendHead= (ImageView) itemView.findViewById(R.id.conversation_friendHead);
            friendUsername= (TextView) itemView.findViewById(R.id.conversation_friendUsername);
            last_date= (TextView) itemView.findViewById(R.id.conversation_last_date);
            last_msg= (TextView) itemView.findViewById(R.id.conversation_last_msg);
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

    //Item的点击事件


}
