package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;

/**
 * Created by Administrator on 2018/8/24.
 */

public interface GotoLiveRoomListener {
    void gotoLiveRoom(NewLiveRoom newLiveRoom, NetRequestBean netRequestBean);
}
