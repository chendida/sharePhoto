package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Watermark;
import com.zq.dynamicphoto.bean.WatermarkType;
import com.zq.dynamicphoto.view.EditWaterListener;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/20.
 */

public class WaterMouldListAdapter extends
        RecyclerView.Adapter<WaterMouldListAdapter.WaterMouldViewHolder> {
    ArrayList<WatermarkType> mList;
    private int type;
    private EditWaterListener mListener;
    public WaterMouldListAdapter(ArrayList<WatermarkType> mList,int type,EditWaterListener listener) {
        this.mList = mList;
        this.type = type;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public WaterMouldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WaterMouldViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_water_mould,null));
    }

    @Override
    public void onBindViewHolder(@NonNull WaterMouldViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class WaterMouldViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.style_name)
        TextView styleName;
        @BindView(R.id.rcl_water_list)
        RecyclerView rclWaterList;
        WaterMouldViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //设置layoutManager
            rclWaterList.setLayoutManager(new StaggeredGridLayoutManager(3
                    ,StaggeredGridLayoutManager.VERTICAL));
        }

        public void bind(WatermarkType watermark) {
            if (watermark != null){
                if (watermark.getWatermarkList() != null){
                    if (watermark.getWatermarkList().size() != 0){
                        WaterMouldItemAdapter adapter=new WaterMouldItemAdapter((ArrayList<Watermark>)
                                watermark.getWatermarkList(),mListener);
                        rclWaterList.setAdapter(adapter);
                    }
                }
                if (!TextUtils.isEmpty(watermark.getWatermarkName())){
                    styleName.setText(watermark.getWatermarkName());
                }
            }
        }
    }

    public void addWatermarkList(ArrayList<WatermarkType> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initWatermarkList(ArrayList<WatermarkType> dynamicList) {
        this.mList.clear();
        addWatermarkList(dynamicList);
    }
}
