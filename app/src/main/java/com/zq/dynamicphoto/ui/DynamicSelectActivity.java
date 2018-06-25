package com.zq.dynamicphoto.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.utils.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicSelectActivity extends BaseActivity {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rcl_select_photo_list)
    RecyclerView rclSelectPhotoList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_select;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TitleUtils.setTitleBar(getResources().getString(R.string.tv_select_dynamic),tvTitle
        ,layoutBack,ivCamera,tvFinish);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.layout_back, R.id.layout_finish, R.id.layout_clear_input, R.id.layout_filter})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                break;
            case R.id.layout_clear_input:
                etSearch.setText(null);
                break;
            case R.id.layout_filter:

                break;
        }
    }
}
