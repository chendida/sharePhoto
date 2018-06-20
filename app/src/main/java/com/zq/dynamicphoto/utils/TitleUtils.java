package com.zq.dynamicphoto.utils;


import android.view.View;
import android.widget.ImageView;
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


    public static void setTitleBar(String title, TextView tvTitle,
                                   AutoRelativeLayout layoutBack,
                                   ImageView ivCamera, TextView tvFinish){
        tvTitle.setText(title);
        layoutBack.setVisibility(View.VISIBLE);
        ivCamera.setVisibility(View.GONE);
        tvFinish.setVisibility(View.VISIBLE);
        tvFinish.setText("完成");
    }
}
