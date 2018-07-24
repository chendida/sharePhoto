package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.mylive.bean.ChargeOrder;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.view.OrdersOperate;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/5/7.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChargeOrder> mList;
    private OrdersOperate mListener;
    public OrderListAdapter(ArrayList<ChargeOrder> mList,OrdersOperate listener) {
        this.mList = mList;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, final int position) {
        final OrderListViewHolder holder = (OrderListViewHolder) parent;
        holder.bind(position);
        final ChargeOrder chargeOrder = mList.get(position);
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "是否确认取消？";
                mListener.updateOrderStatus(msg,chargeOrder,4,holder.btnOk,holder.btnSendOutGoods,
                        holder.btnCancel,holder.layoutOrderStatus,holder.tvOrderStatus);
            }
        });
        holder.btnSendOutGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "是否确认发货？";
                mListener.updateOrderStatus(msg,chargeOrder,3,holder.btnOk,holder.btnSendOutGoods,
                        holder.btnCancel,holder.layoutOrderStatus,holder.tvOrderStatus);
            }
        });
        holder.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "是否确认订单？";
                mListener.updateOrderStatus(msg,chargeOrder,2,holder.btnOk,holder.btnSendOutGoods,
                        holder.btnCancel,holder.layoutOrderStatus,holder.tvOrderStatus);
            }
        });
    }

    public void updateView(int i, AutoRelativeLayout btnOk, AutoRelativeLayout btnSend,
                            AutoRelativeLayout btnCancel, AutoRelativeLayout layoutOrderStatus,
                            TextView tvOrderStatus) {
        if (i == 2){
            ToastUtils.showShort("订单确认成功");
            layoutOrderStatus.setBackgroundColor(MyApplication.getAppContext().getResources().getColor(R.color.red_color));
            tvOrderStatus.setText("已确认");
            btnOk.setVisibility(View.GONE);
        }else if (i == 3){
            ToastUtils.showShort("订单发货成功");
            layoutOrderStatus.setBackgroundColor(MyApplication.getAppContext().getResources().getColor(R.color.red_color));
            tvOrderStatus.setText("已发货");
            btnOk.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
        }else if (i == 4){
            ToastUtils.showShort("订单取消成功");
            layoutOrderStatus.setBackgroundColor(MyApplication.getAppContext().getResources().getColor(R.color.tv_text_normal));
            tvOrderStatus.setText("已取消");
            btnSend.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void addOrderList(ArrayList<ChargeOrder> orders) {
        this.mList.addAll(orders);
        notifyDataSetChanged();
    }

    public void initOrderList(ArrayList<ChargeOrder> orders) {
        if (mList != null) {
            this.mList.clear();
        } else {
            mList = new ArrayList<>();
        }
        addOrderList(orders);
    }


    class OrderListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_order_pic)
        ImageView ivOrderPic;
        @BindView(R.id.tv_order_num)
        TextView tvOrderNum;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.tv_contact_phone)
        TextView tvContactPhone;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_order_content)
        TextView tvContent;
        @BindView(R.id.tv_leave_msg)
        TextView tvLeaveMsg;
        @BindView(R.id.tv_order_status)
        TextView tvOrderStatus;
        @BindView(R.id.btn_cancel)
        AutoRelativeLayout btnCancel;//取消按钮
        @BindView(R.id.btn_ok)
        AutoRelativeLayout btnOk;//确定按钮
        @BindView(R.id.btn_send_out_goods)
        AutoRelativeLayout btnSendOutGoods;//发货按钮
        @BindView(R.id.layout_order_status)
        AutoRelativeLayout layoutOrderStatus;

        OrderListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            ChargeOrder chargeOrder = mList.get(position);
            if (chargeOrder != null){
                if (chargeOrder.getOrderStatus() == 1){//待确认
                    layoutOrderStatus.setBackgroundColor(MyApplication.getAppContext().getResources().getColor(R.color.red_color));
                    tvOrderStatus.setText("待确认");
                }else if (chargeOrder.getOrderStatus() == 2){//已确认
                    layoutOrderStatus.setBackgroundColor(MyApplication.getAppContext().getResources().getColor(R.color.red_color));
                    tvOrderStatus.setText("已确认");
                    btnOk.setVisibility(View.GONE);
                }else if (chargeOrder.getOrderStatus() == 3){//已确认
                    layoutOrderStatus.setBackgroundColor(MyApplication.getAppContext().getResources().getColor(R.color.red_color));
                    tvOrderStatus.setText("已发货");
                    btnOk.setVisibility(View.GONE);
                    btnSendOutGoods.setVisibility(View.GONE);
                }else if (chargeOrder.getOrderStatus() == 4){//已确认
                    layoutOrderStatus.setBackgroundColor(MyApplication.getAppContext().getResources().getColor(R.color.tv_text_normal));
                    tvOrderStatus.setText("已取消");
                    btnSendOutGoods.setVisibility(View.GONE);
                    btnOk.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(chargeOrder.getDynamicImg())){
                    ImageLoaderUtils.displayImg(ivOrderPic,chargeOrder.getDynamicImg());
                }
                if (!TextUtils.isEmpty(chargeOrder.getAddress())){
                    tvAddress.setText("收货地址:"+chargeOrder.getAddress());
                }
                if (!TextUtils.isEmpty(chargeOrder.getUserName())){
                    tvContactName.setText(chargeOrder.getUserName());
                }
                if (!TextUtils.isEmpty(chargeOrder.getMoblie())){
                    tvContactPhone.setText(chargeOrder.getMoblie());
                }
                if (chargeOrder.getBuyNum() != null){
                    tvOrderNum.setText(String.valueOf(chargeOrder.getBuyNum()));
                }
                if (!TextUtils.isEmpty(chargeOrder.getMessage())){
                    tvLeaveMsg.setText("留言:"+chargeOrder.getMessage());
                }
                if (!TextUtils.isEmpty(chargeOrder.getContent())){
                    tvContent.setText(chargeOrder.getContent());
                }
            }
        }
    }
}
