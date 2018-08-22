package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Image;

/**
 * Created by Administrator on 2018/8/22.
 */

public class VipHintDialog extends Dialog implements View.OnClickListener {

    /**
     * 上下文对象 *
     */
    Activity context;

    private OnItemClickListener mListener;


    public VipHintDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    public VipHintDialog(Activity context, int themeResId, OnItemClickListener mListener) {
        super(context, themeResId);
        this.context = context;
        this.mListener = mListener;
    }

    public VipHintDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
    }

    protected VipHintDialog(@NonNull Activity context, boolean cancelable,
                             @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_hint_dialog);
        setCanceledOnTouchOutside(false);
        initView();
        getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = context.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView() {
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.layout_sure).setOnClickListener(this);
        findViewById(R.id.layout_close).setOnClickListener(this);
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setColorFilter(context.getResources().getColor(R.color.tv_text_color1));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_close:
                dismiss();
                break;
            case R.id.layout_cancel:
                if (mListener != null) {
                    mListener.onClick(1,this);
                }
                break;
            case R.id.layout_sure:
                if (mListener != null) {
                    mListener.onClick(2,this);
                }
                break;
        }
    }


    public interface OnItemClickListener {
        void onClick(int position,Dialog dialog);
    }
}
