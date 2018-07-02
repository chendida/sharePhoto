package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.ImageBucket;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/30.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoListViewHolder> {
    ArrayList<ImageBucket> mList;

    public PhotoListAdapter(ArrayList<ImageBucket> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public PhotoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoListViewHolder(View.inflate(parent.getContext(), R.layout.photo_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoListViewHolder holder, final int position) {
        holder.bind(mList.get(position));
        holder.layoutAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoWaterPhotoListAdapter(v.getContext(),mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class PhotoListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_image_num)
        TextView tvImageNum;
        @BindView(R.id.tv_photo_name)
        TextView tvPhotoName;
        @BindView(R.id.layout_avatar)
        AutoLinearLayout layoutAvatar;

        PhotoListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(ImageBucket imageBucket) {
            if (imageBucket != null){
                if (imageBucket.getImageList() != null){
                    if (imageBucket.getImageList().size() != 0){
                        ImageLoaderUtils.displayImg(ivAvatar,imageBucket.getImageList().get(0).getImagePath());
                    }
                }
                tvPhotoName.setText(imageBucket.getBucketName());
                tvImageNum.setText("("+imageBucket.getImageList().size()+")");
            }
        }
    }
}
