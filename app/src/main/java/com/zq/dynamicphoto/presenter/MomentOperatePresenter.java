package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.MomentDeleteImp;
import com.zq.dynamicphoto.model.MomentListGetImp;
import com.zq.dynamicphoto.view.IFriendCircleView;

/**
 * Created by Administrator on 2018/6/22.
 */

public class MomentOperatePresenter<T extends IFriendCircleView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel momentListGetImp = new MomentListGetImp();//请求朋友圈列表的model

    BaseModel momentDeleteImp = new MomentDeleteImp();//删除朋友圈的model


    //3.构造方法
    public MomentOperatePresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getMomentList(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (momentListGetImp != null){
                momentListGetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().getMomentListResult(result);
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


    public void deleteMoment(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (momentDeleteImp != null){
                momentDeleteImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().deleteMomentResult(result);
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
