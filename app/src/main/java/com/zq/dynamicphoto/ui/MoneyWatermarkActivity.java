package com.zq.dynamicphoto.ui;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.WatermarkManager;
import com.zq.dynamicphoto.utils.WatermarkMoneyManager;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 价签水印
 */
public class MoneyWatermarkActivity extends BaseActivity<ILoadView, AddWatermarkPresenter<ILoadView>>
        implements UploadView, ILoadView {
    private static final String TAG = "MoneyWatermarkActivity";
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.iv_head1)
    ImageView ivHead1;
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
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
    @BindView(R.id.check_water_bg_setting)
    CheckBox checkWaterBgSetting;
    @BindView(R.id.layout_init_pic)
    AutoRelativeLayout layoutInitPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title1)
    StrokeTextView tvTitle1;
    @BindView(R.id.tv_title2)
    StrokeTextView tvTitle2;
    @BindView(R.id.tv_title3)
    StrokeTextView tvTitle3;
    @BindView(R.id.tv_title4)
    StrokeTextView tvTitle4;
    @BindView(R.id.et_title1)
    TextView etTitle1;
    @BindView(R.id.et_title2)
    TextView etTitle2;
    @BindView(R.id.et_title3)
    TextView etTitle3;
    @BindView(R.id.et_title4)
    TextView etTitle4;
    @BindView(R.id.layout_et_title2)
    AutoRelativeLayout layoutEtTitle2;
    @BindView(R.id.layout_et_title3)
    AutoRelativeLayout layoutEtTitle3;
    @BindView(R.id.layout_et_title4)
    AutoRelativeLayout layoutEtTitle4;
    @BindView(R.id.check_title1)
    CheckBox checkTitle1;
    @BindView(R.id.tv_title1_hint)
    TextView tvTitle1Hint;
    @BindView(R.id.check_title2)
    CheckBox checkTitle2;
    @BindView(R.id.tv_title2_hint)
    TextView tvTitle2Hint;
    @BindView(R.id.check_title3)
    CheckBox checkTitle3;
    @BindView(R.id.tv_title3_hint)
    TextView tvTitle3Hint;
    @BindView(R.id.check_title4)
    CheckBox checkTitle4;
    @BindView(R.id.tv_title4_hint)
    TextView tvTitle4Hint;
    private String path, url;
    int screentWidth, screentHeight;
    int realHeight = 0;
    int realWidth = 0;
    private String watermarkId;

    @Override
    protected int getLayoutId() {
        watermarkId = getIntent().getStringExtra(Constans.WATERMARKID);
        if (watermarkId.equals(Constans.WATERMARKID_6003)) {
            return R.layout.activity_money_watermark;
        } else if (watermarkId.equals(Constans.WATERMARKID_6005) ||
                watermarkId.equals(Constans.WATERMARKID_6006)) {
            return R.layout.activity_watermark6005;
        } else if (watermarkId.equals(Constans.WATERMARKID_6008)) {
            return R.layout.activity_watermark6008;
        } else if (watermarkId.equals(Constans.WATERMARKID_6007)) {
            return R.layout.activity_watermark6007;
        } else if (watermarkId.equals(Constans.WATERMARKID_6009)) {
            return R.layout.activity_watermark6009;
        } else if (watermarkId.equals(Constans.WATERMARKID_6010)) {
            return R.layout.activity_watermark6010;
        } else if (watermarkId.equals(Constans.WATERMARKID_6011)) {
            return R.layout.activity_watermark6011;
        } else if (watermarkId.equals(Constans.WATERMARKID_6002)) {
            return R.layout.activity_watermark6002;
        } else if (watermarkId.equals(Constans.WATERMARKID_6004)) {
            return R.layout.activity_watermark6004;
        } else {
            return R.layout.activity_watermark6001;
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
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color7));
        //增加整体布局监听
        setLayoutListener();
        WatermarkMoneyManager.getInstance().initView(this, layoutInitPic, layoutWholeWaterContent,
                ivHead, ivHead1, tvTitle1, tvTitle2, tvTitle3, tvTitle4, etTitle1, etTitle2, etTitle3, etTitle4,
                checkFullWatermark, checkWaterBgSetting);
    }

    private void updateView() {
        if (watermarkId.equals(Constans.WATERMARKID_6003)) {
            etTitle1.setText(getResources().getString(R.string.tv_happy));
            etTitle2.setText(getResources().getString(R.string.tv_new));
            etTitle3.setText(getResources().getString(R.string.tv_year));
            etTitle4.setText(getResources().getString(R.string.tv_hello_2018));
            layoutEtTitle2.setVisibility(View.VISIBLE);
            layoutEtTitle3.setVisibility(View.VISIBLE);
            layoutEtTitle4.setVisibility(View.VISIBLE);
            checkTitle1.setVisibility(View.GONE);
            checkTitle2.setVisibility(View.GONE);
            checkTitle3.setVisibility(View.GONE);
            checkTitle4.setVisibility(View.GONE);
        } else if (watermarkId.equals(Constans.WATERMARKID_6005)
                || watermarkId.equals(Constans.WATERMARKID_6006)) {
            if (watermarkId.equals(Constans.WATERMARKID_6005)) {
                ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_6005));
            } else if (watermarkId.equals(Constans.WATERMARKID_6006)) {
                ivHead.setImageDrawable(getResources().getDrawable(R.drawable.water_6006));
            }
            checkTitle1.setVisibility(View.GONE);
            tvTitle1Hint.setText(getResources().getString(R.string.input_text));
            etTitle1.setText(getResources().getString(R.string.tv_default_money));
        } else if (watermarkId.equals(Constans.WATERMARKID_6007)) {
            layoutEtTitle2.setVisibility(View.VISIBLE);
            etTitle1.setText(getResources().getString(R.string.tv_default_money));
            etTitle2.setText(getResources().getString(R.string.tv_256));
            tvTitle1Hint.setText(getResources().getString(R.string.xianjia));
            tvTitle2Hint.setText(getResources().getString(R.string.yuanjia));
        } else if (watermarkId.equals(Constans.WATERMARKID_6009)) {
            layoutEtTitle2.setVisibility(View.VISIBLE);
            etTitle1.setText(getResources().getString(R.string.tv_default_money));
            etTitle2.setText(getResources().getString(R.string.tv_256));
        } else if (watermarkId.equals(Constans.WATERMARKID_6010)) {
            layoutEtTitle2.setVisibility(View.VISIBLE);
            etTitle1.setText(getResources().getString(R.string.tv_default_money));
            etTitle2.setText(getResources().getString(R.string.tv_256));
            tvTitle2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvTitle1Hint.setText(getResources().getString(R.string.xianjia));
            tvTitle2Hint.setText(getResources().getString(R.string.yuanjia));
            etTitle2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (watermarkId.equals(Constans.WATERMARKID_6011)) {
            layoutEtTitle2.setVisibility(View.VISIBLE);
            etTitle1.setText(getResources().getString(R.string.tv_default_money));
            etTitle2.setText(getResources().getString(R.string.tv_256));
            tvTitle2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvTitle1Hint.setText(getResources().getString(R.string.xianjia));
            tvTitle2Hint.setText(getResources().getString(R.string.yuanjia));
            etTitle2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (watermarkId.equals(Constans.WATERMARKID_6001)) {
            layoutEtTitle2.setVisibility(View.VISIBLE);
            layoutEtTitle3.setVisibility(View.VISIBLE);
            etTitle1.setText(getResources().getString(R.string.tv_default_years));
            etTitle2.setText(getResources().getString(R.string.tv_gonghe));
            etTitle3.setText(getResources().getString(R.string.tv_xinxi));
        } else if (watermarkId.equals(Constans.WATERMARKID_6002)
                || watermarkId.equals(Constans.WATERMARKID_6004)) {
            etTitle1.setText(getResources().getString(R.string.tv_fu));
            checkTitle1.setVisibility(View.GONE);
            tvTitle1Hint.setText(getResources().getString(R.string.phone_input));
        }else if (watermarkId.equals(Constans.WATERMARKID_6008)) {
            checkTitle1.setVisibility(View.GONE);
            tvTitle1Hint.setText(getResources().getString(R.string.phone_input));
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
                    WatermarkMoneyManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkMoneyManager.getInstance().setRealWidth((int) maxWidth);
                    realHeight = (int) maxHeight;
                    float w = width / heightScale;
                    realWidth = (int) w;
                } else {
                    WatermarkMoneyManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkMoneyManager.getInstance().setRealWidth((int) maxWidth);
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

        checkTitle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tvTitle1.setVisibility(View.VISIBLE);
                }else {
                    tvTitle1.setVisibility(View.GONE);
                }
                WatermarkMoneyManager.getInstance().refreshChanged();
            }
        });

        checkTitle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tvTitle2.setVisibility(View.VISIBLE);
                }else {
                    tvTitle2.setVisibility(View.GONE);
                }
                WatermarkMoneyManager.getInstance().refreshChanged();
            }
        });

        checkTitle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tvTitle3.setVisibility(View.VISIBLE);
                }else {
                    tvTitle3.setVisibility(View.GONE);
                }
                WatermarkMoneyManager.getInstance().refreshChanged();
            }
        });

        checkTitle4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tvTitle4.setVisibility(View.VISIBLE);
                }else {
                    tvTitle4.setVisibility(View.GONE);
                }
                WatermarkMoneyManager.getInstance().refreshChanged();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Message image) {
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.layout_whole_color, R.id.check_full_watermark,
            R.id.check_water_bg_setting, R.id.layout_bg_setting,
            R.id.layout_back, R.id.layout_finish, R.id.layout_full_screen_watermark,
            R.id.layout_et_title1, R.id.layout_et_title2, R.id.layout_et_title3, R.id.layout_et_title4,
            R.id.check_title1,R.id.check_title2,R.id.check_title3,R.id.check_title4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                MoneyWatermarkActivity.this.finish();
                break;
            case R.id.layout_finish:
                addWater();
                break;
            case R.id.layout_whole_color:
                WatermarkMoneyManager.getInstance().showWholeColorDialog();
                break;
            case R.id.check_water_bg_setting://水印背景开关
                if (!checkWaterBgSetting.isChecked()) {
                    WatermarkMoneyManager.getInstance().closeWaterBg();
                } else {
                    WatermarkMoneyManager.getInstance().updateBgColor();
                }
                break;
            case R.id.check_full_watermark://全屏水印开关
                if (!checkFullWatermark.isChecked()) {
                    WatermarkMoneyManager.getInstance().setScreenWatermarkInit();
                } else {
                    if (WatermarkMoneyManager.getInstance().getDefault_screen_num() == 1) {
                        WatermarkMoneyManager.getInstance().setDefault_screen_num(2);
                    }
                    WatermarkMoneyManager.getInstance().setScreenWatermark();
                }
                break;
            case R.id.layout_full_screen_watermark://全屏水印设置
                WatermarkMoneyManager.getInstance().showFullScreenWatermarkDialog();
                break;
            case R.id.layout_bg_setting:
                WatermarkMoneyManager.getInstance().showWaterBgDialog();
                break;
            case R.id.layout_et_title1:
                WatermarkMoneyManager.getInstance().showTextEditDialog(1);
                break;
            case R.id.layout_et_title2:
                WatermarkMoneyManager.getInstance().showTextEditDialog(2);
                break;
            case R.id.layout_et_title3:
                WatermarkMoneyManager.getInstance().showTextEditDialog(3);
                break;
            case R.id.layout_et_title4:
                WatermarkMoneyManager.getInstance().showTextEditDialog(4);
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
    protected AddWatermarkPresenter<ILoadView> createPresenter() {
        return new AddWatermarkPresenter<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
    public void showData(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                WaterImage image = new WaterImage(url);
                EventBus.getDefault().post(image);
                MoneyWatermarkActivity.this.finish();
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }
}
