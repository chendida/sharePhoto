package com.zq.dynamicphoto.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.ui.Watermark0001Activity;
import com.zq.dynamicphoto.ui.widge.FullScreenWatermarkDialog;
import com.zq.dynamicphoto.ui.widge.StrokeTextView;
import com.zq.dynamicphoto.ui.widge.TextAlignDialog;
import com.zq.dynamicphoto.ui.widge.TextColorDialog;
import com.zq.dynamicphoto.ui.widge.TextFontDialog;
import com.zq.dynamicphoto.ui.widge.TextOutlineDialog;
import com.zq.dynamicphoto.ui.widge.TitleBgColorDialog;
import com.zq.dynamicphoto.ui.widge.WaterBgDialog;
import com.zq.dynamicphoto.ui.widge.WaterIconDialog;
import com.zq.dynamicphoto.ui.widge.WaterTitleDialog;
import com.zq.dynamicphoto.ui.widge.WholeColorEditDialog;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;
/**
 * 水印操作管理类
 * Created by Administrator on 2018/8/6.
 */

public class WatermarkManager implements WatermarkSeekBarListener{
    private static final String TAG = "WatermarkManager";
    private static WatermarkManager instance;
    private WaterIconDialog waterIconDialog;
    private FullScreenWatermarkDialog fullScreenWatermarkDialog;
    private WaterBgDialog waterBgDialog;
    private WholeColorEditDialog colorEditDialog;
    private TextAlignDialog textAlignDialog;
    private TextOutlineDialog titleOutlineDialog;
    private TextOutlineDialog secondTitleOutlineDialog;
    private int editTitleType = 1;//1表示编辑第一标题，2表示编辑第二标题
    private int default_screen_num = 1;//默认的全屏水印的数量是2*2
    private int default_watermark_space = 0;//默认的全屏水印的间距
    private int position = 0;//保存水印背景颜色色值
    private int titleOutlinePosition = 0;//保存第一标题描边颜色
    private int secondTitleOutlinePosition = 0;//保存第二标题描边颜色
    private int round = 0;//默认圆角值
    private String watermarkTitle = "微商云管家";
    private float degree = 1.0f;//第一标题描边宽度
    private float secondDegree = 1.0f;//第二标题描边宽度
    private int outLineAlpha = 255;//默认的第一标题描边透明度
    private int secondOutLineAlpha = 255;//默认的第二标题描边透明度
    private int bgAlpha = 255;//默认的水印背景透明度
    private int titleBgColorAlpha = 255;//默认的第一标题背景颜色透明度
    private int secondTitleBgColorAlpha = 255;//默认的第二标题背景颜色透明度
    private int titleBgColorIndex = 0;//默认的第一标题背景颜色的色值下标
    private int secondTitleBgColorIndex = 0;//默认的第二标题背景颜色的色值下标
    int realHeight = 0;
    int realWidth = 0;
    private AutoRelativeLayout layoutInitPic;
    private AutoRelativeLayout layoutWholeWaterContent;
    private CheckBox checkFullWatermark;
    private CheckBox checkWaterBgSetting;
    private ImageView ivHead,ivWaterIcon,ivWaterIconHint;
    private StrokeTextView tvTitle,tvSecondTitle;
    private TextView tvTitleHint,tvSecondTitleHint;
    private CheckBox checkIsShowIcon;
    private BaseActivity mContext;
    private Boolean titleOutlineIsOpen = false;//第一标题描边开关状态
    private Boolean titleBgIsOpen = false;//第一标题背景开关状态
    private Boolean secondTitleOutlineIsOpen = false;//第二标题描边开关状态
    private Boolean secondTitleBgIsOpen = false;//第二标题背景开关状态
    public int getDefault_watermark_space() {
        return default_watermark_space;
    }

    public void setDefault_watermark_space(int default_watermark_space) {
        this.default_watermark_space = default_watermark_space;
    }

    public void setRealHeight(int realHeight) {
        this.realHeight = realHeight;
    }

    public void setDefault_screen_num(int default_screen_num) {
        this.default_screen_num = default_screen_num;
    }

    public int getDefault_screen_num() {

        return default_screen_num;
    }

    public void setRealWidth(int realWidth) {
        this.realWidth = realWidth;
    }

    /**
     * 单例
     * @return
     */
    public static WatermarkManager getInstance(){
        if (null == instance) {
            synchronized (WatermarkManager.class) {
                if (null == instance) {
                    instance = new WatermarkManager();
                }
            }
        }
        return instance;
    }

