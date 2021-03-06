package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.LabelsCreateImp;
import com.zq.dynamicphoto.model.LabelsGetImp;
import com.zq.dynamicphoto.view.ILabelView;
/**
 * Created by Administrator on 2018/6/20.
 */

public class LabelsPresenter<T extends ILabelView> extends BasePresenter<T>{
    BaseModel labelsGetImp = new LabelsGetImp();//请求标签列表的model

    BaseModel labelsCreateImp = new LabelsCreateImp();//创建标签的model


    //3.构造方法
    public LabelsPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getLabelsList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (labelsGetImp != null){
                labelsGetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetLabelsResult(result);
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


    public void createLabels(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (labelsCreateImp != null){
                labelsCreateImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().createLabelsResult(result);
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
