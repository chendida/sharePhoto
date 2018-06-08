package com.zq.dynamicphoto.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * 界面跳转工具类
 * Created by Administrator on 2018/6/8.
 */

public class MFGT {
    public static void startActivity(Activity activity, Class cls){
        activity.startActivity(new Intent(activity,cls));
    }
}
