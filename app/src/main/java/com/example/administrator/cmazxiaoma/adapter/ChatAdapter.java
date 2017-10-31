package com.example.administrator.cmazxiaoma.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ChatAdapter extends FragmentPagerAdapter{
    private Context mcontext;
    private List<Fragment> mlist;

    public ChatAdapter(Context mcontext,FragmentManager fragmentManager, List<Fragment>mlist){
        super(fragmentManager);
        this.mlist=mlist;
        this.mcontext=mcontext;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }
}
