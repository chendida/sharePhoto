package com.zq.dynamicphoto.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zq.dynamicphoto.R;

/**
 * 主播联系方式弹窗
 * Created by Administrator on 2018/5/3.
 */

public class AnchorContactDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;
    private OnItemClickListener mListener;
    private String wx,phone,hint;

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }

    public AnchorContactDialog(Activity context) {
        super(context);
        this.mContext = context;
    }

    public AnchorContactDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public AnchorContactDialog(Activity context, int themeResId, OnItemClickListener mListener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = mListener;
    }

    public AnchorContactDialog(Activity context, int themeResId, OnItemClickListener mListener, String wx, String phone, String hint) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = mListener;
        this.wx = wx;
        this.hint = hint;
        this.phone = phone;
    }

    protected AnchorContactDialog(Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_anchor_contact);
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
        findViewById(R.id.layout_anchor_wx).setOnClickListener(this);
        findViewById(R.id.layout_anchor_phone).setOnClickListener(this);
        findViewById(R.id.layout_outside).setOnClickListener(this);
        TextView tvWx = (TextView) findViewById(R.id.tv_anchor_wx);
        TextView tvPhone = (TextView) findViewById(R.id.tv_anchor_phone);
        TextView tvHint = (TextView) findViewById(R.id.tv_anchor_hint);
        tvWx.setText(wx);
        tvHint.setText(hint);
        tvPhone.setText(phone);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_anchor_wx:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
            case R.id.layout_anchor_phone:
                if(mListener != null){
                    mListener.onClick(this, 2);
                }
                break;
            case R.id.layout_outside:
                if (mListener != null){
                    mListener.onClick(this,3);
                }
        }
    }
}
