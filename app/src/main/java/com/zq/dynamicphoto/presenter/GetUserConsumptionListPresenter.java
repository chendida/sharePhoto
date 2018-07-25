package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.GetUserConsumptionListImp;
import com.zq.dynamicphoto.model.LabelsCreateImp;
import com.zq.dynamicphoto.view.ILoadView;

/**
 * Created by Administrator on 2018/7/25.
 */

public class GetUserConsumptionListPresenter< T extends ILoadView>
        extends BasePresenter<T> {
    BaseModel getUserConsumptionListImp = new GetUserConsumptionListImp();//创建标签的model

    //3.构造方法
    public GetUserConsumptionListPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getUserConsumptionList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getUserConsumptionListImp != null){
                getUserConsumptionListImp.loadData(new BaseModel.OnLoadListener() {
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
