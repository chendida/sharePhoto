package com.zq.dynamicphoto.utils;


import android.view.View;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by Administrator on 2018/6/8.
 */

public class TitleUtils {
    public static void setTitleBar(String title, TextView tvTitle,
                            AutoRelativeLayout layoutBack,AutoRelativeLayout layoutCamera){
        tvTitle.setText(title);
        layoutBack.setVisibility(View.VISIBLE);
        layoutCamera.setVisibility(View.GONE);
    }


    public static void setTitleBar(AutoRelativeLayout layoutBack,AutoRelativeLayout layoutCamera){
        layoutBack.setVisibility(View.GONE);
        layoutCamera.setVisibility(View.VISIBLE);
    }
}
