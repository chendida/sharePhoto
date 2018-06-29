package com.zq.dynamicphoto.utils;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/6/29.
 */

public class LoadingUtils {
    private static KProgressHUD mKProgressHUD;
    public static void showLoading(Context mContext){
        mKProgressHUD = KProgressHUD.create(mContext);
        mKProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setLabel(mContext.getResources().getString(R.string.loading))
                .show();
    }

    public static void hideLoading(){
        if (mKProgressHUD != null) {
            mKProgressHUD.dismiss();
        }
    }
}
