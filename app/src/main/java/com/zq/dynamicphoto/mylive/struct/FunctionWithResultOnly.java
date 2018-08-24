package com.zq.dynamicphoto.mylive.struct;

/**
 * 只有返回值接口的抽象
 * Created by Administrator on 2018/4/24.
 */

public abstract class FunctionWithResultOnly<Result> extends Function {
    public FunctionWithResultOnly(String mFunctionName) {
        super(mFunctionName);
    }

    public abstract Result function();
}
