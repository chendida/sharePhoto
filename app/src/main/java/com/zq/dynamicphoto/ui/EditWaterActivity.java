package com.zq.dynamicphoto.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserWatermark;
import com.zq.dynamicphoto.bean.WaterImage;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.tools.Message;
import com.zq.dynamicphoto.presenter.AddWatermarkPresenter;
import com.zq.dynamicphoto.ui.widge.StrokeTextView;
import com.zq.dynamicphoto.utils.ColorUtils;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.WatermarkManager;
import com.zq.dynamicphoto.view.ILoadView;
import com.zq.dynamicphoto.view.UploadView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import butterknife.BindView;
import butterknife.OnClick;

public class EditWaterActivity extends BaseActivity<ILoadView, AddWatermarkPresenter<ILoadView>>
        implements UploadView, ILoadView {
    private static final String TAG = "EditWaterActivity";
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_water_title)
    StrokeTextView tvWaterTitle;
    @BindView(R.id.tv_water_mark_title)
    TextView tvWatermarkTitle;
    @BindView(R.id.tv_water_wx)
    TextView tvWaterWx;
    @BindView(R.id.iv_water_icon)
    ImageView ivWaterIcon;
    @BindView(R.id.iv_water_icon_hint)
    ImageView ivWaterIconHint;
    @BindView(R.id.tv_wx)
    StrokeTextView tvWx;
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.layout_whole_water_content)
    AutoRelativeLayout layoutWholeWaterContent;
    @BindView(R.id.layout_bg)
    AutoRelativeLayout layoutBg;
    @BindView(R.id.check_full_watermark)
    CheckBox checkFullWatermark;
    @BindView(R.id.check_is_select_icon)
    CheckBox checkIsShowIcon;
    @BindView(R.id.check_water_bg_setting)
    CheckBox checkWaterBgSetting;
    @BindView(R.id.layout_init_pic)
    AutoRelativeLayout layoutInitPic;
    @BindView(R.id.layout_show_icon_and_wx)
    AutoRelativeLayout layoutShowIconAndWx;
    private String path,url;
    int screentWidth, screentHeight;
    int realHeight = 0;
    int realWidth = 0;
    private String watermarkTitle = "微商云管家";

    @Override
    protected int getLayoutId() {
        String watermarkId = getIntent().getStringExtra(Constans.WATERMARKID);
        if (watermarkId.equals(Constans.WATERMARKID_5021)){
            return R.layout.layout_watermarkid_5021;
        }else if (watermarkId.equals(Constans.WATERMARKID_5003)){
            return R.layout.layout_watermarkid_5003;
        }else if (watermarkId.equals(Constans.WATERMARKID_5004)||
                watermarkId.equals(Constans.WATERMARKID_5005)||
                watermarkId.equals(Constans.WATERMARKID_5006)||
                watermarkId.equals(Constans.WATERMARKID_5007)||
                watermarkId.equals(Constans.WATERMARKID_5011)||
                watermarkId.equals(Constans.WATERMARKID_5012)||
                watermarkId.equals(Constans.WATERMARKID_5013)){
            return R.layout.layout_watermarkid_5004;
        }else if (watermarkId.equals(Constans.WATERMARKID_5010)){
            return R.layout.layout_watermarkid_5010;
        }else if (watermarkId.equals(Constans.WATERMARKID_5015)){
            return R.layout.layout_watermarkid_5015;
        }else{
            return R.layout.activity_edit_water;
        }
    }

    @Override
    protected void initView() {
        updateView();
        screentWidth = getResources().getDisplayMetrics().widthPixels;//屏幕宽度
        screentHeight = getResources().getDisplayMetrics().heightPixels;//屏幕宽度
        EventBus.getDefault().register(this);
        layoutBack.setVisibility(View.VISIBLE);
        ivCamera.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.mould_preview));
        tvFinish.setText(getResources().getString(R.string.create_water));
        tvWaterTitle.setText(watermarkTitle, TextView.BufferType.SPANNABLE);
        tvWatermarkTitle.setText(watermarkTitle);
        tvWx.setText(getResources().getString(R.string.tv_default_wx));
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color7));
        //增加整体布局监听
        setLayoutListener();
        WatermarkManager.getInstance().initView(layoutInitPic, layoutWholeWaterContent,
                ivHead, ivWaterIcon, ivWaterIconHint, tvWaterTitle, tvWatermarkTitle, tvWx, tvWaterWx
                , checkFullWatermark, checkWaterBgSetting, checkIsShowIcon, this);
    }

    private void updateView() {
        String watermarkId = getIntent().getStringExtra(Constans.WATERMARKID);
        if (watermarkId.equals(Constans.WATERMARKID_5023)){
            return;
        }else if (watermarkId.equals(Constans.WATERMARKID_5022)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5022));
            updateViewColor(1);
        }else if(watermarkId.equals(Constans.WATERMARKID_5008)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5008));
            int color = ColorUtils.getColor(3);
            ivWaterIcon.setColorFilter(color);
            tvWaterTitle.setTextColor(color);
            tvWx.setTextColor(color);
        }else if(watermarkId.equals(Constans.WATERMARKID_5016)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5016));
            updateViewColor(1);
        }else if(watermarkId.equals(Constans.WATERMARKID_5017)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5017));
        }else if(watermarkId.equals(Constans.WATERMARKID_5018)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5018));
            updateViewColor(1);
        }else if(watermarkId.equals(Constans.WATERMARKID_5019)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5019));
            updateViewColor(1);
        }else if(watermarkId.equals(Constans.WATERMARKID_5003)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5003));
        }else if(watermarkId.equals(Constans.WATERMARKID_5004)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5004));
        }else if(watermarkId.equals(Constans.WATERMARKID_5005)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5005));
        }else if(watermarkId.equals(Constans.WATERMARKID_5006)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5006));
        }else if(watermarkId.equals(Constans.WATERMARKID_5007)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5007));
            int color = ColorUtils.getColor(27);
            ivWaterIcon.setColorFilter(color);
            tvWaterTitle.setTextColor(color);
            tvWx.setTextColor(color);
        }else if(watermarkId.equals(Constans.WATERMARKID_5011)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5011));
        }else if(watermarkId.equals(Constans.WATERMARKID_5012)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5012));
        }else if(watermarkId.equals(Constans.WATERMARKID_5013)){
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_5013));
        }
    }

    private void updateViewColor(int flag){
        if (flag == 1){//黑色
            ivWaterIcon.setImageDrawable(getResources().getDrawable(R.drawable.water_icon021));
            ivWaterIconHint.setImageDrawable(getResources().getDrawable(R.drawable.water_icon021));
            tvWaterTitle.setTextColor(getResources().getColor(R.color.black));
            tvWx.setTextColor(getResources().getColor(R.color.black));
            layoutBg.setBackgroundColor(getResources().getColor(R.color.white));
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
                    WatermarkManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkManager.getInstance().setRealWidth((int) maxWidth);
                    realHeight = (int) maxHeight;
                    float w = width / heightScale;
                    realWidth = (int) w;
                } else {
                    WatermarkManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkManager.getInstance().setRealWidth((int) maxWidth);
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
                    int ivHeadWidth = (int) (width * maxHeight/height);
                    layoutParams.width = ivHeadWidth;
                    ivHead.setLayoutParams(layoutParams);
                    tvWaterTitle.changeTextSize(TypedValue.COMPLEX_UNIT_PX, 66);
                    tvWx.changeTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
                    Log.i(TAG,"ivHead width = " +ivHeadWidth+",height = " +maxHeight);
                }
                Log.i(TAG,"ivHead width = " +maxHeight+",height = " +height);
            }
        });

        checkIsShowIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                layoutShowIconAndWx.setVisibility(isChecked ? View.VISIBLE:View.GONE);
                WatermarkManager.getInstance().refreshChanged();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Message image) {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected AddWatermarkPresenter<ILoadView> createPresenter() {
        return new AddWatermarkPresenter<>();
    }

    @OnClick({R.id.layout_bg, R.id.layout_whole_color, R.id.check_full_watermark,
            R.id.check_water_bg_setting, R.id.layout_bg_setting, R.id.layout_water_title,
            R.id.layout_back, R.id.layout_finish, R.id.layout_full_screen_watermark,
            R.id.layout_small_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_small_icon:
                WatermarkManager.getInstance().showTextEditDialog(2);
                break;
            case R.id.layout_back:
                setResult(Constans.RESULT_CODE);
                EditWaterActivity.this.finish();
                break;
            case R.id.layout_finish:
                setResult(Constans.RESULT_CODE_FINISH);
                addWater();
                break;
            case R.id.layout_bg:
                break;
            case R.id.layout_whole_color:
                WatermarkManager.getInstance().showWholeColorDialog();
                break;
            case R.id.check_water_bg_setting://水印背景开关
                if (!checkWaterBgSetting.isChecked()) {
                    WatermarkManager.getInstance().closeWaterBg();
                } else {
                    WatermarkManager.getInstance().updateBgColor();
                }
                break;
            case R.id.check_full_watermark://全屏水印开关
                if (!checkFullWatermark.isChecked()) {
                    WatermarkManager.getInstance().setScreenWatermarkInit();
                } else {
                    if (WatermarkManager.getInstance().getDefault_screen_num() == 1) {
                        WatermarkManager.getInstance().setDefault_screen_num(2);
                    }
                    WatermarkManager.getInstance().setScreenWatermark();
                }
                break;
            case R.id.layout_full_screen_watermark://全屏水印设置
                WatermarkManager.getInstance().showFullScreenWatermarkDialog();
                break;
            case R.id.layout_bg_setting:
                WatermarkManager.getInstance().showWaterBgDialog();
                break;
            case R.id.layout_water_title:
                WatermarkManager.getInstance().showTextEditDialog(1);
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

    /**
     * 解决将图片保存在本地在相册中不能看到图片的问题
     * 让Gallery上能马上看到该图片
     */
    private void scanPhoto(Context context, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onUploadProcess(int percent) {

    }

    @Override
    public void onUploadResult(int code, String url) {
        Log.i(TAG, "code = " + code + ",url = " + url);
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

    @Override
    public void showData(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                WaterImage image = new WaterImage(url);
                EventBus.getDefault().post(image);
                EditWaterActivity.this.finish();
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }
}
