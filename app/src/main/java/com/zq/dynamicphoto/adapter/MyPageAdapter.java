package com.zq.dynamicphoto.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/24.
 */

public class MyPageAdapter extends FragmentPagerAdapter {
    private ArrayList<String> mTitleStrs;
    private ArrayList<Fragment>mFragments;
    public MyPageAdapter(FragmentManager fm, ArrayList<String> mTitleStrs, ArrayList<Fragment> mFragments) {
        super(fm);
        this.mTitleStrs = mTitleStrs;
        this.mFragments = mFragments;
    }

    @Override
    public int getCount() {
        return mTitleStrs == null ? 0 : mTitleStrs.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleStrs.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
