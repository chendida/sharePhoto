package com.zq.dynamicphoto.ui;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PicAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicBean;
import com.zq.dynamicphoto.bean.DynamicForward;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.bean.MessageEvent;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.model.GetVipStatusImp;
import com.zq.dynamicphoto.presenter.GetVipStatusPresenter;
import com.zq.dynamicphoto.ui.widge.ShareDialog;
import com.zq.dynamicphoto.ui.widge.VipHintDialog;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.PermissionUtils;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.SaveLabelUtils;
import com.zq.dynamicphoto.utils.ShareUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.ILoadView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditDynamicActivity extends BaseActivity<ILoadView,GetVipStatusPresenter<ILoadView>> implements
        PicAdapter.AddPicListener,ImageSaveUtils.DownLoadListener,ILoadView {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.layout_article)
    AutoRelativeLayout layoutArticle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.et_description_content)
    EditText etDescriptionContent;
    @BindView(R.id.id_grid_view_commit_answers)
    GridView mGridView;
    @BindView(R.id.check_clause)
    CheckBox checkClause;
    @BindView(R.id.tv_who_can_see)
    TextView tvWhoCanSee;
    private PicAdapter mAdapter;
    private final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于500ms
    private long lastClickTime;
    private Integer userId;
    private Dynamic dynamic;
    private ShareDialog shareDialog;
    private Boolean isUploadPic = false;
    private Boolean isVip = false;
    private int type = 0;
    SharedPreferences sp = SharedPreferencesUtils.getInstance();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_pic;
    }

    @Override
    protected void initView() {
        userId = sp.getInt(Constans.USERID, 0);
        dynamic = (Dynamic) getIntent().getSerializableExtra(Constans.DYNAMIC);
        TitleUtils.setTitleBar(getResources().getString(R.string.edit_image_and_text), tvTitle, layoutBack, ivCamera, tvFinish);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        updateView(dynamic);
        initListener();
    }

    private void updateView(Dynamic dynamic) {
        ArrayList<LocalMedia> mSelectedImages = new ArrayList<>();
        mAdapter = new PicAdapter(this, mSelectedImages, this);
        if (dynamic != null){
            etDescriptionContent.setText(dynamic.getContent());
            if (dynamic.getIsOpen() != null) {
                if (dynamic.getUserId().equals(userId)) {
                    if (dynamic.getIsOpen() == 1) {
                        tvWhoCanSee.setText(getResources().getString(R.string.everyone_can_see));
                    } else {
                        tvWhoCanSee.setText(getResources().getString(R.string.one_can_see));
                    }
                }
            }
            if (dynamic.getDynamicLabels() != null) {
                SaveLabelUtils.getInstance().getDynamicLabels().clear();
                if (dynamic.getUserId().equals(userId)) {
                    if (dynamic.getDynamicLabels().size() != 0) {
                        SaveLabelUtils.getInstance().getDynamicLabels().addAll(dynamic.getDynamicLabels());
                    }
                }
            }
            if (dynamic.getDynamicType() == PictureConfig.TYPE_VIDEO){//视频
                if (dynamic.getDynamicVideos() != null) {
                    if (dynamic.getDynamicVideos().size() != 0) {
                        for (DynamicVideo dynamicVideo : dynamic.getDynamicVideos()) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(dynamicVideo.getVideoURL());
                            mSelectedImages.add(localMedia);
                        }
                    }
                }
            }else{//图文
                if (dynamic.getDynamicPhotos() != null) {
                    if (dynamic.getDynamicPhotos().size() != 0) {
                        for (DynamicPhoto dynamicPhoto : dynamic.getDynamicPhotos()) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(dynamicPhoto.getThumbnailURL());
                            mSelectedImages.add(localMedia);
                        }
                    }
                }
            }
        }
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        layoutArticle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!TextUtils.isEmpty(etDescriptionContent.getText().toString())) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(etDescriptionContent.getText());
                    Toast.makeText(EditDynamicActivity.this, "文本已复制到粘贴板", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
    }

    @Override
    protected GetVipStatusPresenter<ILoadView> createPresenter() {
        return new GetVipStatusPresenter<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接收图片选择器返回结果，更新所选图片集合
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    PicSelectUtils.getInstance().clear();
                    isUploadPic = true;
                    // 图片选择结果回调
                    ArrayList<LocalMedia> newFiles = (ArrayList<LocalMedia>) PictureSelector.obtainMultipleResult(data);
                    mAdapter.initDynamicList(newFiles);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<DynamicLabel> dynamicLabels = SaveLabelUtils.getInstance().getDynamicLabels();
        if (dynamicLabels != null) {
            if (dynamicLabels.size() != 0) {
                String str = "";
                for (DynamicLabel d : dynamicLabels) {
                    str = str + d.getLabeltext() + ",";
                }
                if (!TextUtils.isEmpty(str)) {
                    tvLabel.setText(str.substring(0, str.length() - 1));
                }
            } else {
                tvLabel.setText(getResources().getString(R.string.by_label_category));
            }
        }
    }

    /**
     * 添加图片回调
     *
     * @param view
     * @param i
     */
    @Override
    public void onAddButtonClick(View view, int i) {
        ArrayList<LocalMedia> localMedia = mAdapter.getmList();
        switch (view.getId()) {
            case R.id.id_item_add_pic:
                PicSelectUtils.getInstance().gotoSelectPicOrVideo(localMedia, this,Constans.MAX_PIC_NUM);
                break;
            case R.id.iv_item_image_view://预览
                PicSelectUtils.getInstance().preview(i, localMedia, this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveLabelUtils.getInstance().getDynamicLabels().clear();
        EventBus.getDefault().unregister(this);
        if (shareDialog != null){
            if (shareDialog.isShowing()){
                shareDialog.dismiss();
                shareDialog = null;
            }else {
                shareDialog = null;
            }
        }
        ImageSaveUtils.getInstance(this).clearListener();
    }

    public boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    @OnClick({R.id.layout_back, R.id.layout_article, R.id.layout_label, R.id.layout_who_can_see,
             R.id.tv_about_clause, R.id.btn_one_key_share
            , R.id.layout_finish})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_finish:
                type = 0;
                getVipStatus();
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_article:
                SoftUtils.softShow(this);
                break;
            case R.id.layout_label:
                MFGT.gotoAddLabelActivity(this);
                break;
            case R.id.layout_who_can_see:
                PermissionUtils.showSeePermissionSelectDialog(this,tvWhoCanSee);
                break;
            case R.id.tv_about_clause:
                SharedPreferences sp = SharedPreferencesUtils.getInstance();
                String agreement = sp.getString(Constans.AGREEMENT, "");
                MFGT.gotoHtmlManagerActivity(this,agreement,getResources().getString(R.string.about_clause));
                break;
            case R.id.btn_one_key_share:
                if (mAdapter.getmList().size() != 0){
                    Dynamic dynamic = new Dynamic();
                    if (mAdapter.getmList().get(0).getPath().endsWith(".mp4")){
                        dynamic.setDynamicType(PictureConfig.TYPE_VIDEO);
                        ArrayList<DynamicVideo>videos = new ArrayList<>();
                        for (LocalMedia media:mAdapter.getmList()) {
                            DynamicVideo video = new DynamicVideo();
                            video.setVideoURL(media.getPath());
                            videos.add(video);
                        }
                        dynamic.setDynamicVideos(videos);
                    }else {
                        dynamic.setDynamicType(PictureConfig.TYPE_IMAGE);
                        ArrayList<DynamicPhoto>photos = new ArrayList<>();
                        for (LocalMedia media:mAdapter.getmList()) {
                            DynamicPhoto photo = new DynamicPhoto();
                            photo.setThumbnailURL(media.getPath());
                            photos.add(photo);
                        }
                        dynamic.setDynamicPhotos(photos);
                    }
                    dynamic.setContent(etDescriptionContent.getText().toString());
                    showShareDialog(dynamic);
                }else {
                    ToastUtils.showShort("至少选择一张图片或视频");
                }
                break;
        }
    }

    public void getVipStatus() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null) {
            mPresenter.getVipStatus(netRequestBean);
        }
    }

    private void uploadDynamic() {
        if (!isUploadPic) {
            if (checkClause.isChecked()) {
                toUpload(type);
            } else {
                Toast.makeText(this, getResources().getString(R.string.please_read_and_agree_clause), Toast.LENGTH_SHORT).show();
            }
        }else {//改变了图片
            if (isVip){
                if (checkClause.isChecked()) {
                    toUpload(type);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.please_read_and_agree_clause), Toast.LENGTH_SHORT).show();
                }
            }else {
                showVipHintDialog();
            }
        }
    }

    private void showVipHintDialog() {
        new VipHintDialog(this, R.style.dialog, new VipHintDialog.OnItemClickListener() {
            @Override
            public void onClick(int position, Dialog dialog) {
                dialog.dismiss();
                switch (position){
                    case 1://了解详情

                        break;
                    case 2://立即开通
                        MFGT.gotoOpenVipActivity(EditDynamicActivity.this);
                        break;
                }
            }
        }).show();
    }

    private void showShareDialog(final Dynamic dynamic) {
        shareDialog = new ShareDialog(this, R.style.dialog, new ShareDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1://分享给好友
                        type = 1;
                        getVipStatus();
                        //toUpload(1);
                        break;
                    case 2://分享给微信朋友圈
                        type = 2;
                        getVipStatus();
                        //toUpload(2);
                        break;
                    case 3://批量保存
                        if (dynamic != null){
                            showLoading();
                            ImageSaveUtils.getInstance(EditDynamicActivity.this).saveAll(dynamic);
                        }
                        break;
                }
            }
        });
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtils.showShort(getResources().getString(R.string.have_no_wx));
        }else {
            shareDialog.show();
        }
    }

    private void toUpload(int isShare) {
        if (isFastClick()) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        String content = etDescriptionContent.getText().toString();
        ArrayList<LocalMedia> mSelectedImages = mAdapter.getmList();
        if (TextUtils.isEmpty(content) && mSelectedImages.size() == 0) {
            ToastUtils.showShort("图片和文本不能都为空");
            return;
        }
        ArrayList<String>images = new ArrayList<>();
        for (LocalMedia localMedia:mSelectedImages) {
            images.add(localMedia.getPath());
        }
        DynamicBean dynamicBean = new DynamicBean();
        dynamicBean.setIsShare(isShare);
        if (dynamic.getUserId().equals(userId)){
            dynamicBean.setRequestType(Constans.EDIT_DYNAMIC);
        }else {
            DynamicForward dynamicForward = new DynamicForward();
            dynamicForward.setUdynamicId(dynamic.getId());
            dynamicForward.setIuserId(userId);
            dynamicForward.setUuserId(dynamic.getUserId());
            dynamicBean.setDynamicForward(dynamicForward);
            dynamicBean.setRequestType(Constans.REPEAT_DYNAMIC);
        }
        if (mSelectedImages.size() > 0) {
            if (mSelectedImages.get(0).getPath().startsWith("http") && mSelectedImages.get(0).getPath().endsWith(".mp4")){
                images.add(dynamic.getDynamicVideos().get(0).getVideoCover());
            }
            if (mSelectedImages.get(0).getPath().endsWith(".mp4")){
                dynamicBean.setPicType(PictureConfig.TYPE_VIDEO);
            }else {
                dynamicBean.setPicType(PictureConfig.TYPE_IMAGE);
            }
        }else {
            dynamicBean.setPicType(PictureConfig.TYPE_IMAGE);
        }
        dynamicBean.setDynamicId(dynamic.getId());
        dynamicBean.setmSelectedImages(images);
        dynamicBean.setContent(content);
        dynamicBean.setDynamicLabels(SaveLabelUtils.getInstance().getDynamicLabels());
        dynamicBean.setPermission(tvWhoCanSee.getText().toString());
        MessageEvent messageEvent = new MessageEvent(dynamicBean);
        EventBus.getDefault().post(messageEvent);
        finish();
    }

    @Override
    public void callBack(int code, String msg) {
        hideLoading();
        ToastUtils.showShort(msg);
    }

    @Override
    public void showData(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            if (userInfo != null) {
                if (userInfo.getIsVip() != null){
                    if (userInfo.getIsVip() == 1){
                        isVip = true;
                    }else {
                        isVip = false;
                    }
                    uploadDynamic();
                }else {
                    ToastUtils.showShort(getResources().getString(R.string.data_error));
                }
            }else {
                ToastUtils.showShort(getResources().getString(R.string.data_error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
