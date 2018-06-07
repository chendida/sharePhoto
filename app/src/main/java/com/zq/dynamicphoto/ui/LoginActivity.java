package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.presenter.WxLoginPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IWxLoginView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<IWxLoginView,WxLoginPresenter<IWxLoginView>> implements IWxLoginView{
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
    protected WxLoginPresenter<IWxLoginView> createPresenter() {
        return new WxLoginPresenter<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                break;
        }
    }


    public void wxLogin() {
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            Toast.makeText(this, "您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("wxLogin","微信登录1");
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MyApplication.mWxApi.sendReq(req);
    }

    @Override
    public void showData(Result result) {
        if (result != null) {
            dealWithResult(result);
        }else {
            showFailed();
        }
    }


    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            User user = new Gson().fromJson(jsonObject.optString("user"), User.class);
            SharedPreferences sp = SharedPreferencesUtils.getInstance();
            SharedPreferences.Editor edit = sp.edit();
            if (userInfo != null){
                if (userInfo.getUserId() != 0){
                    edit.putInt("userId",userInfo.getUserId());
                }
                if (!TextUtils.isEmpty(userInfo.getUserLogo())){
                    //对返回的图片地址中的“\”去掉
                    String userLogo = userInfo.getUserLogo().replaceAll("\"", "");
                    edit.putString("userLogo",userLogo);
                }
                if (!TextUtils.isEmpty(userInfo.getBgImage())){
                    edit.putString("bgImage",userInfo.getBgImage());
                }
                if (!TextUtils.isEmpty(userInfo.getUrl())){
                    edit.putString("photoUrl",userInfo.getUrl());
                }
                if (!TextUtils.isEmpty(userInfo.getRemarkName())){
                    edit.putString("remarkName",userInfo.getRemarkName());
                }
                if (user != null){
                    if (!TextUtils.isEmpty(user.getPhone())){
                        edit.putBoolean("isBind",true);
                    }
                    if (!TextUtils.isEmpty(user.getUnionId())){
                        edit.putString("unionId",user.getUnionId());
                    }
                }
                edit.putBoolean("isLogin",true);
                edit.commit();
                startActivity(new Intent(this, HomeActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
