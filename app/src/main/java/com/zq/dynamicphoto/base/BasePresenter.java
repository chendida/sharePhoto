package com.zq.dynamicphoto.base;

import java.lang.ref.WeakReference;

/**
 * @author wong
 */
public class BasePresenter<T> {
    //1.view层的引用
    protected WeakReference<T> mView;
    //进行绑定
    public  void attachView(T view){
        mView = new WeakReference<T>(view);
    }
    //进行解绑
    public void detachView(){
        mView.clear();
    }
}