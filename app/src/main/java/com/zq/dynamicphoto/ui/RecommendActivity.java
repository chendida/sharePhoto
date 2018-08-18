package com.zq.dynamicphoto.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserWatermark;
import com.zq.dynamicphoto.bean.WaterImage;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.tools.Message;
import com.zq.dynamicphoto.presenter.AddWatermarkPresenter;
import com.zq.dynamicphoto.ui.widge.StrokeTextView;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.WatermarkRecommedManager;
import com.zq.dynamicphoto.view.ILoadView;
import com.zq.dynamicphoto.view.UploadView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class RecommendActivity extends BaseActivity<ILoadView, AddWatermarkPresenter<ILoadView>>
        implements UploadView, ILoadView {
    private static final String TAG = "RecommendActivity";
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_title1)
    StrokeTextView tvTitle1;
    @BindView(R.id.iv_head1)
    ImageView ivHead1;
    @BindView(R.id.iv_wx_icon)
    ImageView ivWxIcon;
    @BindView(R.id.tv_title2)
    StrokeTextView tvTitle2;
    @BindView(R.id.layout_init_pic)
    AutoRelativeLayout layoutInitPic;
    @BindView(R.id.layout_whole_water_content)
    AutoRelativeLayout layoutWholeWaterContent;
    @BindView(R.id.layout_avatar_frame)
    AutoRelativeLayout layoutAvatarFrame;
    @BindView(R.id.layout_wx_and_icon)
    AutoRelativeLayout layoutWxAndIcon;
    @BindView(R.id.layout_et_title1)
    AutoRelativeLayout layoutEtTitle1;
    @BindView(R.id.layout_et_title3)
    AutoRelativeLayout layoutEtTitle3;
    @BindView(R.id.check_full_watermark)
    CheckBox checkFullWatermark;
    @BindView(R.id.check_water_bg_setting)
    CheckBox checkWaterBgSetting;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.et_title2)
    TextView etTitle2;
    @BindView(R.id.check_title3)
    CheckBox checkTitle3;
    @BindView(R.id.iv_wx_icon_hint)
    ImageView ivWxIconHint;
    @BindView(R.id.et_title3)
    TextView etTitle3;
    int screentWidth, screentHeight;
    int realHeight = 0;
    int realWidth = 0;
    String watermarkId;
    RequestOptions options = new RequestOptions()
            .circleCrop();
    private Boolean isChange = false;//是否更换过头像
    private String avatarPath = "";//图片路径
    private int frameType = 1;//默认的是不加边框是圆形
    private String path, url;
    @Override
    protected int getLayoutId() {
        watermarkId = getIntent().getStringExtra(Constans.WATERMARKID);
        if (watermarkId.equals(Constans.WATERMARKID_7008)
                || watermarkId.equals(Constans.WATERMARKID_7004)) {
            return R.layout.activity_recommend;
        }else if (watermarkId.equals(Constans.WATERMARKID_7005)){
            return R.layout.activity_recommend_7005;
        }else if (watermarkId.equals(Constans.WATERMARKID_7006)){
            return R.layout.activity_recommend_7006;
        }else if (watermarkId.equals(Constans.WATERMARKID_7007)){
            return R.layout.activity_recommend_7007;
        }else if (watermarkId.equals(Constans.WATERMARKID_7009)){
            return R.layout.activity_recommend_7009;
        }else if (watermarkId.equals(Constans.WATERMARKID_7011)){
            return R.layout.activity_recommend_7011;
        }else if (watermarkId.equals(Constans.WATERMARKID_7013)){
            return R.layout.activity_recommend_7013;
        }else if (watermarkId.equals(Constans.WATERMARKID_7014)){
            return R.layout.activity_recommend_7014;
        }else {
            return R.layout.activity_recommend_7015;
        }
    }

    @Override
    protected void initView() {
        screentWidth = getResources().getDisplayMetrics().widthPixels;//屏幕宽度
        screentHeight = getResources().getDisplayMetrics().heightPixels;//屏幕宽度
        EventBus.getDefault().register(this);
        layoutBack.setVisibility(View.VISIBLE);
        ivCamera.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.mould_preview));
        tvFinish.setText(getResources().getString(R.string.create_water));
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color7));
        //增加整体布局监听
        setLayoutListener();
        WatermarkRecommedManager.getInstance().initView(this,layoutInitPic,layoutWholeWaterContent,
                ivHead,ivHead1,ivWxIcon,ivWxIconHint,tvTitle1,tvTitle2,etTitle2,etTitle3,checkFullWatermark,checkWaterBgSetting);
        updateView();
    }

    private void updateView() {
        if (watermarkId.equals(Constans.WATERMARKID_7004)){
            etTitle2.setText(getResources().getString(R.string.tv_default_app_name));
            etTitle3.setText(getResources().getString(R.string.tv_default_wx));
            tvTitle1.setText(getResources().getString(R.string.tv_default_app_name));
            tvTitle2.setText(getResources().getString(R.string.tv_default_wx));
            layoutEtTitle1.setVisibility(View.VISIBLE);
        }else if (watermarkId.equals(Constans.WATERMARKID_7005)){
            etTitle2.setText(getResources().getString(R.string.tv_default_app_name));
            etTitle3.setText(getResources().getString(R.string.tv_default_wx));
            tvTitle1.setText(getResources().getString(R.string.tv_default_app_name));
            tvTitle2.setText(getResources().getString(R.string.tv_default_wx));
            WatermarkRecommedManager.getInstance().textOutline(29,8);
        }else if (watermarkId.equals(Constans.WATERMARKID_7006)
                || watermarkId.equals(Constans.WATERMARKID_7007)
                || watermarkId.equals(Constans.WATERMARKID_7011)){
            etTitle2.setText(getResources().getString(R.string.tv_default_app_name));
            etTitle3.setText(getResources().getString(R.string.tv_default_wx));
            tvTitle1.setText(getResources().getString(R.string.tv_default_app_name));
            tvTitle2.setText(getResources().getString(R.string.tv_default_wx));
        }else if (watermarkId.equals(Constans.WATERMARKID_7009)){
            etTitle2.setText(getResources().getString(R.string.tv_default_app_name));
            tvTitle1.setText(getResources().getString(R.string.tv_default_app_name));
            layoutEtTitle3.setVisibility(View.GONE);
        }else if (watermarkId.equals(Constans.WATERMARKID_7013)){
            etTitle2.setText(getResources().getString(R.string.tv_default_app_name));
            tvTitle1.setText(getResources().getString(R.string.tv_default_app_name));
            layoutEtTitle3.setVisibility(View.GONE);
            layoutEtTitle1.setVisibility(View.VISIBLE);
            ivAvatar.setImageDrawable(getResources().getDrawable(R.drawable.water_7013_avatar));
        }else if (watermarkId.equals(Constans.WATERMARKID_7014)){
            etTitle2.setText(getResources().getString(R.string.tv_default_7014));
            tvTitle1.setText(getResources().getString(R.string.tv_default_7014));
            layoutEtTitle3.setVisibility(View.GONE);
        }else if (watermarkId.equals(Constans.WATERMARKID_7015)){
            etTitle2.setText(getResources().getString(R.string.tv_input_content));
            tvTitle1.setText(getResources().getString(R.string.tv_input_content));
            layoutEtTitle3.setVisibility(View.GONE);
        }else if (watermarkId.equals(Constans.WATERMARKID_7008)){
            layoutEtTitle1.setVisibility(View.VISIBLE);
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_top_7008));
            ivHead1.setImageDrawable(getResources().getDrawable(R.drawable.water_bottom_7008));
            ivAvatar.setImageDrawable(getResources().getDrawable(R.drawable.water_top_7008));
            etTitle2.setText(getResources().getString(R.string.tv_default_app_name));
            etTitle3.setText(getResources().getString(R.string.tv_default_wx));
            tvTitle1.setText(getResources().getString(R.string.tv_default_app_name));
            tvTitle2.setText(getResources().getString(R.string.tv_default_wx));
        }
    }

    private void setLayoutListener() {
        ViewTreeObserver vto = layoutInitPic.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutInitPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layoutInitPic.getMeasuredHeight();
                int width = layoutInitPic.getMeasuredWidth();
                float maxHeight = (float) 410 / Constans.DEFAULT_HEIGHT * screentHeight;
                float maxWidth = (float) 700 / Constans.DEFAULT_WIDTH * screentWidth;
                float heightScale = (float) height / maxHeight;
                float widthScale = (float) width / maxWidth;
                if (heightScale > widthScale) {
                    WatermarkRecommedManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkRecommedManager.getInstance().setRealWidth((int) maxWidth);
                    realHeight = (int) maxHeight;
                    float w = width / heightScale;
                    realWidth = (int) w;
                } else {
                    WatermarkRecommedManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkRecommedManager.getInstance().setRealWidth((int) maxWidth);
                    realWidth = (int) maxWidth;
                    float h = height / widthScale;
                    realHeight = (int) h;
                }
                ViewGroup.LayoutParams layoutParams = layoutWholeWaterContent.getLayoutParams();
                layoutParams.width = realWidth;
                layoutParams.height = realHeight;
                layoutWholeWaterContent.setLayoutParams(layoutParams);
            }
        });

        ViewTreeObserver vto1 = ivHead.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivHead.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                float maxHeight = (float) 350 / Constans.DEFAULT_HEIGHT * screentHeight;
                int width = ivHead.getMeasuredWidth();
                int height = ivHead.getMeasuredHeight();
                if (height > maxHeight) {
                    ViewGroup.LayoutParams layoutParams = ivHead.getLayoutParams();
                    layoutParams.height = (int) maxHeight;
                    int ivHeadWidth = (int) (width * maxHeight / height);
                    layoutParams.width = ivHeadWidth;
                    ivHead.setLayoutParams(layoutParams);
                    Log.i(TAG, "ivHead width = " + ivHeadWidth + ",height = " + maxHeight);
                }
                Log.i(TAG, "ivHead width = " + maxHeight + ",height = " + height);
            }
        });

        checkFullWatermark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (WatermarkRecommedManager.getInstance().getDefault_screen_num() == 1) {
                        WatermarkRecommedManager.getInstance().setDefault_screen_num(2);
                    }
                    WatermarkRecommedManager.getInstance().setScreenWatermark();
                }else {
                    WatermarkRecommedManager.getInstance().setScreenWatermarkInit();
                }
            }
        });

        checkWaterBgSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    WatermarkRecommedManager.getInstance().updateBgColor();
                }else {
                    WatermarkRecommedManager.getInstance().closeWaterBg();
                }
            }
        });

        checkTitle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                layoutWxAndIcon.setVisibility(isChecked ? View.VISIBLE:View.GONE);
                WatermarkRecommedManager.getInstance().refreshChanged();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected AddWatermarkPresenter<ILoadView> createPresenter() {
        return new AddWatermarkPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_finish, R.id.layout_whole_color,
            R.id.check_full_watermark, R.id.layout_full_screen_watermark,
            R.id.check_water_bg_setting, R.id.layout_bg_setting, R.id.layout_et_title1,
            R.id.layout_et_title2, R.id.check_title3, R.id.layout_et_title3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                RecommendActivity.this.finish();
                break;
            case R.id.layout_finish:
                addWater();
                break;
            case R.id.layout_whole_color:
                WatermarkRecommedManager.getInstance().showWholeColorDialog();
                break;
            case R.id.check_full_watermark:
                break;
            case R.id.layout_full_screen_watermark:
                WatermarkRecommedManager.getInstance().showFullScreenWatermarkDialog();
                break;
            case R.id.check_water_bg_setting:
                break;
            case R.id.layout_bg_setting:
                WatermarkRecommedManager.getInstance().showWaterBgDialog();
                break;
            case R.id.layout_et_title1:
                MFGT.gotoUploadWaterAvatarActivity(this,watermarkId,isChange,frameType,avatarPath);
                break;
            case R.id.layout_et_title2:
                WatermarkRecommedManager.getInstance().showTextEditDialog(1);
                break;
            case R.id.check_title3:
                break;
            case R.id.layout_et_title3:
                WatermarkRecommedManager.getInstance().showTextEditDialog(2);
                break;
        }
    }

    private void addWater() {
        showLoading();
        layoutWholeWaterContent.setDrawingCacheEnabled(true);
        Bitmap bitmap = layoutWholeWaterContent.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        path = saveBitmap(System.currentTimeMillis() + ".png", bytes);
        CosUtils.getInstance(this).uploadToCos(path, 10);
        layoutWholeWaterContent.setDrawingCacheEnabled(false);
    }

    private String saveBitmap(String imgName, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            //filePath = null;
            try {
                String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/共享相册";
                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                imgName = filePath + "/" + imgName;
                fos = new FileOutputStream(imgName);
                fos.write(bytes);
                //scanPhoto(MyApplication.getAppContext(),imgName);
                return imgName;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ToastUtils.showShort("请检查SD卡是否可用");
        }
        return imgName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constans.REQUEST_CODE){
            if (resultCode == Constans.RESULT_CODE){
                isChange = data.getBooleanExtra(Constans.AVATAR_CHANGE, false);
                frameType = data.getIntExtra(Constans.FRAME_TYPE, 0);
                avatarPath = data.getStringExtra(Constans.AVATAR_PATH);
                Log.i(TAG,"frameType = " + frameType);
                if (!isChange){//图片没变
                    if (frameType == 2){
                        layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_square_shadow));
                    }else if (frameType == 3){
                        layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_circle_shadow));
                    }else {
                        layoutAvatarFrame.setBackground(null);
                    }
                    WatermarkRecommedManager.getInstance().refreshChanged();
                }else {//图片变化
                    Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
                    if (frameType == 0){
                        loadSquare(bitmap);
                    }else if (frameType == 1){
                        loadCirclePic(bitmap);
                    }else if (frameType == 2){
                        loadSquareAndShadow(bitmap);
                    }else if (frameType == 3){
                        loadCirclePicAndShadow(bitmap);
                    }
                }
            }
        }
    }


    /**
     * 单纯的处理图片成圆形显示，不加边框
     * @param bitmap
     */
    private void loadCirclePic(Bitmap bitmap){
        ivAvatar.setImageBitmap(bitmap);
        layoutAvatarFrame.setBackground(null);
        Glide.with(this).load(bitmap).apply(options).into(ivHead);
        synchorCirclePic();
    }
    /**
     * 处理图片成圆形显示并加边框
     * @param bitmap
     */
    private void loadCirclePicAndShadow(Bitmap bitmap){
        ivAvatar.setImageBitmap(bitmap);
        layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_circle_shadow));
        Glide.with(this).load(bitmap).apply(options).into(ivHead);
        synchorCirclePic();
    }

    private void synchorCirclePic(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                hideLoading();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WatermarkRecommedManager.getInstance().refreshChanged();
                    }
                });
            }
        };
        showLoading();
        timer.schedule(task,1000);
    }

    /**
     * 单纯的处理图片成正方形显示，不加边框
     * @param bitmap
     */
    private void loadSquare(Bitmap bitmap){
        ivHead.setImageBitmap(bitmap);
        ivAvatar.setImageBitmap(bitmap);
        layoutAvatarFrame.setBackground(null);
        WatermarkRecommedManager.getInstance().refreshChanged();
    }
    /**
     * 处理图片成正方形显示并加边框
     * @param bitmap
     */
    private void loadSquareAndShadow(Bitmap bitmap){
        ivHead.setImageBitmap(bitmap);
        ivAvatar.setImageBitmap(bitmap);
        layoutAvatarFrame.setBackground(getResources().getDrawable(R.drawable.shape_square_shadow));
        WatermarkRecommedManager.getInstance().refreshChanged();
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        hideLoading();
        if (code == Constans.REQUEST_OK) {
            this.url = url;
            UserWatermark userWatermark = new UserWatermark();
            userWatermark.setWatermarkType(1);//1表示图片水印，2表示文字水印
            userWatermark.setWatermarkUrl(url);
            int userId = SharedPreferencesUtils.getInstance().getInt(Constans.USERID, 0);
            userWatermark.setUserId(userId);
            NetRequestBean netRequestBean = new NetRequestBean();
            netRequestBean.setDeviceProperties(DrUtils.getInstance());
            netRequestBean.setUserWatermark(userWatermark);
            if (mPresenter != null) {
                mPresenter.addWatermarkPic(netRequestBean);
            }
        } else {
            ToastUtils.showShort(getResources().getString(R.string.tv_water_mark_upload_fail));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Message image) {
    }

    @Override
    public void showData(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                WaterImage image = new WaterImage(url);
                EventBus.getDefault().post(image);
                RecommendActivity.this.finish();
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }
}
