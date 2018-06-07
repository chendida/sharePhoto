package com.zq.dynamicphoto.log;


import android.util.Log;

import com.orhanobut.logger.Logger;
import com.zq.dynamicphoto.BuildConfig;

import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by Administrator on 2018/2/3.
 */

public class MyLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Logger.t("SharePhoto");
        if (message != null && BuildConfig.DEBUG) {
            if (message.startsWith("{") || message.startsWith("[")) {
                Logger.json(message);
            } else {
                Logger.d(message);
            }
        }
    }
}
