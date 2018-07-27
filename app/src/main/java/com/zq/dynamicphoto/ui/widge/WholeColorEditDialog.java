package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/7/26.
 */

public class WholeColorEditDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;

    public WholeColorEditDialog(@NonNull  Activity context) {
        super(context);
        this.mContext = context;
    }

    public WholeColorEditDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected WholeColorEditDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_whole_water_color_edit);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
            /*case R.id.share_wx:
                if(mListener != null){
                    mListener.onClick(this, 1);
                }
                break;
            case R.id.share_wx_friend:
                if(mListener != null){
                    mListener.onClick(this, 2);
                }
                break;*/
        }
    }
}
