package com.zq.dynamicphoto.model.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.User;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.HomeActivity;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 处理数据
 * Created by Administrator on 2018/6/8.
 */

public class DataUtils {
    //由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法
    private volatile static DataUtils instance;

    public static DataUtils getInstance(){
        if (instance == null) {
            synchronized (DataUtils.class) {
                if (instance == null) {
                    instance = new DataUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 处理登录成功后返回的数据
     * @param result
     */
    public void dealLoginResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            UserInfo userInfo = new Gson().fromJson(jsonObject.optString("userInfo"), UserInfo.class);
            User user = new Gson().fromJson(jsonObject.optString("user"), User.class);
            SharedPreferences sp = SharedPreferencesUtils.getInstance();
            SharedPreferences.Editor edit = sp.edit();
            if (userInfo != null){
                if (userInfo.getUserId() != 0){
                    edit.putInt(Constans.USERID,userInfo.getUserId());
                }
                if (!TextUtils.isEmpty(userInfo.getUserLogo())){
                    //对返回的图片地址中的“\”去掉
                    String userLogo = userInfo.getUserLogo().replaceAll("\"", "");
                    edit.putString(Constans.USERLOGO,userLogo);
                }
                if (!TextUtils.isEmpty(userInfo.getBgImage())){
                    edit.putString(Constans.BGIMAGE,userInfo.getBgImage());
                }
                if (!TextUtils.isEmpty(userInfo.getUrl())){
                    edit.putString(Constans.PHOTOURL,userInfo.getUrl());
                }
                if (!TextUtils.isEmpty(userInfo.getRemarkName())){
                    edit.putString(Constans.REMARKNAME,userInfo.getRemarkName());
                }
                if (userInfo.getIsVip() != null){
                    if (userInfo.getIsVip() == 1) {
                        edit.putBoolean(Constans.IS_VIP, true);
                        if (!TextUtils.isEmpty(userInfo.getVip())){
                            edit.putString(Constans.VIP,userInfo.getVip());
                        }
                    }else {
                        edit.putBoolean(Constans.IS_VIP, false);
                    }
                }
                if (user != null){
                    if (!TextUtils.isEmpty(user.getPhone())){
                        edit.putBoolean(Constans.ISBIND,true);
                    }
                    if (!TextUtils.isEmpty(user.getUnionId())){
                        edit.putString(Constans.UNIONID,user.getUnionId());
                    }
                }
                edit.putBoolean(Constans.ISLOGIN,true);
                edit.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
