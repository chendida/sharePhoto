package com.zq.dynamicphoto.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.model.data.DataUtils;
import com.zq.dynamicphoto.presenter.PhotoInfoPresenter;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.IPhotoInfoView;
import com.zq.dynamicphoto.view.UploadView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoInfoActivity extends BaseActivity<IPhotoInfoView,PhotoInfoPresenter<IPhotoInfoView>>
        implements IPhotoInfoView,UploadView{

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_photo_url)
    TextView tvPhotoUrl;
    @BindView(R.id.et_photo_name)
    EditText etPhotoName;
    @BindView(R.id.et_contact_phone)
    EditText etContactPhone;
    @BindView(R.id.et_wx_num)
    EditText etWxNum;
    @BindView(R.id.iv_add_wx_code)
    ImageView ivAddWxCode;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.layout_photo_url)
    AutoRelativeLayout layoutPhotoUrl;
    private static String avatarUrl,twoCodeUrl;
    private Boolean isSelectAvatar;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_info;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TitleUtils.setTitleBar(getResources().getString(R.string.title_photo_info), tvTitle,
                layoutBack, layoutFinish);
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        String photoUrl = sp.getString(Constans.PHOTOURL, "");
        tvPhotoUrl.setText(photoUrl);
        layoutPhotoUrl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!TextUtils.isEmpty(tvPhotoUrl.getText().toString())) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(tvPhotoUrl.getText());
                    ToastUtils.showShort("相册地址已复制到粘贴板");
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        getPhotoInfo();
    }

    @Override
    protected PhotoInfoPresenter<IPhotoInfoView> createPresenter() {
        return new PhotoInfoPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.btn_save,R.id.layout_wx_two_code,R.id.iv_avatar})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_save:
                if (isAll()) {
                    showLoading();
                    uploadImages();
                }
                break;
            case R.id.iv_avatar:
                isSelectAvatar = true;
                PicSelectUtils.getInstance().gotoSelectPic(this, 1);
                break;
            case R.id.layout_wx_two_code:
                isSelectAvatar = false;
                PicSelectUtils.getInstance().gotoSelectPic(this, 1);
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
            if (isSelectAvatar) {
                avatarUrl = mSelectPic.get(0).getPath();
                ImageLoaderUtils.displayImg(ivAvatar, avatarUrl);
            } else {
                twoCodeUrl = mSelectPic.get(0).getPath();
                ImageLoaderUtils.displayImg(ivAddWxCode, twoCodeUrl);
            }
        }
    }

    public boolean isAll() {
        if (TextUtils.isEmpty(etPhotoName.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.please_input_photo_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadImages() {
        if (twoCodeUrl.startsWith("http") && avatarUrl.startsWith("http")){
            editPhotoInfo();
        }else if (avatarUrl.startsWith("http") && !twoCodeUrl.startsWith("http")){
            compressImage(twoCodeUrl,9);
        }else if (!avatarUrl.startsWith("http") && twoCodeUrl.startsWith("http")){
            compressImage(avatarUrl,4);
        }else {
            compressImage(avatarUrl,4);
            compressImage(twoCodeUrl,9);
        }
    }

    /**
     * 压缩并上传图片
     *
     * @param
     */
    private void compressImage(final String src, final int flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(src).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        CosUtils.getInstance(PhotoInfoActivity.this).uploadToCos(outfile,flag);
                    }
                });
            }
        }).start();
    }

    private void editPhotoInfo() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setMobile(etContactPhone.getText().toString());
        userInfo.setUserLogo(avatarUrl);
        userInfo.setWxQRCode(twoCodeUrl);
        userInfo.setRemarkName(etPhotoName.getText().toString());
        userInfo.setWx(etWxNum.getText().toString());
        userInfo.setIntroduce(etDescription.getText().toString());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null){
            mPresenter.editPhotoInfo(netRequestBean);
        }
    }

    @Override
    public void showGetPhotoInfo(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            if (userInfo != null) {
                updateView(userInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateView(UserInfo userInfo) {
        if (!TextUtils.isEmpty(userInfo.getUserLogo())) {
            avatarUrl = userInfo.getUserLogo();
            ImageLoaderUtils.displayImg(ivAvatar,avatarUrl);
        }
        if (!TextUtils.isEmpty(userInfo.getRemarkName())) {
            etPhotoName.setText(userInfo.getRemarkName());
        }
        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            etContactPhone.setText(userInfo.getMobile());
        }
        if (!TextUtils.isEmpty(userInfo.getWx())) {
            etWxNum.setText(userInfo.getWx());
        }
        if (!TextUtils.isEmpty(userInfo.getWxQRCode())) {
            twoCodeUrl = userInfo.getWxQRCode();
            ImageLoaderUtils.displayImg(ivAddWxCode,twoCodeUrl);
        }
        if (!TextUtils.isEmpty(userInfo.getIntroduce())) {
            etDescription.setText(userInfo.getIntroduce());
        }
        if (!TextUtils.isEmpty(userInfo.getPhotoURL())) {
            tvPhotoUrl.setText(userInfo.getPhotoURL());
        }
        etPhotoName.setSelection(etPhotoName.getText().length());
        etDescription.setSelection(etDescription.getText().length());
        etWxNum.setSelection(etWxNum.getText().length());
    }

    @Override
    public void showEditPhotoInfo(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                SharedPreferences sp = SharedPreferencesUtils.getInstance();
                SharedPreferences.Editor edit = sp.edit();
                if (!TextUtils.isEmpty(avatarUrl)){
                    edit.putString(Constans.USERLOGO,avatarUrl);
                }
                if(!TextUtils.isEmpty(etPhotoName.getText().toString())){
                    edit.putString(Constans.REMARKNAME,etPhotoName.getText().toString());
                }
                edit.commit();
                ToastUtils.showShort("编辑成功");
                finish();
            } else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    public void getPhotoInfo() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null){
            mPresenter.getPhotoInfo(netRequestBean);
        }
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        if (0 == code) {
            if (url.contains("logo")){
                avatarUrl = url;
            }else {
                twoCodeUrl = url;
            }
            if (avatarUrl.startsWith("http") && twoCodeUrl.startsWith("http")) {
                hideLoading();
                editPhotoInfo();
            }
        } else {
            hideLoading();
            ToastUtils.showShort("上传图片失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        avatarUrl = null;
        twoCodeUrl = null;
    }
}
