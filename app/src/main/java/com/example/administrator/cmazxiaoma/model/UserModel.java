package com.example.administrator.cmazxiaoma.model;

import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.model.listener.QueryUserListener;
import com.example.administrator.cmazxiaoma.model.listener.UpdateCacheListener;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/3/1.
 */

public class UserModel extends BaseModel{
    /**单例模式 保证一个类仅有一个实例，并提供一个访问它的全局访问点
     * 适用场景:
     * 1.应用中每次启动只会存在一个实例
     * 2.应用中某个实例对象需要频繁的被访问
     *
     *利用static关键字在类加载时就会初始化,并缓存到静态内存中特点
     * 确保了调用getInstance()不会为null
     *饿汉式
     * 优点:能够在线程安全下实现单例
     * 缺点:由于类一加载，就会浪费系统资源
    */
    private static final  UserModel ourInstance=new UserModel();
    //对外暴露的接口，获得实例
    public static UserModel getInstance() {
        return ourInstance;
    }
    //私有构造法，防止被实例化，避免外部访问,适用反射还是可以访问的，相对安全
    private UserModel(){}

    /**根据id或者username 查询用户信息**/
    public void queryUserInfo(String objectId,String username,final QueryUserListener listener){
        BmobQuery<MyUser> query=new BmobQuery<>();
        //如果objectId为空，那么就执行根据id来查询用户信息
        if(objectId==null){
            query.addWhereEqualTo("username",username);
            //否则就根据username 来查询用户信息
        }else{
            query.addWhereEqualTo("objectId",objectId);
        }
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()>0){
                        listener.internalDone(list.get(0),null);
                    }else{
                        listener.internalDone(new BmobException(000,"查询无此人"));
                    }

                }else{
                    listener.internalDone(new BmobException(e));
                }
            }
        });

    }


    /*****更新用户信息**********
    更新消息发送方A的用户信息*/
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener){
        final BmobIMConversation conversation=event.getConversation();
        final BmobIMUserInfo info=event.getFromUserInfo();
        final BmobIMMessage msg=event.getMessage();
        String username=info.getName();
        String title=conversation.getConversationTitle();
        UtilsLog.i("会话标题="+title+",发送方:="+username);
        /*******************/
        /**
         * 判断是否需要更新,判断依据:sdk内部，对于单独会话来说,是用objectId来表示新会话标题的Id
         * 因此需要对比用户名和会话标题,两者不一样，则需要更新用户信息
         */
        if(!username.equals(title)) {
            UtilsLog.i("进行比较");
            UtilsLog.i("发现了这个用户的新老head不一样");
            //第二步查询用户信息,是根据id来查询信息的
            UserModel.getInstance().queryUserInfo(info.getUserId(), null, new QueryUserListener() {
                public void done(MyUser s, BmobException e) {
                    if (e == null) {
                        String name = s.getUsername();
                        String avater = s.getUser_img();
                        conversation.setConversationIcon(avater);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avater);
                        //第三步:调用updateUserInfo方法更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //如果消息是暂离消息,则不更新会话资料
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }

                    } else {
                        UtilsLog.i("更新消息发送A的用户资料失败,原因=" + e);
                    }
                    listener.done(null);
                }

            });

        }listener.internalDone(null);
    }
    }

