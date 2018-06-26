package com.zq.dynamicphoto.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.FollowListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.utils.TitleUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的关注界面
 */
public class MyFollowsActivity extends BaseActivity {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rcl_my_follow_list)
    RecyclerView rclMyFollowList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    ArrayList<UserInfo>userInfos;
    private FollowListAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_follows;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.tv_my_follow), tvTitle, layoutBack,layoutFinish);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (userInfos == null){
            userInfos = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclMyFollowList.setLayoutManager(manager);
        mAdapter = new FollowListAdapter(this,userInfos);
        rclMyFollowList.setAdapter(mAdapter);
        rclMyFollowList.setNestedScrollingEnabled(false);
        rclMyFollowList.setHasFixedSize(true);
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

    @OnClick({R.id.layout_back, R.id.iv_clear_input})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_clear_input:
                break;
        }
    }
}
