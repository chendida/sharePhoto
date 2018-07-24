package com.zq.dynamicphoto.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.bean.UserWatermark;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.view.WaterMouldView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SelectWaterPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<UserWatermark> mList;
    private static final int HEAD_TYPE = 00001;
    private static final int BODY_TYPE = 00002;
    private int headCount = 1;//头部个数，后续可以自己拓展
    private HeadViewHolder headViewHolder;
    private WaterPicViewHolder waterPicViewHolder;
    private WaterMouldView clickListener;
    private int getBodySize() {
        return mList.size();
    }

    private boolean isHead(int position) {
        return headCount != 0 && position < headCount;
    }


    public SelectWaterPicAdapter(ArrayList<UserWatermark> mList,WaterMouldView view) {
        this.mList = mList;
        this.clickListener = view;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHead(position)) {
            return HEAD_TYPE;
        } else {
            return BODY_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD_TYPE:
                headViewHolder = new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_water_head,null));
                return headViewHolder;
            case BODY_TYPE:
                waterPicViewHolder = new WaterPicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_water_item,null));
                return waterPicViewHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder parent, final int position) {
        if (parent instanceof HeadViewHolder){
            HeadViewHolder holder = (HeadViewHolder) parent;
            holder.clickCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("6666666666","create");
                    clickListener.showSelectDialog();
                }
            });
        }else if (parent instanceof WaterPicViewHolder){
            WaterPicViewHolder holder = (WaterPicViewHolder) parent;
            holder.bind(mList.get(position-1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Image image = new Image(mList.get(position -1).getWatermarkUrl());
                    clickListener.addWaterImage(image);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? headCount : getBodySize()+headCount;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.click_create)
        ImageView clickCreate;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class WaterPicViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_water_pic)
        ImageView ivWaterPic;

        WaterPicViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(UserWatermark userWatermark) {
            if (userWatermark != null) {
                if (!TextUtils.isEmpty(userWatermark.getWatermarkUrl())) {
                    ImageLoaderUtils.displayImg(ivWaterPic, userWatermark.getWatermarkUrl());
                }
            }
        }
    }
}
