package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/27.
 */

public interface IPhotoInfoView extends BaseView {
    void  showGetPhotoInfo(Result result);

    void showEditPhotoInfo(Result result);
}
