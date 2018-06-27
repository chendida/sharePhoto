package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/3/17.
 */

public class DynamicDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }

    public DynamicDialog(Activity context) {
        super(context);
        this.mContext = context;
    }

    public DynamicDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public DynamicDialog(Activity context, int themeResId, OnItemClickListener mListener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = mListener;
    }

    protected DynamicDialog(Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_daynamic_goto);
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
        findViewById(R.id.lay_cancel).setOnClickListener(this);
        findViewById(R.id.layout_goto_photo).setOnClickListener(this);
        findViewById(R.id.layout_set_permission).setOnClickListener(this);
        findViewById(R.id.layout_complain).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lay_cancel:
                dismiss();
                break;
            case R.id.layout_goto_photo:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
            case R.id.layout_set_permission:
                if(mListener != null){
                    mListener.onClick(this, 2);
                }
                break;
            case R.id.layout_complain:
                if(mListener != null){
                    mListener.onClick(this, 3);
                }
                break;
        }
    }
}
