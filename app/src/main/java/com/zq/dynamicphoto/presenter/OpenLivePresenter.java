package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.CreateLiveRoomImp;
import com.zq.dynamicphoto.model.GetLiveInfoImp;
import com.zq.dynamicphoto.view.IOpenLiveView;

/**
 * Created by Administrator on 2018/7/25.
 */

public class OpenLivePresenter<T extends IOpenLiveView> extends BasePresenter<T> {
    //2.model层的引用
    BaseModel createLiveRoomImp = new CreateLiveRoomImp();//创建直播间

    BaseModel getLiveInfoImp = new GetLiveInfoImp();//获取直播间信息


    //3.构造方法
    public OpenLivePresenter() {
    }

    //4.执行操作(UI逻辑)
    public void creLiveRoom(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (createLiveRoomImp != null){
                createLiveRoomImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showOpenLiveResult(result);
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


    public void getLiveRoomInfo(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getLiveInfoImp != null){
                getLiveInfoImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGetLiveInfo(result);
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
