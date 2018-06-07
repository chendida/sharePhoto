package com.zq.dynamicphoto.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;

import java.util.logging.Logger;

import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wong
 */
public abstract class BaseActivity<V,T extends BasePresenter<V>> extends RxAppCompatActivity implements BaseView {
    @Nullable
    @Inject
    protected T mPresenter;

    KProgressHUD mKProgressHUD;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }
        unbinder = ButterKnife.bind(this);
        attachView();
        initView();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract T createPresenter();

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isConnected()) {
            showNoNet();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        detachView();
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 贴上view
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void showLoading() {
        Log.i("mKProgressHUD","mKProgressHUD1");
        mKProgressHUD = KProgressHUD.create(this);
        Log.i("mKProgressHUD","mKProgressHUD2");
        mKProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setLabel(getResources().getString(R.string.loading))
                .show();
    }

    @Override
    public void hideLoading() {
        if (mKProgressHUD != null) {
            mKProgressHUD.dismiss();
        }
    }

    @Override
    public void showSuccess() {
    }

    @Override
    public void showFailed() {
        ToastUtils.showShort(R.string.request_api_failed);
    }

    @Override
    public void showNoNet() {
        ToastUtils.showShort(R.string.network_connect_error);
    }

    @Override
    public void onRetry() {
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
