package com.example.administrator.cmazxiaoma.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.example.administrator.cmazxiaoma.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/10.
 *定义个性字体
 */

public class UtilTools {

    public static void setFont(Context mcontext, TextView textView) {
        //设置字体
        Typeface typeface = Typeface.createFromAsset(mcontext.getAssets(), "fonts/FONT.TTF");
        textView.setTypeface(typeface);
    }

    public static void Toast(Context mcontext, String data) {
        Toast toast = Toast.makeText(mcontext, data, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
     //判断邮箱格式
    public static boolean isEmail(String email) {
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
    //判断手机格式
    public static boolean isMobile(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static void setAlertDialog(Context mcontext,String data){
        new AlertDialog.Builder(mcontext)
                .setMessage(data)
                .create()
                .show();
    }

    //将String类型的字符串转换成图片 通过Base64来解压
    public static void setStringtoImage(Context mcontext,ImageView imageView,String imgString)
    {
        if(imgString!=null)
        {
            byte[] bytesArray=Base64.decode(imgString,Base64.DEFAULT);
            ByteArrayInputStream is=new ByteArrayInputStream(bytesArray);
            Bitmap bitmap= BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
        }else{
            return ;
        }
    }

    //将图片转换成String，通过Base64来压缩
    public static String getImagetoString(Context mcontext,ImageView imageView){
        BitmapDrawable drawable= (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,os);
        byte[] bytesArray=os.toByteArray();
        String imgString=new String(Base64.encodeToString(bytesArray,Base64.DEFAULT));
        return imgString;
    }

    //隐藏输入法
    public static void HideSoftInputViewGroup(Context mcontext, ViewGroup v){
        InputMethodManager im= (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        //判断键盘时候打开
        boolean open=im.isActive();
        if(open){
            try {
                im.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }catch (Exception e){
                e.printStackTrace();
                UtilsLog.i("该界面返回时，键盘为隐藏");
            }
        }
    }

    public static void HideSoftInputView(Context mcontext, View v){
        InputMethodManager im= (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        //判断键盘时候打开
        boolean open=im.isActive();
        if(open){
            try {
                im.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }catch(Exception e){
                e.printStackTrace();
                UtilsLog.i("该界面返回时，键盘为隐藏");
            }
        }

    }

    //判断网络是否连接
    public static boolean isNetworkAvailable(Context mcontext) {
        boolean flag=false;
        //得到网络连接信息
        ConnectivityManager con= (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if(con.getActiveNetworkInfo()!=null){
            flag=con.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }


    public  static String  DatetoTime(Date date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

}
