package com.zq.dynamicphoto.mylive.fragment;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.View;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class LiveOrdersFragment extends BaseFragment {

    private int position;

    @SuppressLint("ValidFragment")
    public LiveOrdersFragment(int position) {
        this.position = position;
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_orders;
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

    @Override
    protected void loadData() {

    }
}
