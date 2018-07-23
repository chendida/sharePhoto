package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/7/20.
 */

public class WaterMouldSelectDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private OnItemClickListener mListener;

    public WaterMouldSelectDialog(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    public WaterMouldSelectDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public WaterMouldSelectDialog(@NonNull Activity context, int themeResId,OnItemClickListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
    }

    protected WaterMouldSelectDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_water_mould);
        setCanceledOnTouchOutside(false);
        initView();
        mContext.getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView(){
        findViewById(R.id.lay_cancel).setOnClickListener(this);
        findViewById(R.id.layout_water_mould).setOnClickListener(this);
        findViewById(R.id.layout_money_water_design).setOnClickListener(this);
        findViewById(R.id.layout_photo_upload).setOnClickListener(this);
        findViewById(R.id.layout_outside).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_outside:
                dismiss();
                break;
            case R.id.lay_cancel:
                dismiss();
                break;
            case R.id.layout_water_mould:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
            case R.id.layout_money_water_design:
                if(mListener != null){
                    mListener.onClick(this, 2);
                }
                break;
            case R.id.layout_photo_upload:
                if(mListener != null){
                    mListener.onClick(this, 3);
                }
                break;
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}
