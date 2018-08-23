package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DynamicDeleteImp;
import com.zq.dynamicphoto.model.DynamicLoadImp;
import com.zq.dynamicphoto.model.DynamicStickImp;
import com.zq.dynamicphoto.model.UploadBgImp;
import com.zq.dynamicphoto.view.IDynamicView;
import com.zq.dynamicphoto.view.ILoadView;

/**
 * Created by Administrator on 2018/6/11.
 */

public class DynamicLoadPresenter<T extends IDynamicView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel baseModel = new DynamicLoadImp();//请求动态列表的model

    BaseModel deleteDynamicModel = new DynamicDeleteImp();//删除动态的model

    BaseModel stickDynamicModel = new DynamicStickImp();//置顶动态的model

    BaseModel uploadBgImp = new UploadBgImp();//修改背景图片

    //3.构造方法
    public DynamicLoadPresenter() {
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


    public void fetchDeleteDynamic(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (deleteDynamicModel != null){
                deleteDynamicModel.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showDeleteResult(result);
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


    public void fetchStickDynamic(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (stickDynamicModel != null){
                stickDynamicModel.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showStickResult(result);
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

    public void updateBg(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (uploadBgImp != null){
                uploadBgImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showUpdateBgResult(result);
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
