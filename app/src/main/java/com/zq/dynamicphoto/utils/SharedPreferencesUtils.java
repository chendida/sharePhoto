package com.zq.dynamicphoto.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zq.dynamicphoto.MyApplication;

/**
 * Created by Administrator on 2017/7/11.
 */

public class SharedPreferencesUtils {


    private static SharedPreferences instace = null;
    private static Context mContext;
    public static SharedPreferences getInstance(){
        if (instace == null){
            synchronized (SharedPreferencesUtils.class){
                mContext = MyApplication.getInstance().getApplicationContext();
                instace = mContext.getSharedPreferences("config", mContext.MODE_PRIVATE);
            }
        }
        return instace;
    }
}
