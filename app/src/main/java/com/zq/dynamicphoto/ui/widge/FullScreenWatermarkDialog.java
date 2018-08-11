package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

/**
 * Created by Administrator on 2018/7/27.
 */

public class FullScreenWatermarkDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private WatermarkSeekBarListener mListener;
    /*private int space;
    private int num;*/

    public FullScreenWatermarkDialog(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    public FullScreenWatermarkDialog(@NonNull Activity context, int themeResId,
                                     /*int space,int num,*/WatermarkSeekBarListener listener) {
        super(context, themeResId);
        this.mContext = context;
        /*this.space = space;
        this.num = num;*/
        this.mListener = listener;
    }

    protected FullScreenWatermarkDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_full_screen_watermark);
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
        SeekBar seekBarNum = findViewById(R.id.seek_bar_num);
        SeekBar seekBarSpace = findViewById(R.id.seek_bar_space);
        seekBarNum.setMax(6);
        seekBarNum.setProgress(2);
        seekBarSpace.setMax(100);

        seekBarSpace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mListener != null){
                    mListener.onSpaceListener(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mListener != null){
                    mListener.onNumListener(progress);
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
}
