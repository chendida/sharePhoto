package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.TextFontAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.utils.AddLocalSourceUtils;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

import java.util.ArrayList;

/**
 * 字体编辑弹窗
 * Created by Administrator on 2018/8/8.
 */

public class TextFontDialog extends Dialog implements View.OnClickListener{
    private BaseActivity mContext;
    private WatermarkSeekBarListener mListener;
    private int size;
    private int space;
    public TextFontDialog(@NonNull Context context) {
        super(context);
    }

    public TextFontDialog(@NonNull BaseActivity context, int themeResId,
                          WatermarkSeekBarListener listener,int size,float space) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
        this.size = size;
        this.space = (int) (space * 10);
    }

    protected TextFontDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text_font);
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
        SeekBar seekBarTextSize = findViewById(R.id.seek_bar_num);
        SeekBar seekBarTextSpace = findViewById(R.id.seek_bar_space);
        if (size < 101) {
            seekBarTextSize.setProgress(size);
        }else {
            seekBarTextSize.setProgress(100);
        }
        if (space < 101){
            seekBarTextSpace.setProgress(space);
        }else {
            seekBarTextSpace.setProgress(100);
        }
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mListener.onTextSizeChange(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTextSpace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mListener.onTextSpaceChange(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        RecyclerView recyclerView = findViewById(R.id.rcl_text_font_list);
        LinearLayoutManager manager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        ArrayList<Drawable> textFontPic = AddLocalSourceUtils.getInstance().getTextFontPic(mContext);
        ArrayList<String> textFontName = AddLocalSourceUtils.getInstance().getTextFontName();
        TextFontAdapter mAdapter = new TextFontAdapter(mContext,textFontPic, textFontName,mListener);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_cancel){
            dismiss();
        }
    }
}
