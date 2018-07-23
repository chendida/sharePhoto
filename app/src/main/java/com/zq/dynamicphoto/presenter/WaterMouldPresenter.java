package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.GetAllWaterMouldImp;
import com.zq.dynamicphoto.model.GetSingleWaterMouldImp;
import com.zq.dynamicphoto.view.IGetWaterMouldView;
import com.zq.dynamicphoto.view.ILoadView;

/**
 * Created by Administrator on 2018/7/20.
 */

public class WaterMouldPresenter<T extends IGetWaterMouldView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel baseModel = new GetAllWaterMouldImp();

    BaseModel singleWaterMouldImp = new GetSingleWaterMouldImp();
    //3.构造方法

    public WaterMouldPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getAllWaterList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (baseModel != null){
                baseModel.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().getAllWaterMould(result);
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

    //4.执行操作(UI逻辑)
    public void getSingleWaterList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (singleWaterMouldImp != null){
                singleWaterMouldImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().getSingleWaterMould(result);
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
