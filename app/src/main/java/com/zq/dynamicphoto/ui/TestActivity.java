package com.zq.dynamicphoto.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {

    @BindView(R.id.iv_picture)
    ImageView ivPicture;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        /*Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.icon_add_bg);
        Bitmap bmp1= BitmapFactory.decodeResource(getResources(), R.drawable.icon_play);
        Bitmap waterMaskCenter = ImageUtil.createWaterMaskCenter(bmp, bmp1);
        ImageLoaderUtils.displayImg(ivPicture,waterMaskCenter);*/
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
