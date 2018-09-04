package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.checkphoto.AlxImageLoader;
import com.zq.dynamicphoto.utils.checkphoto.SelectPhotoEntity;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/2.
 */

public class WaterPhotoListAdapter extends RecyclerView.Adapter<WaterPhotoListAdapter.PhotoViewHolder> {
    ArrayList<SelectPhotoEntity> mList;
    private MyClickListener mListener;
    private PhotoViewHolder mHolder;
    private Boolean isPreview;
    private AlxImageLoader alxImageLoader;
    int maxSelectedPhotoCount = 100;
    private Activity mActivity;

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {
        void clickListener(Image imageItem,Boolean isAdd);
    }

    public WaterPhotoListAdapter(ArrayList<SelectPhotoEntity> mList,
                                 MyClickListener listener,Boolean isPreview) {
        this.mList = mList;
        this.mListener = listener;
        this.isPreview = isPreview;
        this.alxImageLoader = new AlxImageLoader(MyApplication.getAppContext());
    }

    public ArrayList<SelectPhotoEntity> getmList() {
        return mList;
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
                    for (SelectPhotoEntity pic:mList) {
                        LocalMedia media = new LocalMedia();
                        media.setPath(pic.url);
                        list.add(media);
                    }
                    PicSelectUtils.getInstance().preview(position,list,v.getContext());
                }else {
                    /*SelectPhotoEntity entity = (SelectPhotoEntity) v.getTag(R.id.rlPhoto);
                    ImageView ivSelect = (ImageView) v.findViewById(R.id.iv_select);
                    if (mActivity == null) return;*/
                    if (checkIsExistedInSelectedPhotoArrayList(mList.get(position))) {
                        holder.layout_photo.setAlpha(1.0f);
                        holder.check_photo.setChecked(false);
                        holder.check_photo.setVisibility(View.GONE);
                        removeSelectedPhoto(mList.get(position));
                    } else if (!isFullInSelectedPhotoArrayList()){
                        holder.layout_photo.setAlpha(0.6f);
                        holder.check_photo.setVisibility(View.VISIBLE);
                        holder.check_photo.setChecked(true);
                        addSelectedPhoto(mList.get(position));
                    } else {
                        return;
                    }
                    //if (mActivity instanceof CallBackActivity)((CallBackActivity)mActivity).updateSelectActivityViewUI();
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

        public void bind(SelectPhotoEntity imageItem) {
            if (imageItem != null){
                if (!TextUtils.isEmpty(imageItem.url)){
                    alxImageLoader.setAsyncBitmapFromSD(imageItem.url,ivPhoto,300,
                            false,true,true);
                    //ImageLoaderUtils.displayImg(ivPhoto,imageItem.getPath());
                }
                if (!checkIsExistedInSelectedPhotoArrayList(imageItem)){
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
/*
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
    }*/

    /**
     * 判断某张照片是否已经被选择过
     * @param entity
     * @return
     */
    HashSet<SelectPhotoEntity> selectedPhotosSet = new HashSet<>(100);
    public boolean checkIsExistedInSelectedPhotoArrayList(SelectPhotoEntity photo) {
        if (selectedPhotosSet == null || selectedPhotosSet.size() == 0) return false;
        if(selectedPhotosSet.contains(photo))return true;
        return false;
    }

    public void removeSelectedPhoto(SelectPhotoEntity photo) {
        selectedPhotosSet.remove(photo);
    }
    public boolean isFullInSelectedPhotoArrayList() {
        if (maxSelectedPhotoCount > 0 && selectedPhotosSet.size() < maxSelectedPhotoCount) return false;
        return true;
    }

    public void addSelectedPhoto(SelectPhotoEntity photo) {
        selectedPhotosSet.add(photo);
    }
}
