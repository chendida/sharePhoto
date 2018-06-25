package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.PicSelectUtils;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DynamicPicAdapter extends RecyclerView.Adapter<DynamicPicAdapter.DynamicPicViewHolder> {
    ArrayList<String>mList;
    Activity activity;

    public DynamicPicAdapter(ArrayList<String> mList,Activity activity) {
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public DynamicPicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic_pic, null,false);
        return new DynamicPicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DynamicPicViewHolder holder, final int position) {
        holder.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<LocalMedia>localMedias = new ArrayList<>();
                for (String url:mList) {
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(url);
                    localMedias.add(localMedia);
                }
                if (mList.get(position).endsWith(".mp4")){

                }else {
                    PicSelectUtils.getInstance().preview(position,localMedias,activity);
                }
            }
        });
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
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
}
