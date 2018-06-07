package com.zq.dynamicphoto;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zq.dynamicphoto.common.Constans;
import javax.inject.Inject;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by Administrator on 2018/6/7.
 */

public class MyApplication extends Application implements HasSupportFragmentInjector, HasActivityInjector {
    private static MyApplication mInstance;

    public static IWXAPI mWxApi;
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjectorActivity;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjectorSupportFragment;

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        Utils.init(mInstance);
        ButterKnife.setDebug(true);
        registerToWX();
    }

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, Constans.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constans.APP_ID);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjectorActivity;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjectorSupportFragment;
    }
}
