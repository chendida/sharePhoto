package com.zq.dynamicphoto.mylive.struct;

/**
 * 只有参数接口的抽象
 * Created by Administrator on 2018/4/24.
 */

public abstract class FunctionWithParamOnly<Param> extends Function {
    public FunctionWithParamOnly(String mFunctionName) {
        super(mFunctionName);
    }

    public abstract void function(Param param);
}
