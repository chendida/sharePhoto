package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;

import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

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
                break;
            case R.id.layout_bind_phone:
                break;
            case R.id.layout_label_manager:
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
