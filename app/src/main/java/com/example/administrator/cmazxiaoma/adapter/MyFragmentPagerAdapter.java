package com.example.administrator.cmazxiaoma.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 * MyFragmentPagerAdapter
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    //5个fragment
    private List<Fragment>  mFragment;
    //5个fragment的标题
    private List<String> mlist;

    public MyFragmentPagerAdapter(FragmentManager fragmentManager,List<Fragment> mFragment,List<String> mlist){
        super(fragmentManager);
        this.mFragment=mFragment;
        this.mlist=mlist;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mlist.get(position);
    }
}
