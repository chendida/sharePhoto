package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.TitleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.tv_bind_phone)
    TextView tvBindPhone;
    boolean isBind = false;//是否绑定过手机号

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        isBind = sp.getBoolean(Constans.ISBIND, false);
        if (isBind){
            tvBindPhone.setText(getResources().getString(R.string.reset_pwd));
        }else {
            tvBindPhone.setText(getResources().getString(R.string.bind_phone));
        }
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.tv_setting), tvTitle,
                layoutBack, layoutFinish);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @OnClick({R.id.layout_back, R.id.layout_bind_phone, R.id.layout_label_manager, R.id.layout_exit_login})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_bind_phone:
                MFGT.gotoResetPwdActivity(this,isBind);
                break;
            case R.id.layout_label_manager:
                MFGT.gotoLabelManagerActivity(this);
                break;
            case R.id.layout_exit_login:
                logout();
                break;
        }
    }

    private void logout() {
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        SharedPreferences.Editor edit = sp.edit();
        edit.clear().commit();
        finishAffinity();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }
}
