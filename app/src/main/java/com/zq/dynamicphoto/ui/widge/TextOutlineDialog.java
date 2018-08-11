package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

/**
 * 描边
 * Created by Administrator on 2018/8/1.
 */

public class TextOutlineDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private OnItemClickListener listener;
    private WatermarkSeekBarListener mListener;
    private Boolean isOpen;//描边开关是否打开
    private float degree;
    private int alpha;
    public TextOutlineDialog(@NonNull Context context) {
        super(context);
    }

    public TextOutlineDialog(@NonNull Activity context, int themeResId,
                             WatermarkSeekBarListener mListener,
                             OnItemClickListener listener,Boolean isOepn,float degree,int alpha) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
        this.mListener = mListener;
        this.isOpen = isOepn;
        this.degree = degree;
        this.alpha = alpha;
    }

    protected TextOutlineDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text_outline);
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
        TextView tvTitle = findViewById(R.id.tv_dialog_title);
        TextView function1 = findViewById(R.id.watermark_num);
        TextView function2 = findViewById(R.id.watermark_space);
        SeekBar seekBarOutline = findViewById(R.id.seek_bar_num);
        SeekBar seekBarAlpha = findViewById(R.id.seek_bar_space);
        tvTitle.setText(mContext.getResources().getString(R.string.tv_stroke));
        function1.setText(mContext.getResources().getString(R.string.degree));
        function2.setText(mContext.getResources().getString(R.string.watermark_alpha));
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
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        seekBarListener(seekBarOutline,seekBarAlpha);
        seekBarAlpha.setMax(255);
        seekBarAlpha.setProgress(alpha);
        seekBarOutline.setProgress((int) (degree*10));
    }

    private void seekBarListener(SeekBar seekBarOutline, SeekBar seekBarAlpha) {
        seekBarOutline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null){
                    mListener.onTextOutlineDegree(progress,isOpen);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null){
                    mListener.onTextOutlineAlpha(progress,isOpen);
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
                if(listener != null){
                    Log.i("color","iv_water_color0000");
                    listener.onClick(this, 1);
                }
                break;
            case R.id.iv_water_color001:
                if(listener != null){
                    listener.onClick(this, 2);
                }
                break;
            case R.id.iv_water_color002:
                if(listener != null){
                    listener.onClick(this, 3);
                }
                break;
            case R.id.iv_water_color003:
                if(listener != null){
                    listener.onClick(this, 4);
                }
                break;
            case R.id.iv_water_color004:
                if(listener != null){
                    listener.onClick(this, 5);
                }
                break;
            case R.id.iv_water_color005:
                if(listener != null){
                    listener.onClick(this, 6);
                }
                break;
            case R.id.iv_water_color006:
                if(listener != null){
                    listener.onClick(this, 7);
                }
                break;
            case R.id.iv_water_color007:
                if(listener != null){
                    listener.onClick(this, 8);
                }
                break;
            case R.id.iv_water_color008:
                if(listener != null){
                    listener.onClick(this, 9);
                }
                break;
            case R.id.iv_water_color009:
                if(listener != null){
                    listener.onClick(this, 10);
                }
                break;
            case R.id.iv_water_color010:
                if(listener != null){
                    listener.onClick(this, 11);
                }
                break;
            case R.id.iv_water_color011:
                if(listener != null){
                    listener.onClick(this, 12);
                }
                break;
            case R.id.iv_water_color012:
                if(listener != null){
                    listener.onClick(this, 13);
                }
                break;
            case R.id.iv_water_color013:
                if(listener != null){
                    listener.onClick(this, 14);
                }
                break;
            case R.id.iv_water_color014:
                if(listener != null){
                    listener.onClick(this, 15);
                }
                break;
            case R.id.iv_water_color015:
                if(listener != null){
                    listener.onClick(this, 16);
                }
                break;
            case R.id.iv_water_color016:
                if(listener != null){
                    listener.onClick(this, 17);
                }
                break;
            case R.id.iv_water_color017:
                if(listener != null){
                    listener.onClick(this, 18);
                }
                break;
            case R.id.iv_water_color018:
                if(listener != null){
                    listener.onClick(this, 19);
                }
                break;
            case R.id.iv_water_color019:
                if(listener != null){
                    listener.onClick(this, 20);
                }
                break;
            case R.id.iv_water_color020:
                if(listener != null){
                    listener.onClick(this, 21);
                }
                break;
            case R.id.iv_water_color021:
                if(listener != null){
                    listener.onClick(this, 22);
                }
                break;
            case R.id.iv_water_color022:
                if(listener != null){
                    listener.onClick(this, 23);
                }
                break;
            case R.id.iv_water_color023:
                if(listener != null){
                    listener.onClick(this, 24);
                }
                break;
            case R.id.iv_water_color024:
                if(listener != null){
                    listener.onClick(this, 25);
                }
                break;
            case R.id.iv_water_color025:
                if(listener != null){
                    listener.onClick(this, 26);
                }
                break;
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}
