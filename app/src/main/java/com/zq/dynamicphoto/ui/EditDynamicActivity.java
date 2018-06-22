package com.zq.dynamicphoto.ui;

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

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PicAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicBean;
import com.zq.dynamicphoto.bean.DynamicForward;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.bean.MessageEvent;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.SaveLabelUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.utils.TitleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditDynamicActivity extends BaseActivity implements PicAdapter.AddPicListener {

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_pic;
    }

    @Override
    protected void initView() {
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
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
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接收图片选择器返回结果，更新所选图片集合
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    PicSelectUtils.getInstance().clear();
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
                PicSelectUtils.getInstance().gotoSelectPicOrVideo(localMedia, this);
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
            R.id.check_clause, R.id.tv_about_clause, R.id.btn_one_key_share
            , R.id.layout_finish})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_finish:
                if (checkClause.isChecked()) {
                    toUpload();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.please_read_and_agree_clause), Toast.LENGTH_SHORT).show();
                }
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
                break;
            case R.id.check_clause:
                break;
            case R.id.tv_about_clause:
                break;
            case R.id.btn_one_key_share:
                break;
        }
    }

    private void toUpload() {
        if (isFastClick()) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        String content = etDescriptionContent.getText().toString();
        ArrayList<LocalMedia> mSelectedImages = mAdapter.getmList();
        if (TextUtils.isEmpty(content) && mSelectedImages.size() == 0) {
            Toast.makeText(this, "图片和文本不能都为空", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String>images = new ArrayList<>();
        for (LocalMedia localMedia:mSelectedImages) {
            images.add(localMedia.getPath());
        }
        DynamicBean dynamicBean = new DynamicBean();
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
}
