package com.zq.dynamicphoto.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.FriendCircleAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.MomentOperatePresenter;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IFriendCircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 朋友圈
 */
public class FriendCircleFragment extends BaseFragment<IFriendCircleView,
        MomentOperatePresenter<IFriendCircleView>> implements IFriendCircleView,
        FriendCircleAdapter.MyClickListener{
    @BindView(R.id.rcl_friend_circle_list)
    RecyclerView rclFriendCircleList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    FriendCircleAdapter mAdapter;
    ArrayList<Moments> friendCircleList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_circle;
    }

    @Override
    protected void initView(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rclFriendCircleList.setLayoutManager(manager);
        mAdapter = new FriendCircleAdapter(getActivity(), friendCircleList,this,getActivity());
        rclFriendCircleList.setAdapter(mAdapter);
        rclFriendCircleList.setNestedScrollingEnabled(false);
        rclFriendCircleList.setHasFixedSize(true);

        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getFriendCircleList(pager);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getFriendCircleList(pager);
                } else {
                    ToastUtils.showShort("没有更多数据");
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    private void getFriendCircleList(int pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        Moments moments = new Moments();
        moments.setUserId(userId);
        moments.setPage(pager);
        moments.setTitle(mAdapter.getEtSearchContent());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setMoments(moments);
        if (mPresenter != null){
            mPresenter.getMomentList(netRequestBean);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected MomentOperatePresenter<IFriendCircleView> createPresenter() {
        return new MomentOperatePresenter<>();
    }

    @Override
    protected void loadData() {
        pager = 1;
        getFriendCircleList(pager);
    }

    @Override
    public void getMomentListResult(Result result) {
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
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"),UserInfo.class);
            friendCircleList = new Gson().fromJson(jsonObject.optString("momentsList"), new TypeToken<List<Moments>>() {
            }.getType());
            Log.i("momentsList", "size = " + friendCircleList.size());
            if (friendCircleList.size() != 0) {
                if (pager == 1){
                    mAdapter.initMomentsList(friendCircleList);
                    mAdapter.notifyDataSetChanged();
                }else {
                    mAdapter.addMomentsList(friendCircleList);
                }
            }else {
                if (pager == 1){
                    mAdapter.initMomentsList(friendCircleList);
                    mAdapter.notifyDataSetChanged();
                }
            }
            if (userInfo != null){
                mAdapter.setUserInfo(userInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMomentResult(Result result) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void clickListener(View v, Moments moments) {
        switch (v.getId()){
            case R.id.tv_delete:
                deleteMoment(moments);
                break;
            case R.id.tv_edit:

                break;
            case R.id.layout_article:
                MFGT.gotoHtmlManagerActivity(getActivity(),"moments.html?id="+moments.getId(),
                        getResources().getString(R.string.tv_friend_circle_details));
                break;
            case R.id.et_search:
                pager = 1;
                getFriendCircleList(pager);
                break;
        }
    }

    private void deleteMoment(Moments moments) {
        DeviceProperties dr = DrUtils.getInstance();
        Moments moment = new Moments();
        moment.setId(moments.getId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setMoments(moment);
        if (mPresenter != null){
            mPresenter.deleteMoment(netRequestBean);
        }
    }
}
