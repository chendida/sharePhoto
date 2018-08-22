package com.zq.dynamicphoto.utils;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import com.zq.dynamicphoto.ui.widge.FullScreenWatermarkDialog;
import com.zq.dynamicphoto.ui.widge.StrokeTextView;
import com.zq.dynamicphoto.ui.widge.TextAlignDialog;
import com.zq.dynamicphoto.ui.widge.TextColorDialog;
import com.zq.dynamicphoto.ui.widge.TextFontDialog;
import com.zq.dynamicphoto.ui.widge.TextOutlineDialog;
import com.zq.dynamicphoto.ui.widge.TitleBgColorDialog;
import com.zq.dynamicphoto.ui.widge.WaterBgDialog;
import com.zq.dynamicphoto.ui.widge.WaterTitleDialog;
import com.zq.dynamicphoto.ui.widge.WholeColorEditDialog;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

/**
 * Created by Administrator on 2018/8/21.
 */

public class WatermarkLabelManager implements WatermarkSeekBarListener {
    private static WatermarkLabelManager instance;
    private BaseActivity mContext;
    private ImageView ivHead;
    private AutoRelativeLayout layoutInitPic,layoutWholeWaterContent;
    private StrokeTextView tvTitle1,tvTitle2,tvTitle3,tvTitle4;
    private TextView etTitle1,etTitle2,etTitle3,etTitle4;
    private int default_screen_num = 1;//默认的全屏水印的数量是2*2
    private int default_watermark_space = 0;//默认的全屏水印的间距
    private CheckBox checkFullWatermark;
    private CheckBox checkWaterBgSetting;
    private int round = 0;//默认圆角值
    int realHeight = 0;
    int realWidth = 0;
    private int position = 0;//保存水印背景颜色色值
    private int bgAlpha = 255;//默认的水印背景透明度
    private Boolean check_title1_outline = false;//第一标题描边开关状态
    private Boolean check_title1_bg = false;//第一标题背景开关状态
    private Boolean check_title2_outline = false;//第二标题描边开关状态
    private Boolean check_title2_bg = false;//第二标题背景开关状态
    private Boolean check_title3_outline = false;//第三标题描边开关状态
    private Boolean check_title3_bg = false;//第三标题背景开关状态
    private Boolean check_title4_outline = false;//第四标题描边开关状态
    private Boolean check_title4_bg = false;//第四标题背景开关状态
    private int editTitleType = 1;//1表示编辑第一标题，2表示编辑第二标题,以此类推
    //描边数据
    private float degree1 = 1.0f;//第一标题描边宽度
    private float degree2 = 1.0f;//第二标题描边宽度
    private float degree3 = 1.0f;//第3标题描边宽度
    private float degree4 = 1.0f;//第4标题描边宽度
    private int outLineAlpha1 = 255;//默认的第一标题描边透明度
    private int outLineAlpha2 = 255;//默认的第二标题描边透明度
    private int outLineAlpha3 = 255;//默认的第二标题描边透明度
    private int outLineAlpha4 = 255;//默认的第二标题描边透明度
    private int outline_color_position1 = 0;//保存第一标题描边颜色
    private int outline_color_position2 = 0;//保存第二标题描边颜色
    private int outline_color_position3 = 0;//保存第3标题描边颜色
    private int outline_color_position4 = 0;//保存第3标题描边颜色
    //文字颜色背景
    private int bg_color_alpha_title1 = 255;//默认的第一标题背景颜色透明度
    private int bg_color_alpha_title2 = 255;//默认的第二标题背景颜色透明度
    private int bg_color_alpha_title3 = 255;//默认的第3标题背景颜色透明度
    private int bg_color_alpha_title4 = 255;//默认的第4标题背景颜色透明度
    private int bg_color_index_title1 = 0;//默认的第一标题背景颜色的色值下标
    private int bg_color_index_title2 = 0;//默认的第二标题背景颜色的色值下标
    private int bg_color_index_title3 = 0;//默认的第二标题背景颜色的色值下标
    private int bg_color_index_title4 = 0;//默认的第二标题背景颜色的色值下标
    //文字颜色和透明度
    private int color_index_title1 = 0;//第一标题颜色
    private int color_index_title2 = 0;//第二标题颜色
    private int color_index_title3 = 0;//第3标题颜色
    private int color_index_title4 = 0;//第4标题颜色
    private int color_alpha_title1 = 255;//第一标题颜色透明度
    private int color_alpha_title2 = 255;//第二标题颜色透明度
    private int color_alpha_title3 = 255;//第3标题颜色透明度
    private int color_alpha_title4 = 255;//第4标题颜色透明度

