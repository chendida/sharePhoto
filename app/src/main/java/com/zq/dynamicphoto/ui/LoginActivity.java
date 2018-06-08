package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
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
import com.zq.dynamicphoto.view.ILoginView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<ILoginView,WxLoginPresenter<ILoginView>> implements ILoginView {
    public static String token,openId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected WxLoginPresenter<ILoginView> createPresenter() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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