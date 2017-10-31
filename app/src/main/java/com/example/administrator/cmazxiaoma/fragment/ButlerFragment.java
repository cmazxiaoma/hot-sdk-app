package com.example.administrator.cmazxiaoma.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.adapter.ButlerChatListApater;
import com.example.administrator.cmazxiaoma.entity.ButlerChatData;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.service.SmsService;
import com.example.administrator.cmazxiaoma.utils.MyDatabaseHelper;
import com.example.administrator.cmazxiaoma.utils.StaticClass;
import com.example.administrator.cmazxiaoma.utils.UtilSharePre;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2017/2/11.
 */

public class ButlerFragment extends Fragment implements View.OnClickListener, TextWatcher, View.OnTouchListener {
    private ListView butler_mlistView;
    private List<ButlerChatData> mlist = new ArrayList<>();
    private ButlerChatListApater adapter;
    private EditText user_msg;
    private Button btn_send_user_msg;
    private MyDatabaseHelper databaseHelper;
    private MyUser user;
    private SpeechSynthesizer mTts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, null);
        //创建MyDatabaseHelper对象，指定数据库版本为1，此处使用相对路径即可
        //数据库文件会自动保存在程序的数据文件夹的databases目录下
        databaseHelper = new MyDatabaseHelper(getActivity(), "cm_butler_chat.dp3", 1);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        user = BmobUser.getCurrentUser(MyUser.class);
        butler_mlistView = (ListView) view.findViewById(R.id.butler_chat_mlistView);
        butler_mlistView.setOnTouchListener(this);
        user_msg = (EditText) view.findViewById(R.id.butler_chat_user_msg);
        btn_send_user_msg = (Button) view.findViewById(R.id.btn_butler_send_msg);
        btn_send_user_msg.setOnClickListener(this);
        user_msg.addTextChangedListener(this);
        mlist = (List<ButlerChatData>) queryData();
        //设置设备器
        adapter = new ButlerChatListApater(getActivity(), mlist);
        butler_mlistView.setAdapter(adapter);
        //机器人先说话
        addLeftItem(getResources().getString(R.string.butler_start_msg));
        //获取sharePreference里面储存的是否开启短信监听
        boolean isChecked_sms=UtilSharePre.getBoolean(getActivity(),"sms_switch",false);
        if(isChecked_sms){
            getActivity().stopService(new Intent(getActivity(),SmsService.class));
        }else{
         getActivity().stopService(new Intent(getActivity(),SmsService.class));
        }
    }

    private void startSpeak(String text) {
        mTts.startSpeaking(text, mSynListener);
    }

    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_butler_send_msg:
                //隐藏输入法
                //UtilTools.HideSoftInputViewGroup(getActivity(),butler_mlistView);
                String  info=user_msg.getText().toString().trim();
                //清空输入框
                user_msg.setText("");
                //显示出用户说出来的话
                addRightItem(info);
                //post
                HttpParams params=new HttpParams();
                 params.put("key",StaticClass.TULING_APP_KEY);
                params.put("info",info);
                RxVolley.post(StaticClass.tuling_api, params, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        UtilsLog.i("图灵机器人="+t);
                        try {
                            JSONObject jsonObject=new JSONObject(t);
                            int code=jsonObject.getInt("code");
                            if(code==100000){
                                String  text=jsonObject.getString("text");
                                UtilsLog.i("图灵机器人返回的="+text);
                                addLeftItem(text);
                            }else if(code==200000){
                                String text=jsonObject.getString("text");
                                String url=jsonObject.getString("url");
                                addLeftItem(text+" 链接:"+url);

                            }
                            else{
                                UtilsLog.i("图灵机器人连接失败");
                                addLeftItem("[图灵机器人连接失败]-异常码="+code);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            break;
        }

    }

    public List<ButlerChatData> queryData(){
        //查询数据库
        Cursor cursor=databaseHelper.getReadableDatabase().rawQuery("select * from user_butler_chat_record where user= ? ",new String[]{user.getUsername()});
        if(cursor==null){
            return  null;

        }
       List<ButlerChatData>mlist=DataToBulterChatData(cursor);
        cursor.close();
        return  mlist;

    }

    public void insertData(SQLiteDatabase db, String username,String text, int type) {
        ContentValues values=new ContentValues();
        values.put("user",username);
        values.put("text",text);
        values.put("type",type);
        long rowId=db.insert("user_butler_chat_record",null,values);
        if(rowId!=-1)
        {
            UtilsLog.i("插入聊天记录数据="+rowId);
        }else{
            UtilsLog.i("插入聊天记录数据失败");
        }
    }


    private List<ButlerChatData>DataToBulterChatData(Cursor cursor) {
        List<ButlerChatData> mlist=new ArrayList<>();
        while(cursor.moveToNext()){
            ButlerChatData data=new ButlerChatData();
            data.setText(cursor.getString(2));  //这里要记住啊，出现大问题了
            data.setType(cursor.getInt(3));
            mlist.add(data);
        }
        return mlist;
    }

    //添加左边文本
    private void addLeftItem(String text){
        boolean butler_switch=UtilSharePre.getBoolean(getActivity(),"butler_switch",false);
        if(butler_switch){
            startSpeak(text);
        }
        ButlerChatData data=new ButlerChatData();
        data.setType(ButlerChatListApater.VALUE_LEFT_TEXT);
        data.setText(text);
        mlist.add(data);
        //向数据库插入数据
        insertData(databaseHelper.getReadableDatabase(),user.getUsername(),text,ButlerChatListApater.VALUE_LEFT_TEXT);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚到到底部
        butler_mlistView.setSelection(butler_mlistView.getBottom());
    }
    //添加右边文本
    private void addRightItem(String text){
        ButlerChatData data=new ButlerChatData();
        data.setType(ButlerChatListApater.VALUE_RIGHT_TEXT);
        data.setText(text);
        mlist.add(data);
        //插入数据
        insertData(databaseHelper.getReadableDatabase(),user.getUsername(),text,ButlerChatListApater.VALUE_RIGHT_TEXT);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        butler_mlistView.setSelection(butler_mlistView.getBottom());
    }

    //后台强制结束进程是不会回调onDestroy方法的

    public void onDestroy(){
        super.onDestroy();
        UtilsLog.d("ButlerFragment----onDestroy");
        //当在聊天窗口 程序被强制结束，会回调onDestroy
        BmobIM.getInstance().disConnect();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(user_msg.getText().toString().trim())){
            btn_send_user_msg.setEnabled(true);
        }else{
            btn_send_user_msg.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        UtilTools.HideSoftInputViewGroup(getActivity(),butler_mlistView);
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        UtilsLog.i("机器人聊天界面----omPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilsLog.i("机器人聊天界面----onResume");
    }
}
