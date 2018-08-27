package com.zq.dynamicphoto.ui;

import android.graphics.Bitmap;
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
import com.zq.dynamicphoto.utils.WatermarkLabelManager;
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
/**
 * 标签贴纸水印模板
 */
public class LabelWatermarkActivity extends BaseActivity<ILoadView, AddWatermarkPresenter<ILoadView>>
        implements UploadView, ILoadView {
    private static final String TAG = "LabelWatermarkActivity";
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.tv_title1)
    StrokeTextView tvTitle1;
    @BindView(R.id.tv_title2)
    StrokeTextView tvTitle2;
    @BindView(R.id.tv_title3)
    StrokeTextView tvTitle3;
    @BindView(R.id.tv_title4)
    StrokeTextView tvTitle4;
    @BindView(R.id.layout_init_pic)
    AutoRelativeLayout layoutInitPic;
    @BindView(R.id.layout_whole_water_content)
    AutoRelativeLayout layoutWholeWaterContent;
    @BindView(R.id.check_full_watermark)
    CheckBox checkFullWatermark;
    @BindView(R.id.check_water_bg_setting)
    CheckBox checkWaterBgSetting;
    @BindView(R.id.et_title1_hint)
    TextView etTitle1Hint;
    @BindView(R.id.et_title1)
    TextView etTitle1;
    @BindView(R.id.et_title2_hint)
    TextView etTitle2Hint;
    @BindView(R.id.et_title2)
    TextView etTitle2;
    @BindView(R.id.et_title3_hint)
    TextView etTitle3Hint;
    @BindView(R.id.et_title3)
    TextView etTitle3;
    @BindView(R.id.et_title4_hint)
    TextView etTitle4Hint;
    @BindView(R.id.et_title4)
    TextView etTitle4;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.layout_et_title1)
    AutoRelativeLayout layoutEtTitle1;
    @BindView(R.id.layout_et_title2)
    AutoRelativeLayout layoutEtTitle2;
    @BindView(R.id.layout_et_title3)
    AutoRelativeLayout layoutEtTitle3;
    @BindView(R.id.layout_et_title4)
    AutoRelativeLayout layoutEtTitle4;
    private String watermarkId;
    int screentWidth, screentHeight;
    int realHeight = 0;
    int realWidth = 0;
    private String path, url;
    @Override
    protected int getLayoutId() {
        watermarkId = getIntent().getStringExtra(Constans.WATERMARKID);
        if (watermarkId.equals(Constans.WATERMARKID_1002)){
            return R.layout.activity_watermark1002;
        }else if (watermarkId.equals(Constans.WATERMARKID_1003)){
            return R.layout.activity_watermark1003;
        }else if (watermarkId.equals(Constans.WATERMARKID_1004)){
            return R.layout.activity_watermark1004;
        }else if (watermarkId.equals(Constans.WATERMARKID_1005)){
            return R.layout.activity_watermark1005;
        }else if (watermarkId.equals(Constans.WATERMARKID_1006)){
            return R.layout.activity_watermark1006;
        }else if (watermarkId.equals(Constans.WATERMARKID_1009)){
            return R.layout.activity_watermark1009;
        }else if (watermarkId.equals(Constans.WATERMARKID_1010)){
            return R.layout.activity_watermark1010;
        }else if (watermarkId.equals(Constans.WATERMARKID_1011)){
            return R.layout.activity_watermark1011;
        }else if (watermarkId.equals(Constans.WATERMARKID_1012)){
            return R.layout.activity_watermark1012;
        }else if (watermarkId.equals(Constans.WATERMARKID_1013)){
            return R.layout.activity_watermark1013;
        }else {
            return R.layout.activity_scorll_test;
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
        setListener();
        WatermarkLabelManager.getInstance().initView(this, layoutInitPic, layoutWholeWaterContent,
                ivHead, tvTitle1, tvTitle2, tvTitle3, tvTitle4, etTitle1, etTitle2, etTitle3, etTitle4,
                checkFullWatermark, checkWaterBgSetting);
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Message image) {
    }

    private void setListener() {
        checkFullWatermark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (WatermarkLabelManager.getInstance().getDefault_screen_num() == 1) {
                        WatermarkLabelManager.getInstance().setDefault_screen_num(2);
                    }
                    WatermarkLabelManager.getInstance().setScreenWatermark();
                }else {
                    WatermarkLabelManager.getInstance().setScreenWatermarkInit();
                }
            }
        });

        checkWaterBgSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    WatermarkLabelManager.getInstance().closeWaterBg();
                } else {
                    WatermarkLabelManager.getInstance().updateBgColor();
                }
            }
        });

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
                    WatermarkLabelManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkLabelManager.getInstance().setRealWidth((int) maxWidth);
                    realHeight = (int) maxHeight;
                    float w = width / heightScale;
                    realWidth = (int) w;
                } else {
                    WatermarkLabelManager.getInstance().setRealHeight((int) maxHeight);
                    WatermarkLabelManager.getInstance().setRealWidth((int) maxWidth);
                    realWidth = (int) maxWidth;
                    float h = height / widthScale;
                    realHeight = (int) h;
                }
                ViewGroup.LayoutParams layoutParams = layoutWholeWaterContent.getLayoutParams();
                layoutParams.width = realWidth;
                layoutParams.height = realHeight;
                Log.i("space","realHeight = " + realHeight);
                Log.i("space","realWidth = " + realWidth);
                layoutWholeWaterContent.setLayoutParams(layoutParams);
            }
        });
    }


    private void updateView() {
        if (watermarkId.equals(Constans.WATERMARKID_1001)) {
            layoutEtTitle2.setVisibility(View.VISIBLE);
            layoutEtTitle3.setVisibility(View.VISIBLE);
            layoutEtTitle4.setVisibility(View.VISIBLE);
            tvTitle1.setText(getResources().getString(R.string.tv_title_1001));
            etTitle2Hint.setText(getResources().getString(R.string.subhead1));
            etTitle3Hint.setText(getResources().getString(R.string.subhead2));
            etTitle4Hint.setText(getResources().getString(R.string.subhead3));
            etTitle1.setText(getResources().getString(R.string.tv_title_1001));
            etTitle2.setText(getResources().getString(R.string.tv_quanchang));
            etTitle3.setText(getResources().getString(R.string.tv_wuzhe));
            etTitle4.setText(getResources().getString(R.string.tv_qishou));
        }else if (watermarkId.equals(Constans.WATERMARKID_1002)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1002));
            etTitle1.setText(getResources().getString(R.string.tv_title_1002));
        }else if (watermarkId.equals(Constans.WATERMARKID_1003)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1003));
            etTitle1.setText(getResources().getString(R.string.tv_title_1003));
        }else if (watermarkId.equals(Constans.WATERMARKID_1004)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1004));
            etTitle1.setText(getResources().getString(R.string.tv_title_1004));
        }else if (watermarkId.equals(Constans.WATERMARKID_1005)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1005));
            etTitle1.setText(getResources().getString(R.string.tv_title_1005));
        }else if (watermarkId.equals(Constans.WATERMARKID_1006)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1006));
            etTitle1.setText(getResources().getString(R.string.tv_title_1006));
        }else if (watermarkId.equals(Constans.WATERMARKID_1009)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1009));
            etTitle1.setText(getResources().getString(R.string.tv_title_1009));
            layoutEtTitle2.setVisibility(View.VISIBLE);
            tvTitle2.setText(getResources().getString(R.string.tv_title2_1009));
            etTitle2.setText(getResources().getString(R.string.tv_title2_1009));
        }else if (watermarkId.equals(Constans.WATERMARKID_1010)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1010));
            etTitle1.setText(getResources().getString(R.string.tv_title_1010));
        }else if (watermarkId.equals(Constans.WATERMARKID_1011)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1011));
            etTitle1.setText(getResources().getString(R.string.tv_title_1011));
        }else if (watermarkId.equals(Constans.WATERMARKID_1012)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1012));
            etTitle1.setText(getResources().getString(R.string.tv_title_1012));
        }else if (watermarkId.equals(Constans.WATERMARKID_1013)){
            tvTitle1.setText(getResources().getString(R.string.tv_title_1013));
            etTitle1.setText(getResources().getString(R.string.tv_title_1013));
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected AddWatermarkPresenter<ILoadView> createPresenter() {
        return new AddWatermarkPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_finish, R.id.layout_whole_color,
            R.id.layout_full_screen_watermark, R.id.layout_bg_setting,
            R.id.layout_et_title1, R.id.layout_et_title2,
            R.id.layout_et_title3, R.id.layout_et_title4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                setResult(Constans.RESULT_CODE);
                this.finish();
                break;
            case R.id.layout_finish:
                setResult(Constans.RESULT_CODE_FINISH);
                addWater();
                break;
            case R.id.layout_whole_color:
                WatermarkLabelManager.getInstance().showWholeColorDialog();
                break;
            case R.id.layout_full_screen_watermark:
                WatermarkLabelManager.getInstance().showFullScreenWatermarkDialog();
                break;
            case R.id.layout_bg_setting:
                WatermarkLabelManager.getInstance().showWaterBgDialog();
                break;
            case R.id.layout_et_title1:
                WatermarkLabelManager.getInstance().showTextEditDialog(1);
                break;
            case R.id.layout_et_title2:
                WatermarkLabelManager.getInstance().showTextEditDialog(2);
                break;
            case R.id.layout_et_title3:
                WatermarkLabelManager.getInstance().showTextEditDialog(3);
                break;
            case R.id.layout_et_title4:
                WatermarkLabelManager.getInstance().showTextEditDialog(4);
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
                LabelWatermarkActivity.this.finish();
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }
}
