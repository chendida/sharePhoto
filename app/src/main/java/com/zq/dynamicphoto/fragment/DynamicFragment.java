package com.zq.dynamicphoto.fragment;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.DynamicListAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserRelation;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.DynamicLoadPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IDynamicView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
/**
 * 动态列表界面
 */
public class DynamicFragment extends BaseFragment<IDynamicView,DynamicLoadPresenter<IDynamicView>>
        implements IDynamicView,DynamicListAdapter.MyClickListener{

    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.refresh_Layout)
    SmartRefreshLayout refreshLayout;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    ArrayList<Dynamic> dynamicsList;
    DynamicListAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView(View view) {
        if (dynamicsList == null) {
            dynamicsList = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rcl.setLayoutManager(manager);
        mAdapter = new DynamicListAdapter(getActivity(), dynamicsList,this);
        rcl.setAdapter(mAdapter);
        rcl.setNestedScrollingEnabled(false);
        rcl.setHasFixedSize(true);
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
                    Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        getDynamicList(pager);
    }

    @Override
    protected DynamicLoadPresenter<IDynamicView> createPresenter() {
        return new DynamicLoadPresenter<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount", 1);
            dynamicsList = new Gson().fromJson(jsonObject.optString("dynamicList"), new TypeToken<List<Dynamic>>() {
            }.getType());
            Log.i("dynamicList", "size = " + dynamicsList.size());
            if (dynamicsList.size() != 0) {
                if (pager == 1) {
                    mAdapter.initDynamicList(dynamicsList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.addDynamicList(dynamicsList);
                }
            } else {
                if (pager == 1) {
                    mAdapter.initDynamicList(dynamicsList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDynamicList(int pager){
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserRelation userRelation = new UserRelation();
        userRelation.setIuserId(userId);
        userRelation.setPage(pager);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserRelation(userRelation);
        if (mPresenter != null) {
            mPresenter.fetch(netRequestBean);
        }
    }

    @Override
    public void clickListener(View view,int position, NetRequestBean netRequestBean) {
        switch (view.getId()){
            case R.id.tv_delete:
                deleteDynamic(netRequestBean);
                break;
            case R.id.tv_stick:
                stickDynamic(netRequestBean);
                break;
        }
    }

    /**
     * 置顶动态
     * @param netRequestBean
     */
    private void stickDynamic(NetRequestBean netRequestBean) {
        if (mPresenter != null)
            mPresenter.fetchStickDynamic(netRequestBean);
    }

    /**
     * 删除动态
     * @param netRequestBean
     */
    private void deleteDynamic(NetRequestBean netRequestBean){
        if (mPresenter != null)
            mPresenter.fetchDeleteDynamic(netRequestBean);
    }

    /**
     * 删除回调
     * @param result
     */
    @Override
    public void showDeleteResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.tv_delete_success));
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    /**
     * 置顶回调
     * @param result
     */
    @Override
    public void showStickResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.tv_stick_success));
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }
}