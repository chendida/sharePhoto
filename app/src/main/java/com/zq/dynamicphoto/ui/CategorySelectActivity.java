package com.zq.dynamicphoto.ui;

import android.app.Dialog;
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

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.SelectLabelListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.SelectLabelPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.view.ILiveOrdersView;
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

public class CategorySelectActivity extends BaseActivity<ILoadView,
        SelectLabelPresenter<ILoadView>> implements ILoadView {
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.rcl_label_list)
    RecyclerView rclLabelList;
    SelectLabelListAdapter mAdapter;
    ArrayList<DynamicLabel> dynamicLabels = new ArrayList<>();
    @BindView(R.id.et_search)
    EditText etSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_category_select;
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        layoutFinish.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.kinds));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rclLabelList.setLayoutManager(manager);
        mAdapter = new SelectLabelListAdapter(this, dynamicLabels);
        rclLabelList.setAdapter(mAdapter);
        rclLabelList.setHasFixedSize(true);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftUtils.softShow(CategorySelectActivity.this);
                    getLabelList();
                    return true;
                }
                return false;
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getLabelList();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected SelectLabelPresenter<ILoadView> createPresenter() {
        return new SelectLabelPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_search_label, R.id.layout_clear_input})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_search_label:
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

    public void getLabelList() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        DynamicLabel dynamicLabel = new DynamicLabel();
        dynamicLabel.setLabeltext(etSearch.getText().toString());
        dynamicLabel.setUserId(userId);
        netRequestBean.setDynamicLabel(dynamicLabel);
        if (mPresenter != null){
            mPresenter.getLabelList(netRequestBean);
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            dynamicLabels = new Gson().fromJson(jsonObject.optString("dynamicLabelList"), new TypeToken<List<DynamicLabel>>() {
            }.getType());
            if (dynamicLabels != null) {
                if (dynamicLabels.size() != 0) {
                    mAdapter.initLabelList(dynamicLabels);
                    mAdapter.notifyDataSetChanged();
                }else {
                    mAdapter.initLabelList(dynamicLabels);
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
