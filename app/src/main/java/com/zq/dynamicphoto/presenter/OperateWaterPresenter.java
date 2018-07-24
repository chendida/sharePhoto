package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DeleteWaterMouldImp;
import com.zq.dynamicphoto.model.GetAddWaterMouldListImp;
import com.zq.dynamicphoto.view.IOperateWaterView;

/**
 * Created by Administrator on 2018/7/23.
 */

public class OperateWaterPresenter<T extends IOperateWaterView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel getAddWaterMouldListImp = new GetAddWaterMouldListImp();//获取添加过的水印列表

    BaseModel deleteWaterMouldImp = new DeleteWaterMouldImp();//删除添加过的水印的model


    //3.构造方法
    public OperateWaterPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getAddWaterMouldList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getAddWaterMouldListImp != null){
                getAddWaterMouldListImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showAddWaterMouldList(result);
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


    public void deleteWaterMould(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (deleteWaterMouldImp != null){
                deleteWaterMouldImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().deleteWaterMould(result);
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
