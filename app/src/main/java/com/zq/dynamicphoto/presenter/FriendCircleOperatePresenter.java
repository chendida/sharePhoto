package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.EditMomentsImp;
import com.zq.dynamicphoto.model.MomentsDatailsGetImp;
import com.zq.dynamicphoto.model.UploadMomentsImp;
import com.zq.dynamicphoto.view.IUploadMomentsView;

/**
 * Created by Administrator on 2018/6/25.
 */

public class FriendCircleOperatePresenter<T extends IUploadMomentsView> extends BasePresenter<T>{
    //2.model层的引用
    BaseModel uploadMomentsImp = new UploadMomentsImp();//新增朋友圈的model

    BaseModel editMomentsImp = new EditMomentsImp();//编辑朋友圈的model

    BaseModel momentsDatailsGetImp = new MomentsDatailsGetImp();//获取朋友圈详情

    //3.构造方法
    public FriendCircleOperatePresenter() {
    }

    //4.执行操作(UI逻辑)
    public void uploadMoments(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (uploadMomentsImp != null){
                uploadMomentsImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showUploadMomentsResult(result);
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


    public void editMoments(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (editMomentsImp != null){
                editMomentsImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showEditMomentsResult(result);
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


    public void getMomentsDetails(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (momentsDatailsGetImp != null){
                momentsDatailsGetImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetMomentsDetailsResult(result);
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
