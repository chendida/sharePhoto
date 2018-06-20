package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/20.
 */

public interface ILabelView extends BaseView{
    void showGetLabelsResult(Result result);

    void createLabelsResult(Result result);
}
