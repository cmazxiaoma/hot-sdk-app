package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.ButlerChatData;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/19.
 *
 */

public class ButlerChatListApater extends BaseAdapter{
    //左边的text
    public  static  final int  VALUE_LEFT_TEXT=1;
    //右边的text
    public  static final int VALUE_RIGHT_TEXT=2;
    private Context mcontext;
    private LayoutInflater inflater;
    private ButlerChatData data;
    private List<ButlerChatData> mlist;


    public ButlerChatListApater(Context mcontext,List<ButlerChatData>mlist){
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
        ViewHolderLeftText viewHodlerLeftText=null;
        ViewHolderRightText viewHolderRightText=null;
        //获取当前要显示的type，根据这个type来区别数据的加载
        int type=getItemViewType(position);
        //判断是否是第一次加载
        if(convertView==null){
            UtilsLog.i("第一次加载ButlerCharListAdapter");
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHodlerLeftText=new ViewHolderLeftText();
                    convertView=inflater.inflate(R.layout.butler_chat_item_left,null);
                    viewHodlerLeftText.chat_left_text= (TextView) convertView.findViewById(R.id.chat_left_text);
                    convertView.setTag(viewHodlerLeftText);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText=new ViewHolderRightText();
                    convertView=inflater.inflate(R.layout.butler_chat_item_right,null);
                    viewHolderRightText.chat_right_text= (TextView) convertView.findViewById(R.id.chat_right_text);
                    viewHolderRightText.chat_user_img= (CircleImageView) convertView.findViewById(R.id.chat_user_img);
                    convertView.setTag(viewHolderRightText);
                    break;
            }

        }else{
            //如果不是第一次加载,直接从缓存里面拿出数据
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHodlerLeftText= (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText= (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }
        //赋值
        data=mlist.get(position);
        switch (type){
            case VALUE_LEFT_TEXT:
                viewHodlerLeftText.chat_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                //设置登录用户和机器人聊天的头像
                //如果用户没有设置过头像，那么就用默认头像
                if(BmobUser.getCurrentUser(MyUser.class).getUser_img()==null)
                {
                    viewHolderRightText.chat_user_img.setImageResource(R.drawable.add_pic);
                }else
                {
                    UtilTools.setStringtoImage(mcontext,viewHolderRightText.chat_user_img, BmobUser.getCurrentUser(MyUser.class).getUser_img());
                }

                viewHolderRightText.chat_right_text.setText(data.getText());
                break;
        }

        return convertView;
    }
     //根据数据源的position 来返回显示的item
    @Override
    public int getItemViewType(int position) {
        ButlerChatData data=mlist.get(position);
        int type=data.getType();
        return type ;
    }

    //返回所有的layout数量

    @Override
    public int getViewTypeCount() {
        return 3; //一般是mlist.size()1
    }
    //左边的文本
    private class ViewHolderLeftText{
        private TextView chat_left_text;
    }

    //右边的文本
   private class ViewHolderRightText{
        private TextView chat_right_text;
        private CircleImageView chat_user_img;
    }
}