    public void setRealHeight(int realHeight) {
        this.realHeight = realHeight;
    }

    public void setRealWidth(int realWidth) {
        this.realWidth = realWidth;
    }

    /**
     * 单例
     * @return
     */
    public static WatermarkLabelManager getInstance(){
        if (null == instance) {
            synchronized (WatermarkMoneyManager.class) {
                if (null == instance) {
                    instance = new WatermarkLabelManager();
                }
            }
        }
        return instance;
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

    @Override
    public void onTextAlpha(int process) {
        if (editTitleType == 1){
            color_alpha_title1 = process;
            int color = ColorUtils.getColor(color_index_title1);
            tvTitle1.setTextColor(Color.argb(process,Color.red(color),Color.green(color),Color.blue(color)));
        }else if (editTitleType == 2){
            color_alpha_title2 = process;
            int color = ColorUtils.getColor(color_index_title2);
            tvTitle2.setTextColor(Color.argb(process,Color.red(color),Color.green(color),Color.blue(color)));
        }else if (editTitleType == 3){
            color_alpha_title3 = process;
            int color = ColorUtils.getColor(color_index_title3);
            tvTitle3.setTextColor(Color.argb(process,Color.red(color),Color.green(color),Color.blue(color)));
        }else if (editTitleType == 4){
            color_alpha_title4 = process;
            int color = ColorUtils.getColor(color_index_title4);
            tvTitle4.setTextColor(Color.argb(process,Color.red(color),Color.green(color),Color.blue(color)));
        }
        refreshChanged();
    }

    @Override
    public void onTextOutlineDegree(int process, Boolean isOpen) {
        if (editTitleType == 1){
            degree1 = process / 10;
            if (isOpen) {
                setTextViewOutline(tvTitle1,ColorUtils.getColor(outline_color_position1),degree1
                        ,outLineAlpha1);
                refreshChanged();
            }
        }else if (editTitleType == 2){
            degree2 = process / 10;
            if (isOpen) {
                setTextViewOutline(tvTitle2,ColorUtils.getColor(outline_color_position2),degree2
                        ,outLineAlpha2);
                refreshChanged();
            }
        }else if (editTitleType == 3){
            degree3 = process / 10;
            if (isOpen) {
                setTextViewOutline(tvTitle3,ColorUtils.getColor(outline_color_position3),degree3
                        ,outLineAlpha3);
                refreshChanged();
            }
        }else if (editTitleType == 4){
            degree4 = process / 10;
            if (isOpen) {
                setTextViewOutline(tvTitle4,ColorUtils.getColor(outline_color_position4),degree4
                        ,outLineAlpha4);
                refreshChanged();
            }
        }
    }

    @Override
    public void onTextOutlineAlpha(int process, Boolean isOpen) {
        if (editTitleType == 1){
            outLineAlpha1 = process;
            if (isOpen) {
                setTextViewOutline(tvTitle1,ColorUtils.getColor(outline_color_position1),degree1
                        ,outLineAlpha1);
                refreshChanged();
            }
        }else if (editTitleType == 2){
            outLineAlpha2 = process;
            if (isOpen) {
                setTextViewOutline(tvTitle2,ColorUtils.getColor(outline_color_position2),degree2
                        ,outLineAlpha2);
                refreshChanged();
            }
        }else if (editTitleType == 3){
            outLineAlpha3 = process;
            if (isOpen) {
                setTextViewOutline(tvTitle3,ColorUtils.getColor(outline_color_position3),degree3
                        ,outLineAlpha3);
                refreshChanged();
            }
        }else if (editTitleType == 4){
            outLineAlpha4 = process;
            if (isOpen) {
                setTextViewOutline(tvTitle4,ColorUtils.getColor(outline_color_position4),degree4
                        ,outLineAlpha4);
                refreshChanged();
            }
        }
    }

    @Override
    public void onTitleBgColorAlpha(int process, Boolean isOpen) {
        if (editTitleType == 1){
            bg_color_alpha_title1 = process;
            if (isOpen) {
                updateTitleBgColor(bg_color_index_title1,true);
            }
        }else if (editTitleType == 2){
            bg_color_alpha_title2 = process;
            if (isOpen) {
                updateTitleBgColor(bg_color_index_title2,true);
            }
        }else if (editTitleType == 3){
            bg_color_alpha_title3 = process;
            if (isOpen) {
                updateTitleBgColor(bg_color_index_title3,true);
            }
        }else if (editTitleType == 4){
            bg_color_alpha_title4 = process;
            if (isOpen) {
                updateTitleBgColor(bg_color_index_title4,true);
            }
        }
    }

    @Override
    public void onTextSizeChange(int process) {
        if (editTitleType == 1) {
            refreshTextSize(tvTitle1,process);
        }else if (editTitleType == 2){
            refreshTextSize(tvTitle2,process);
        }else if (editTitleType == 3){
            refreshTextSize(tvTitle3,process);
        }else if (editTitleType == 4){
            refreshTextSize(tvTitle4,process);
        }
    }

    private void refreshTextSize(StrokeTextView textView,int process){
        textView.changeTextSize(TypedValue.COMPLEX_UNIT_PX, process);
        refreshChanged();
    }

    @Override
    public void onTextSpaceChange(int process) {
        if (editTitleType == 1) {
            refreshTextSpace(tvTitle1,process);
        }else if (editTitleType == 2){
            refreshTextSpace(tvTitle2,process);
        }else if (editTitleType == 3){
            refreshTextSpace(tvTitle3,process);
        }else if (editTitleType == 4){
            refreshTextSpace(tvTitle4,process);
        }
    }

    private void refreshTextSpace(StrokeTextView tvTitle, int process) {
        tvTitle.setSpacing(process/10);
        refreshChanged();
    }

    @Override
    public void onHideIcon() {

    }

    @Override
    public void onChangeIcon(Drawable drawable) {

    }

    @Override
    public void onChangeTextFont(Typeface typeface) {
        if (editTitleType == 1){
            tvTitle1.setTypeFont(typeface);
        }else if (editTitleType == 2){
            tvTitle2.setTypeFont(typeface);
        }else if (editTitleType == 3){
            tvTitle2.setTypeFont(typeface);
        }else if (editTitleType == 4){
            tvTitle2.setTypeFont(typeface);
        }
        refreshChanged();
    }

    public void initView(BaseActivity activity,AutoRelativeLayout layoutInitPic,
                         AutoRelativeLayout layoutWholeWaterContent,ImageView ivHead,
                         StrokeTextView tvTitle1,
                         StrokeTextView tvTitle2,StrokeTextView tvTitle3,StrokeTextView tvTitle4,
                         TextView etTitle1,TextView etTitle2,TextView etTitle3,TextView etTitle4,
                         CheckBox checkFullWatermark,CheckBox checkWaterBgSetting) {
        this.mContext = activity;
        this.layoutInitPic = layoutInitPic;
        this.layoutWholeWaterContent = layoutWholeWaterContent;
        this.ivHead = ivHead;
        this.tvTitle1 = tvTitle1;
        this.tvTitle2 = tvTitle2;
        this.tvTitle3 = tvTitle3;
        this.tvTitle4 = tvTitle4;
        this.etTitle1 = etTitle1;
        this.etTitle2 = etTitle2;
        this.etTitle3 = etTitle3;
        this.etTitle4 = etTitle4;
        this.checkFullWatermark = checkFullWatermark;
        this.checkWaterBgSetting = checkWaterBgSetting;
    }

    /**
     * 显示整体水印弹窗dialog
     */
    public void showWholeColorDialog() {
        new WholeColorEditDialog(mContext, R.style.dialog,
                new WholeColorEditDialog.OnItemClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int position) {
                        updateWholeColor(position);
                    }
                }).show();
    }

    /**
     * 更新整体颜色
     * @param position
     */
    private void updateWholeColor(int position) {
        int color = ColorUtils.getColor(position);
        ivHead.setColorFilter(color);
        tvTitle1.setTextColor(color);
        if (tvTitle2 != null){
            tvTitle2.setTextColor(color);
        }
        if (tvTitle3 != null){
            tvTitle3.setTextColor(color);
        }
        if (tvTitle4 != null){
            tvTitle4.setTextColor(color);
        }
        refreshChanged();
    }

    /**
     * 多个水印时刷新界面
     */
    public void refreshChanged() {
        if (!(layoutInitPic.getVisibility() == View.VISIBLE)){
            setScreenWatermark();
        }
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
        Log.i("space","newWidth = " + newWidth);
        Log.i("space","newHeight = " + newHeight);
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
     * 还原全屏水印
     */
    public void setScreenWatermarkInit() {
        layoutInitPic.setVisibility(View.VISIBLE);
        layoutWholeWaterContent.setBackground(null);
    }

    public int getDefault_screen_num() {
        return default_screen_num;
    }

    public void setDefault_screen_num(int default_screen_num) {
        this.default_screen_num = default_screen_num;
    }

    /**
     * 全屏水印dialog
     */
    public void showFullScreenWatermarkDialog() {
        new FullScreenWatermarkDialog(mContext,
                R.style.dialog,default_watermark_space,default_screen_num,
                this).show();
    }

    /**
     * 显示水印背景设置弹窗
     */
    public void showWaterBgDialog() {
        new WaterBgDialog(mContext, R.style.dialog,
                mContext.getResources().getString(R.string.watermark_bg),
                mContext.getString(R.string.watermark_alpha),
                mContext.getString(R.string.watermark_corner), bgAlpha,round,this,
                new WaterBgDialog.OnItemClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int position) {
                        setPosition(position);
                        updateBgColor();
                    }
                }).show();
    }

    /**
     * 记录水印背景颜色色值
     * @param position
     */
    private void setPosition(int position) {
        this.position = position;
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
     * 关闭水印背景
     */
    public void closeWaterBg() {
        layoutInitPic.setBackgroundColor(mContext.getResources().getColor(R.color.alpha));
        refreshChanged();
    }

    /**
     * 展示标题或者第二标题文字图标编辑弹窗
     */
    public void showTextEditDialog(final int type) {
        String tvContent = "";
        Boolean outlineStatus = false;
        Boolean bgStatus = false;
        if (type == 1){
            tvContent = etTitle1.getText().toString();
            outlineStatus = check_title1_outline;
            bgStatus = check_title1_bg;
        }else if (type == 2){
            tvContent = etTitle2.getText().toString();
            outlineStatus = check_title2_outline;
            bgStatus = check_title2_bg;
        }else if (type == 3){
            tvContent = etTitle3.getText().toString();
            outlineStatus = check_title3_outline;
            bgStatus = check_title3_bg;
        }else if (type == 4){
            tvContent = etTitle4.getText().toString();
            outlineStatus = check_title4_outline;
            bgStatus = check_title4_bg;
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
                                //Log.i("size","size = " + tvTitle.getTextSize());
                                if (type == 1) {
                                    showTextFontDialog((int) tvTitle1.getTextSize(),tvTitle1.getSpacing());
                                }else if (type == 2){
                                    showTextFontDialog((int) tvTitle2.getTextSize(),tvTitle2.getSpacing());
                                }else if (type == 3){
                                    showTextFontDialog((int) tvTitle3.getTextSize(),tvTitle3.getSpacing());
                                }else if (type == 4){
                                    showTextFontDialog((int) tvTitle4.getTextSize(),tvTitle4.getSpacing());
                                }
                                break;
                            case 2://文字颜色修改
                                if (type == 1){
                                    showTextColorDialog(type,color_alpha_title1);
                                }else if (type == 2){
                                    showTextColorDialog(type,color_alpha_title2);
                                }else if (type == 3){
                                    showTextColorDialog(type,color_alpha_title3);
                                }else if (type == 4){
                                    showTextColorDialog(type,color_alpha_title4);
                                }
                                break;
                            case 3://文字对齐
                                showTextAlignDialog(type);
                                break;
                            case 4://文字描边设置
                                if (type == 1){
                                    showTextOutlineDialog(isOpen,degree1,outLineAlpha1);
                                }else if (type == 2){
                                    showTextOutlineDialog(isOpen,degree2,outLineAlpha2);
                                }else if (type == 3){
                                    showTextOutlineDialog(isOpen,degree3,outLineAlpha3);
                                }else if (type == 4){
                                    showTextOutlineDialog(isOpen,degree4,outLineAlpha4);
                                }
                                break;
                            case 5://文字背景设置
                                if (type == 1) {
                                    showTextBgDialog(isOpen, bg_color_alpha_title1);
                                }else if (type == 2){
                                    showTextBgDialog(isOpen, bg_color_alpha_title2);
                                }else if (type == 3){
                                    showTextBgDialog(isOpen, bg_color_alpha_title3);
                                }else if (type == 4){
                                    showTextBgDialog(isOpen, bg_color_alpha_title4);
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
                },false,type,outlineStatus,bgStatus).show();
    }

    /**
     * 更新文字描边开关状态和界面
     * @param isOpen
     */
    private void updateOutlineStatus(Boolean isOpen) {
        if (isOpen){
            if (editTitleType == 1){
                check_title1_outline = true;
                updateTextOutline(outline_color_position1,true);
            }else if (editTitleType == 2){
                check_title2_outline = true;
                updateTextOutline(outline_color_position2,true);
            }else if (editTitleType == 3){
                check_title3_outline = true;
                updateTextOutline(outline_color_position3,true);
            }else if (editTitleType == 4){
                check_title4_outline = true;
                updateTextOutline(outline_color_position4,true);
            }
        }else {
            if (editTitleType == 1){
                check_title1_outline = false;
                setTextViewOutline(tvTitle1,ColorUtils.getColor(0),degree1,outLineAlpha1);
                refreshChanged();
            }else if (editTitleType == 2){
                check_title2_outline = false;
                setTextViewOutline(tvTitle2,ColorUtils.getColor(0),degree2,outLineAlpha2);
                refreshChanged();
            }else if (editTitleType == 3){
                check_title3_outline = false;
                setTextViewOutline(tvTitle3,ColorUtils.getColor(0),degree3,outLineAlpha3);
                refreshChanged();
            }else if (editTitleType == 4){
                check_title4_outline = false;
                setTextViewOutline(tvTitle4,ColorUtils.getColor(0),degree4,outLineAlpha4);
                refreshChanged();
            }
        }
    }

    /**
     * 更新标题背景颜色状态
     * @param isOpen
     */
    private void updateTitleBgStatus(Boolean isOpen) {
        if (isOpen){
            if (editTitleType == 1){
                check_title1_bg = true;
                updateTitleBgColor(bg_color_index_title1,true);
            }else if (editTitleType == 2){
                check_title2_bg = true;
                updateTitleBgColor(bg_color_index_title2,true);
            }else if (editTitleType == 3){
                check_title3_bg = true;
                updateTitleBgColor(bg_color_index_title3,true);
            }else if (editTitleType == 4){
                check_title4_bg = true;
                updateTitleBgColor(bg_color_index_title4,true);
            }
        }else {
            if (editTitleType == 1){
                check_title1_bg = false;
                tvTitle1.setBackgroundColor(ColorUtils.getColor(0));
                refreshChanged();
            }else if (editTitleType == 2){
                check_title2_bg = false;
                tvTitle2.setBackgroundColor(ColorUtils.getColor(0));
                refreshChanged();
            }else if (editTitleType == 3){
                check_title3_bg = false;
                tvTitle3.setBackgroundColor(ColorUtils.getColor(0));
                refreshChanged();
            }else if (editTitleType == 4){
                check_title4_bg = false;
                tvTitle4.setBackgroundColor(ColorUtils.getColor(0));
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
            bg_color_index_title1 = position;
            if (isOpen){
                tvTitle1.setBackgroundColor(color);
                tvTitle1.getBackground().setAlpha(bg_color_alpha_title1);
                refreshChanged();
            }
        }else if (editTitleType == 2){
            bg_color_index_title2 = position;
            if (isOpen){
                tvTitle2.setBackgroundColor(color);
                tvTitle2.getBackground().setAlpha(bg_color_alpha_title2);
                refreshChanged();
            }
        }else if (editTitleType == 3){
            bg_color_index_title3 = position;
            if (isOpen){
                tvTitle3.setBackgroundColor(color);
                tvTitle3.getBackground().setAlpha(bg_color_alpha_title3);
                refreshChanged();
            }
        }else if (editTitleType == 4){
            bg_color_index_title4 = position;
            if (isOpen){
                tvTitle4.setBackgroundColor(color);
                tvTitle4.getBackground().setAlpha(bg_color_alpha_title4);
                refreshChanged();
            }
        }
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
            outline_color_position1 = position;
            if (isOpen){
                setTextViewOutline(tvTitle1,color,degree1,outLineAlpha1);
                refreshChanged();
            }
        }else if (editTitleType == 2){
            outline_color_position2 = position;
            if (isOpen){
                setTextViewOutline(tvTitle2,color,degree2,outLineAlpha2);
                refreshChanged();
            }
        }else if (editTitleType == 3){
            outline_color_position3 = position;
            if (isOpen){
                setTextViewOutline(tvTitle3,color,degree3,outLineAlpha3);
                refreshChanged();
            }
        }else if (editTitleType == 4){
            outline_color_position4 = position;
            if (isOpen){
                setTextViewOutline(tvTitle4,color,degree4,outLineAlpha4);
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
     * 显示文字对齐弹窗
     */
    private void showTextAlignDialog(final int type) {
        new TextAlignDialog(mContext, R.style.dialog,
                new TextAlignDialog.OnItemClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int position) {
                        switch (position){
                            case 0://左
                                if (type == 1){
                                    tvTitle1.setPlace(Gravity.START);
                                }else if (type == 2){
                                    tvTitle2.setPlace(Gravity.START);
                                }else if (type == 3){
                                    tvTitle3.setPlace(Gravity.START);
                                }else if (type == 4){
                                    tvTitle4.setPlace(Gravity.START);
                                }
                                break;
                            case 1://中
                                if (type == 1){
                                    tvTitle1.setPlace(Gravity.CENTER_HORIZONTAL);
                                }else if (type == 2){
                                    tvTitle2.setPlace(Gravity.CENTER_HORIZONTAL);
                                }else if (type == 3){
                                    tvTitle3.setPlace(Gravity.CENTER_HORIZONTAL);
                                }else if (type == 4){
                                    tvTitle4.setPlace(Gravity.CENTER_HORIZONTAL);
                                }
                                break;
                            case 2://右
                                if (type == 1){
                                    tvTitle1.setPlace(Gravity.END);
                                }else if (type == 2){
                                    tvTitle2.setPlace(Gravity.END);
                                }else if (type == 3){
                                    tvTitle3.setPlace(Gravity.END);
                                }else if (type == 4){
                                    tvTitle4.setPlace(Gravity.END);
                                }
                                break;
                        }
                        refreshChanged();
                    }
                }).show();
    }

    /**
     * 显示文字字体和大小，间距编辑距离弹窗
     */
    private void showTextFontDialog(int size,float space) {
        new TextFontDialog(mContext, R.style.dialog, this,size,space).show();
    }

    /**
     * 展示改变文字颜色的弹窗
     */
    private void showTextColorDialog(final int type,int alpha) {
        new TextColorDialog(mContext, R.style.dialog,alpha, this,
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
            color_index_title1 = position;
            changeTextColor(color_alpha_title1,color,tvTitle1);
        }else if (type == 2){
            color_index_title2 = position;
            changeTextColor(color_alpha_title2,color,tvTitle2);
        }else if (type == 3){
            color_index_title3 = position;
            changeTextColor(color_alpha_title3,color,tvTitle3);
        }else if (type == 4){
            color_index_title4 = position;
            changeTextColor(color_alpha_title4,color,tvTitle4);
        }
        refreshChanged();
    }

    private void changeTextColor(int alpha,int color,StrokeTextView tvTitle){
        tvTitle.setTextColor(Color.argb(alpha,Color.red(color),
                Color.green(color),Color.blue(color)));
    }

    /**
     * 更新文字
     * @param content
     * @param type
     */
    private void updateTextContent(String content, int type) {
        if (type == 1){
            changeTextContent(tvTitle1,etTitle1,content);
        }else if (type == 2){
            changeTextContent(tvTitle2,etTitle2,content);
        }else if (type == 3){
            changeTextContent(tvTitle3,etTitle3,content);
        }else if (type == 4){
            changeTextContent(tvTitle4,etTitle4,content);
        }
        refreshChanged();
    }

    private void changeTextContent(StrokeTextView tvTitle,TextView etTitle,String content){
        tvTitle.changeTextContent(content);
        etTitle.setText(content);
    }
}
