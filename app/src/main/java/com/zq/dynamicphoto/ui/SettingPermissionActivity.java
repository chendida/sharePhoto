package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserPermissions;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.PermissionPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.IPermissionView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingPermissionActivity extends BaseActivity<IPermissionView,
        PermissionPresenter<IPermissionView>> implements IPermissionView {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.check_no_see)
    CheckBox checkNoSee;
    @BindView(R.id.check_open_no_allow)
    CheckBox checkOpenNoAllow;
    private Integer uUserId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_permission;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.set_permission), tvTitle,
                layoutBack, layoutFinish);
    }

    @Override
    protected void initData() {
        uUserId = getIntent().getIntExtra(Constans.USERID, 0);
        getPermissionStatus();
    }

    @Override
    protected PermissionPresenter<IPermissionView> createPresenter() {
        return new PermissionPresenter<>();
    }

    @Override
    public void showGetPermissionStatusResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == 0) {
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
            ArrayList<UserPermissions> list = new Gson().fromJson(jsonObject.optString("userPermissionsList"), new TypeToken<List<UserPermissions>>() {
            }.getType());
            if (list != null) {
                updateView(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateView(ArrayList<UserPermissions> list) {
        if (list.size() == 1) {
            if (list.get(0) != null) {
                if (list.get(0).getPermissionsType() == 1) {
                    checkOpenNoAllow.setChecked(true);
                } else if (list.get(0).getPermissionsType() == 2) {
                    checkNoSee.setChecked(true);
                }
            }
        } else if (list.size() == 2) {
            checkOpenNoAllow.setChecked(true);
            checkNoSee.setChecked(true);
        }
    }

    @Override
    public void showSetPermissionResult(Result result) {
        showRequestSuccess(result);
    }

    private void showRequestSuccess(Result result) {
        if (result != null) {
            if (result.getResultCode() == 0) {
                ToastUtils.showShort(getResources().getString(R.string.set_permission_success));
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    @Override
    public void showCancelPermissionResult(Result result) {
        showRequestSuccess(result);
    }

    @OnClick({R.id.layout_back, R.id.check_no_see, R.id.check_open_no_allow})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.check_no_see:
                if (checkNoSee.isChecked()){
                    setPermission(2);
                }else {
                    cancelSetPermission(2);
                }
                break;
            case R.id.check_open_no_allow:
                if (checkOpenNoAllow.isChecked()){
                    setPermission(1);
                }else {
                    cancelSetPermission(1);
                }
                break;
        }
    }

    private void cancelSetPermission(int type) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserPermissions userPermissions = new UserPermissions();
        userPermissions.setIuserId(userId);
        userPermissions.setUuserId(uUserId);
        userPermissions.setPermissionsType(type);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserPermissions(userPermissions);
        if (mPresenter != null){
            mPresenter.setCancelPermission(netRequestBean);
        }
    }

    public void getPermissionStatus() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserPermissions userPermissions = new UserPermissions();
        userPermissions.setIuserId(userId);
        userPermissions.setUuserId(uUserId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserPermissions(userPermissions);
        if (mPresenter != null) {
            mPresenter.getPermissionStatus(netRequestBean);
        }
    }

    public void setPermission(int type) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserPermissions userPermissions = new UserPermissions();
        userPermissions.setIuserId(userId);
        userPermissions.setUuserId(uUserId);
        userPermissions.setPermissionsType(type);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserPermissions(userPermissions);
        if (mPresenter != null){
            mPresenter.setPermission(netRequestBean);
        }
    }
}
