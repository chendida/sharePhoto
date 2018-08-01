package com.zq.dynamicphoto.view;

/**
 * Created by Administrator on 2018/7/31.
 */

public interface WatermarkSeekBarListener {
    void onNumListener(int process);//全屏水印个数

    void onSpaceListener(int process);//全屏水印间距

    void onWatermarkCorner(int process);//背景圆角

    void onWatermarkAlpha(int process);//背景透明度

    void onTextAlpha(int process);//文字透明度
}
