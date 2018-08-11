package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

/**
 * Created by Administrator on 2018/8/1.
 */

public class TextColorDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private WatermarkSeekBarListener listener;
    private OnItemClickListener mTextColorListener;
    public TextColorDialog(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    public TextColorDialog(@NonNull Activity context, int themeResId,
                           WatermarkSeekBarListener listener,OnItemClickListener colorListener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
        this.mTextColorListener = colorListener;
    }

    protected TextColorDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_water_text_color_setting);
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
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.iv_water_color000).setOnClickListener(this);
        findViewById(R.id.iv_water_color001).setOnClickListener(this);
        findViewById(R.id.iv_water_color002).setOnClickListener(this);
        findViewById(R.id.iv_water_color003).setOnClickListener(this);
        findViewById(R.id.iv_water_color004).setOnClickListener(this);
        findViewById(R.id.iv_water_color005).setOnClickListener(this);
        findViewById(R.id.iv_water_color006).setOnClickListener(this);
        findViewById(R.id.iv_water_color007).setOnClickListener(this);
        findViewById(R.id.iv_water_color008).setOnClickListener(this);
        findViewById(R.id.iv_water_color009).setOnClickListener(this);
        findViewById(R.id.iv_water_color010).setOnClickListener(this);
        findViewById(R.id.iv_water_color011).setOnClickListener(this);
        findViewById(R.id.iv_water_color012).setOnClickListener(this);
        findViewById(R.id.iv_water_color013).setOnClickListener(this);
        findViewById(R.id.iv_water_color014).setOnClickListener(this);
        findViewById(R.id.iv_water_color015).setOnClickListener(this);
        findViewById(R.id.iv_water_color016).setOnClickListener(this);
        findViewById(R.id.iv_water_color017).setOnClickListener(this);
        findViewById(R.id.iv_water_color018).setOnClickListener(this);
        findViewById(R.id.iv_water_color019).setOnClickListener(this);
        findViewById(R.id.iv_water_color020).setOnClickListener(this);
        findViewById(R.id.iv_water_color021).setOnClickListener(this);
        findViewById(R.id.iv_water_color022).setOnClickListener(this);
        findViewById(R.id.iv_water_color023).setOnClickListener(this);
        findViewById(R.id.iv_water_color024).setOnClickListener(this);
        findViewById(R.id.iv_water_color025).setOnClickListener(this);
        SeekBar seekBarAlpha = findViewById(R.id.seek_bar_alpha);
        seekBarAlpha.setMax(255);
        seekBarAlpha.setProgress(255);
        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (listener != null){
                    listener.onTextAlpha(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.iv_water_color000:
                Log.i("color","iv_water_color000");
                if(mTextColorListener != null){
                    Log.i("color","iv_water_color0000");
                    mTextColorListener.onClick(this, 1);
                }
                break;
            case R.id.iv_water_color001:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 2);
                }
                break;
            case R.id.iv_water_color002:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 3);
                }
                break;
            case R.id.iv_water_color003:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 4);
                }
                break;
            case R.id.iv_water_color004:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 5);
                }
                break;
            case R.id.iv_water_color005:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 6);
                }
                break;
            case R.id.iv_water_color006:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 7);
                }
                break;
            case R.id.iv_water_color007:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 8);
                }
                break;
            case R.id.iv_water_color008:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 9);
                }
                break;
            case R.id.iv_water_color009:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 10);
                }
                break;
            case R.id.iv_water_color010:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 11);
                }
                break;
            case R.id.iv_water_color011:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 12);
                }
                break;
            case R.id.iv_water_color012:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 13);
                }
                break;
            case R.id.iv_water_color013:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 14);
                }
                break;
            case R.id.iv_water_color014:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 15);
                }
                break;
            case R.id.iv_water_color015:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 16);
                }
                break;
            case R.id.iv_water_color016:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 17);
                }
                break;
            case R.id.iv_water_color017:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 18);
                }
                break;
            case R.id.iv_water_color018:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 19);
                }
                break;
            case R.id.iv_water_color019:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 20);
                }
                break;
            case R.id.iv_water_color020:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 21);
                }
                break;
            case R.id.iv_water_color021:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 22);
                }
                break;
            case R.id.iv_water_color022:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 23);
                }
                break;
            case R.id.iv_water_color023:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 24);
                }
                break;
            case R.id.iv_water_color024:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 25);
                }
                break;
            case R.id.iv_water_color025:
                if(mTextColorListener != null){
                    mTextColorListener.onClick(this, 26);
                }
                break;
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}
