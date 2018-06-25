package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendCircleActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.iv_add_article_pic)
    ImageView ivAddArticlePic;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    private Boolean isSelectBg;
    private static String bgUrl, titleUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend_circle;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        String userLogo = sp.getString(Constans.USERLOGO, "");
        String remarkName = sp.getString(Constans.REMARKNAME, "");
        if (!TextUtils.isEmpty(userLogo)) {
            ImageLoaderUtils.displayImg(ivAvatar, userLogo);
        }
        if (!TextUtils.isEmpty(remarkName)) {
            tvNick.setText(remarkName);
        }
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

    @OnClick({R.id.layout_cancel, R.id.layout_finish, R.id.iv_add_article_pic,
            R.id.layout_bg, R.id.iv_add_dynamic})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_cancel:
                finish();
                break;
            case R.id.layout_finish:
                break;
            case R.id.iv_add_article_pic:
                isSelectBg = false;
                PicSelectUtils.getInstance().gotoSelectPic(this, 1);
                break;
            case R.id.layout_bg:
                isSelectBg = true;
                PicSelectUtils.getInstance().gotoSelectPic(this, 1);
                break;
            case R.id.iv_add_dynamic:
                MFGT.gotoDynamicSelectActivity(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    PicSelectUtils.getInstance().clear();
                    // 图片选择结果回调
                    ArrayList<LocalMedia> newFiles = (ArrayList<LocalMedia>) PictureSelector.obtainMultipleResult(data);
                    updateImages(newFiles);
                    break;
            }
        }
    }

    private void updateImages(ArrayList<LocalMedia> mSelectPic) {
        if (mSelectPic.size() != 0) {
            if (isSelectBg) {
                ImageLoaderUtils.displayImg(ivBg,mSelectPic.get(0).getPath());
            } else {
                ImageLoaderUtils.displayImg(ivAddArticlePic,mSelectPic.get(0).getPath());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgUrl = null;
        titleUrl = null;
    }
}
