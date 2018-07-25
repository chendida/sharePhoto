package com.zq.dynamicphoto;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMGroupTipsType;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.ext.group.TIMGroupAssistantListener;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zq.dynamicphoto.bean.Bounced;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.RefreshEvent;
import com.zq.dynamicphoto.utils.AppInit;
import com.zq.dynamicphoto.view.GroupListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.blankj.utilcode.util.ScreenUtils.getScreenDensity;

/**
 * Created by Administrator on 2018/6/7.
 */

public class MyApplication extends Application implements
        HasSupportFragmentInjector, HasActivityInjector {
    private static final String TAG = "MyApplication";
    private static MyApplication mInstance;

    public static IWXAPI mWxApi;

    private static ArrayList<Bounced> bouncedList;

    private DisplayMetrics displayMetrics = null;

    private GroupListener groupListener;

    public void setGroupListener(GroupListener groupListener) {
        this.groupListener = groupListener;
    }

    public static ArrayList<Bounced> getBouncedList() {
        return bouncedList;
    }

    public static void setBouncedList(ArrayList<Bounced> bouncedList) {
        MyApplication.bouncedList = bouncedList;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

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

    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        Utils.init(mInstance);
        ButterKnife.setDebug(true);
        registerToWX();
        initIMSDK();
        AppInit.getAppInit();
    }

    private void initIMSDK() {
        //初始化SDK基本配置
        TIMSdkConfig config = new TIMSdkConfig(Constans.SDK_APPID)
                .enableCrashReport(true)
                .enableLogPrint(true)
                .setLogLevel(TIMLogLevel.DEBUG)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/");
        //初始化SDK
        boolean init = TIMManager.getInstance().init(getApplicationContext(), config);
        Log.i(TAG,"init = " + init + Environment.getExternalStorageDirectory().getPath() + "/justfortest/");
        //将用户配置与通讯管理器进行绑定
        //设置群组事件监听器
        //基本用户配置
        TIMUserConfig userConfig = new TIMUserConfig()
                //设置用户状态变更事件监听器
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        Log.i(TAG, "onForceOffline");
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新 userSig 重新登录 SDK
                        Log.i(TAG, "onUserSigExpired");
                    }
                })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        Log.i(TAG, "onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        Log.i(TAG, "onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        Log.i(TAG, "onWifiNeedAuth");
                    }
                })
                //设置群组事件监听器
                .setGroupEventListener(new TIMGroupEventListener() {
                    @Override
                    public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                        Log.i(TAG, "onGroupTipsEvent, type: " + elem.getTipsType());
                        if (elem.getTipsType() == TIMGroupTipsType.Join){
                            Log.i(TAG,"join user = " + elem.getOpUserInfo().getNickName());
                            if (groupListener != null) {
                                groupListener.onMemberJoin("", null,elem.getOpUserInfo().getNickName());
                            }
                        }else if (elem.getTipsType() == TIMGroupTipsType.Quit){
                            Log.i(TAG,"quit user = " + elem.getOpUserInfo().getNickName());
                            if (groupListener != null) {
                                groupListener.onMemberQuit("", null,elem.getOpUserInfo().getNickName());
                            }
                        }
                    }
                });
        userConfig = new TIMUserConfigGroupExt(userConfig)
                .enableGroupStorage(true)
                .setGroupAssistantListener(new TIMGroupAssistantListener() {
                    @Override
                    public void onMemberJoin(String s, List<TIMGroupMemberInfo> list) {
                        Log.i(TAG, "onMemberJoin");
                    }
                    @Override
                    public void onMemberQuit(String s, List<String> list) {
                        Log.i(TAG, "onMemberQuit");
                    }

                    @Override
                    public void onMemberUpdate(String s, List<TIMGroupMemberInfo> list) {
                        if (groupListener != null) {
                            groupListener.onMemberUpdate(s, list);
                        }
                    }

                    @Override
                    public void onGroupAdd(TIMGroupCacheInfo timGroupCacheInfo) {
                        if (groupListener != null) {
                            groupListener.onGroupAdd(timGroupCacheInfo);
                        }
                    }

                    @Override
                    public void onGroupDelete(String s) {
                        Log.i(TAG, "onGroupDelete");
                        if (groupListener != null) {
                            Log.i(TAG, "onGroupDelete ss");
                            groupListener.onGroupDelete(s);
                        }
                    }

                    @Override
                    public void onGroupUpdate(TIMGroupCacheInfo timGroupCacheInfo) {
                        if (groupListener != null) {
                            groupListener.onGroupUpdate(timGroupCacheInfo);
                        }
                    }
                });
        TIMManager.getInstance().setUserConfig(userConfig);
        RefreshEvent.getInstance().init(userConfig);
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

    public int dp2px(float f)
    {
        return (int)(0.5F + f * getScreenDensity());
    }

    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.widthPixels;
    }

    public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }
}
