package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.model.data.DataUtils;
import com.zq.dynamicphoto.presenter.WxLoginPresenter;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.ILoadView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<ILoadView,WxLoginPresenter<ILoadView>> implements ILoadView {
    public static String token,openId;
    @Override
    protected int getLayoutId() {
        if (!XXPermissions.isHasPermission(this, Permission.READ_PHONE_STATE)) {
            requestPermission();
        }
        return R.layout.activity_login;
    }

    private void requestPermission() {
        XXPermissions.with(this)
                .permission(Permission.READ_PHONE_STATE) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected WxLoginPresenter<ILoadView> createPresenter() {
        return new WxLoginPresenter<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        boolean isLogin = sp.getBoolean(Constans.ISLOGIN, false);
        if (isLogin){
            MFGT.startActivity(this,HomeActivity.class);
            finish();
        }
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(openId)) {
            wxLoginCallback(token, openId);
        }
    }

    /**
     * 拿到了微信登录的token和openId,调用后台登录接口
     * @param token
     * @param openId
     */
    private void wxLoginCallback(String token, String openId) {
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        dr.setAccessToken(token);
        dr.setOpenId(openId);
        User user = new User();
        user.setOpenId(openId);
        netRequestBean.setUser(user);
        netRequestBean.setDeviceProperties(dr);
        if (mPresenter != null) {
            mPresenter.fetch(netRequestBean);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        token = null;
        openId = null;
    }

    @OnClick({R.id.btn_wx_login, R.id.layout_phone_login})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wx_login:
                wxLogin();
                break;
            case R.id.layout_phone_login:
                MFGT.startActivity(this,PhoneLoginActivity.class);
                break;
        }
    }


    public void wxLogin() {
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            Toast.makeText(this, "您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("loadData","微信登录1");
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MyApplication.mWxApi.sendReq(req);
    }

    @Override
    public void showData(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                DataUtils.getInstance().dealLoginResult(result);
                MFGT.startActivity(this,HomeActivity.class);
                finish();
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }
}
