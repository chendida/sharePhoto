package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/3/7.
 */

public class SelectPicDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private OnItemClickListener mListener;
    private String title1,title2;

    public SelectPicDialog(Activity context) {
        super(context);
        this.mContext = context;
    }


    public SelectPicDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public SelectPicDialog(String title1,String title2,Activity context, int themeResId,OnItemClickListener mListener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = mListener;
        this.title1 = title1;
        this.title2 = title2;
    }

    protected SelectPicDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_pic);
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
        findViewById(R.id.layout_pic).setOnClickListener(this);
        findViewById(R.id.layout_video).setOnClickListener(this);
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        TextView tv1 = findViewById(R.id.tv_title1);
        TextView tv2 = findViewById(R.id.tv_title2);
        tv1.setText(title1);
        tv2.setText(title2);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lay_cancel:
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.layout_pic:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
            case R.id.layout_video:
                if(mListener != null){
                    mListener.onClick(this, 2);
                }
                break;
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}