    /**
     * 展示标题或者第二标题文字图标编辑弹窗
     */
    public void showTextEditDialog(final int type) {
        String tvContent = "";
        Boolean outlineStatus;
        Boolean bgStatus;
        if (type == 1){
            tvContent = tvTitle.getText().toString();
            outlineStatus = titleOutlineIsOpen;
            bgStatus = titleBgIsOpen;
        }else {
            tvContent = tvSecondTitleHint.getText().toString();
            outlineStatus = secondTitleOutlineIsOpen;
            bgStatus = secondTitleBgIsOpen;
        }
        editTitleType = type;
        new WaterTitleDialog(mContext, R.style.dialog, tvContent,
                new WaterTitleDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position, String content,Boolean isOpen) {
                switch (position){
                    case 0://修改文字
                        updateTextContent(content,type);
                        break;
                    case 1://字体，大小，间距
                        Log.i("size","size = " + tvTitle.getTextSize());
                        if (type == 1) {
                            showTextFontDialog((int) tvTitle.getTextSize(),tvTitle.getSpacing());
                        }else {
                            showTextFontDialog((int) tvSecondTitle.getTextSize(),tvSecondTitle.getSpacing());
                        }
                        break;
                    case 2://文字颜色修改
                        showTextColorDialog(type);
                        break;
                    case 3://文字对齐或者更换图标弹窗
                        if (type == 1){
                            showTextAlignDialog();
                        }else {
                            showWaterIconDialog();
                        }
                        break;
                    case 4://文字描边设置
                        if (type == 1){
                            showTextOutlineDialog(isOpen,degree,outLineAlpha);
                        }else {
                            showTextOutlineDialog(isOpen,secondDegree,secondOutLineAlpha);
                        }
                        break;
                    case 5://文字背景设置
                        if (type == 1) {
                            showTextBgDialog(isOpen, titleBgColorAlpha);
                        }else {
                            showTextBgDialog(isOpen,secondTitleBgColorAlpha);
                        }
                        break;
                    case 6://文字背景的开关
                        updateTitleBgStatus(isOpen);
                        break;
                    case 7://文字描边开关
                        updateOutlineStatus(isOpen);
                        break;
                }
            }
        },type,outlineStatus,bgStatus).show();
    }

    /**
     * 显示文字字体和大小，间距编辑距离弹窗
     */
    private void showTextFontDialog(int size,float space) {
        new TextFontDialog(mContext, R.style.dialog, this,size,space).show();
    }

    /**
     * 更新标题背景颜色状态
     * @param isOpen
     */
    private void updateTitleBgStatus(Boolean isOpen) {
        if (isOpen){
            if (editTitleType == 1){
                titleBgIsOpen = true;
                updateTitleBgColor(titleBgColorIndex,true);
            }else {
                secondTitleBgIsOpen = true;
                updateTitleBgColor(secondTitleBgColorIndex,true);
            }
        }else {
            if (editTitleType == 1){
                titleBgIsOpen = false;
                tvTitle.setBackgroundColor(ColorUtils.getColor(0));
                refreshChanged();
            }else {
                secondTitleBgIsOpen = false;
                tvSecondTitle.setBackgroundColor(ColorUtils.getColor(0));
                refreshChanged();
            }
        }
    }

    /**
     * 展示背景颜色编辑弹窗
     * @param isOpen
     */
    private void showTextBgDialog(final Boolean isOpen,int alpha) {
        new TitleBgColorDialog(mContext, R.style.dialog, this,
                new TitleBgColorDialog.OnItemClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int position) {
                        updateTitleBgColor(position,isOpen);
                    }
                },isOpen,alpha).show();
    }

    /**
     * 更新文字背景颜色
     * @param position
     * @param isOpen
     */
    private void updateTitleBgColor(int position, Boolean isOpen) {
        int color = ColorUtils.getColor(position);
        if (editTitleType == 1){
            titleBgColorIndex = position;
            if (isOpen){
                tvTitle.setBackgroundColor(color);
                tvTitle.getBackground().setAlpha(titleBgColorAlpha);
                refreshChanged();
            }
        }else {
            secondTitleBgColorIndex = position;
            if (isOpen){
                tvSecondTitle.setBackgroundColor(color);
                tvSecondTitle.getBackground().setAlpha(secondTitleBgColorAlpha);
                refreshChanged();
            }
        }
    }

    /**
     * 更新文字描边开关状态和界面
     * @param isOpen
     */
    private void updateOutlineStatus(Boolean isOpen) {
        if (isOpen){
            if (editTitleType == 1){
                titleOutlineIsOpen = true;
                updateTextOutline(titleOutlinePosition,true);
            }else {
                secondTitleOutlineIsOpen = true;
                updateTextOutline(secondTitleOutlinePosition,true);
            }
        }else {
            if (editTitleType == 1){
                titleOutlineIsOpen = false;
                setTextViewOutline(tvTitle,ColorUtils.getColor(0),degree,outLineAlpha);
                //tvTitle.init(ColorUtils.getColor(0),degree,outLineAlpha,tvTitle.getText().toString());
                refreshChanged();
            }else {
                secondTitleOutlineIsOpen = false;
                setTextViewOutline(tvSecondTitle,ColorUtils.getColor(0),
                        secondDegree,secondOutLineAlpha);
                //tvSecondTitle.init(ColorUtils.getColor(0),secondDegree,secondOutLineAlpha,tvSecondTitle.getText().toString());
                refreshChanged();
            }
        }
    }

    /**
     * 设置描边
     */
    private void setTextViewOutline(StrokeTextView textView,int color,float degree,int alpha) {
        textView.setOutLine(color,degree,alpha);
    }


    /**
     * 显示标题文字描边弹窗
     */
    private void showTextOutlineDialog(final Boolean isOepn,float degree,int alpha) {
        new TextOutlineDialog(mContext, R.style.dialog,
                this,new TextOutlineDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                updateTextOutline(position,isOepn);
            }
        },isOepn,degree,alpha).show();
    }

    /**
     * 文字描边
     * @param position
     */
    private void updateTextOutline(int position,Boolean isOpen) {
        int color = ColorUtils.getColor(position);
        if (editTitleType == 1){
            titleOutlinePosition = position;
            if (isOpen){
                //tvTitle.setTextO(color,degree,outLineAlpha);
                setTextViewOutline(tvTitle,color,degree,outLineAlpha);
                refreshChanged();
            }
        }else {
            secondTitleOutlinePosition = position;
            if (isOpen){
                setTextViewOutline(tvSecondTitle,color,secondDegree,secondOutLineAlpha);
                //tvSecondTitle.init(color,secondDegree,secondOutLineAlpha,tvSecondTitle.getText().toString());
                refreshChanged();
            }
        }
    }

    /**
     * 显示文字对齐弹窗
     */
    private void showTextAlignDialog() {
        if (textAlignDialog == null){
            textAlignDialog = new TextAlignDialog(mContext, R.style.dialog,
                    new TextAlignDialog.OnItemClickListener() {
                @Override
                public void onClick(Dialog dialog, int position) {
                    switch (position){
                        case 0://左
                            tvTitle.setPlace(Gravity.START);
                            break;
                        case 1://中
                            tvTitle.setPlace(Gravity.CENTER_HORIZONTAL);
                            break;
                        case 2://右
                            tvTitle.setPlace(Gravity.END);
                            break;
                    }
                    refreshChanged();
                }
            });
        }
        textAlignDialog.show();
    }

    /**
     * 更新文字
     * @param content
     * @param type
     */
    private void updateTextContent(String content, int type) {
        if (type == 1){
            tvTitle.changeTextContent(content);
            tvTitleHint.setText(content);
        }else {
            tvSecondTitle.changeTextContent(content);
            tvSecondTitleHint.setText(content);
        }
        refreshChanged();
    }

    /**
     * 展示改变文字颜色的弹窗
     */
    private void showTextColorDialog(final int type) {
        new TextColorDialog(mContext, R.style.dialog, this,
                new TextColorDialog.OnItemClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int position) {
                        updateTextColor(position,type);
                    }
                }).show();
    }

    /**
     * 更改文字颜色
     */
    private void updateTextColor(int position,int type) {
        int color = ColorUtils.getColor(position);
        if (type == 1) {
            tvTitle.setTextColor(color);
        }else {
            tvSecondTitle.setTextColor(color);
        }
        refreshChanged();
    }


    /**
     * 全屏水印的设置
     */
    public void setScreenWatermark() {
        Log.i("66666666666","tt = " + "setScreenWatermark()");
        layoutInitPic.refreshDrawableState();
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
        Bitmap repeater = createRepeater(default_screen_num,
                zoomImg(getRoundedCornerBitmap(bitmap,round),newWidth, newHeight),default_watermark_space);
        layoutInitPic.setVisibility(View.GONE);
        layoutWholeWaterContent.setBackground(new BitmapDrawable(repeater));
        layoutInitPic.setDrawingCacheEnabled(false);
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

    // 等比缩放图片
    private Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
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


    /**
     * 横向水印
     * @param count
     * @param src
     * @param space
     * @return
     */
    private Bitmap createRepeater(int count, Bitmap src,int space) {
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

    /**
     * 纵向水印
     * @param count
     * @param src
     * @param space
     * @return
     */
    private Bitmap createYRepeater(int count, Bitmap src,int space) {
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

    /**
     * 展示图标弹窗
     */
    private void showWaterIconDialog() {
        if (waterIconDialog == null) {
            waterIconDialog = new WaterIconDialog(mContext, R.style.dialog,this);
        }
        waterIconDialog.show();
    }

    /**
     * 全屏水印dialog
     */
    public void showFullScreenWatermarkDialog() {
        if (fullScreenWatermarkDialog == null){
            fullScreenWatermarkDialog = new FullScreenWatermarkDialog(mContext,
                    R.style.dialog,this);
        }
        fullScreenWatermarkDialog.show();
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
        isRefreshChanged(checkFullWatermark);
        ToastUtils.showShort("选的数量" + default_screen_num);
    }

    /**
     * 还原全屏水印
     */
    public void setScreenWatermarkInit() {
        layoutInitPic.setVisibility(View.VISIBLE);
        layoutWholeWaterContent.setBackground(null);
    }

    @Override
    public void onSpaceListener(int process) {
        default_watermark_space = process / default_screen_num;
        isRefreshChanged(checkFullWatermark);
    }

    @Override
    public void onWatermarkBgCorner(int process) {
        round = process/2;
        if (!(layoutInitPic.getVisibility() == View.VISIBLE)) {
            isRefreshChanged(checkWaterBgSetting);
        }else {
            setClipViewCornerRadius(layoutInitPic,round);
        }
    }

    /**
     * 设置视图裁剪的圆角半径
     * @param radius
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setClipViewCornerRadius(View view, final int radius) {
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
    public void onWatermarkBgAlpha(int process) {
        bgAlpha = process;
        layoutInitPic.getBackground().setAlpha(bgAlpha);
        isRefreshChanged(checkWaterBgSetting);
    }

    /**
     * 是否刷新改变界面
     * @param checkBox
     */
    private void isRefreshChanged(CheckBox checkBox){
        if (checkBox != null) {
            if (checkBox.isChecked()) {
                setScreenWatermark();
            }
        }
    }

    @Override
    public void onTextAlpha(int process) {

    }

    @Override
    public void onTextOutlineDegree(int process,Boolean isOpen) {
        if (editTitleType == 1){
            degree = process / 10;
            if (isOpen) {
                /*tvTitle.init(ColorUtils.getColor(titleOutlinePosition), degree,
                        outLineAlpha, tvTitle.getText().toString());*/
                setTextViewOutline(tvTitle,ColorUtils.getColor(titleOutlinePosition),degree
                    ,outLineAlpha);
                refreshChanged();
            }
        }else {
            secondDegree = process / 10;
            if (isOpen) {
                setTextViewOutline(tvSecondTitle,ColorUtils.getColor(secondTitleOutlinePosition),
                        secondDegree,secondOutLineAlpha);
               /* tvSecondTitle.init(ColorUtils.getColor(secondTitleOutlinePosition), secondDegree,
                        secondOutLineAlpha, tvSecondTitle.getText().toString());*/
                refreshChanged();
            }
        }
    }

    @Override
    public void onTextOutlineAlpha(int process,Boolean isOpen) {
        if (editTitleType == 1){
            outLineAlpha = process;
            if (isOpen) {
                setTextViewOutline(tvTitle,ColorUtils.getColor(titleOutlinePosition),degree
                        ,outLineAlpha);
                refreshChanged();
            }
        }else {
            secondOutLineAlpha = process;
            if (isOpen) {
                /*tvSecondTitle.init(ColorUtils.getColor(secondTitleOutlinePosition), secondDegree,
                        secondOutLineAlpha, tvSecondTitle.getText().toString());*/
                setTextViewOutline(tvSecondTitle,ColorUtils.getColor(secondTitleOutlinePosition),
                        secondDegree,secondOutLineAlpha);
                refreshChanged();
            }
        }
    }

    @Override
    public void onTitleBgColorAlpha(int process,Boolean isOpen) {
        if (editTitleType == 1){
            titleBgColorAlpha = process;
            if (isOpen) {
                updateTitleBgColor(titleBgColorIndex,true);
            }
        }else {
            secondTitleBgColorAlpha = process;
            if (isOpen) {
                updateTitleBgColor(secondTitleBgColorIndex,true);
            }
        }
    }

    /**
     * 展示整体颜色修改弹窗
     */
    public void showWholeColorDialog() {
        if (colorEditDialog == null){
            colorEditDialog = new WholeColorEditDialog(mContext, R.style.dialog,
                    new WholeColorEditDialog.OnItemClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int position) {
                            updateWholeColor(position);
                        }
                    });
        }
        colorEditDialog.show();
    }

    /**
     * 更改整体水印颜色
     * @param position
     */
    private void updateWholeColor(int position) {
        int color = ColorUtils.getColor(position);
        ivHead.setColorFilter(color);
        ivWaterIcon.setColorFilter(color);
        tvTitle.setTextColor(color);
        tvSecondTitle.setTextColor(color);
        refreshChanged();
    }

    public void refreshChanged(){
        if (!(layoutInitPic.getVisibility() == View.VISIBLE)){
            setScreenWatermark();
        }
    }

    public void initView(AutoRelativeLayout layoutInitPic, AutoRelativeLayout layoutWholeWaterContent,
                         ImageView ivHead, ImageView ivWaterIcon, ImageView ivWaterIconHint,
                         StrokeTextView tvWaterTitle, TextView tvTitleHint,
                         StrokeTextView tvWx, TextView tvWaterWx,
                         CheckBox checkFullWatermark, CheckBox checkWaterBgSetting,
                         CheckBox checkIsShowIcon,BaseActivity activity) {
        this.layoutWholeWaterContent = layoutWholeWaterContent;
        this.layoutInitPic = layoutInitPic;
        this.ivHead = ivHead;
        this.ivWaterIcon = ivWaterIcon;
        this.ivWaterIconHint = ivWaterIconHint;
        this.tvTitle = tvWaterTitle;
        this.tvTitleHint = tvTitleHint;
        this.tvSecondTitle = tvWx;
        this.tvSecondTitleHint = tvWaterWx;
        this.checkFullWatermark = checkFullWatermark;
        this.checkWaterBgSetting = checkWaterBgSetting;
        this.checkIsShowIcon = checkIsShowIcon;
        this.mContext = activity;

        tvTitle.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                tvTitle.setLayout(left,top,right,bottom);
            }
        });
    }

    /**
     * 关闭水印背景
     */
    public void closeWaterBg() {
        layoutInitPic.setBackgroundColor(mContext.getResources().getColor(R.color.alpha));
        refreshChanged();
    }

    /**
     * 打开水印背景开关
     */
    public void updateBgColor() {
        if (checkWaterBgSetting.isChecked()) {
            int color = ColorUtils.getColor(position);
            layoutInitPic.setBackgroundColor(color);
            layoutInitPic.getBackground().setAlpha(bgAlpha);
            if (checkFullWatermark.isChecked()) {
                isRefreshChanged(checkWaterBgSetting);
            }
        }
    }

    /**
     * 显示水印背景设置弹窗
     */
    public void showWaterBgDialog() {
        if (waterBgDialog == null) {
            waterBgDialog = new WaterBgDialog(mContext, R.style.dialog,
                    mContext.getResources().getString(R.string.watermark_bg),
                    mContext.getString(R.string.watermark_alpha),
                    mContext.getString(R.string.watermark_corner), this,
                    new WaterBgDialog.OnItemClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int position) {
                            setPosition(position);
                            updateBgColor();
                        }
                    });
        }
        waterBgDialog.show();
    }

    /**
     * 记录水印背景颜色色值
     * @param position
     */
    private void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onTextSizeChange(int process) {
        if (editTitleType == 1) {
            refreshTextSize(tvTitle,process);
        }else {
            refreshTextSize(tvSecondTitle,process);
        }
    }

    private void refreshTextSize(StrokeTextView textView,int process){
        textView.changeTextSize(TypedValue.COMPLEX_UNIT_PX, process);
        refreshChanged();
    }

    @Override
    public void onTextSpaceChange(int process) {
        if (editTitleType == 1) {
            refreshTextSpace(tvTitle,process);
        }else {
            refreshTextSpace(tvSecondTitle,process);
        }
    }

    private void refreshTextSpace(StrokeTextView tvTitle, int process) {
        tvTitle.setSpacing(process/10);
        refreshChanged();
    }

    @Override
    public void onChangeIcon(Drawable drawable) {
        if (ivWaterIcon.getVisibility() == View.GONE){
            ivWaterIcon.setVisibility(View.VISIBLE);
        }
        ivWaterIcon.setImageDrawable(drawable);
        ivWaterIconHint.setImageDrawable(drawable);
        refreshChanged();
    }

    @Override
    public void onHideIcon() {
        ivWaterIcon.setVisibility(View.GONE);
        refreshChanged();
    }

    @Override
    public void onChangeTextFont(Typeface typeface) {
        if (editTitleType == 1){
            tvTitle.setTypeFont(typeface);
        }else {
            tvSecondTitle.setTypeFont(typeface);
        }
        refreshChanged();
    }
}
