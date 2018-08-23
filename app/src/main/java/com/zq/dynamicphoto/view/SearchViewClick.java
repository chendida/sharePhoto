package com.zq.dynamicphoto.view;

import android.view.View;

import com.zq.dynamicphoto.bean.NetRequestBean;

/**
 * Created by Administrator on 2018/8/23.
 */

public interface SearchViewClick {
    void clickListener(View view, int position, NetRequestBean netRequestBean, DynamicDelete listener);
}
