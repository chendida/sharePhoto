package com.zq.dynamicphoto.mylive.struct;

/**
 * 有参数有返回值接口的抽象
 * Created by Administrator on 2018/4/24.
 */

public abstract class FunctionWithParamAndResult<Result,Param> extends Function {
    public FunctionWithParamAndResult(String mFunctionName) {
        super(mFunctionName);
    }

    public abstract Result function(Param param);
}
