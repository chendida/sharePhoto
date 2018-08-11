package com.zq.dynamicphoto.view;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2018/7/31.
 */

public interface WatermarkSeekBarListener {
    void onNumListener(int process);//全屏水印个数

    void onSpaceListener(int process);//全屏水印间距

    void onWatermarkBgCorner(int process);//背景圆角

    void onWatermarkBgAlpha(int process);//背景透明度

    void onTextAlpha(int process);//文字透明度

    void onTextOutlineDegree(int process,Boolean isOpen);//文字描边粗细

    void onTextOutlineAlpha(int process,Boolean isOpen);//文字描边的透明度

    void onTitleBgColorAlpha(int process,Boolean isOpen);//标题的背景透明度

    void onTextSizeChange(int process);//文字大小

    void onTextSpaceChange(int process);//文字间距

    void onHideIcon();//隐藏小图标

    void onChangeIcon(Drawable drawable);//切换小图标

    void onChangeTextFont(Typeface typeface);//字体切换
}
