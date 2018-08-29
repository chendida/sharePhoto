package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.PicSelectUtils;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/2.
 */

public class WaterPhotoListAdapter extends RecyclerView.Adapter<WaterPhotoListAdapter.PhotoViewHolder> {
    ArrayList<Image> mList;
    private MyClickListener mListener;
    private PhotoViewHolder mHolder;
    private Boolean isPreview;

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {
        void clickListener(Image imageItem,Boolean isAdd);
    }

    public WaterPhotoListAdapter(ArrayList<Image> mList,MyClickListener listener,Boolean isPreview) {
        this.mList = mList;
        this.mListener = listener;
        this.isPreview = isPreview;
    }

    private int getBodySize() {
        return mList.size();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mHolder = new PhotoViewHolder(View.inflate(parent.getContext(), R.layout.water_photo_list, null));
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoViewHolder holder, final int position) {
        holder.bind(mList.get(position));
        holder.layout_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPreview){
                    ArrayList<LocalMedia> list = new ArrayList<>();
                    for (Image pic:mList) {
                        LocalMedia media = new LocalMedia();
                        media.setPath(pic.getPath());
                        list.add(media);
                    }
                    PicSelectUtils.getInstance().preview(position,list,v.getContext());
                }else {
                    if (holder.check_photo.getVisibility() == View.GONE) {
                        mList.get(position).setSelected(true);
                        mListener.clickListener(mList.get(position), true);
                        holder.layout_photo.setAlpha(0.6f);
                        holder.check_photo.setVisibility(View.VISIBLE);
                        holder.check_photo.setChecked(true);
                    } else {
                        mList.get(position).setSelected(false);
                        mListener.clickListener(mList.get(position), false);
                        holder.layout_photo.setAlpha(1.0f);
                        holder.check_photo.setChecked(false);
                        holder.check_photo.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : getBodySize();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.layout_photo)
        AutoRelativeLayout layout_photo;
        @BindView(R.id.check_photo)
        CheckBox check_photo;

        PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(Image imageItem) {
            if (imageItem != null){
                if (!TextUtils.isEmpty(imageItem.getPath())){
                    ImageLoaderUtils.displayImg(ivPhoto,imageItem.getPath());
                }
                if (!imageItem.isSelected()){
                    layout_photo.setAlpha(1.0f);
                    check_photo.setChecked(false);
                    check_photo.setVisibility(View.GONE);
                }else {
                    layout_photo.setAlpha(0.6f);
                    check_photo.setVisibility(View.VISIBLE);
                    check_photo.setChecked(true);
                }
            }
        }
    }

    public void deleteImageItem(Image imageItem) {
        if (mList != null){
            int position;
            for (position = 0; position < mList.size(); position++) {
                if (mList.get(position).getPath().equals(imageItem.getPath())){
                    notifyItemChanged(position);
                }
            }
        }
    }

    public void addImageItem(Image imageItem) {
        if (mList != null){
            mList.add(0,imageItem);
            notifyDataSetChanged();
        }
    }
}
