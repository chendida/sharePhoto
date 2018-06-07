package com.zq.dynamicphoto.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by Administrator on 2018/6/7.
 */

public interface BaseView {
    /**
     * 显示进度中
     */
    void showLoading();

    /**
     * 隐藏进度
     */
    void hideLoading();

    /**
     * 显示请求成功
     */
    void showSuccess();

    /**
     * 失败重试
     */
    void showFailed();

    /**
     * 显示当前网络不可用
     */
    void showNoNet();

    /**
     * 重试
     */
    void onRetry();

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();
}
