package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zq.dynamicphoto.adapter.DynamicSelectAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.DynamicSelectPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.IDynamicSelectView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DynamicSelectActivity extends BaseActivity<IDynamicSelectView,
        DynamicSelectPresenter<IDynamicSelectView>> implements IDynamicSelectView{

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
    private DynamicSelectAdapter mAdapter;
    ArrayList<Dynamic>dynamics;
    private Integer pager = 1;
    private Integer pagerCount = 0;
    private Integer momentId = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_select;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TitleUtils.setTitleBar(getResources().getString(R.string.tv_select_dynamic),tvTitle
        ,layoutBack,ivCamera,tvFinish);
        if (dynamics  == null){
            dynamics = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclSelectPhotoList.setLayoutManager(manager);
        mAdapter = new DynamicSelectAdapter(dynamics,10);
        rclSelectPhotoList.setAdapter(mAdapter);
        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getDynamicList(pager);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getDynamicList(pager);
                } else {
                    ToastUtils.showShort(getResources().getString(R.string.tv_no_more_data));
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    private void getDynamicList(Integer pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        Dynamic dynamic = new Dynamic();
        dynamic.setMomentId(momentId);
        dynamic.setUserId(userId);
        dynamic.setPage(pager);
        dynamic.setTitle(etSearch.getText().toString());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setDynamic(dynamic);
        if (mPresenter != null){
            mPresenter.getDynamicSelectList(netRequestBean);
        }
    }

    @Override
    protected void initData() {
        momentId = (Integer) getIntent().getSerializableExtra(Constans.MOMENTS_ID);
        pager = 1;
        getDynamicList(pager);
    }

    @Override
    protected DynamicSelectPresenter<IDynamicSelectView> createPresenter() {
        return new DynamicSelectPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_finish, R.id.layout_clear_input, R.id.layout_filter})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                ArrayList<Dynamic> selectList = mAdapter.getSelectList();
                if (selectList.size() == 0){
                    ToastUtils.showShort(getResources().getString(R.string.please_least_select_one));
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(Constans.DYNAMIC,selectList);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
            case R.id.layout_clear_input:
                etSearch.setText(null);
                break;
            case R.id.layout_filter:
                pager = 1;
                getDynamicList(pager);
                break;
        }
    }

    @Override
    public void showDynamicList(Result result) {
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
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount", 1);
            dynamics = new Gson().fromJson(jsonObject.optString("dynamicList"), new TypeToken<List<Dynamic>>() {
            }.getType());
            Log.i("dynamicList", "size = " + dynamics.size());
            if (dynamics.size() != 0) {
                if (pager == 1) {
                    mAdapter.initDynamicList(dynamics);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.addDynamicList(dynamics);
                }
            } else {
                if (pager == 1) {
                    mAdapter.initDynamicList(dynamics);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
