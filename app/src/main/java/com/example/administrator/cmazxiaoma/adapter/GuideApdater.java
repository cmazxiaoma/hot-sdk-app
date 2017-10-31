package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */

public class GuideApdater extends PagerAdapter {
    private List<View> mlist;
    private Context mcontext;
    public GuideApdater(Context mcontext,List<View> mlist){
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
        ((ViewPager)container).addView(mlist.get(position));
        return mlist.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mlist.get(position));
        //super.destroyItem(container, position, object);
        //删除了  就没有上下文了
    }
}
