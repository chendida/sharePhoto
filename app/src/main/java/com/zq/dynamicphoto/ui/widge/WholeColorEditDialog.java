package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/7/26.
 */

public class WholeColorEditDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;
    private OnItemClickListener mListener;

    public WholeColorEditDialog(@NonNull  Activity context) {
        super(context);
        this.mContext = context;
    }

    public WholeColorEditDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public WholeColorEditDialog(@NonNull Activity context, int themeResId, OnItemClickListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
    }

    protected WholeColorEditDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_whole_water_color_edit);
        setCanceledOnTouchOutside(false);
        initView();
        getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView() {
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.iv_water_color000).setOnClickListener(this);
        findViewById(R.id.iv_water_color001).setOnClickListener(this);
        findViewById(R.id.iv_water_color002).setOnClickListener(this);
        findViewById(R.id.iv_water_color003).setOnClickListener(this);
        findViewById(R.id.iv_water_color004).setOnClickListener(this);
        findViewById(R.id.iv_water_color005).setOnClickListener(this);
        findViewById(R.id.iv_water_color006).setOnClickListener(this);
        findViewById(R.id.iv_water_color007).setOnClickListener(this);
        findViewById(R.id.iv_water_color008).setOnClickListener(this);
        findViewById(R.id.iv_water_color009).setOnClickListener(this);
        findViewById(R.id.iv_water_color010).setOnClickListener(this);
        findViewById(R.id.iv_water_color011).setOnClickListener(this);
        findViewById(R.id.iv_water_color012).setOnClickListener(this);
        findViewById(R.id.iv_water_color013).setOnClickListener(this);
        findViewById(R.id.iv_water_color014).setOnClickListener(this);
        findViewById(R.id.iv_water_color015).setOnClickListener(this);
        findViewById(R.id.iv_water_color016).setOnClickListener(this);
        findViewById(R.id.iv_water_color017).setOnClickListener(this);
        findViewById(R.id.iv_water_color018).setOnClickListener(this);
        findViewById(R.id.iv_water_color019).setOnClickListener(this);
        findViewById(R.id.iv_water_color020).setOnClickListener(this);
        findViewById(R.id.iv_water_color021).setOnClickListener(this);
        findViewById(R.id.iv_water_color022).setOnClickListener(this);
        findViewById(R.id.iv_water_color023).setOnClickListener(this);
        findViewById(R.id.iv_water_color024).setOnClickListener(this);
        findViewById(R.id.iv_water_color025).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.iv_water_color000:
                Log.i("color","iv_water_color000");
                if(mListener != null){
                    Log.i("color","iv_water_color0000");
                    mListener.onClick(this, 1);
                }
                break;
            case R.id.iv_water_color001:
                if(mListener != null){
                    mListener.onClick(this, 2);
                }
                break;
            case R.id.iv_water_color002:
                if(mListener != null){
                    mListener.onClick(this, 3);
                }
                break;
            case R.id.iv_water_color003:
                if(mListener != null){
                    mListener.onClick(this, 4);
                }
                break;
            case R.id.iv_water_color004:
                if(mListener != null){
                    mListener.onClick(this, 5);
                }
                break;
            case R.id.iv_water_color005:
                if(mListener != null){
                    mListener.onClick(this, 6);
                }
                break;
            case R.id.iv_water_color006:
                if(mListener != null){
                    mListener.onClick(this, 7);
                }
                break;
            case R.id.iv_water_color007:
                if(mListener != null){
                    mListener.onClick(this, 8);
                }
                break;
            case R.id.iv_water_color008:
                if(mListener != null){
                    mListener.onClick(this, 9);
                }
                break;
            case R.id.iv_water_color009:
                if(mListener != null){
                    mListener.onClick(this, 10);
                }
                break;
            case R.id.iv_water_color010:
                if(mListener != null){
                    mListener.onClick(this, 11);
                }
                break;
            case R.id.iv_water_color011:
                if(mListener != null){
                    mListener.onClick(this, 12);
                }
                break;
            case R.id.iv_water_color012:
                if(mListener != null){
                    mListener.onClick(this, 13);
                }
                break;
            case R.id.iv_water_color013:
                if(mListener != null){
                    mListener.onClick(this, 14);
                }
                break;
            case R.id.iv_water_color014:
                if(mListener != null){
                    mListener.onClick(this, 15);
                }
                break;
            case R.id.iv_water_color015:
                if(mListener != null){
                    mListener.onClick(this, 16);
                }
                break;
            case R.id.iv_water_color016:
                if(mListener != null){
                    mListener.onClick(this, 17);
                }
                break;
            case R.id.iv_water_color017:
                if(mListener != null){
                    mListener.onClick(this, 18);
                }
                break;
            case R.id.iv_water_color018:
                if(mListener != null){
                    mListener.onClick(this, 19);
                }
                break;
            case R.id.iv_water_color019:
                if(mListener != null){
                    mListener.onClick(this, 20);
                }
                break;
            case R.id.iv_water_color020:
                if(mListener != null){
                    mListener.onClick(this, 21);
                }
                break;
            case R.id.iv_water_color021:
                if(mListener != null){
                    mListener.onClick(this, 22);
                }
                break;
            case R.id.iv_water_color022:
                if(mListener != null){
                    mListener.onClick(this, 23);
                }
                break;
            case R.id.iv_water_color023:
                if(mListener != null){
                    mListener.onClick(this, 24);
                }
                break;
            case R.id.iv_water_color024:
                if(mListener != null){
                    mListener.onClick(this, 25);
                }
                break;
            case R.id.iv_water_color025:
                if(mListener != null){
                    mListener.onClick(this, 26);
                }
                break;
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}
