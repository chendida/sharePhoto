package com.zq.dynamicphoto.mylive.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.MyPageAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.mylive.fragment.LiveOrdersFragment;
import com.zq.dynamicphoto.utils.TitleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MyOrdersActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    private ArrayList<String> mTitleStrs = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private int mCurrentTabPos = 0;
    private MyPageAdapter pageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_orders;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.tv_my_orders),tvTitle,layoutBack,layoutFinish);
        LiveOrdersFragment fragment1 = new LiveOrdersFragment(0);
        LiveOrdersFragment fragment2 = new LiveOrdersFragment(1);
        LiveOrdersFragment fragment3 = new LiveOrdersFragment(2);
        LiveOrdersFragment fragment4 = new LiveOrdersFragment(3);
        LiveOrdersFragment fragment5 = new LiveOrdersFragment(4);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mFragments.add(fragment4);
        mFragments.add(fragment5);

        mTitleStrs.add(getResources().getString(R.string.tv_all));
        mTitleStrs.add(getResources().getString(R.string.tv_to_be_confirm));
        mTitleStrs.add(getResources().getString(R.string.tv_confirmed));
        mTitleStrs.add(getResources().getString(R.string.tv_shipped));
        mTitleStrs.add(getResources().getString(R.string.tv_canceled));
    }

    @Override
    protected void initData() {
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(),mTitleStrs,mFragments);
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mCurrentTabPos);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.layout_back)
    public void onClicked() {
        finish();
    }
}
