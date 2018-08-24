package com.zq.dynamicphoto.mylive.struct;

/**
 * 抽象接口，所有接口都有函数名，作为基类
 * Created by Administrator on 2018/4/24.
 */

public abstract class Function {
    public String mFunctionName;

    public Function(String mFunctionName) {
        this.mFunctionName = mFunctionName;
    }
}
