package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.ui.EditWaterActivity;
import com.zq.dynamicphoto.utils.SoftKeyBoardListener;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

/**
 * Created by Administrator on 2018/7/27.
 */

public class WaterTitleDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private String content;
    private AutoRelativeLayout layoutTranslation;
    private OnItemClickListener mListener;
    private EditText etContent;
    public WaterTitleDialog(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    public WaterTitleDialog(@NonNull Activity context, int themeResId,
                            String content,OnItemClickListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.mListener = listener;
    }

    protected WaterTitleDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_water_title_setting);
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
        layoutTranslation = findViewById(R.id.layout_translation);
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.btn_edit_finish).setOnClickListener(this);
        findViewById(R.id.layout_title_size).setOnClickListener(this);
        findViewById(R.id.layout_title_color).setOnClickListener(this);
        findViewById(R.id.layout_title_align).setOnClickListener(this);
        findViewById(R.id.layout_title_stroke).setOnClickListener(this);
        findViewById(R.id.layout_title_bg_color).setOnClickListener(this);
        findViewById(R.id.check_title_bg_color).setOnClickListener(this);
        etContent = findViewById(R.id.et_content);
        etContent.setText(content);
        etContent.setSelection(etContent.getText().toString().length());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.btn_edit_finish:
                if (mListener != null){
                    mListener.onClick(this,0,etContent.getText().toString());
                    SoftUtils.hideSoftKeyboard(mContext,etContent);
                }
                break;
            case R.id.layout_title_size://字体大小，间距设置
                if (mListener != null){
                    mListener.onClick(this,1,etContent.getText().toString());
                }
                break;
            case R.id.layout_title_color://文字颜色设置
                if (mListener != null){
                    mListener.onClick(this,2,etContent.getText().toString());
                }
                break;
            case R.id.layout_title_align://文字对齐设置
                if (mListener != null){
                    mListener.onClick(this,3,etContent.getText().toString());
                }
                break;
            case R.id.layout_title_stroke://文字描边
                if (mListener != null){
                    mListener.onClick(this,4,etContent.getText().toString());
                }
                break;
            case R.id.layout_title_bg_color://文字背景
                if (mListener != null){
                    mListener.onClick(this,5,etContent.getText().toString());
                }
                break;
            case R.id.check_title_bg_color://文字背景的开关
                if (mListener != null){
                    mListener.onClick(this,6,etContent.getText().toString());
                }
                break;
            case R.id.check_title_stroke://文字描边开关
                if (mListener != null){
                    mListener.onClick(this,7,etContent.getText().toString());
                }
                break;
        }
    }

    public void showLayoutTransaliton(Boolean isShow) {
        if (isShow){
            layoutTranslation.setVisibility(View.VISIBLE);
        }else {
            layoutTranslation.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position,String content);
    }
}
