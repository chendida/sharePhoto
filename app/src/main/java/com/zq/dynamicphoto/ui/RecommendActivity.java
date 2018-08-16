package com.zq.dynamicphoto.ui;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.StrokeTextView;
import com.zq.dynamicphoto.utils.WatermarkManager;
import com.zq.dynamicphoto.utils.WatermarkMoneyManager;
import com.zq.dynamicphoto.utils.WatermarkRecommedManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class RecommendActivity extends BaseActivity {
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
    @BindView(R.id.layout_wx_and_icon)
    AutoRelativeLayout layoutWxAndIcon;
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
    @Override
    protected int getLayoutId() {
        watermarkId = getIntent().getStringExtra(Constans.WATERMARKID);
        return R.layout.activity_recommend;
    }

    @Override
    protected void initView() {
        updateView();
        screentWidth = getResources().getDisplayMetrics().widthPixels;//屏幕宽度
        screentHeight = getResources().getDisplayMetrics().heightPixels;//屏幕宽度
        //EventBus.getDefault().register(this);
        layoutBack.setVisibility(View.VISIBLE);
        ivCamera.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.mould_preview));
        tvFinish.setText(getResources().getString(R.string.create_water));
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color7));
        //增加整体布局监听
        setLayoutListener();
        WatermarkRecommedManager.getInstance().initView(this,layoutInitPic,layoutWholeWaterContent,
                ivHead,ivHead1,ivWxIcon,ivWxIconHint,tvTitle1,tvTitle2,etTitle2,etTitle3,checkFullWatermark,checkWaterBgSetting);
    }

    private void updateView() {
        if (watermarkId.equals(Constans.WATERMARKID_7004)){
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
    protected BasePresenter createPresenter() {
        return null;
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
}
