package com.zq.dynamicphoto.view;

import com.zq.dynamicphoto.base.BaseView;
import com.zq.dynamicphoto.bean.Result;

/**
 * Created by Administrator on 2018/6/27.
 */

public interface IBindPhoneView extends BaseView {
    void showBindPhoneResult(Result result);

    void showGetAuthCodeResult(Result result);

    void showResetPwdResult(Result result);
}
