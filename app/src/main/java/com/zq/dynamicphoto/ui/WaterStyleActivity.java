package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.MyPageAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.WaterMouldFragment;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 水印样式
 */
public class WaterStyleActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private ArrayList<String> mTitleStrs = new ArrayList<>();
    private ArrayList<Fragment>mFragments = new ArrayList<>();
    private int mCurrentTabPos = 0;
    private MyPageAdapter pageAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_water_style;
    }

    @Override
    protected void initView() {
        WaterMouldFragment fragment1 = new WaterMouldFragment(0);
        WaterMouldFragment fragment2 = new WaterMouldFragment(1);
        WaterMouldFragment fragment3 = new WaterMouldFragment(2);
        WaterMouldFragment fragment4 = new WaterMouldFragment(3);
        WaterMouldFragment fragment5 = new WaterMouldFragment(4);
        WaterMouldFragment fragment6 = new WaterMouldFragment(5);
        WaterMouldFragment fragment7 = new WaterMouldFragment(6);
        WaterMouldFragment fragment8 = new WaterMouldFragment(7);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mFragments.add(fragment4);
        mFragments.add(fragment5);
        mFragments.add(fragment6);
        mFragments.add(fragment7);
        mFragments.add(fragment8);

        mTitleStrs.add(getResources().getString(R.string.tv_all));
        mTitleStrs.add(getResources().getString(R.string.tv_label_sticker));
        mTitleStrs.add(getResources().getString(R.string.tv_two_code));
        mTitleStrs.add(getResources().getString(R.string.tv_kinds_shape));
        mTitleStrs.add(getResources().getString(R.string.tv_more_mould));
        mTitleStrs.add(getResources().getString(R.string.tv_lower_water));
        mTitleStrs.add(getResources().getString(R.string.tv_money_mould));
        mTitleStrs.add(getResources().getString(R.string.tv_good_request));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constans.REQUEST_CODE){
            if (resultCode == Constans.RESULT_CODE_FINISH){
                WaterStyleActivity.this.finish();
            }
        }
    }
}
