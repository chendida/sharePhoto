package com.zq.dynamicphoto.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.utils.TitleUtils;
import butterknife.BindView;

public class AddLabelActivity extends BaseActivity {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_label;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.add_label),tvTitle, layoutBack, layoutFinish);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
