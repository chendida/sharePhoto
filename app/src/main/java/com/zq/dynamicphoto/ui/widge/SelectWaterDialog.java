package com.zq.dynamicphoto.ui.widge;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zq.dynamicphoto.R;
/**
 * 主播界面产品列表弹窗
 * Created by Administrator on 2018/5/3.
 */

@SuppressLint("ValidFragment")
public class SelectWaterDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.water_pic_popup_window, container, false);
        initData();
        return view;
    }

    @SuppressLint("ValidFragment")
    public SelectWaterDialog(/*NewLiveRoom newLiveRoom*/) {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                dismiss();
            }
        };
        return dialog;
    }

    private void initData() {
        /*LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rclGoods.setLayoutManager(manager);
        *//*mAdapter = new LiveGoodsAdapter(mContext, goods, newLiveRoom);
        rclGoods.setAdapter(mAdapter);*//*
        rclGoods.setHasFixedSize(true);*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
