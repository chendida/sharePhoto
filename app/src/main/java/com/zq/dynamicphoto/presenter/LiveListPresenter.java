package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.GetLiveListImp;
import com.zq.dynamicphoto.model.GotoLiveRoomImp;
import com.zq.dynamicphoto.view.ILiveListView;

/**
 * 直播间列表表示层
 * Created by Administrator on 2018/8/24.
 */

public class LiveListPresenter<T extends ILiveListView> extends BasePresenter<T> {
    BaseModel getLiveListImp = new GetLiveListImp();//直播间列表

    BaseModel gotoLiveRoomImp = new GotoLiveRoomImp();//进入直播间


    //3.构造方法
    public LiveListPresenter() {
    }

    //4.执行操作(UI逻辑)
    public void getLiveListRoom(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (getLiveListImp != null){
                getLiveListImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showLiveListRoomResult(result);
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


    public void gotoLiveRoom(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (gotoLiveRoomImp != null){
                gotoLiveRoomImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showGotoLiveRoomResult(result);
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
