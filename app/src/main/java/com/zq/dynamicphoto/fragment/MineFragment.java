package com.zq.dynamicphoto.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.ui.SettingActivity;
import com.zq.dynamicphoto.utils.MFGT;

import butterknife.OnClick;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @OnClick({R.id.layout_mine_info, R.id.layout_open_vip, R.id.layout_my_two_code, R.id.layout_my_follow, R.id.layout_my_fans, R.id.layout_problem, R.id.layout_about_app, R.id.layout_setting})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_mine_info:
                break;
            case R.id.layout_open_vip:
                break;
            case R.id.layout_my_two_code:
                break;
            case R.id.layout_my_follow:
                break;
            case R.id.layout_my_fans:
                break;
            case R.id.layout_problem:
                break;
            case R.id.layout_about_app:
                break;
            case R.id.layout_setting:
                MFGT.startActivity(getActivity(), SettingActivity.class);
                break;
        }
    }
}
