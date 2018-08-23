package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.SearchDynamicListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.bean.UserRelation;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.fragment.DynamicFragment;
import com.zq.dynamicphoto.presenter.SearchPresenter;
import com.zq.dynamicphoto.ui.widge.DoubleTimeSelectDialog;
import com.zq.dynamicphoto.ui.widge.DynamicDialog;
import com.zq.dynamicphoto.ui.widge.ShareDialog;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SaveContactSelectUtils;
import com.zq.dynamicphoto.utils.SaveSelectLabelUtils;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.DynamicDelete;
import com.zq.dynamicphoto.view.ISearchView;
import com.zq.dynamicphoto.view.SearchViewClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity<ISearchView,
        SearchPresenter<ISearchView>> implements ISearchView,SearchViewClick,
        ImageSaveUtils.DownLoadListener{
    private static final String TAG = "SearchActivity";
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.layout_spinner_menu)
    AutoLinearLayout layoutSpinnerMenu;
    @BindView(R.id.layout_init)
    AutoRelativeLayout layoutInit;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    Boolean isSpinner = false;//下拉筛选框是否展开，默认不展开
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_label_name)
    TextView tvLabelName;
    @BindView(R.id.tv_contact_name)
    TextView tvContactName;
    Dialog dialogUtils;
    @BindView(R.id.rcl_dynamic_list)
    RecyclerView rclDynamicList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    SearchDynamicListAdapter mAdapter;
    ArrayList<Dynamic> dynamicsList;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd;

    public String paramsStrTime = "";
    private String beginTime,finishTime;
    private DynamicDelete listener;
    private int positon;
    private ShareDialog shareDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        /**
         * 输入框搜索完成按钮的监听
         */
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    softShow();
                    getSearchDynamic(1);
                    return true;
                }
                return false;
            }
        });
        if (dynamicsList == null){
            dynamicsList = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclDynamicList.setLayoutManager(manager);
        mAdapter = new SearchDynamicListAdapter(dynamicsList,this);
        rclDynamicList.setAdapter(mAdapter);
        rclDynamicList.setNestedScrollingEnabled(false);
        rclDynamicList.setHasFixedSize(true);
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo userInfo = SaveContactSelectUtils.getInstance().getUserInfo();
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getRemarkName())) {
                tvContactName.setText(userInfo.getRemarkName());
            } else {
                UserInfo MyUserInfo = new UserInfo();
                userInfo.setUserId(-1);
                userInfo.setRemarkName("不限");
                SaveContactSelectUtils.getInstance().setUserInfo(MyUserInfo);
            }
        }

        DynamicLabel dynamicLabel = SaveSelectLabelUtils.getInstance().getDynamicLabel();
        if (dynamicLabel != null) {
            if (!TextUtils.isEmpty(dynamicLabel.getLabeltext())) {
                tvLabelName.setText(dynamicLabel.getLabeltext());
            } else {
                DynamicLabel myDynamicLabel = new DynamicLabel();
                myDynamicLabel.setId(-1);
                myDynamicLabel.setLabeltext("不限");
                SaveSelectLabelUtils.getInstance().setDynamicLabel(myDynamicLabel);
            }
        }
    }

    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     */
    private void softShow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                getSearchDynamic(pager);//上拉刷新
                refreshlayout.finishRefresh(600);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pager++;
                if (pagerCount >= pager) {
                    getSearchDynamic(pager);
                } else {
                    Toast.makeText(SearchActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                }
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    private void getSearchDynamic(int pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserRelation userRelation = new UserRelation();
        userRelation.setIuserId(userId);
        userRelation.setPage(pager);
        Dynamic dynamic = new Dynamic();
        UserInfo userInfo = SaveContactSelectUtils.getInstance().getUserInfo();
        if (userInfo != null){
            if (userInfo.getUserId() != null){
                if (userInfo.getUserId() != -1){
                    dynamic.setUserId(userInfo.getUserId());
                }
            }
        }
        DynamicLabel dynamicLabel = SaveSelectLabelUtils.getInstance().getDynamicLabel();
        if (dynamicLabel != null){
            if (dynamicLabel.getId() != null){
                if (dynamicLabel.getId() != -1){
                    dynamic.setLabelId(dynamicLabel.getId());
                }
            }
        }
        if (tvTime.getText().equals("不限")){
            finishTime = "";
            beginTime = "";
        }else {
            dynamic.setBeginTime(beginTime);
            dynamic.setEndTime(finishTime);
        }
        dynamic.setTitle(etSearch.getText().toString());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setDynamic(dynamic);
        netRequestBean.setUserRelation(userRelation);
        if (mPresenter != null){
            mPresenter.searchDynamicList(netRequestBean);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected SearchPresenter<ISearchView> createPresenter() {
        return new SearchPresenter<>();
    }

    @Override
    public void showDeleteResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.tv_delete_success));
                if (listener != null){
                    listener.deleteSuccess(positon);
                }
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

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

    @Override
    public void showGetDynamicList(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else {
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
                refreshLayout.setVisibility(View.VISIBLE);
                layoutInit.setVisibility(View.GONE);
                if (pager == 1) {
                    mAdapter.initDynamicList(dynamicsList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.addDynamicList(dynamicsList);
                }
            }else {
                if (pager == 1){
                    refreshLayout.setVisibility(View.GONE);
                    layoutInit.setVisibility(View.VISIBLE);
                }else {
                    layoutInit.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示软件盘
     *
     * @param editText
     */
    private void oneShowSoft(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 操作筛选框
     */
    private void operateSpinner() {
        if (isSpinner) {
            isSpinner = false;
            layoutSpinnerMenu.setVisibility(View.GONE);
            ivMenu.setImageResource(R.drawable.menu_close);
        } else {
            isSpinner = true;
            layoutSpinnerMenu.setVisibility(View.VISIBLE);
            layoutInit.setVisibility(View.GONE);
            ivMenu.setImageResource(R.drawable.menu_open);
        }
    }

    @OnClick({R.id.layout_search, R.id.layout_spinner, R.id.layout_clear, R.id.layout_filter
            , R.id.layout_clear_input, R.id.layout_label, R.id.layout_issuer, R.id.layout_time
            ,R.id.layout_back})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_search:
                etSearch.setFocusable(true);
                etSearch.setFocusableInTouchMode(true);
                etSearch.requestFocus();
                oneShowSoft(etSearch);
                break;
            case R.id.layout_spinner://展开下拉的搜索条件
                operateSpinner();
                break;
            case R.id.layout_clear:
                SaveSelectLabelUtils.getInstance().setDynamicLabel(null);
                SaveContactSelectUtils.getInstance().setUserInfo(null);
                beginTime = "";
                finishTime = "";
                tvTime.setText(getResources().getString(R.string.no_limit));
                tvLabelName.setText(getResources().getString(R.string.no_limit));
                tvContactName.setText(getResources().getString(R.string.no_limit));
                break;
            case R.id.layout_filter:
                pager = 1;
                getSearchDynamic(pager);
                break;
            case R.id.layout_clear_input://清除编辑框
                etSearch.setText(null);
                break;
            case R.id.layout_label://标签
                MFGT.gotoCategorySelectActivity(this);
                break;
            case R.id.layout_issuer://发布人
                MFGT.gotoContactSelectActivity(this);
                break;
            case R.id.layout_time://发布时间
                showCustomTimePicker();
                break;
        }
    }

    public void showCustomTimePicker() {
        String beginDeadTime = "2001-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    beginTime = startTime;
                    finishTime = endTime;
                    tvTime.setText(startTime.replace("-", ".") + "-" + endTime.replace("-", "."));
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
    public void clickListener(View view, int position, NetRequestBean netRequestBean,
                              DynamicDelete listener) {
        int userId = SharedPreferencesUtils.getInstance().getInt(Constans.USERID, 0);
        switch (view.getId()){
            case R.id.tv_delete:
                this.listener = listener;
                this.positon = position;
                deleteDynamic(netRequestBean);
                break;
            case R.id.tv_stick:
                stickDynamic(netRequestBean);
                break;
            case R.id.tv_all_save:
                Dynamic dynamic = netRequestBean.getDynamic();
                if (dynamic != null){
                    showLoading();
                    ImageSaveUtils.getInstance(this).saveAll(dynamic);
                }
                break;
            case R.id.iv_avatar:
                showSelectDialog(netRequestBean.getDynamic());
                break;
            case R.id.layout_one_key_share:
                showShareDialog(netRequestBean.getDynamic());
                break;
            case  R.id.layout_search:
                MFGT.gotoSearchActivity(this);
                break;
            case R.id.iv_my_avatar:
                MFGT.gotoHtmlPhotoDetailsActivity(this,"friends.html?userId="+
                                userId,
                        getResources().getString(R.string.tv_photo_details),
                        userId);
                break;
            case R.id.layout_share_my_photo:
                MFGT.gotoHtmlManagerActivity(this,"share.html?userid="+userId,
                        getResources().getString(R.string.photo_two_code),1);
                break;
        }
    }

    private void showShareDialog(final Dynamic dynamic) {
        shareDialog = new ShareDialog(this, R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://分享给好友
                        ShareUtils.getInstance().shareFriend(dynamic,1,SearchActivity.this);
                        break;
                    case 2://分享给微信朋友圈
                        ShareUtils.getInstance().shareFriend(dynamic,2,SearchActivity.this);
                        break;
                    case 3://批量保存
                        if (dynamic != null){
                            showLoading();
                            ImageSaveUtils.getInstance(SearchActivity.this).saveAll(dynamic);
                        }
                        break;
                }
            }
        });
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtils.showShort(getResources().getString(R.string.have_no_wx));
        }else {
            shareDialog.show();
        }
    }

    private void showSelectDialog(final Dynamic dynamic) {
        new DynamicDialog(this, R.style.dialog, new DynamicDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://进入相册
                        MFGT.gotoHtmlPhotoDetailsActivity(SearchActivity.this,"friends.html?userId="+
                                        dynamic.getUserId(),
                                getResources().getString(R.string.tv_photo_details),
                                dynamic.getUserId());
                        break;
                    case 2://设置权限
                        startActivity(new Intent(SearchActivity.this,
                                SettingPermissionActivity.class)
                                .putExtra(Constans.USERID,dynamic.getUserId()));
                        break;
                    case 3://投诉
                        MFGT.gotoHtmlManagerActivity(SearchActivity.this,"feedback.html?userId="+dynamic.getUserId(),
                                getResources().getString(R.string.tv_feedback));
                        break;
                }
            }
        }).show();
    }


    private void stickDynamic(NetRequestBean netRequestBean) {
        if (mPresenter != null)
            mPresenter.dynamicStick(netRequestBean);
    }

    private void deleteDynamic(NetRequestBean netRequestBean) {
        if (mPresenter != null)
            mPresenter.dynamicDelete(netRequestBean);
    }

    @Override
    public void callBack(int code, String msg) {
        hideLoading();
        ToastUtils.showShort(msg);
    }
}
