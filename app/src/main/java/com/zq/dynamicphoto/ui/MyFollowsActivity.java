package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.FollowListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.bean.UserRelation;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.FollowListGetPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.ILoadView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的关注界面
 */
public class MyFollowsActivity extends BaseActivity<ILoadView,FollowListGetPresenter<ILoadView>>
        implements ILoadView {

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
    private Integer pager = 1;
    private Integer pagerCount = 0;
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
    public void onResume() {
        super.onResume();
        pager = 1;
        getFollowList(pager);
    }

    private void getFollowList(Integer pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserRelation userRelation = new UserRelation();
        UserInfo userInfo = new UserInfo();
        userInfo.setRemarkName(etSearch.getText().toString());
        userRelation.setIuserId(userId);
        userRelation.setPage(pager);
        userRelation.setRelationType(1);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserRelation(userRelation);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null){
            mPresenter.getFollowList(netRequestBean);
        }
    }

    @Override
    protected void initData() {
        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getFollowList(pager);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getFollowList(pager);
                } else {
                    ToastUtils.showShort(getResources().getString(R.string.tv_no_more_data));
                }
                refreshlayout.finishLoadmore(1000);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    pager = 1;
                    getFollowList(pager);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected FollowListGetPresenter<ILoadView> createPresenter() {
        return new FollowListGetPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.iv_clear_input})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_clear_input:
                etSearch.setText(null);
                break;
        }
    }

    @Override
    public void showData(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }


    private void dealWithResult(Result result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount", 1);
            userInfos = new Gson().fromJson(jsonObject.optString("userInfoList"), new TypeToken<List<UserInfo>>() {
            }.getType());
            if (userInfos != null) {
                if (userInfos.size() != 0) {
                    if (pager == 1) {
                        mAdapter.initFollowList(userInfos);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.addFollowList(userInfos);
                    }
                } else {
                    if (pager == 1) {
                        mAdapter.initFollowList(userInfos);
                        mAdapter.notifyDataSetChanged();
                        ToastUtils.showShort("您当前没有关注用户");
                    } else {
                        mAdapter.initFollowList(userInfos);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
