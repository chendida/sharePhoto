package com.zq.dynamicphoto.utils;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.bean.Bounced;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.net.RetrofitApiService;
import com.zq.dynamicphoto.net.util.RetrofitUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/6/22.
 */

public class AppInit {
    /**
     * 初始化
     */
    public static void getAppInit() {
        //准备数据
        final DeviceProperties dr = DrUtils.getInstance();
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);

        //获取api接口对象进行网络请求
        RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getRetrofitApiService();

        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<Result> call = retrofitApiService.initApp(netRequestBean);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result != null){
                    dealWithResult(result);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            }
        });
    }

    private static void dealWithResult(Result result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.getData());
            String forwordUrl = (String) jsonObject.opt(Constans.FORWORDURL);
            String agreement = (String) jsonObject.opt(Constans.AGREEMENT);
            ArrayList<Bounced> bouncedList = new Gson().fromJson(jsonObject.optString("bouncedList"), new TypeToken<List<Bounced>>() {
            }.getType());
            SharedPreferences sp = SharedPreferencesUtils.getInstance();
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(Constans.FORWORDURL,forwordUrl);
            edit.putString(Constans.AGREEMENT,agreement);
            edit.commit();
            if (bouncedList != null){
                if (bouncedList.size() != 0){
                    MyApplication.setBouncedList(bouncedList);
                }else {
                    MyApplication.setBouncedList(null);
                }
            }else {
                MyApplication.setBouncedList(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
