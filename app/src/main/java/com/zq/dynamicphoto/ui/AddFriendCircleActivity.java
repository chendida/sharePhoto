package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.FriendCircleDynamicListAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.FriendCircleOperatePresenter;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.IUploadMomentsView;
import com.zq.dynamicphoto.view.UploadView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加|编辑朋友圈界面
 */
public class AddFriendCircleActivity extends BaseActivity<IUploadMomentsView,
        FriendCircleOperatePresenter<IUploadMomentsView>> implements IUploadMomentsView, UploadView {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.iv_add_article_pic)
    ImageView ivAddArticlePic;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.rcl_dynamic_list)
    RecyclerView rclDynamicList;
    @BindView(R.id.et_article_describe)
    EditText etArticleDescribe;
    @BindView(R.id.et_sign)
    EditText etSign;
    private Boolean isUpload = true;
    private Boolean isSelectBg;
    private static String bgUrl, titleUrl;
    private static final int code = 1001;
    FriendCircleDynamicListAdapter mAdapter;
    ArrayList<Dynamic> dynamicsList;
    private Moments moments;

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

        if (dynamicsList == null) {
            dynamicsList = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rclDynamicList.setLayoutManager(manager);
        mAdapter = new FriendCircleDynamicListAdapter(this, dynamicsList);
        rclDynamicList.setAdapter(mAdapter);
        rclDynamicList.setNestedScrollingEnabled(false);
        rclDynamicList.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
        moments = (Moments) getIntent().getSerializableExtra(Constans.MOMENTS);
        if (moments != null) {
            isUpload = false;
            getMomentsDetails();
        }
    }

    @Override
    protected FriendCircleOperatePresenter<IUploadMomentsView> createPresenter() {
        return new FriendCircleOperatePresenter<>();
    }


    @OnClick({R.id.layout_cancel, R.id.layout_finish, R.id.iv_add_article_pic,
            R.id.layout_bg, R.id.iv_add_dynamic})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_cancel:
                finish();
                break;
            case R.id.layout_finish:
                if (isAll()) {
                    showLoading();
                    if (isUpload){//添加朋友圈动态
                        uploadImages();
                    }else {//编辑朋友圈动态
                        editImages();
                    }
                }
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
                if (isUpload) {
                    MFGT.gotoDynamicSelectActivity(this, code);
                }else {
                    MFGT.gotoDynamicSelectActivity(this,code,moments.getId());
                }
                break;
        }
    }

    private void editImages() {
        if (bgUrl.startsWith("http") && titleUrl.startsWith("http")){
            editMoments();
        }else if (bgUrl.startsWith("http") && !titleUrl.startsWith("http")){
            compressImage(titleUrl,6);
        }else if (!bgUrl.startsWith("http") && titleUrl.startsWith("http")){
            compressImage(bgUrl,5);
        }else {
            uploadImages();
        }
    }

    private void editMoments() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        String ids = "";
        for (Dynamic dynamic:mAdapter.getmList()) {
            ids = ids + dynamic.getId() + ",";
        }
        String dynamicIds = ids.substring(0, ids.length() - 1);
        Moments moments = new Moments();
        moments.setBgImage(bgUrl);
        moments.setForwardLogo(titleUrl);
        moments.setTitle(etArticleDescribe.getText().toString());
        moments.setSignature(etSign.getText().toString());
        moments.setUserId(userId);
        moments.setDynamicIds(dynamicIds);
        moments.setId(this.moments.getId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setMoments(moments);
        if (mPresenter != null){
            mPresenter.editMoments(netRequestBean);
        }
    }

    private void uploadImages() {
        compressImage(titleUrl,6);
        compressImage(bgUrl,5);
    }

    /**
     * 上传朋友圈
     */
    private void uploadMoments() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        ArrayList<Dynamic> dynamicsList = mAdapter.getmList();
        String ids = "";
        for (Dynamic dynamic : dynamicsList) {
            ids = ids + dynamic.getId() + ",";
        }
        String dynamicIds = "";
        if (!TextUtils.isEmpty(ids)) {
            dynamicIds = ids.substring(0, ids.length() - 1);
        }
        Moments moments = new Moments();
        moments.setBgImage(bgUrl);
        moments.setForwardLogo(titleUrl);
        moments.setTitle(etArticleDescribe.getText().toString());
        moments.setUserId(userId);
        moments.setSignature(etSign.getText().toString());
        moments.setDynamicIds(dynamicIds);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setMoments(moments);
        if (mPresenter != null){
            mPresenter.uploadMoments(netRequestBean);
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
                        CosUtils.getInstance(AddFriendCircleActivity.this).uploadToCos(outfile,flag);
                    }
                });
            }
        }).start();
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
                case code:
                    dynamicsList = (ArrayList<Dynamic>) data.getSerializableExtra(Constans.DYNAMIC);
                    mAdapter.initDynamicList(dynamicsList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void updateImages(ArrayList<LocalMedia> mSelectPic) {
        if (mSelectPic.size() != 0) {
            if (isSelectBg) {
                bgUrl = mSelectPic.get(0).getPath();
                ImageLoaderUtils.displayImg(ivBg, bgUrl);
            } else {
                titleUrl = mSelectPic.get(0).getPath();
                ImageLoaderUtils.displayImg(ivAddArticlePic, titleUrl);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgUrl = null;
        titleUrl = null;
    }

    @Override
    public void showUploadMomentsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.upload_moments_success));
                finish();
            } else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void showEditMomentsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort(getResources().getString(R.string.edit_moments_success));
                finish();
            } else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void showGetMomentsDetailsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWith(result);
            } else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    private void dealWith(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            Moments moments = new Gson().fromJson(jsonObject.optString("moments"), Moments.class);
            if (moments != null) {
                updateView(moments);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateView(Moments moments) {
        if (!TextUtils.isEmpty(moments.getBgImage())) {
            bgUrl = moments.getBgImage();
            ImageLoaderUtils.displayImg(ivBg,bgUrl);
        }
        if (!TextUtils.isEmpty(moments.getForwardLogo())) {
            titleUrl = moments.getForwardLogo();
            ImageLoaderUtils.displayImg(ivAddArticlePic,titleUrl);
        }
        if (!TextUtils.isEmpty(moments.getSignature())) {
            etSign.setText(moments.getSignature());
        }
        if (!TextUtils.isEmpty(moments.getTitle())) {
            etArticleDescribe.setText(moments.getTitle());
        }
        if (moments.getDynamicList() != null) {
            if (moments.getDynamicList().size() != 0) {
                mAdapter.initDynamicList((ArrayList<Dynamic>) moments.getDynamicList());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        if (0 == code) {
            if (url.contains("forward")){
                titleUrl = url;
            }else {
                bgUrl = url;
            }
            if (titleUrl.startsWith("http") && bgUrl.startsWith("http")) {
                hideLoading();
                if (isUpload) {
                    uploadMoments();
                }else {
                    editMoments();
                }
            }
        } else {
            hideLoading();
            ToastUtils.showShort("上传图片失败");
        }
    }

    public boolean isAll() {
        if (TextUtils.isEmpty(titleUrl)) {
            ToastUtils.showShort(getResources().getString(R.string.tv_title_pic_is_null));
            return false;
        } else if (TextUtils.isEmpty(etArticleDescribe.getText().toString())){
            ToastUtils.showShort(getResources().getString(R.string.content_is_null));
            return false;
        } else if (TextUtils.isEmpty(bgUrl)){
            ToastUtils.showShort(getResources().getString(R.string.bg_is_null));
            return false;
        } else if (mAdapter.getmList().size() == 0){
            ToastUtils.showShort(getResources().getString(R.string.dynamic_is_null));
            return false;
        }
        return true;
    }

    /**
     * 获取朋友圈详情
     */
    private void getMomentsDetails() {
        DeviceProperties dr = DrUtils.getInstance();
        Moments moments = new Moments();
        moments.setId(this.moments.getId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setMoments(moments);
        if (mPresenter != null){
            mPresenter.getMomentsDetails(netRequestBean);
        }
    }
}
