package com.zq.dynamicphoto.presenter;

import android.util.Log;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.WxLoginImp;
import com.zq.dynamicphoto.view.IWxLoginView;

/**
 * Created by Administrator on 2018/6/7.
 */

public class WxLoginPresenter<T extends IWxLoginView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel baseModel = new WxLoginImp();
    //3.构造方法

    public WxLoginPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void fetch(NetRequestBean netRequestBean){
        Log.i("wxLogin","fetch");
        if (mView.get() != null){
            Log.i("wxLogin","1");
            mView.get().showLoading();
            Log.i("wxLogin","1"+(baseModel == null));
            if (baseModel != null){
                Log.i("wxLogin","2");
                baseModel.wxLogin(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        Log.i("wxLogin","3");
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showData(result);
                        }
                    }

                    @Override
                    public void onFail() {
                        Log.i("wxLogin","4");
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showFailed();
                        }
                    }
                },netRequestBean);
            }
        }
    }
}
