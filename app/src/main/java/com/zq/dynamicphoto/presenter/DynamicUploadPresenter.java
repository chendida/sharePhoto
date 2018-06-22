package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DynamicEditImp;
import com.zq.dynamicphoto.model.DynamicRepeatImp;
import com.zq.dynamicphoto.model.DynamicUploadImp;
import com.zq.dynamicphoto.view.IUploadDynamicView;

import java.io.Serializable;

/**
 * 上传，编辑，转发动态表示层
 * Created by Administrator on 2018/6/21.
 */

public class DynamicUploadPresenter<T extends IUploadDynamicView> extends BasePresenter<T> implements Serializable{
    //2.model层的引用
    BaseModel dynamicUploadImp = new DynamicUploadImp();//上传动态的model
    BaseModel dynamicEditImp = new DynamicEditImp();//编辑动态model;
    BaseModel dynamicRepeatImp = new DynamicRepeatImp();

    //3.构造方法
    public DynamicUploadPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void dynamicUpload(NetRequestBean netRequestBean){
        if (mView.get() != null){
            //mView.get().showLoading();
            if (dynamicUploadImp != null){
                dynamicUploadImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            //mView.get().hideLoading();
                            mView.get().showUploadDynamicResult(result);
                        }
                    }

                    @Override
                    public void onFail() {
                        if (mView != null){
                            //mView.get().hideLoading();
                            mView.get().showFailed();
                        }
                    }
                },netRequestBean);
            }
        }
    }


    public void dynamicEdit(NetRequestBean netRequestBean){
        if (mView.get() != null){
            //mView.get().showLoading();
            if (dynamicEditImp != null){
                dynamicEditImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            //mView.get().hideLoading();
                            mView.get().showEditDynamicResult(result);
                        }
                    }

                    @Override
                    public void onFail() {
                        if (mView != null){
                            //mView.get().hideLoading();
                            mView.get().showFailed();
                        }
                    }
                },netRequestBean);
            }
        }
    }


    public void dynamicRepeat(NetRequestBean netRequestBean){
        if (mView.get() != null){
            //mView.get().showLoading();
            if (dynamicRepeatImp != null){
                dynamicRepeatImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            //mView.get().hideLoading();
                            mView.get().showRepeatDynamicResult(result);
                        }
                    }

                    @Override
                    public void onFail() {
                        if (mView != null){
                            //mView.get().hideLoading();
                            mView.get().showFailed();
                        }
                    }
                },netRequestBean);
            }
        }
    }
}
