package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.AuthCode;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.BindPhonePresenter;
import com.zq.dynamicphoto.utils.MD5;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.IBindPhoneView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 重置密码或者绑定手机号
 */
public class ResetPwdActivity extends BaseActivity<IBindPhoneView,BindPhonePresenter<IBindPhoneView>>
        implements IBindPhoneView{

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_time_num)
    TextView tvTimeNum;
    @BindView(R.id.layout_get_identifying_code)
    AutoRelativeLayout layoutGetIdentifyingCode;
    private Boolean isBind = false;//是否绑定过手机号码
    MyCountDownTimer myCountDownTimer;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initView() {
        isBind = getIntent().getBooleanExtra(Constans.ISBIND, false);
        String title = "";
        if (isBind) {//绑定过
            title = getResources().getString(R.string.reset_pwd);
        } else {//未绑定
            title = getResources().getString(R.string.bind_phone);
        }
        TitleUtils.setTitleBar(title, tvTitle,
                layoutBack, layoutFinish);
        myCountDownTimer = new MyCountDownTimer(61000,1000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BindPhonePresenter<IBindPhoneView> createPresenter() {
        return new BindPhonePresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_get_identifying_code, R.id.btn_login})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_get_identifying_code:
                if (!TextUtils.isEmpty(etPhone.getText().toString())) {
                    if (isBind) {
                        getAutoCode(2);
                    }else {
                        getAutoCode(1);
                    }
                } else {
                    ToastUtils.showShort("电话号码不能为空");
                }
                break;
            case R.id.btn_login:
                if (isAll()) {
                    if (isBind) {
                        resetPwd();
                    }else {
                        bindPhone();
                    }
                }
                break;
        }
    }

    /**
     * 重置密码
     */
    private void resetPwd() {
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        User user = new User();
        user.setPhone(etPhone.getText().toString());
        user.setPwd(MD5.getMessageDigest(etPwd.getText().toString()));
        AuthCode authCode = new AuthCode();
        authCode.setCode(etCode.getText().toString());
        netRequestBean.setAuthCode(authCode);
        netRequestBean.setUser(user);
        netRequestBean.setDeviceProperties(dr);
        if (mPresenter != null){
            mPresenter.resetPwd(netRequestBean);
        }
    }

    /**
     * 绑定手机号
     */
    private void bindPhone() {
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        User user = new User();
        user.setPhone(etPhone.getText().toString());
        user.setPwd(MD5.getMessageDigest(etPwd.getText().toString()));
        user.setUserId(userId);
        AuthCode authCode = new AuthCode();
        authCode.setCode(etCode.getText().toString());
        netRequestBean.setUser(user);
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setAuthCode(authCode);
        if (mPresenter != null){
            mPresenter.bindPhone(netRequestBean);
        }
    }

    public boolean isAll() {
        if (TextUtils.isEmpty(etPhone.getText().toString())){
            Toast.makeText(this,getResources().getString(R.string.please_input_phone), Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etPwd.getText().toString())){
            Toast.makeText(this,getResources().getString(R.string.please_input_pwd), Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etCode.getText().toString())){
            Toast.makeText(this,getResources().getString(R.string.please_input_code), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 获取验证码
     */
    public void getAutoCode(int type) {
        tvTimeNum.setVisibility(View.VISIBLE);
        layoutGetIdentifyingCode.setVisibility(View.GONE);
        myCountDownTimer.start();
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        AuthCode authCode = new AuthCode();
        authCode.setMobile(etPhone.getText().toString());
        authCode.setModel(type);
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setAuthCode(authCode);
        if (mPresenter != null){
            mPresenter.getAuthCode(netRequestBean);
        }
    }

    @Override
    public void showBindPhoneResult(Result result) {
        if (result != null){
            if (result.getResultCode() == Constans.REQUEST_OK){
                ToastUtils.showShort("绑定成功");
                finish();
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void showGetAuthCodeResult(Result result) {
        if (result != null){
            if (result.getResultCode() == Constans.REQUEST_OK){
                ToastUtils.showShort("获取验证码成功");
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void showResetPwdResult(Result result) {
        if (result != null){
            if (result.getResultCode() == Constans.REQUEST_OK){
                ToastUtils.showShort("重置成功");
                finish();
            }else if (result.getResultCode() == -10){
                ToastUtils.showShort("验证码错误");
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        private MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            if (tvTimeNum != null) {
                tvTimeNum.setText(l / 1000 + "s");
            }
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            if (tvTimeNum != null) {
                tvTimeNum.setVisibility(View.GONE);
            }
            if (layoutGetIdentifyingCode != null) {
                layoutGetIdentifyingCode.setVisibility(View.VISIBLE);
            }
        }
    }
}
