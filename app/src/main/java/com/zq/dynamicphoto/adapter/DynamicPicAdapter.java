package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DynamicPicAdapter extends RecyclerView.Adapter<DynamicPicAdapter.DynamicPicViewHolder> {
    ArrayList<String>mList;

    public DynamicPicAdapter(ArrayList<String> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public DynamicPicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic_pic, null);
        return new DynamicPicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DynamicPicViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.iv_play)
        ImageView ivPlay;

        DynamicPicViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            String url = mList.get(position);
            if (!TextUtils.isEmpty(url)){
                if (url.endsWith(".mp4")){
                    ivPlay.setVisibility(View.VISIBLE);
                }else {
                    ivPlay.setVisibility(View.GONE);
                }
                ImageLoaderUtils.displayImg(ivPicture,url);
            }
        }
    }

    public void addDynamicPicList(ArrayList<String> urlList) {
        this.mList.addAll(urlList);
        notifyDataSetChanged();
    }

    public void initDynamicPicList(ArrayList<String> urlList) {
        this.mList.clear();
        addDynamicPicList(urlList);
    }
}
