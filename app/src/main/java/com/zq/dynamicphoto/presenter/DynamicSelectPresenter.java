package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DynamicSelectImp;
import com.zq.dynamicphoto.view.IDynamicSelectView;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DynamicSelectPresenter<T extends IDynamicSelectView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel dynamicSelectImp = new DynamicSelectImp();//请求动态选择列表的model

    //3.构造方法
    public DynamicSelectPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getDynamicSelectList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (dynamicSelectImp != null){
                dynamicSelectImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showDynamicList(result);
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
