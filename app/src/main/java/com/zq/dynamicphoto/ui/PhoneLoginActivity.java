package com.zq.dynamicphoto.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.model.data.DataUtils;
import com.zq.dynamicphoto.presenter.PhoneLoginPresenter;
import com.zq.dynamicphoto.utils.MD5;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.ILoginView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手机号登录界面
 */
public class PhoneLoginActivity extends BaseActivity<ILoginView,PhoneLoginPresenter<ILoginView>> implements ILoginView {
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_login;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.phone_login),tvTitle,layoutBack,layoutFinish);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected PhoneLoginPresenter<ILoginView> createPresenter() {
        return new PhoneLoginPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_forget_pwd, R.id.btn_login})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_forget_pwd:
                MFGT.startActivity(this,ResetPwdActivity.class);
                break;
            case R.id.btn_login:
                if (isAll()) {
                    login();
                }
                break;
        }
    }

    private void login() {
        NetRequestBean netRequestBean = new NetRequestBean();
        DeviceProperties dr = DrUtils.getInstance();
        User user = new User();
        user.setPhone(etPhone.getText().toString());
        user.setPwd(MD5.getMessageDigest(etPwd.getText().toString()));
        netRequestBean.setUser(user);
        netRequestBean.setDeviceProperties(dr);
        if (mPresenter != null) {
            mPresenter.fetch(netRequestBean);
        }
    }

    private boolean isAll() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.please_input_phone), Toast.LENGTH_SHORT).show();
            etPhone.setFocusable(true);
            return false;
        } else if (etPhone.getText().toString().length() != 11 || !etPhone.getText().toString().substring(0, 1).equals("1")) {
            Toast.makeText(this, getResources().getString(R.string.please_input_phone_format), Toast.LENGTH_SHORT).show();
            etPhone.setFocusable(true);
            return false;
        } else if (TextUtils.isEmpty(etPwd.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.please_input_pwd), Toast.LENGTH_SHORT).show();
            etPwd.setFocusable(true);
            return false;
        } else if (etPwd.getText().toString().length() < 6) {
            Toast.makeText(this, getResources().getString(R.string.pwd_length), Toast.LENGTH_SHORT).show();
            etPwd.setFocusable(true);
            return false;
        }
        return true;
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
