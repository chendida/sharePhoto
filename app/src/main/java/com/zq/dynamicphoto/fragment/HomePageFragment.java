package com.zq.dynamicphoto.fragment;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import java.util.ArrayList;
import butterknife.BindView;

/**
 * 首页
 */
public class HomePageFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private HomePageAdapter mHomePageAdapter;
    private DynamicFragment mDynamicFragment;
    private LiveListFragment mLiveListFragment;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitleStrs = new ArrayList<>();
    LayoutInflater mLayoutInflater;
    private int mCurrentTabPos = 0;


    @Override
    protected int getLayoutId() {
        mLayoutInflater = LayoutInflater.from(getActivity());
        return R.layout.fragment_home_page;
    }

    @Override
    protected void initView(View view) {
        initFragments();
    }

    private void initFragments() {
        mDynamicFragment = new DynamicFragment();
        mLiveListFragment = new LiveListFragment();
        mFragments.add(mDynamicFragment);
        mFragments.add(mLiveListFragment);

        mTitleStrs.add(getResources().getString(R.string.tv_dynamic));
        mTitleStrs.add(getResources().getString(R.string.tv_live));
    }

    @Override
    protected void initData() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mHomePageAdapter = new HomePageAdapter(getChildFragmentManager());
        viewPager.setAdapter(mHomePageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mCurrentTabPos);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFragments.clear();
        mTitleStrs.clear();
    }

    private class HomePageAdapter extends FragmentPagerAdapter{

        private HomePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
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
}
