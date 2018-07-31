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
import android.widget.TextView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

/**
 * Created by Administrator on 2018/7/27.
 */

public class WaterBgDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private String title,fuction1,fuction2;
    private WatermarkSeekBarListener mListener;
    private OnItemClickListener mBgColorListener;
    public WaterBgDialog(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    public WaterBgDialog(@NonNull Activity context, int themeResId,String title,
                         String fuction1,String fuction2,WatermarkSeekBarListener listener,
                         OnItemClickListener mBgColorListener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.fuction1 = fuction1;
        this.fuction2 = fuction2;
        this.mListener = listener;
        this.mBgColorListener = mBgColorListener;
    }

    protected WaterBgDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_water_bg_setting);
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
        TextView textView = findViewById(R.id.tv_dialog_title);
        TextView tv2 = findViewById(R.id.watermark_space);
        TextView tv1 = findViewById(R.id.watermark_num);
        textView.setText(title);
        tv1.setText(fuction1);
        tv2.setText(fuction2);
        SeekBar seekBarAlpha = findViewById(R.id.seek_bar_num);
        seekBarAlpha.setMax(255);
        seekBarAlpha.setProgress(255);
        SeekBar seekBarCorner = findViewById(R.id.seek_bar_space);
        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null){
                    mListener.onWatermarkAlpha(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarCorner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null){
                    mListener.onWatermarkCorner(progress);
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
                if(mBgColorListener != null){
                    Log.i("color","iv_water_color0000");
                    mBgColorListener.onClick(this, 1);
                }
                break;
            case R.id.iv_water_color001:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 2);
                }
                break;
            case R.id.iv_water_color002:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 3);
                }
                break;
            case R.id.iv_water_color003:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 4);
                }
                break;
            case R.id.iv_water_color004:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 5);
                }
                break;
            case R.id.iv_water_color005:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 6);
                }
                break;
            case R.id.iv_water_color006:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 7);
                }
                break;
            case R.id.iv_water_color007:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 8);
                }
                break;
            case R.id.iv_water_color008:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 9);
                }
                break;
            case R.id.iv_water_color009:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 10);
                }
                break;
            case R.id.iv_water_color010:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 11);
                }
                break;
            case R.id.iv_water_color011:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 12);
                }
                break;
            case R.id.iv_water_color012:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 13);
                }
                break;
            case R.id.iv_water_color013:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 14);
                }
                break;
            case R.id.iv_water_color014:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 15);
                }
                break;
            case R.id.iv_water_color015:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 16);
                }
                break;
            case R.id.iv_water_color016:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 17);
                }
                break;
            case R.id.iv_water_color017:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 18);
                }
                break;
            case R.id.iv_water_color018:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 19);
                }
                break;
            case R.id.iv_water_color019:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 20);
                }
                break;
            case R.id.iv_water_color020:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 21);
                }
                break;
            case R.id.iv_water_color021:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 22);
                }
                break;
            case R.id.iv_water_color022:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 23);
                }
                break;
            case R.id.iv_water_color023:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 24);
                }
                break;
            case R.id.iv_water_color024:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 25);
                }
                break;
            case R.id.iv_water_color025:
                if(mBgColorListener != null){
                    mBgColorListener.onClick(this, 26);
                }
                break;
        }
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }
}
