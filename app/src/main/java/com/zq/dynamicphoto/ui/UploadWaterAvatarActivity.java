package com.zq.dynamicphoto.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上传水印头像
 */
public class UploadWaterAvatarActivity extends BaseActivity {

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.layout_avatar_frame)
    AutoRelativeLayout layoutAvatarFrame;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.check_frame)
    CheckBox checkFrame;
    @BindView(R.id.check_circle)
    CheckBox checkCircle;
    private String watermarkId;
    RequestOptions options = new RequestOptions()
            .circleCrop();
    private String path;
    private Boolean isSelect = false;
    private int frameType;
    @Override
    protected int getLayoutId() {
        watermarkId = getIntent().getStringExtra(Constans.WATERMARKID);
        isSelect = getIntent().getBooleanExtra(Constans.AVATAR_CHANGE,false);
        path = getIntent().getStringExtra(Constans.AVATAR_PATH);
        frameType = getIntent().getIntExtra(Constans.FRAME_TYPE,1);
        return R.layout.activity_upload_water_avatar;
    }

    /**
     * 得到资源文件中图片的Uri
     * @param id 资源id
     * @return path
     */
    public String getPathDrawableRes(int id) {
        Resources resources = getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return path;
    }

    @Override
    protected void initView() {
        if (watermarkId.equals(Constans.WATERMARKID_7004)){
            if (!isSelect) {//没改变
                ivAvatar.setImageDrawable(getResources().getDrawable(R.drawable.water_top_7004));
                if (frameType ==2){
                    checkFrame.setChecked(true);
                    checkCircle.setChecked(false);
                    layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_square_shadow));
                }else if (frameType == 3){
                    checkFrame.setChecked(true);
                    checkCircle.setChecked(true);
                    layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_circle_shadow));
                }else if (frameType == 1){
                    checkCircle.setChecked(true);
                    checkFrame.setChecked(false);
                }else {
                    checkCircle.setChecked(false);
                    checkFrame.setChecked(false);
                }
            }else {
                if (frameType == 0){
                    checkCircle.setChecked(false);
                    checkFrame.setChecked(false);
                    loadSquare(path);
                }else if (frameType == 1){
                    checkCircle.setChecked(true);
                    checkFrame.setChecked(false);
                    loadCirclePic(path);
                }else if (frameType == 2){
                    checkCircle.setChecked(false);
                    checkFrame.setChecked(true);
                    loadSquareAndShadow(path);
                }else{
                    checkCircle.setChecked(true);
                    checkFrame.setChecked(true);
                    loadCirclePicAndShadow(path);
                }
            }
        }
        setListener();
    }

    /**
     * 单纯的处理图片成圆形显示，不加边框
     * @param path
     */
    private void loadCirclePic(String path){
        Glide.with(this).load(path).apply(options).into(ivAvatar);
    }
    /**
     * 处理图片成圆形显示并加边框
     * @param path
     */
    private void loadCirclePicAndShadow(String path){
        Glide.with(this).load(path).apply(options).into(ivAvatar);
        layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_circle_shadow));
    }

    /**
     * 单纯的处理图片成正方形显示，不加边框
     * @param path
     */
    private void loadSquare(String path){
        Glide.with(this).load(path).into(ivAvatar);
    }
    /**
     * 处理图片成正方形显示并加边框
     * @param path
     */
    private void loadSquareAndShadow(String path){
        Glide.with(this).load(path).into(ivAvatar);
        layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_square_shadow));
    }

    private void setListener() {
        checkCircle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkFrame.isChecked()){
                        if (isSelect) {
                            loadCirclePicAndShadow(path);
                        }else {
                            layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_circle_shadow));
                        }
                    }else {
                        if (isSelect) {
                            loadCirclePic(path);
                        }
                    }
                }else {
                    if (checkFrame.isChecked()){
                        if (isSelect) {
                            loadSquareAndShadow(path);
                        }else {
                            layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_square_shadow));
                        }
                    }else {
                        if (isSelect) {
                            loadSquare(path);
                        }
                    }
                }
            }
        });

        checkFrame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkCircle.isChecked()){//添加圆形边框
                        if (isSelect) {
                            loadCirclePicAndShadow(path);
                        }else {
                            layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_circle_shadow));
                        }
                    }else {//添加正方形边框
                        if (isSelect) {
                            loadSquareAndShadow(path);
                        }else {
                            layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_square_shadow));
                        }
                    }
                }else {//去除边框
                    layoutAvatarFrame.setBackground(null);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @OnClick({R.id.layout_back, R.id.layout_finish, R.id.iv_avatar, R.id.tv_click_upload,
            R.id.layout_frame, R.id.layout_circle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                if (!checkFrame.isChecked() && !checkCircle.isChecked()){//不加边框，不是圆形
                    frameType = 0;
                }else if (!checkFrame.isChecked() && checkCircle.isChecked()){//不加边框，是圆形
                    frameType = 1;
                }else if (checkFrame.isChecked() && !checkCircle.isChecked()){//加边框，不是圆形
                    frameType = 2;
                }else if (checkFrame.isChecked() && checkCircle.isChecked()){//加边框，是圆形
                    frameType = 3;
                }
                setResult(Constans.RESULT_CODE,new Intent()
                        .putExtra(Constans.AVATAR_CHANGE,isSelect)
                        .putExtra(Constans.FRAME_TYPE,frameType)
                        .putExtra(Constans.AVATAR_PATH,path));
                finish();
                break;
            case R.id.iv_avatar:
            case R.id.tv_click_upload:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .previewImage(true)
                        .enableCrop(true)
                        .withAspectRatio(1,1)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.layout_frame:
                changeCheckStatus(checkFrame);
                break;
            case R.id.layout_circle:
                changeCheckStatus(checkCircle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    isSelect = true;
                    // 图片选择结果回调
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    path = localMedia.get(0).getCutPath();
                    updatePic(path);
                    break;
            }
        }
    }

    private void updatePic(String path) {
        if (checkFrame.isChecked()){
            if (checkCircle.isChecked()){
                loadCirclePicAndShadow(path);
            }else {
                loadSquareAndShadow(path);
            }
        }else {
            if (checkCircle.isChecked()){
                loadCirclePic(path);
            }else {
                loadSquare(path);
            }
        }
    }

    private void changeCheckStatus(CheckBox checkBox){
        if (checkBox.isChecked()){
            checkBox.setChecked(false);
        }else {
            checkBox.setChecked(true);
        }
    }
}
