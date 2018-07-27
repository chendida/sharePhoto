package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Watermark;
import com.zq.dynamicphoto.bean.WatermarkType;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.view.EditWaterListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/20.
 */

public class WaterMouldItemAdapter extends RecyclerView.Adapter
        <WaterMouldItemAdapter.WaterMouldItemViewHolder> {
    ArrayList<Watermark> mList;
    private EditWaterListener mListener;
    public WaterMouldItemAdapter(ArrayList<Watermark> mList,EditWaterListener listener) {
        this.mList = mList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public WaterMouldItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WaterMouldItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_water_mould_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final WaterMouldItemViewHolder holder, final int position) {
        holder.bind(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.click(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class WaterMouldItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_item)
        ImageView ivItem;
        @BindView(R.id.tv_id)
        TextView tvId;
        WaterMouldItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(Watermark watermark) {
            if (watermark != null){
                if (!TextUtils.isEmpty(watermark.getWatermarkUrl())){
                    ImageLoaderUtils.displayImg(ivItem,watermark.getWatermarkUrl());
                }
                if (watermark.getWatermarkId() != null){
                    tvId.setText(String.valueOf(watermark.getWatermarkId()));
                }
            }
        }
    }

    public void addWatermarkList(ArrayList<Watermark> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initWatermarkList(ArrayList<Watermark> dynamicList) {
        this.mList.clear();
        addWatermarkList(dynamicList);
    }
}
