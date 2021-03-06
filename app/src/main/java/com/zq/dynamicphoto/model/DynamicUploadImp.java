package com.zq.dynamicphoto.model;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.net.util.DataManager;

import java.io.Serializable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/21.
 * 上传动态model
 */

public class DynamicUploadImp implements BaseModel,Serializable{
    @Override
    public void loadData(final OnLoadListener onLoadListener, NetRequestBean netRequestBean) {
        DataManager.getInstance().createDynamic(netRequestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (onLoadListener != null){
                            onLoadListener.onComplete(result);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (onLoadListener != null){
                            onLoadListener.onFail();
                        }
                    }
                });
    }
}
