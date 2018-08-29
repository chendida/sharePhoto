package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Folder;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/30.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoListViewHolder> {
    ArrayList<Folder> mList;
    private Boolean isPhotoSelectView;//是否是图片选择界面
    private SelectListener mListener;

    public void initFolders(ArrayList<Folder> folders) {
        if (mList != null){
            mList.clear();
            mList.addAll(folders);
            notifyDataSetChanged();
        }
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface SelectListener {
        void selectListener(Folder folder);
    }

    public PhotoListAdapter(ArrayList<Folder> mList,Boolean flag,SelectListener listener) {
        this.mList = mList;
        this.isPhotoSelectView = flag;
        this.mListener = listener;
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
                if (isPhotoSelectView){
                    mListener.selectListener(mList.get(position));
                }else {
                    MFGT.gotoWaterPhotoListActivity(v.getContext(), mList.get(position),false);
                }
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

        public void bind(Folder imageBucket) {
            if (imageBucket != null){
                if (imageBucket.getImages() != null){
                    if (imageBucket.getImages().size() != 0){
                        ImageLoaderUtils.displayImg(ivAvatar,imageBucket.getImages().get(0).getPath());
                    }
                }
                tvPhotoName.setText(imageBucket.getName());
                String num = imageBucket.getImages().size()+"";
                tvImageNum.setText(num);
            }
        }
    }
}
