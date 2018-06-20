package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.LabelDeleteImp;
import com.zq.dynamicphoto.model.LabelEditImp;
import com.zq.dynamicphoto.model.LabelsGetImp;
import com.zq.dynamicphoto.view.ILabelManagerView;

/**
 * Created by Administrator on 2018/6/20.
 */

public class LabelsManagerPresenter<T extends ILabelManagerView> extends BasePresenter<T>{
    BaseModel labelsGetImp = new LabelsGetImp();//请求标签列表的model

    BaseModel labelsDeleteImp = new LabelDeleteImp();//删除标签的model

    BaseModel labelsEditImp = new LabelEditImp();//编辑标签的model

    //3.构造方法
    public LabelsManagerPresenter() {
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


    public void deleteLabels(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (labelsDeleteImp != null){
                labelsDeleteImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().deleteLabelResult(result);
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

    public void editLabels(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (labelsEditImp != null){
                labelsEditImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().editLabelsResult(result);
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
