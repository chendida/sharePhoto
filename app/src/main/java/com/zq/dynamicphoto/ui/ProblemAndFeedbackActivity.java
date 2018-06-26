package com.zq.dynamicphoto.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.TitleUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ProblemAndFeedbackActivity extends BaseActivity {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preblem_and_feedbace;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.tv_problem), tvTitle, layoutBack,layoutFinish);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @OnClick({R.id.layout_back, R.id.layout_problem, R.id.layout_contact_custom_service,
            R.id.layout_feedback, R.id.layout_share_fail,
            R.id.layout_safe, R.id.layout_synchro_friend})
    public void onClicked(View view) {
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_problem:
                MFGT.gotoHtmlManagerActivity(this,"problem.html?userId="+userId,
                        getResources().getString(R.string.tv_FAQ));
                break;
            case R.id.layout_contact_custom_service:
                if (checkApkExist(this, "com.tencent.mobileqq")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+"1492117043"+"&version=1")));
                }else{
                    ToastUtils.showShort("本机未安装QQ应用");
                }
                break;
            case R.id.layout_feedback:
                MFGT.gotoHtmlManagerActivity(this,"feedback.html?userId="+userId,
                        getResources().getString(R.string.tv_feedback));
                break;
            case R.id.layout_share_fail:
                MFGT.gotoHtmlManagerActivity(this,"problem2.html?userId="+userId,
                        getResources().getString(R.string.tv_share_friend_circle_fail));
                break;
            case R.id.layout_safe:
                MFGT.gotoHtmlManagerActivity(this,"problem5.html?userId="+userId,
                        getResources().getString(R.string.tv_private_safe));
                break;
            case R.id.layout_synchro_friend:
                MFGT.gotoHtmlManagerActivity(this,"problem6.html",
                        getResources().getString(R.string.tv_synchro_friend_circle));
                break;
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
