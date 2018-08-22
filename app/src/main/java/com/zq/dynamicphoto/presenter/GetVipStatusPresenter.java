package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.GetVipStatusImp;
import com.zq.dynamicphoto.model.LabelsCreateImp;
import com.zq.dynamicphoto.model.LabelsGetImp;
import com.zq.dynamicphoto.view.ILabelManagerView;
import com.zq.dynamicphoto.view.ILoadView;

/**
 * Created by Administrator on 2018/8/22.
 */

public class GetVipStatusPresenter<T extends ILoadView> extends BasePresenter<T> {
    BaseModel getVipStatusImp = new GetVipStatusImp();//获取当前vip状态



    //3.构造方法
    public GetVipStatusPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getVipStatus(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getVipStatusImp != null){
                getVipStatusImp.loadData(new BaseModel.OnLoadListener() {
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
