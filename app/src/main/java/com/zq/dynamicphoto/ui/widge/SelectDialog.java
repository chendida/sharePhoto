package com.zq.dynamicphoto.ui.widge;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/5/3.
 */

public class SelectDialog extends Dialog implements View.OnClickListener {

    private Activity mContext;
    private OnItemClickListener mListener;
    private String title;
    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }

    public SelectDialog(Activity context) {
        super(context);
        this.mContext = context;
    }

    public SelectDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public SelectDialog(Activity context, int themeResId, OnItemClickListener mListener,String msg) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = mListener;
        this.title = msg;
    }

    protected SelectDialog(Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_sure);
        //setCanceledOnTouchOutside(true);
        initView();
        getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView(){
        findViewById(R.id.layout_sure).setOnClickListener(this);
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.layout_outside).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_sure:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
            case R.id.layout_cancel:
                if(mListener != null){
                    mListener.onClick(this, 2);
                }
                break;
            case R.id.layout_outside:
                dismiss();
        }
    }
}
