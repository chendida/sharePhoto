package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.DynamicUploadImp;
import com.zq.dynamicphoto.view.IUploadDynamicView;

/**
 * 上传，编辑，转发动态表示层
 * Created by Administrator on 2018/6/21.
 */

public class DynamicUploadPresenter<T extends IUploadDynamicView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel dynamicUploadImp = new DynamicUploadImp();//上传动态的model


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
}
