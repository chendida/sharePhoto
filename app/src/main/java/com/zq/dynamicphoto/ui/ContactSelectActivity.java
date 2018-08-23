package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.ContactListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
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
import com.zq.dynamicphoto.view.ILoadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactSelectActivity extends BaseActivity<ILoadView,
        FollowListGetPresenter<ILoadView>> implements ILoadView {
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.rcl_contact_list)
    RecyclerView rclContactList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    private Integer pager = 1;
    private Integer pagerCount = 0;
    ArrayList<UserInfo> userInfos = new ArrayList<>();
    ContactListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_select;
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.select_contact));
        layoutFinish.setVisibility(View.GONE);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rclContactList.setLayoutManager(manager);
        mAdapter = new ContactListAdapter(this, userInfos);
        rclContactList.setAdapter(mAdapter);
        rclContactList.setHasFixedSize(true);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftUtils.softShow(ContactSelectActivity.this);
                    getContactList(1);
                    return true;
                }
                return false;
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getContactList(pager);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected FollowListGetPresenter<ILoadView> createPresenter() {
        return new FollowListGetPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_search_contact,R.id.layout_clear_input})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_search_contact:
                etSearch.setFocusable(true);
                etSearch.setFocusableInTouchMode(true);
                etSearch.requestFocus();
                SoftUtils.oneShowSoft(etSearch);
                break;
            case R.id.layout_clear_input:
                etSearch.setText(null);
                break;
        }
    }

    public void getContactList(int pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserInfo userInfo = new UserInfo();
        userInfo.setRemarkName(etSearch.getText().toString());
        UserRelation userRelation = new UserRelation();
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
                        mAdapter.initContactList(userInfos);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.addContactList(userInfos);
                    }
                }else {
                    mAdapter.initContactList(userInfos);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showData(Result result) {
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
}
