package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.ChatMsg;
import com.example.administrator.cmazxiaoma.entity.GroupChat;
import com.example.administrator.cmazxiaoma.event.ConversationFriend;
import com.example.administrator.cmazxiaoma.utils.UtilPicasso;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import java.security.acl.Group;
import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<ChatMsg> mlist;
    private List<GroupChat>mlist_1;
    private Context mcontext;
    private String MyHead;
    private String friendHead_1;
    private int  chatType;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_chat_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //单聊
        if(chatType== ConversationFriend.FRIEND_CHAT){
            ChatMsg msg=mlist.get(position);
            if(msg.getType()==ChatMsg.TYPE_RECEIVE){
                UtilTools.setStringtoImage(mcontext,holder.friendHead,friendHead_1);
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftContent.setText(msg.getContent());
                holder.date.setText(msg.getDate());
            }else if(msg.getType()==ChatMsg.TYPE_SEND){
                UtilTools.setStringtoImage(mcontext,holder.MyHead,MyHead);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.rightContent.setText(msg.getContent());
                holder.date.setText(msg.getDate());
            }
            //群聊
        }else{
            GroupChat msg=mlist_1.get(position);
            if(msg.getType()==GroupChat.TYPE_RECEIVE){
                UtilsLog.i("进行了group适配");
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftContent.setText(msg.getContent());
                holder.friendName.setText(msg.getFriendName());
                UtilTools.setStringtoImage(mcontext,holder.friendHead,msg.getFriendHead());
                holder.date.setText(msg.getDate());
            }else if(msg.getType()==GroupChat.TYPE_SEND){
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.rightContent.setText(msg.getContent());
                UtilTools.setStringtoImage(mcontext,holder.MyHead,MyHead);
                holder.date.setText(msg.getDate());
            }
        }

    }



    public MsgAdapter(Context mcontext, List<ChatMsg>mlist, List<GroupChat>mlist_1, String myHead,String friendHead_1, int chaType){
        this.mcontext=mcontext;
        this.mlist=mlist;
        this.mlist_1=mlist_1;
        this.MyHead=myHead;
        this.chatType=chaType;
        this.friendHead_1=friendHead_1;
    }

    @Override
    public int getItemCount() {
       if(chatType==ConversationFriend.FRIEND_CHAT){
           return mlist.size();
       }else {
           return mlist_1.size();
       }
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout,rightLayout;
        TextView leftContent,rightContent,friendName,date;
        ImageView  friendHead,MyHead;

         public ViewHolder(View itemView) {
            super(itemView);
             date= (TextView) itemView.findViewById(R.id.friend_chat_date);
             friendName= (TextView) itemView.findViewById(R.id.friend_chat_friendName);
             leftLayout= (LinearLayout) itemView.findViewById(R.id.friend_chat_leftlayout);
            rightLayout= (LinearLayout) itemView.findViewById(R.id.friend_chat_rightLayout);
            leftContent= (TextView) itemView.findViewById(R.id.friend_chat_left_content);
            rightContent= (TextView) itemView.findViewById(R.id.friend_chat_right_content);
             friendHead= (ImageView) itemView.findViewById(R.id.friend_chat__friendHead);
             MyHead= (ImageView) itemView.findViewById(R.id.friend_chat_myUserHead);
         }
    }
}
