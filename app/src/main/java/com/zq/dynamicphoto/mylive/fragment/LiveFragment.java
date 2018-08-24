package com.zq.dynamicphoto.mylive.fragment;

import android.content.Context;
import android.view.View;

import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.mylive.struct.FunctionManager;
import com.zq.dynamicphoto.mylive.ui.LiveActivity;

/**
 * Created by Administrator on 2018/8/24.
 */

public class LiveFragment extends BaseFragment {

    public static FunctionManager mFunctionManager;
    private LiveActivity mLiveActivity;


    public void setmFunctionManager(FunctionManager functionManager) {
        mFunctionManager = functionManager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LiveActivity){
            mLiveActivity = (LiveActivity) context;
            mLiveActivity.setFunctionForFragment();
        }
    }

    @Override
    protected int getLayoutId() {
        return 0;
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
