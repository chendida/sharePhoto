package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.PhotoInfoEditImp;
import com.zq.dynamicphoto.model.PhotoInfoGetImp;
import com.zq.dynamicphoto.model.WxLoginImp;
import com.zq.dynamicphoto.view.IPhotoInfoView;

/**
 * Created by Administrator on 2018/6/27.
 */

public class PhotoInfoPresenter<T extends IPhotoInfoView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel photoInfoEditImp = new PhotoInfoEditImp();
    BaseModel photoInfoGetImp = new PhotoInfoGetImp();
    //3.构造方法

    public PhotoInfoPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getPhotoInfo(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (photoInfoGetImp != null){
                photoInfoGetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetPhotoInfo(result);
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


    public void editPhotoInfo(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (photoInfoEditImp != null){
                photoInfoEditImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showEditPhotoInfo(result);
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
