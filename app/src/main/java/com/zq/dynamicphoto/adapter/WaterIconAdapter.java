package com.zq.dynamicphoto.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/6.
 */

public class WaterIconAdapter extends RecyclerView.Adapter<WaterIconAdapter.WaterIconViewHolder> {
    private ArrayList<Drawable> mList;
    private WatermarkSeekBarListener mListener;
    public WaterIconAdapter(ArrayList<Drawable> mList, WatermarkSeekBarListener listener) {
        this.mList = mList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public WaterIconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WaterIconViewHolder(View.inflate(parent.getContext(),
                R.layout.water_icon_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull WaterIconViewHolder holder, final int position) {
        holder.bind(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){
                    mListener.onHideIcon();
                }else {
                    mListener.onChangeIcon(mList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class WaterIconViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_water_icon)
        ImageView ivWaterIcon;

        WaterIconViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(Drawable drawable) {
            if (drawable != null){
                ivWaterIcon.setImageDrawable(drawable);
            }
        }
    }
}
