package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.zq.dynamicphoto.R;

/**
 * 相册动态引导页
 * Created by Administrator on 2018/4/12.
 */

public class DynamicLeadDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }

    public DynamicLeadDialog(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    public DynamicLeadDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public DynamicLeadDialog(Activity context, int themeResId, OnItemClickListener mListener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = mListener;
    }

    protected DynamicLeadDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dynamic_lead);
        setCanceledOnTouchOutside(false);
        initView();
        getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView(){
        findViewById(R.id.btn_i_know).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_i_know:
                dismiss();
                break;
        }
    }
}
