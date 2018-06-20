package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.content.Intent;

import com.zq.dynamicphoto.ui.AddLabelActivity;
import com.zq.dynamicphoto.ui.AddPicActivity;
import com.zq.dynamicphoto.ui.LabelManagerActivity;

/**
 * 界面跳转工具类
 * Created by Administrator on 2018/6/8.
 */

public class MFGT {
    public static void startActivity(Activity activity, Class cls){
        activity.startActivity(new Intent(activity,cls));
    }

    public static void gotoAddPicActivity(Activity activity){
        activity.startActivity(new Intent(activity, AddPicActivity.class));
    }

    public static void gotoAddLabelActivity(Activity activity){
        activity.startActivity(new Intent(activity, AddLabelActivity.class));
    }

    public static void gotoLabelManagerActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LabelManagerActivity.class));
    }
}
