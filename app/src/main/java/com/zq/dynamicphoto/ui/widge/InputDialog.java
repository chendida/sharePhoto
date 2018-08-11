package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/5/8.
 */

public class InputDialog extends Dialog implements View.OnClickListener {
    /**
     * 上下文对象 *
     */
    Activity context;


    public EditText et_label;

    private TextView tvTitle;

    private String labelName;


    private OnItemClickListener mListener;



    public InputDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    public InputDialog(Activity context, int themeResId, OnItemClickListener mListener,
                       String labelName) {
        super(context, themeResId);
        this.context = context;
        this.mListener = mListener;
        this.labelName = labelName;
    }

    public InputDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
    }

    protected InputDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_input_dialog);
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
        findViewById(R.id.layout_save).setOnClickListener(this);
        et_label = findViewById(R.id.et_edit_label);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(labelName);
        et_label.setHint(labelName);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.layout_save:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
        }
    }


    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}