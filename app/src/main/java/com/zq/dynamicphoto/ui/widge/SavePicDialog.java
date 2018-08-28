package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.view.SaveWaterImage;

/**
 * 保存图片dialog，记录进度
 * Created by Administrator on 2018/8/27.
 */

public class SavePicDialog extends Dialog implements SaveWaterImage{
    private Activity mContext;
    private int size;
    private TextView tvNum;
    private SeekBar seekBar;
    public SavePicDialog(Activity context) {
        super(context);
        this.mContext = context;
    }


    public SavePicDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public SavePicDialog(Activity context, int themeResId,int size) {
        super(context, themeResId);
        this.mContext = context;
        this.size = size;
    }

    protected SavePicDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_pic_process);
        setCanceledOnTouchOutside(false);
        initView();
        mContext.getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView(){
        tvNum = findViewById(R.id.tv_save_num);
        seekBar = findViewById(R.id.seek_bar_process);
        seekBar.setMax(size);
    }

    @Override
    public void startSave(int size) {
        show();
        String str = 0 + "/" + size + ")";
        tvNum.setText(str);
    }

    @Override
    public void endSave() {
        dismiss();
    }

    @Override
    public void saveProcess(final int num, final int size) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = (num+1) + "/" + size + ")";
                tvNum.setText(str);
                seekBar.setProgress(num+1);
            }
        });
    }
}
