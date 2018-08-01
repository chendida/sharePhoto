package com.zq.dynamicphoto.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
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
import com.zq.dynamicphoto.ui.widge.FullScreenWatermarkDialog;
import com.zq.dynamicphoto.ui.widge.TextColorDialog;
import com.zq.dynamicphoto.ui.widge.WaterBgDialog;
import com.zq.dynamicphoto.ui.widge.WaterTitleDialog;
import com.zq.dynamicphoto.ui.widge.WholeColorEditDialog;
import com.zq.dynamicphoto.utils.ColorUtils;
import com.zq.dynamicphoto.utils.CosUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftKeyBoardListener;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.view.ILoadView;
import com.zq.dynamicphoto.view.UploadView;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

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

public class EditWaterActivity extends BaseActivity<ILoadView, AddWatermarkPresenter<ILoadView>>
        implements UploadView, ILoadView,WatermarkSeekBarListener {
    private static final String TAG = "EditWaterActivity";
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_water_title)
    TextView tvWaterTitle;
    @BindView(R.id.tv_water_mark_title)
    TextView tvWatermarkTitle;
    @BindView(R.id.iv_water_icon)
    ImageView ivWaterIcon;
    @BindView(R.id.tv_wx)
    TextView tvWx;
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
    @BindView(R.id.check_full_watermark)
    CheckBox checkFullWatermark;
    @BindView(R.id.check_water_bg_setting)
    CheckBox checkWaterBgSetting;
    @BindView(R.id.layout_init_pic)
    AutoRelativeLayout layoutInitPic;
    private WholeColorEditDialog colorEditDialog;
    private WaterBgDialog waterBgDialog;
    private WaterTitleDialog waterTitleDialog;
    private TextColorDialog textColorDialog;
    private String path;
    int screentWidth, screentHeight;
    int realHeight = 0;
    int realWidth = 0;
    private int default_screen_num = 1;//默认的全屏水印的数量是2*2
    private int default_watermark_space = 0;//默认的全屏水印的间距
    private int position = 0;//保存背景颜色色值
    private int round = 0;//默认圆角值
    private String watermarkTitle = "微商云管家";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_water;
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
        tvWaterTitle.setText(watermarkTitle);
        tvWatermarkTitle.setText(watermarkTitle);
        tvFinish.setTextColor(getResources().getColor(R.color.tv_text_color7));
        initDialog();
        //增加整体布局监听
        setLayoutListener();
        softKeyboardListnenr();
    }

    private void setLayoutListener() {
        ViewTreeObserver vto = layoutWholeWaterContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutWholeWaterContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layoutWholeWaterContent.getMeasuredHeight();
                int width = layoutWholeWaterContent.getMeasuredWidth();
                Log.i(TAG, "height = " + height);
                Log.i(TAG, "width = " + width);
                float maxHeight = (float) 410 / Constans.DEFAULT_HEIGHT * screentHeight;
                float maxWidth = (float) 700 / Constans.DEFAULT_WIDTH * screentWidth;
                Log.i(TAG, "maxHeight = " + maxHeight);
                Log.i(TAG, "maxWidth = " + maxWidth);
                float heightScale = (float) height / maxHeight;
                float widthScale = (float) width / maxWidth;
                if (heightScale > widthScale) {
                    realHeight = (int) maxHeight;
                    float w = width / heightScale;
                    realWidth = (int) w;
                } else {
                    realWidth = (int) maxWidth;
                    float h = height / widthScale;
                    realHeight = (int) h;
                }
                ViewGroup.LayoutParams layoutParams = layoutWholeWaterContent.getLayoutParams();
                layoutParams.width = realWidth;
                layoutParams.height = realHeight;
                layoutWholeWaterContent.setLayoutParams(layoutParams);
                Log.i(TAG, "realWidth = " + realWidth);
                Log.i(TAG, "realHeight = " + realHeight);
            }
        });

        ViewTreeObserver vto1 = ivHead.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivHead.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = ivHead.getMeasuredHeight();
                int width = ivHead.getMeasuredWidth();
                Log.i(TAG, "height ivHead= " + height);
                Log.i(TAG, "width ivHead = " + width);
                tvWaterTitle.setMaxWidth(width);
            }
        });

        tvWaterTitle.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                float vWidth = v.getWidth();
                TextPaint paint = tvWaterTitle.getPaint();
                String text = tvWaterTitle.getText().toString();
                float textLen = paint.measureText(text);
                float oldSize = tvWaterTitle.getTextSize();
                if (textLen != vWidth){
                    float size = vWidth * oldSize / textLen;
                    tvWaterTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                }
            }
        });
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                Log.i("6666666","show height = " + height);
                if (waterTitleDialog != null){
                    waterTitleDialog.showLayoutTransaliton(true);
                }
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                if (waterTitleDialog != null){
                    waterTitleDialog.showLayoutTransaliton(false);
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Message image) {
        //Log.i(TAG,"image = " + image.getUrl());
    }

    private void initDialog() {
        if (colorEditDialog == null) {
            colorEditDialog = new WholeColorEditDialog(this, R.style.dialog, new WholeColorEditDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    Log.i(TAG, "position = " + position);
                    updateWholeColor(position);
                }
            });
        }
        if (waterBgDialog == null) {
            waterBgDialog = new WaterBgDialog(this, R.style.dialog,
                    getResources().getString(R.string.watermark_bg),
                    getResources().getString(R.string.watermark_alpha),
                    getResources().getString(R.string.watermark_corner), this,
                    new WaterBgDialog.OnItemClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int position) {
                            updateBgColor(position);
                        }
                    });
        }
        if (textColorDialog == null){
            textColorDialog = new TextColorDialog(this, R.style.dialog, this,
                    new TextColorDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    updateTextColor(position);
                }
            });
        }
    }

    /**
     * 更改文字颜色
     */
    private void updateTextColor(int position) {
        int color = ColorUtils.getColor(position);
        tvWaterTitle.setTextColor(color);
        if (!(layoutInitPic.getVisibility() == View.VISIBLE)){
            setScreenWatermark(default_screen_num,default_watermark_space);
        }
    }

    /**
     * 更改水印背景颜色
     * @param position
     */
    private void updateBgColor(int position) {
        this.position = position;
        int color = ColorUtils.getColor(position);
        layoutInitPic.setBackgroundColor(color);
        if (!(layoutInitPic.getVisibility() == View.VISIBLE)){
            setScreenWatermark(default_screen_num,default_watermark_space);
        }
    }

    /**
     * 更改整体水印颜色
     * @param position
     */
    private void updateWholeColor(int position) {
        int color = ColorUtils.getColor(position);
        ivHead.setColorFilter(color);
        ivWaterIcon.setColorFilter(color);
        tvWaterTitle.setTextColor(color);
        tvWx.setTextColor(color);
        if (default_screen_num > 1){
            setScreenWatermark(default_screen_num,default_watermark_space);
        }
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
            R.id.layout_back, R.id.layout_finish, R.id.layout_full_screen_watermark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                addWater();
                break;
            case R.id.layout_bg:
                break;
            case R.id.layout_whole_color:
                colorEditDialog.show();
                break;
            case R.id.check_water_bg_setting:
                if (!checkWaterBgSetting.isChecked()) {
                    layoutInitPic.setBackgroundColor(getResources().getColor(R.color.alpha));
                    if (!(layoutInitPic.getVisibility() == View.VISIBLE)){
                        setScreenWatermark(default_screen_num,default_watermark_space);
                    }
                } else {
                    updateBgColor(position);
                    //layoutInitPic.setBackgroundColor(getResources().getColor(R.color.red_color));
                }
                break;
            case R.id.check_full_watermark://全屏水印开关
                if (!checkFullWatermark.isChecked()) {
                    setScreenWatermarkInit();
                } else {
                    if (default_screen_num == 1){
                        default_screen_num = 2;
                    }
                    setScreenWatermark(default_screen_num,default_watermark_space);
                }
                break;
            case R.id.layout_full_screen_watermark://全屏水印设置
                showFullScreenWatermarkDialog();
                break;
            case R.id.layout_bg_setting:
                waterBgDialog.show();
                break;
            case R.id.layout_water_title:
                showTextEditDialog();
                break;
        }
    }

    private void showTextEditDialog() {
        waterTitleDialog = new WaterTitleDialog(this, R.style.dialog, watermarkTitle, new WaterTitleDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position, String content) {
                switch (position){
                    case 0:
                        watermarkTitle = content;
                        tvWatermarkTitle.setText(content);
                        tvWaterTitle.setText(content);
                        if (!(layoutInitPic.getVisibility() == View.VISIBLE)){
                            setScreenWatermark(default_screen_num,default_watermark_space);
                        }
                        break;
                    case 1:

                        break;
                    case 2:
                        textColorDialog.show();
                        break;
                }
            }
        });
        waterTitleDialog.show();
    }

    /**
     * 还原全屏水印
     */
    private void setScreenWatermarkInit() {
        default_screen_num = 1;
        layoutInitPic.setVisibility(View.VISIBLE);
        layoutWholeWaterContent.setBackground(null);
    }

    /**
     * 全屏水印dialog
     */
    private void showFullScreenWatermarkDialog() {
        new FullScreenWatermarkDialog(this,R.style.dialog,
                default_watermark_space,default_screen_num,this).show();
    }

    /**
     * 全屏水印的设置
     * @param default_screen_num
     * @param default_watermark_space
     */
    private void setScreenWatermark(int default_screen_num, int default_watermark_space) {
        layoutInitPic.setDrawingCacheEnabled(true);
        Bitmap bitmap = layoutInitPic.getDrawingCache();
        int totalSpace = (default_screen_num + 1) * default_watermark_space;//总间距
        int newHeight = 0;
        int newWidth = 0;
        if(totalSpace > 0){
            newHeight = (realHeight - totalSpace) / default_screen_num;
            newWidth = (realWidth - totalSpace)/ default_screen_num;
        }else {
            newHeight = realHeight / default_screen_num;
            newWidth = realWidth / default_screen_num;
        }
        Log.i(TAG,"newHeight = " + newHeight);
        Log.i(TAG,"newWidth = " + newWidth);
        Bitmap repeater = createRepeater(default_screen_num,
                zoomImg(getRoundedCornerBitmap(bitmap,round),newWidth, newHeight),default_watermark_space);
        layoutInitPic.setVisibility(View.GONE);
        layoutWholeWaterContent.setBackground(new BitmapDrawable(repeater));
        layoutWholeWaterContent.setDrawingCacheEnabled(false);
    }

    // 等比缩放图片
    private Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        Log.i(TAG, "width = " + width);
        Log.i(TAG, "height = " + height);
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public Bitmap createRepeater(int count, Bitmap src,int space) {
        Bitmap bitmap = Bitmap.createBitmap(realWidth, src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            Rect rect1 = new Rect(0,0,src.getWidth(),src.getHeight());
            Rect rect = new Rect((idx+1)*space + idx * src.getWidth(),0,
                    (idx+1)*space + (idx+1) * src.getWidth(),src.getHeight());
            canvas.drawBitmap(src, rect1, rect, null);
        }
        return createYRepeater(count,bitmap,space);
    }

    public Bitmap createYRepeater(int count, Bitmap src,int space) {
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), realHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            Rect rect1 = new Rect(0,0,src.getWidth(),src.getHeight());
            Rect rect = new Rect(0,(idx+1)*space + idx * src.getHeight(),src.getWidth(),
                    (idx+1)*space + (idx+1) * src.getHeight());
            canvas.drawBitmap(src,rect1,rect,null);
        }
        return bitmap;
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
                WaterImage image = new WaterImage(path);
                EventBus.getDefault().post(image);
                EditWaterActivity.this.finish();
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }


    @Override
    public void onNumListener(int process) {
        default_screen_num = process;
        if (default_screen_num == 0 || default_screen_num == 1){
            default_screen_num = 1;
            setScreenWatermarkInit();
            ToastUtils.showShort("选的数量" + default_screen_num);
            return;
        }
        default_watermark_space = default_watermark_space / default_screen_num;
        setScreenWatermark(default_screen_num,default_watermark_space);
        ToastUtils.showShort("选的数量" + default_screen_num);
    }

    @Override
    public void onSpaceListener(int process) {
        default_watermark_space = process / default_screen_num;
        setScreenWatermark(default_screen_num,default_watermark_space);
    }

    @Override
    public void onWatermarkCorner(final int process) {
        //setClipViewCornerRadius(layoutInitPic,process);
        round = process/2;
        if (default_screen_num > 1) {
            setScreenWatermark(default_screen_num, default_watermark_space);
        }else {
            setClipViewCornerRadius(layoutInitPic,round);
        }
    }

    /**
     * 绘制图片圆角
     * @param bitmap
     * @param roundPx
     * @return
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xdd424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 设置视图裁剪的圆角半径
     * @param radius
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setClipViewCornerRadius(View view, final int radius) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //不支持5.0版本以下的系统
            return;
        }

        if (view == null) return;

        if (radius <= 0) {
            return;
        }

        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        view.setClipToOutline(true);
    }


        @Override
    public void onWatermarkAlpha(int process) {
        layoutInitPic.getBackground().setAlpha(process);
        if (default_screen_num > 1) {
            setScreenWatermark(default_screen_num, default_watermark_space);
        }
    }

    @Override
    public void onTextAlpha(int process) {
        tvWaterTitle.setAlpha((float) process/255);
        if (!(layoutInitPic.getVisibility() == View.VISIBLE)){
            setScreenWatermark(default_screen_num,default_watermark_space);
        }
    }
}
