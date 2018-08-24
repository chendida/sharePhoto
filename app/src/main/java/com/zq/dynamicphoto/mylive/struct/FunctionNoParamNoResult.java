package com.zq.dynamicphoto.mylive.struct;

/**
 * 无参无返回值类型接口的抽象
 * Created by Administrator on 2018/4/24.
 */

public abstract class FunctionNoParamNoResult extends Function {

    public FunctionNoParamNoResult(String mFunctionName) {
        super(mFunctionName);
    }

    public abstract void function();
}
