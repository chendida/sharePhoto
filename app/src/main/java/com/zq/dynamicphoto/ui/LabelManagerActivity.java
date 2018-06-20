package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.LabelListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.LabelsManagerPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.ILabelManagerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 标签管理界面
 */
public class LabelManagerActivity extends BaseActivity<ILabelManagerView,
        LabelsManagerPresenter<ILabelManagerView>> implements ILabelManagerView,LabelListAdapter.MyClickListener {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.et_search)
    EditText etSearch;
    ArrayList<DynamicLabel> dynamicLabels = new ArrayList<>();
    @BindView(R.id.rcl_label_list)
    RecyclerView rclLabelList;
    private LabelListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_label_manager;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.label_manager),
                tvTitle, layoutBack, layoutFinish);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        /**
         * 输入框搜索完成按钮的监听
         */
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftUtils.softShow(LabelManagerActivity.this);
                    getLabelList();
                    return true;
                }
                return false;
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rclLabelList.setLayoutManager(manager);
        mAdapter = new LabelListAdapter(dynamicLabels, this,this);
        rclLabelList.setAdapter(mAdapter);
        rclLabelList.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLabelList();
    }

    @Override
    protected LabelsManagerPresenter<ILabelManagerView> createPresenter() {
        return new LabelsManagerPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.iv_clear_input, R.id.layout_search})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_clear_input:
                etSearch.setText(null);
                break;
            case R.id.layout_search:
                SoftUtils.softShow(this);
                break;
        }
    }

    private void getLabelList() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        DynamicLabel dynamicLabel = new DynamicLabel();
        dynamicLabel.setUserId(userId);
        dynamicLabel.setLabeltext(etSearch.getText().toString());
        netRequestBean.setDynamicLabel(dynamicLabel);
        if (mPresenter != null) {
            mPresenter.getLabelsList(netRequestBean);
        }
    }

    @Override
    public void showGetLabelsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithGetLabelsResult(result);
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    private void dealWithGetLabelsResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            dynamicLabels = new Gson().fromJson(jsonObject.optString("dynamicLabelList"), new TypeToken<List<DynamicLabel>>() {
            }.getType());
            if (dynamicLabels != null) {
                if (dynamicLabels.size() != 0) {
                    mAdapter.initDynamicLabelList(dynamicLabels);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editLabelsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.tv_edit_success));
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void deleteLabelResult(Result result) {
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

    @Override
    public void clickListener(View view, int position, NetRequestBean netRequestBean) {
        switch (view.getId()){
            case R.id.layout_label:
                if (mPresenter != null){
                    mPresenter.editLabels(netRequestBean);
                }
                break;
            case R.id.btnDelete:
                if (mPresenter != null){
                    mPresenter.deleteLabels(netRequestBean);
                }
                break;
        }
    }
}
