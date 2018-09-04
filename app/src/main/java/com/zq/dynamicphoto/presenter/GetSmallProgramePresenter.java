package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.GetSmallProgrameImp;
import com.zq.dynamicphoto.view.ILoadView;

/**
 * Created by Administrator on 2018/8/30.
 */

public class GetSmallProgramePresenter<T extends ILoadView> extends BasePresenter<T> {
    BaseModel getSmallProgrameImp = new GetSmallProgrameImp();//获取小程序码图片


    //3.构造方法
    public GetSmallProgramePresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getSmallPrograme(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getSmallProgrameImp != null){
                getSmallProgrameImp.loadData(new BaseModel.OnLoadListener() {
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