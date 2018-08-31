package com.zq.dynamicphoto.mylive.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.zq.dynamicphoto.adapter.ConsumptionAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.LiveConsumeRecord;
import com.zq.dynamicphoto.presenter.GetUserConsumptionListPresenter;
import com.zq.dynamicphoto.ui.widge.DoubleTimeSelectDialog;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.ILiveOrdersView;
import com.zq.dynamicphoto.view.ILoadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消费清单界面
 */
public class ConsumptionListActivity extends BaseActivity<ILoadView,
        GetUserConsumptionListPresenter<ILoadView>> implements ILoadView{
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.rcl_consumption)
    RecyclerView rclConsumption;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layout_init)
    AutoRelativeLayout layoutInit;
    @BindView(R.id.et_search)
    EditText etSearch;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    ArrayList<LiveConsumeRecord> liveConsumeRecords;
    ConsumptionAdapter mAdapter;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd;

    private String beginTime = "";
    private String finishTime = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_consumption_list;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.consumption));
        if (liveConsumeRecords == null) {
            liveConsumeRecords = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclConsumption.setLayoutManager(manager);
        mAdapter = new ConsumptionAdapter(liveConsumeRecords);
        rclConsumption.setAdapter(mAdapter);
        rclConsumption.setNestedScrollingEnabled(false);
        rclConsumption.setHasFixedSize(true);
        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getConsumptionList(pager,beginTime,finishTime);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getConsumptionList(pager,beginTime,finishTime);
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    private void getConsumptionList(int pager, String beginTime, String finishTime) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        LiveConsumeRecord liveConsumeRecord = new LiveConsumeRecord();
        liveConsumeRecord.setUserId(userId);
        liveConsumeRecord.setPage(pager);
        liveConsumeRecord.setStartTime(beginTime);
        liveConsumeRecord.setEndTime(finishTime);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setLiveConsumeRecord(liveConsumeRecord);
        if (mPresenter != null){
            mPresenter.getUserConsumptionList(netRequestBean);
        }
    }

    @Override
    protected void initData() {
        pager = 1;
        getConsumptionList(pager,beginTime,finishTime);
    }

    @OnClick({R.id.layout_back, R.id.layout_clear_input, R.id.layout_filter,R.id.et_search})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.et_search:
                //弹出时间选择器
                showCustomTimePicker();
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_clear_input:
                beginTime = "";
                finishTime = "";
                etSearch.setText(null);
                break;
            case R.id.layout_filter:
                pager = 1;
                getConsumptionList(pager,beginTime,finishTime);
                break;
        }
    }


    private void showCustomTimePicker() {
        String beginDeadTime = "2001-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this,
                    beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    pager = 1;
                    beginTime = startTime;
                    finishTime = endTime;
                    etSearch.setText(startTime.replace("-", ".") +
                            "-" + endTime.replace("-", "."));
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
        }
    }

    @Override
    protected GetUserConsumptionListPresenter<ILoadView> createPresenter() {
        return new GetUserConsumptionListPresenter<>();
    }

    @Override
    public void showData(Result result) {
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
            liveConsumeRecords = new Gson().fromJson(jsonObject.optString("liveConsumeRecordList"), new TypeToken<List<LiveConsumeRecord>>() {
            }.getType());
            if (liveConsumeRecords != null) {
                Log.i("dynamicList", "size = " + liveConsumeRecords.size());
                if (liveConsumeRecords.size() != 0) {
                    layoutInit.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        mAdapter.initConsumptionList(liveConsumeRecords);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.addConsumptionList(liveConsumeRecords);
                    }
                } else {
                    if (pager == 1) {
                        layoutInit.setVisibility(View.VISIBLE);
                        mAdapter.initConsumptionList(liveConsumeRecords);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
