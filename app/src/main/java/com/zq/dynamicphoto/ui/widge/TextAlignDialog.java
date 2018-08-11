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
 * Created by Administrator on 2018/8/1.
 */

public class TextAlignDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;
    private OnItemClickListener listener;
    public TextAlignDialog(@NonNull Context context) {
        super(context);
    }

    public TextAlignDialog(@NonNull Activity context, int themeResId,OnItemClickListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    protected TextAlignDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text_align);
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
        findViewById(R.id.layout_outside).setOnClickListener(this);
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.layout_text_left).setOnClickListener(this);
        findViewById(R.id.layout_text_center).setOnClickListener(this);
        findViewById(R.id.layout_text_right).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_outside:
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.layout_text_left:
                if (listener != null){
                    listener.onClick(this,0);
                }
                break;
            case R.id.layout_text_center:
                if (listener != null){
                    listener.onClick(this,1);
                }
                break;
            case R.id.layout_text_right:
                if (listener != null){
                    listener.onClick(this,2);
                }
                break;
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}
