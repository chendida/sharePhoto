package com.zq.dynamicphoto.ui.widge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.WaterIconAdapter;
import com.zq.dynamicphoto.utils.AddLocalSourceUtils;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

import java.util.ArrayList;

/**
 * 图标切换弹窗
 * Created by Administrator on 2018/8/6.
 */

public class WaterIconDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private RecyclerView recyclerView;
    private WaterIconAdapter mAdapter;
    private WatermarkSeekBarListener mListener;
    /*private ImageView ivWaterIcon;
    private ImageView ivWaterIconHint;*/
    public WaterIconDialog(@NonNull Context context) {
        super(context);
    }

    public WaterIconDialog(@NonNull Activity context, int themeResId,
                           WatermarkSeekBarListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
    }

    protected WaterIconDialog(@NonNull Context context, boolean cancelable,
                              @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_water_icon);
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
        recyclerView = findViewById(R.id.rcl_water_icon);
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        ArrayList<Drawable> waterIconList = AddLocalSourceUtils.getInstance().getWaterIconList(mContext);
        mAdapter = new WaterIconAdapter(waterIconList,mListener);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
        }
    }
}
