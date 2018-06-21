package com.zq.dynamicphoto.bean;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

/**
 * Created by Administrator on 2018/6/20.
 */

public class UploadDynamicJob<T> extends Job {
    private static final int PRIORITY = 1;
    private NetRequestBean netRequestBean;
    private T mPresenter;

    public UploadDynamicJob(Params params, NetRequestBean netRequestBean, T mPresenter) {
        super(params);
        this.netRequestBean = netRequestBean;
        this.mPresenter = mPresenter;
    }

    protected UploadDynamicJob(Params params) {
        super(new Params(PRIORITY).persist());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
