package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.ImageItem;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/2.
 */

public class WaterPhotoListAdapter extends RecyclerView.Adapter<WaterPhotoListAdapter.PhotoViewHolder> {
    ArrayList<ImageItem> mList;

    public WaterPhotoListAdapter(ArrayList<ImageItem> mList) {
        this.mList = mList;
    }

    private int getBodySize() {
        return mList.size();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(View.inflate(parent.getContext(), R.layout.water_photo_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : getBodySize();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;

        PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(ImageItem imageItem) {
            if (imageItem != null){
                if (!TextUtils.isEmpty(imageItem.getImagePath())){
                    ImageLoaderUtils.displayImg(ivPhoto,imageItem.getImagePath());
                }
            }
        }
    }
}
