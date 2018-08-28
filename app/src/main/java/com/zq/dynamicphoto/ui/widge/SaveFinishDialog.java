package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.zq.dynamicphoto.R;

/**
 * 图片保存完成dialog
 * Created by Administrator on 2018/8/28.
 */

public class SaveFinishDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private OnItemClickListener mListener;
    public SaveFinishDialog(Activity context) {
        super(context);
        this.mContext = context;
    }


    public SaveFinishDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public SaveFinishDialog(Activity context, int themeResId,OnItemClickListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
    }

    protected SaveFinishDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_pic_finish);
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
        //findViewById(R.id.layout_back_home).setOnClickListener(this);
        findViewById(R.id.layout_see_photos).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.layout_back_home:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;*/
            case R.id.layout_see_photos:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
        }
    }
}
