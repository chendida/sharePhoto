package com.zq.dynamicphoto.mylive.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Dynamic;

/**
 * Created by Administrator on 2018/5/3.
 */

public class BuyDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private OnItemClickListener mListener;
    private TextView buyNum;
    private ImageView ivRemove;
    private Dynamic goods;
    private EditText etAddress,etPhone,etName,etMessage;

    public boolean isAll() {
        if (TextUtils.isEmpty(etAddress.getText().toString())){
            Toast.makeText(mContext, mContext.getResources().getString(R.string.please_input_address), Toast.LENGTH_SHORT).show();
            etAddress.setFocusable(true);
            return false;
        }else if (TextUtils.isEmpty(etPhone.getText().toString())){
            Toast.makeText(mContext, mContext.getResources().getString(R.string.please_input_phone), Toast.LENGTH_SHORT).show();
            etPhone.setFocusable(true);
            return false;
        }else if (TextUtils.isEmpty(etName.getText().toString())){
            Toast.makeText(mContext, mContext.getResources().getString(R.string.please_input_name), Toast.LENGTH_SHORT).show();
            etName.setFocusable(true);
            return false;
        }
        return true;
    }

    public interface OnItemClickListener{
        void onClick(Dialog dialog, int position, String address, String phone, String name, int buyNum, String msg);
    }

    public BuyDialog(Activity context) {
        super(context);
        this.mContext = context;
    }

    public BuyDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public BuyDialog(Activity context, int themeResId, OnItemClickListener mListener, Dynamic goods) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = mListener;
        this.goods = goods;
    }

    protected BuyDialog(Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buy_goods);
        setCanceledOnTouchOutside(false);
        initView();
        getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initView(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_img)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        buyNum = findViewById(R.id.tv_buy_num);
        ivRemove = findViewById(R.id.btn_remove);
        etAddress = findViewById(R.id.et_address);
        etPhone = findViewById(R.id.et_phone);
        etName = findViewById(R.id.et_name);
        etMessage = findViewById(R.id.et_message);
        ivRemove.setOnClickListener(this);
        findViewById(R.id.layout_cancel).setOnClickListener(this);
        findViewById(R.id.btn_buy).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        ImageView ivGood = (ImageView) findViewById(R.id.iv_good);
        TextView tvDescribe = (TextView) findViewById(R.id.tv_good_describe);
        if (goods != null){
            if (!TextUtils.isEmpty(goods.getPromoteImg())){
                Glide.with(mContext).load(goods.getPromoteImg()).apply(options).into(ivGood);
            }
            if (!TextUtils.isEmpty(goods.getContent())){
                tvDescribe.setText(goods.getContent());
            }
        }
    }


    @Override
    public void onClick(View v) {
        int num = Integer.parseInt(buyNum.getText().toString());
        switch (v.getId()){
            case R.id.layout_cancel:
                dismiss();
                break;
            case R.id.btn_remove:
                if (num > 1) {
                    int currentNum = num -1;
                    if (currentNum <= 1){
                        ivRemove.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_jian_no_click));
                    }
                    buyNum.setText(String.valueOf(currentNum));
                }
                break;
            case R.id.btn_add:
                int currentNum = num + 1;
                ivRemove.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_jian));
                buyNum.setText(String.valueOf(currentNum));
                break;
            case R.id.btn_buy:
                if (isAll()) {
                    if (mListener != null) {
                        mListener.onClick(this, 1,etAddress.getText().toString(),
                                etPhone.getText().toString(),etName.getText().toString(),
                                Integer.parseInt(buyNum.getText().toString()),etMessage.getText().toString());
                    }
                }
                break;
        }
    }
}
