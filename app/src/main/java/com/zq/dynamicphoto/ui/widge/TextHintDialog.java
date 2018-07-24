package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/5/16.
 */

public class TextHintDialog extends Dialog implements View.OnClickListener {

    /**
     * 上下文对象 *
     */
    Activity context;
    private String title;
    private TextView tvTitle;

    private OnItemClickListener mListener;



    public TextHintDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    public TextHintDialog(Activity context, int themeResId, OnItemClickListener mListener, String title) {
        super(context, themeResId);
        this.context = context;
        this.mListener = mListener;
        this.title = title;
    }

    public TextHintDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
    }

    protected TextHintDialog(@NonNull Activity context, boolean cancelable,
                             @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_hint_dialog);
        setCanceledOnTouchOutside(false);
        initView();
        getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = context.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView(){
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.layout_sure).setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.layout_sure:
                if(mListener != null){
                    mListener.onClick(this);
                }
                break;
        }
    }


    public interface OnItemClickListener {
        void onClick(Dialog dialog);
    }
}
