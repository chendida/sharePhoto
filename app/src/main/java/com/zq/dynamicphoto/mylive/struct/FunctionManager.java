package com.zq.dynamicphoto.mylive.struct;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * 接口管理类
 * Created by Administrator on 2018/4/24.
 */

public class FunctionManager {
    private static FunctionManager _instance;

    public FunctionManager() {
        mFunctionNoParamNoResult = new HashMap<>();
        mFunctionWithParamOnly = new HashMap<>();
        mFunctionWithResultOnly = new HashMap<>();
        mFunctionWithParamAndResult = new HashMap<>();
    }

    public static FunctionManager getInstance(){
        if (_instance == null){
            _instance = new FunctionManager();
        }
        return _instance;
    }

    /**
     * 用四个容器来装四种类型的抽象接口
     */
    private HashMap<String,FunctionNoParamNoResult> mFunctionNoParamNoResult;
    private HashMap<String,FunctionWithParamOnly> mFunctionWithParamOnly;
    private HashMap<String,FunctionWithResultOnly> mFunctionWithResultOnly;
    private HashMap<String,FunctionWithParamAndResult> mFunctionWithParamAndResult;

    /**
     * 添加无参无返回值接口
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionNoParamNoResult function){
        mFunctionNoParamNoResult.put(function.mFunctionName,function);
        return this;
    }

    /**
     * 调用无参无返回值接口
     * @param functionName
     */
    public void invokeFuction(String functionName){
        if (TextUtils.isEmpty(functionName)){
            return;
        }
        if (mFunctionNoParamNoResult != null){
            FunctionNoParamNoResult f = mFunctionNoParamNoResult.get(functionName);
            if (f != null){
                f.function();
            }else {
                try {
                    throw new FunctionException("has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加无参有返回值接口
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithResultOnly function){
        mFunctionWithResultOnly.put(function.mFunctionName,function);
        return this;
    }

    /**
     * 调用无参有返回值的接口
     * @param functionName
     * @param c
     * @param <Result>
     * @return
     */
    public <Result> Result invokeFuction(String functionName, Class<Result> c){
        if (TextUtils.isEmpty(functionName)){
            return null;
        }
        if (mFunctionWithResultOnly != null){
            FunctionWithResultOnly f = mFunctionWithResultOnly.get(functionName);
            if (f != null){
                if (c != null){
                    return c.cast(f.function());
                }else {
                    return (Result) f.function();
                }
            }else {
                try {
                    throw new FunctionException("has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 添加有参有返回值的接口
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamAndResult function){
        mFunctionWithParamAndResult.put(function.mFunctionName,function);
        return this;
    }

    /**
     * 调用有参有返回值接口
     * @param functionName
     * @param data
     * @param c
     * @param <Result>
     * @param <Param>
     * @return
     */
    public <Result,Param> Result invokeFuction(String functionName, Param data, Class<Result> c){
        if (TextUtils.isEmpty(functionName)){
            return null;
        }
        if (mFunctionWithParamAndResult != null){
            FunctionWithParamAndResult f = mFunctionWithParamAndResult.get(functionName);
            if (f != null){
                if (c != null){
                    return c.cast(f.function(data));
                }else {
                    return (Result) f.function(data);
                }
            }else {
                try {
                    throw new FunctionException("has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 添加有参无返回值接口
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamOnly function){
        mFunctionWithParamOnly.put(function.mFunctionName,function);
        return this;
    }

    /**
     * 调用有参无返回值接口
     * @param functionName
     * @param data
     * @param <Param>
     */
    public <Param> void invokeFuction(String functionName, Param data){
        if (TextUtils.isEmpty(functionName)){
            return;
        }
        if (mFunctionWithParamAndResult != null){
            FunctionWithParamAndResult f = mFunctionWithParamAndResult.get(functionName);
            if (f != null){
                f.function(data);
            }else {
                try {
                    throw new FunctionException("has no this function" + functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
