package com.example.administrator.cmazxiaoma.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.administrator.cmazxiaoma.R;
import com.example.administrator.cmazxiaoma.entity.MyUser;
import com.example.administrator.cmazxiaoma.ui.LoginActivity;
import com.example.administrator.cmazxiaoma.utils.UtilSharePre;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;
import com.example.administrator.cmazxiaoma.view.CustomDialog;
import java.io.File;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/11.
 */

public class UserFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, View.OnTouchListener {
    private Button btn_exit_login,btn_confirm_edit;
    private CheckBox male,female;
    private EditText age,desc,username;
    private TextView edit_user_data;
    private   MyUser user;
    private Button cancel,camera,photogallery;
    private LinearLayout user_linearlayout;
    //圆形头像
    private CircleImageView user_image;
    private CustomDialog customDialog,dialog_exit;
    private Button dialog_exit_pos,dialog_exit_neg;
    private static final String IMAGE_FILE_NAME="cmImg.png";
    private static final int  CAMERA_REQUEST_CODE=0x123;
    private static final int  PHOTO_GALLERY_REQUEST_CODE=0x456;
    private static final int  IMAGE_REQUEST_CODE=0X678;
    private File tempFile;
    private TabLayout mTablayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,null);
        findView(view);
        return view;
    }

    private void findView(View view){
        //获取tablayout
        mTablayout= (TabLayout) view.findViewById(R.id.mTablayout);
        //初始化退出登录的dialog
        dialog_exit=new CustomDialog(getActivity(),500,WindowManager.LayoutParams.WRAP_CONTENT,R.layout.dialog_exit,R.style.dialog_Theme,Gravity.CENTER,R.style.pop_anim_style);
        dialog_exit.setCanceledOnTouchOutside(true);
        dialog_exit_pos= (Button) dialog_exit.findViewById(R.id.dialog_exit_positionButton);
        dialog_exit_neg= (Button) dialog_exit.findViewById(R.id.dialog_exit_negativeButton);
        dialog_exit_pos.setOnClickListener(this);
        dialog_exit_neg.setOnClickListener(this);
        user_linearlayout= (LinearLayout) view.findViewById(R.id.user_linearlayout);
        user_linearlayout.setOnTouchListener(this);
        //初始化上传照片的dialog
        customDialog=new CustomDialog(getActivity(),WindowManager.LayoutParams.MATCH_PARENT,450,R.layout.dialog_photo,R.style.dialog_Theme,Gravity.BOTTOM,R.style.pop_anim_style);
        customDialog.setCanceledOnTouchOutside(true);
        camera= (Button) customDialog.findViewById(R.id.camera);
        photogallery= (Button) customDialog.findViewById(R.id.photogallery);
        cancel= (Button) customDialog.findViewById(R.id.cancel);
        //设置方法
        camera.setOnClickListener(this);
        photogallery.setOnClickListener(this);
        cancel.setOnClickListener(this);
        user_image= (CircleImageView) view.findViewById(R.id.user_image);
        user_image.setOnClickListener(this);
        btn_exit_login= (Button) view.findViewById(R.id.btn_exit_login);
        btn_confirm_edit= (Button) view.findViewById(R.id.btn_confirm_edit);
        male= (CheckBox) view.findViewById(R.id.male);
        female= (CheckBox) view.findViewById(R.id.female);
        age= (EditText) view.findViewById(R.id.edit_user_age);
        desc= (EditText) view.findViewById(R.id.edit_user_desc);
        username=(EditText)view.findViewById(R.id.edit_username);
        edit_user_data= (TextView) view.findViewById(R.id.edit_user_data);
        btn_exit_login.setOnClickListener(this);
        btn_confirm_edit.setOnClickListener(this);
        edit_user_data.setOnClickListener(this);
        male.setOnCheckedChangeListener(this);
        female.setOnCheckedChangeListener(this);
        btn_confirm_edit.setOnClickListener(this);
        //editText监听
        age.addTextChangedListener(this);
        desc.addTextChangedListener(this);
        //获取登录成功的用户信息
        user=BmobUser.getCurrentUser(MyUser.class);
        username.setText(user.getUsername());
        if(TextUtils.isEmpty(user.getDesc())){
            desc.setText("这个人很懒，什么也没有留下");
        }else{
            desc.setText(user.getDesc());
        }

        if(user.getAge()==0||user.getAge()<0){
            age.setText(""+getResources().getInteger(R.integer.default_age));
        }else{
            age.setText(""+user.getAge());
        }

        if(user.isSex()){
            male.setChecked(true);
        }else{
            female.setChecked(true);
        }
        username.setEnabled(false);
        female.setEnabled(false);
        male.setEnabled(false);
        desc.setEnabled(false);
        age.setEnabled(false);
        //获取sharePreference 保存的用户的头像
        //String imgString=UtilSharePre.getString(getActivity(),"user_image",null);
        String imgString=user.getUser_img();
        UtilTools.setStringtoImage(getActivity(),user_image,imgString);
        UtilSharePre.putString(getActivity(),"user_image",imgString);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_exit_login:
                dialog_exit.show();
                break;
            case R.id.dialog_exit_positionButton:
                MyUser.logOut();//清楚缓冲用户对象
                MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
                if(currentUser==null){
                    dialog_exit.dismiss();
                    UtilTools.Toast(getActivity(),"退出成功");
                    UtilsLog.i("currentUser="+currentUser);
                    //清除所有会话
                    BmobIM.getInstance().clearAllConversation();
                    //注销IM服务器
                    //BmobIM.getInstance().disConnect();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
                break;
            case R.id.dialog_exit_negativeButton:
                UtilsLog.i("点击取消");
                dialog_exit.dismiss();
                break;
            case R.id.edit_user_data:
                btn_confirm_edit.setVisibility(View.VISIBLE);
                female.setEnabled(true);
                male.setEnabled(true);
                desc.setEnabled(true);
                age.setEnabled(true);
                break;
            case R.id.btn_confirm_edit:
                int new_age=Integer.parseInt(age.getText().toString());
                String new_desc=desc.getText().toString();
                Boolean new_sex=male.isChecked();
                MyUser new_user=new MyUser();
                new_user.setDesc(new_desc);
                new_user.setAge(new_age);
                new_user.setSex(new_sex);
                new_user.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            UtilTools.Toast(getActivity(),"修改成功");
                            female.setEnabled(false);
                            male.setEnabled(false);
                            desc.setEnabled(false);
                            age.setEnabled(false);
                            btn_confirm_edit.setVisibility(View.INVISIBLE);
                        }else{
                            UtilTools.Toast(getActivity(),"修改失败,原因="+e.getMessage());
                        }
                    }
                });
                break;
            case R.id.user_image:
                customDialog.show();
                break;
            case R.id.cancel:
                customDialog.dismiss();
                break;
            case R.id.camera:
                toCamera();
                break;
            case R.id.photogallery:
                toPhotogallery();
                break;

        }

    }

   //跳转相册
    private void toPhotogallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PHOTO_GALLERY_REQUEST_CODE);
        customDialog.dismiss();
    }
    //跳转相机
    private void toCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        customDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!=0){
            switch (requestCode){
                //图库返回码
                case PHOTO_GALLERY_REQUEST_CODE:
                    if(data!=null){
                        startPhotoZoom(data.getData());
                    }
                    else{
                        String imgString= UtilSharePre.getString(getActivity(),"user_image",null);
                        if(imgString==null) {
                            user_image.setImageResource(R.drawable.add_pic);
                        }else{
                            UtilTools.setStringtoImage(getActivity(),user_image,imgString);
                        }
                    }
                    break;
                //相机返回码
                case CAMERA_REQUEST_CODE:
                    if(data!=null){
                        tempFile=new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    }else{
                        String imgString= UtilSharePre.getString(getActivity(),"user_image",null);
                        if(imgString==null) {
                            user_image.setImageResource(R.drawable.add_pic);
                        }else{
                            UtilTools.setStringtoImage(getActivity(),user_image,imgString);
                        }
                    }
                    break;
                //拿到图片，设置
                case IMAGE_REQUEST_CODE:
                    if(data !=null){
                        setImagetoCircleImageView(data);
                        //既然已经设置了图片，删除原先按个图片文件
                        if(tempFile!=null){
                            tempFile.delete();
                        }
                    }else{
                        String imgString= UtilSharePre.getString(getActivity(),"user_image",null);
                        if(imgString==null) {
                            user_image.setImageResource(R.drawable.add_pic);
                        }else{
                            UtilTools.setStringtoImage(getActivity(),user_image,imgString);
                        }
                    }
                    break;
            }

        }

    }
    private void setImagetoCircleImageView(Intent data) {
        Bundle bundle=data.getExtras();
        if(bundle!=null){
            //重点
            Bitmap bitmap=bundle.getParcelable("data");//序列化的值
            user_image.setImageBitmap(bitmap);
            String imgString=UtilTools.getImagetoString(getActivity(),user_image);
            //本地加载用户头像,UtilSharePre.putString(getActivity(),"user_image",imgString);
             UtilsLog.i("进行了图片转换成String");
            //向Bmon后端云添加用户的头像数据
            MyUser new_user=new MyUser();
            new_user.setUser_img(imgString);
            MyUser myUser=BmobUser.getCurrentUser(MyUser.class);
            new_user.update(myUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        UtilsLog.i("上传用户头像数据成功");
                    }else{
                        UtilsLog.i("上传用户头像数据失败");
                    }
                }
            });
            //为了避免浪费更多的流量去加载用户头像，当用户更改过头像，头像的string上传到Bmob后端云
            //然后再存到本地sharePreference，当用户更换头像失败，或者中途退出更换头像，
            //就默认设置用户上一次设置成功的头像
            UtilSharePre.putString(getActivity(),"user_image",imgString);

        }

    }

    //开始图片裁剪的方法
    private void startPhotoZoom(Uri uri) {
        if(uri==null){
            UtilsLog.i("uri=null");
            return ;
        }else{
            //裁剪
            Intent intent=new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri,"image/*");
            //设置裁剪true 就是裁剪
            intent.putExtra("crop",true);
            //设置裁剪宽 和高
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置裁剪图片的质量
            intent.putExtra("outputX",60);
            intent.putExtra("outputY",60);
            //裁剪好，发送数据
            intent.putExtra("return-data",true);
            startActivityForResult(intent,IMAGE_REQUEST_CODE);

        }
    }

    //监听male多选框 选中监听事件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        UtilsLog.i("多选框="+buttonView.getId());
        UtilsLog.i("male="+R.id.male);
        UtilsLog.i("female="+R.id.female);
       switch (buttonView.getId()){
           case R.id.male:
               if(isChecked){
                   male.setChecked(true);
                   female.setChecked(false);
               }else{
                   male.setChecked(false);
                   female.setChecked(true);
               }
               break;
           case R.id.female:
               if(isChecked){
                   male.setChecked(false);
                   female.setChecked(true);
               }else{
                   male.setChecked(true);
                   female.setChecked(false);
               }

       }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(age.getText().toString().trim())&&!TextUtils.isEmpty(desc.getText().toString().trim()))
        {
            btn_confirm_edit.setEnabled(true);
        }else{
            btn_confirm_edit.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        user_linearlayout.requestFocus();
        UtilsLog.i("user_linearlayout获取焦点了");
        InputMethodManager inputMethodManager= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(user_linearlayout.getWindowToken(),0);
        return false;
    }
}

