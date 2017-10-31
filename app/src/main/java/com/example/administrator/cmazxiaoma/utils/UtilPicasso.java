package com.example.administrator.cmazxiaoma.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by Administrator on 2017/2/20.
 */

public class UtilPicasso {
    //默认加载图片
    public static void loadImageView(Context mcontext,String url,ImageView imageView){
        Picasso.with(mcontext).load(url).into(imageView);
    }

    //默认加载图片(指定大小）
    public static void loadImageViewSize(Context mcontext,String url,int width,int height,ImageView imageView){
        Picasso.with(mcontext).load(url).resize(width,height).centerCrop().into(imageView);
    }
    //加载图片时 设置未加载好时候的图片， 还有加载发生错误时候的图片
    public static void loadImageViewHolder(Context mcontext,String url,int loadImg,int errorImg,int width,int height,ImageView imageView){
        Picasso.with(mcontext).load(url).placeholder(loadImg).error(errorImg).resize(width,height).centerCrop().into(imageView);
    }


   //按比例裁剪 矩形
    public static void loadImageViewCrop(Context mcontext,String url,ImageView imageView){
        Picasso.with(mcontext).load(url).transform(new CropSquareTransformation()).into(imageView);

    }

    public  static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
