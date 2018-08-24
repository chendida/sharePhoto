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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.mylive.bean.ChargeOrder;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.mylive.fragment.BuyDialog;
import com.zq.dynamicphoto.view.UploadLiveOrder;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 直播间产品列表适配器
 * Created by Administrator on 2018/5/3.
 */

public class LiveGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private ArrayList<Dynamic> mList;
    private NewLiveRoom newLiveRoom;
    private UploadLiveOrder listener;
    public LiveGoodsAdapter(Activity mContext, ArrayList<Dynamic> mList,
                            NewLiveRoom newLiveRoom,UploadLiveOrder listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.newLiveRoom = newLiveRoom;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.live_goods_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, final int position) {
        GoodsViewHolder holder = (GoodsViewHolder) parent;
        holder.bind(position);
        holder.layoutOnceBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBuyDialog(mList.get(position));
            }
        });
    }

    private void showBuyDialog(final Dynamic goods) {
        new BuyDialog(mContext, R.style.dialog, new BuyDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position, String address, String phone,
                                String name, int buyNum, String msg) {
                uploadOrder(dialog,address,phone,name,buyNum,msg,goods);
            }
        },goods).show();
    }

    /**
     * 提交订单
     * @param address
     * @param phone
     * @param name
     * @param buyNum
     */
    private void uploadOrder(final Dialog dialog, String address, String phone, String name,
                             int buyNum, String msg, Dynamic dynamic) {
        final DeviceProperties dr = DrUtils.getInstance();
        ChargeOrder chargeOrder = new ChargeOrder();
        chargeOrder.setAddress(address);
        chargeOrder.setBuyNum(buyNum);
        chargeOrder.setDynamicId(dynamic.getId());
        chargeOrder.setMoblie(phone);
        chargeOrder.setUserName(name);
        chargeOrder.setContent(dynamic.getContent());
        chargeOrder.setMessage(msg);
        chargeOrder.setDynamicImg(dynamic.getPromoteImg());
        chargeOrder.setNewLiveId(newLiveRoom.getId());
        chargeOrder.setUserId(newLiveRoom.getUserId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setChargeOrder(chargeOrder);
        listener.uploadLiveOrder(dialog,netRequestBean);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_good)
        ImageView ivGood;
        @BindView(R.id.tv_good_describe)
        TextView tvGoodDescribe;
        @BindView(R.id.tv_line)
        TextView tvLine;
        @BindView(R.id.layout_once_buy)
        AutoRelativeLayout layoutOnceBuy;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.default_img)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Dynamic goods = mList.get(position);
            if (goods != null){
                if (!TextUtils.isEmpty(goods.getPromoteImg())){
                    Glide.with(mContext).load(goods.getPromoteImg()).apply(options).into(ivGood);
                }
                if (!TextUtils.isEmpty(goods.getContent())){
                    tvGoodDescribe.setText(goods.getContent());
                }
            }
            if (position == (mList.size() -1)){
                tvLine.setVisibility(View.GONE);
            }
        }
    }

    public void addLiveGoods(ArrayList<Dynamic> dynamics) {
        if (mList != null) {
            this.mList.clear();
        }else {
            mList = new ArrayList<>();
        }
        this.mList.addAll(dynamics);
        notifyDataSetChanged();
    }
}
