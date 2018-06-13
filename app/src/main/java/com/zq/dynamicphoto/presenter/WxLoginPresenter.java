package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.WxLoginImp;
import com.zq.dynamicphoto.view.ILoadView;

/**
 * Created by Administrator on 2018/6/7.
 */

public class WxLoginPresenter<T extends ILoadView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel baseModel = new WxLoginImp();
    //3.构造方法

    public WxLoginPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void fetch(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (baseModel != null){
                baseModel.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showData(result);
                        }
                    }

                    @Override
                    public void onFail() {
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
