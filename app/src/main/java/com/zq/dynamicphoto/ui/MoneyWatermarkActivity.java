package com.zq.dynamicphoto.ui;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.tools.Message;
import com.zq.dynamicphoto.ui.widge.StrokeTextView;
import com.zq.dynamicphoto.utils.WatermarkManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;

/**
 * 价签水印
 */
public class MoneyWatermarkActivity extends BaseActivity {
    private static final String TAG = "MoneyWatermarkActivity";
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_water_title)
    StrokeTextView tvWaterTitle;
    @BindView(R.id.tv_water_mark_title)
    TextView tvWatermarkTitle;
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
    @BindView(R.id.check_water_bg_setting)
    CheckBox checkWaterBgSetting;
    @BindView(R.id.layout_init_pic)
    AutoRelativeLayout layoutInitPic;
    private String path,url;
    int screentWidth, screentHeight;
    int realHeight = 0;
    int realWidth = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_money_watermark;
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
        tvWaterTitle.setText(getResources().getString(R.string.tv_default_money), TextView.BufferType.SPANNABLE);
        tvWatermarkTitle.setText(getResources().getString(R.string.tv_default_money));
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color7));
        //增加整体布局监听
        setLayoutListener();
        /*WatermarkManager.getInstance().initView(layoutInitPic, layoutWholeWaterContent,
                ivHead, ivWaterIcon, ivWaterIconHint, tvWaterTitle, tvWatermarkTitle, tvWx, tvWaterWx
                , checkFullWatermark, checkWaterBgSetting, checkIsShowIcon, this);*/
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
                    int ivHeadWidth = (int) (width * maxHeight / height);
                    layoutParams.width = ivHeadWidth;
                    ivHead.setLayoutParams(layoutParams);
                    tvWaterTitle.changeTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
                    Log.i(TAG, "ivHead width = " + ivHeadWidth + ",height = " + maxHeight);
                }
                Log.i(TAG, "ivHead width = " + maxHeight + ",height = " + height);
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
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
