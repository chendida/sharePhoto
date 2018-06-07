package com.zq.dynamicphoto.net.util;


import com.google.gson.GsonBuilder;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.log.MyLogger;
import com.zq.dynamicphoto.net.RetrofitApiService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/5/24.
 */

public class RetrofitUtil {
    //声明Retrofit对象
    private Retrofit mRetrofit;

    //声明RetrofitApiService对象
    private RetrofitApiService mRetrofitApiService;

    OkHttpClient.Builder client = new OkHttpClient.Builder();
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new MyLogger()).setLevel(HttpLoggingInterceptor.Level.BODY);
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());

    //由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法
    private volatile static RetrofitUtil instance;

    public static RetrofitUtil getInstance(){
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }
    private RetrofitUtil(){
        init();
    }

    //初始化Retrofit
    private void init() {
        //设置请求超时时间
        client.readTimeout(30,TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .addInterceptor(logging);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constans.Base_Url)
                .client(client.build())
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mRetrofitApiService = mRetrofit.create(RetrofitApiService.class);
    }

    public RetrofitApiService getRetrofitApiService(){
        return mRetrofitApiService;
    }
}
