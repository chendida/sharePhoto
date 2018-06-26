package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.IPermissionCancelSetImp;
import com.zq.dynamicphoto.model.IPermissionSetImp;
import com.zq.dynamicphoto.model.IPermissionStatusGetImp;
import com.zq.dynamicphoto.model.MomentDeleteImp;
import com.zq.dynamicphoto.model.MomentListGetImp;
import com.zq.dynamicphoto.view.IPermissionView;

/**
 * Created by Administrator on 2018/6/26.
 */

public class PermissionPresenter<T extends IPermissionView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel iPermissionStatusGetImp = new IPermissionStatusGetImp();//获取权限状态的model

    BaseModel iPermissionSetImp = new IPermissionSetImp();//设置权限的model

    BaseModel iPermissionCancelSetImp = new IPermissionCancelSetImp();//取消权限的model


    //3.构造方法
    public PermissionPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getPermissionStatus(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (iPermissionStatusGetImp != null){
                iPermissionStatusGetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetPermissionStatusResult(result);
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


    public void setPermission(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (iPermissionSetImp != null){
                iPermissionSetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showSetPermissionResult(result);
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


    public void setCancelPermission(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (iPermissionCancelSetImp != null){
                iPermissionCancelSetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showCancelPermissionResult(result);
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
