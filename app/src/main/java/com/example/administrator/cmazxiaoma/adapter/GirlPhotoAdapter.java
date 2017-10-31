package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.cmazxiaoma.utils.UtilPicasso;
import com.example.administrator.cmazxiaoma.utils.UtilTools;
import com.example.administrator.cmazxiaoma.utils.UtilsLog;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/2/23.
 */

public class GirlPhotoAdapter extends PagerAdapter {
    private Context mcontext;
    private ArrayList<String> mlist;
    private PhotoViewAttacher attacher;
    public GirlPhotoAdapter(Context mcontext,ArrayList<String>mlist){
        this.mcontext=mcontext;
        this.mlist=mlist;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=new ImageView(mcontext);
        imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        UtilPicasso.loadImageView(mcontext,mlist.get(position),imageView);
        attacher=new PhotoViewAttacher(imageView);
        attacher.update();
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
