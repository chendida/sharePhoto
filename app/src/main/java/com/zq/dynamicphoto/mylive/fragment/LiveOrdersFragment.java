package com.zq.dynamicphoto.mylive.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.zq.dynamicphoto.adapter.OrderListAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.ChargeOrder;
import com.zq.dynamicphoto.presenter.OperateOrdersPresenter;
import com.zq.dynamicphoto.ui.widge.TextHintDialog;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.ILiveOrdersView;
import com.zq.dynamicphoto.view.OrdersOperate;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 订单列表界面
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class LiveOrdersFragment extends BaseFragment<ILiveOrdersView,
        OperateOrdersPresenter<ILiveOrdersView>> implements
        ILiveOrdersView, OrdersOperate {
    @BindView(R.id.et_search)
    EditText etSearch;
    private int position;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    @BindView(R.id.rcl_order_list)
    RecyclerView rclOrderList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    OrderListAdapter mAdapter;
    ArrayList<ChargeOrder> ordersList;
    @BindView(R.id.layout_init)
    AutoRelativeLayout layoutInit;
    private AutoRelativeLayout mBtnOk;
    private AutoRelativeLayout mBtnSend;
    private AutoRelativeLayout mBtnCancel;
    private AutoRelativeLayout mLayoutOrderStatus;
    private TextView mTvOrderStatus;
    private int mType;

    @SuppressLint("ValidFragment")
    public LiveOrdersFragment(int position) {
        this.position = position;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_orders;
    }

    @Override
    protected void initView(View view) {
        if (getActivity() != null)
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (ordersList == null) {
            ordersList = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rclOrderList.setLayoutManager(manager);
        mAdapter = new OrderListAdapter(ordersList, this);
        rclOrderList.setAdapter(mAdapter);
        rclOrderList.setNestedScrollingEnabled(false);
        rclOrderList.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
        setListener();
    }

    @OnClick({R.id.layout_clear_input, R.id.layout_filter})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_clear_input:
                etSearch.setText(null);
                break;
            case R.id.layout_filter:
                pager = 1;
                getOrdersList(pager,etSearch.getText().toString());
                break;
        }
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getOrdersList(pager, "");//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getOrdersList(pager, "");
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    @Override
    protected OperateOrdersPresenter<ILiveOrdersView> createPresenter() {
        return new OperateOrdersPresenter<>();
    }

    @Override
    protected void loadData() {
        getOrdersList(pager, "");
    }

    private void getOrdersList(int pager, String content) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        ChargeOrder chargeOrder = new ChargeOrder();
        chargeOrder.setUserId(userId);
        chargeOrder.setPage(pager);
        chargeOrder.setAddress(content);
        if (position == 0) {
            chargeOrder.setOrderStatus(null);
        } else {
            chargeOrder.setOrderStatus(position);
        }
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setChargeOrder(chargeOrder);
        netRequestBean.setDeviceProperties(dr);
        if (mPresenter != null) {
            mPresenter.getOrdersList(netRequestBean);
        }
    }

    @Override
    public void showGetOrdersList(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount", 1);
            ordersList = new Gson().fromJson(jsonObject.optString("chargeOrderList"), new TypeToken<List<ChargeOrder>>() {
            }.getType());
            if (ordersList != null) {
                Log.i("ordersList", "size = " + ordersList.size());
                if (ordersList.size() != 0) {
                    layoutInit.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        mAdapter.initOrderList(ordersList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.addOrderList(ordersList);
                    }
                } else {
                    if (pager == 1) {
                        layoutInit.setVisibility(View.VISIBLE);
                        mAdapter.initOrderList(ordersList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showUpdateOrderStatus(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                mAdapter.updateView(mType, mBtnOk, mBtnSend, mBtnCancel, mLayoutOrderStatus, mTvOrderStatus);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    @Override
    public void updateOrderStatus(String msg, ChargeOrder chargeOrder,
                                  int type, AutoRelativeLayout btnOk, AutoRelativeLayout btnSend,
                                  AutoRelativeLayout btnCancel, AutoRelativeLayout layoutOrderStatus,
                                  TextView tvOrderStatus) {
        showHintDialog(msg, chargeOrder, type, btnOk, btnSend, btnCancel, layoutOrderStatus, tvOrderStatus);
    }

    private void showHintDialog(String msg, final ChargeOrder chargeOrder, final int i, final AutoRelativeLayout btnOk
            , final AutoRelativeLayout btnSend, final AutoRelativeLayout btnCancel, final AutoRelativeLayout layoutOrderStatus, final TextView tvOrderStatus) {
        new TextHintDialog(getActivity(), R.style.dialog, new TextHintDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
                mType = i;
                mBtnOk = btnOk;
                mBtnSend = btnSend;
                mBtnCancel = btnCancel;
                mLayoutOrderStatus = layoutOrderStatus;
                mTvOrderStatus = tvOrderStatus;
                updateOrderStatus(chargeOrder, i);
            }
        }, msg).show();
    }

    private void updateOrderStatus(ChargeOrder order, int i) {
        DeviceProperties dr = DrUtils.getInstance();
        ChargeOrder chargeOrder = new ChargeOrder();
        chargeOrder.setOrderId(order.getOrderId());
        chargeOrder.setOrderStatus(i);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setChargeOrder(chargeOrder);
        if (mPresenter != null) {
            mPresenter.editOrderStatus(netRequestBean);
        }
    }
}
