package com.zq.dynamicphoto.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.TitleUtils;
import butterknife.BindView;
import butterknife.OnClick;

public class AboutAppActivity extends BaseActivity {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_app;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.tv_about_app), tvTitle, layoutBack, layoutFinish);
        String content = getAppName(this) + "v"+ DrUtils.getInstance().getVersion();
        tvAppVersion.setText(content);
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.layout_back, R.id.layout_goto_grade, R.id.layout_system_notify,
            R.id.layout_about_mime})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_goto_grade:
                gotoScore();
                break;
            case R.id.layout_system_notify:
                MFGT.gotoHtmlManagerActivity(this, "notice.html",
                        getResources().getString(R.string.system_notify));
                break;
            case R.id.layout_about_mime:
                MFGT.gotoHtmlManagerActivity(this, "about.html",
                        getResources().getString(R.string.about_mime));
                break;
        }
    }

    private void gotoScore() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
