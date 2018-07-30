package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.AddWatermarkPicImp;
import com.zq.dynamicphoto.model.PhoneLoginImp;
import com.zq.dynamicphoto.view.ILoadView;

/**
 * Created by Administrator on 2018/7/30.
 */

public class AddWatermarkPresenter<T extends ILoadView> extends BasePresenter<T>{
    BaseModel baseModel = new AddWatermarkPicImp();

    //4.执行操作(UI逻辑)
    public void addWatermarkPic(NetRequestBean netRequestBean){
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
}
