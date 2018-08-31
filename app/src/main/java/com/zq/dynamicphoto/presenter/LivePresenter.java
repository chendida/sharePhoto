package com.zq.dynamicphoto.presenter;

import com.zq.dynamicphoto.base.BaseModel;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.model.CloseLiveRoomImp;
import com.zq.dynamicphoto.model.LiveHeartImp;
import com.zq.dynamicphoto.model.UpvoteAnchorImp;
import com.zq.dynamicphoto.view.LiveView;

/**
 * Created by Administrator on 2018/8/24.
 */

public class LivePresenter<T extends LiveView> extends BasePresenter<T> {
    BaseModel closeLiveRoomImp = new CloseLiveRoomImp();//关闭直播间

    BaseModel upvoteAnchorImp = new UpvoteAnchorImp();//给主播点赞

    BaseModel liveHeartImp = new LiveHeartImp();//心跳检测


    //3.构造方法
    public LivePresenter() {
    }

    //4.执行操作(UI逻辑)
    public void liveHeart(NetRequestBean netRequestBean){
        if (mView.get() != null){
            //mView.get().showLoading();
            if (liveHeartImp != null){
                liveHeartImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            //mView.get().hideLoading();
                            mView.get().showLiveHeartResult(result);
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


    public void upvote(NetRequestBean netRequestBean){
        if (mView.get() != null){
            mView.get().showLoading();
            if (upvoteAnchorImp != null){
                upvoteAnchorImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            mView.get().hideLoading();
                            mView.get().showUpvoteResult(result);
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

    public void closeLiveRoom(NetRequestBean netRequestBean){
        if (mView.get() != null){
            //mView.get().showLoading();
            if (closeLiveRoomImp != null){
                closeLiveRoomImp.loadData(new BaseModel.OnLoadListener() {
                    @Override
                    public void onComplete(Result result) {
                        if (mView != null){
                            //mView.get().hideLoading();
                            if(mView.get() != null) {
                                mView.get().showCloseRoomResult(result);
                            }
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
