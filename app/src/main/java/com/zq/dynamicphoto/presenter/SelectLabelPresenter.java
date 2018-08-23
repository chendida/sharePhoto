package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DynamicStickImp;
import com.zq.dynamicphoto.model.GetLabelListImp;
import com.zq.dynamicphoto.view.ILoadView;
import com.zq.dynamicphoto.view.ISearchView;

/**
 * Created by Administrator on 2018/8/23.
 */

public class SelectLabelPresenter<T extends ILoadView> extends BasePresenter<T> {
    BaseModel getLabelListImp = new GetLabelListImp();
    //3.构造方法

    public SelectLabelPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getLabelList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getLabelListImp != null){
                getLabelListImp.loadData(new BaseModel.OnLoadListener() {
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
