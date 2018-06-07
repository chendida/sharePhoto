package com.zq.dynamicphoto.net.util;

import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.net.RetrofitApiService;

import io.reactivex.Observable;

/**
 *该类用来管理RetrofitApiService中对应的各种API接口，
 *当做Retrofit和presenter中的桥梁，Activity就不用直接和retrofit打交道了
 * Created by Administrator on 2018/5/24.
 */

public class DataManager {
    private RetrofitApiService mRetrofitService;
    private volatile static DataManager instance;

    private DataManager(){
        this.mRetrofitService = RetrofitUtil.getInstance().getRetrofitApiService();
    }
    //由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法
    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    //将retrofit的业务方法映射到DataManager中，以后统一用该类来调用业务方法
    //以后再retrofit中增加业务方法的时候，相应的这里也要添加，比如添加一个getOrder
    public Observable<Result> getPhotoDynamic(NetRequestBean netRequestBean){
        return mRetrofitService.getPhotoDynamic(netRequestBean);
    }

    public Observable<Result> wxLogin(NetRequestBean netRequestBean){
        return mRetrofitService.wxLogin(netRequestBean);
    }
}
