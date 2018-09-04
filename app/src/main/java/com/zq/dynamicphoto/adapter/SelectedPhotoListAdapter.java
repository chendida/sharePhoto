package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.luck.picture.lib.entity.LocalMedia;
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

public class SelectedPhotoListAdapter extends RecyclerView.Adapter<SelectedPhotoListAdapter.SelectedViewHolder> {
    ArrayList<SelectPhotoAdapter.SelectPhotoEntity> mList;
    private DeleteListener mListener;

    public ArrayList<SelectPhotoAdapter.SelectPhotoEntity> getmList() {
        return mList;
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface DeleteListener {
        void deleteItem(SelectPhotoAdapter.SelectPhotoEntity imageItem);
    }

    public SelectedPhotoListAdapter(ArrayList<SelectPhotoAdapter.SelectPhotoEntity> mList,DeleteListener listener) {
        this.mList = mList;
        this.mListener = listener;
    }


    @NonNull
    @Override
    public SelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectedViewHolder(View.inflate(parent.getContext(), R.layout.selected_photo_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedViewHolder holder, final int position) {
        holder.bind(mList.get(position));
        final SelectPhotoAdapter.SelectPhotoEntity imageItem = mList.get(position);
        holder.ivDeletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mList.get(position).setSelected(false);
                mList.remove(position);
                mListener.deleteItem(imageItem);
                notifyDataSetChanged();
            }
        });
        holder.ivSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<LocalMedia> list = new ArrayList<>();
                for (SelectPhotoAdapter.SelectPhotoEntity pic:mList) {
                    LocalMedia media = new LocalMedia();
                    media.setPath(pic.url);
                    list.add(media);
                }
                PicSelectUtils.getInstance().preview(position,list,v.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    class SelectedViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_select_photo)
        ImageView ivSelectPhoto;
        @BindView(R.id.iv_delete_pic)
        ImageView ivDeletePic;

        SelectedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(SelectPhotoAdapter.SelectPhotoEntity imageItem) {
            if (imageItem != null){
                if (!TextUtils.isEmpty(imageItem.url)){
                    ImageLoaderUtils.displayImg(ivSelectPhoto,imageItem.url);
                }
            }
        }
    }

    public void addImageItemList(SelectPhotoAdapter.SelectPhotoEntity imageItem) {
        this.mList.add(imageItem);
        notifyDataSetChanged();
    }

    public void deleteImageItemList(SelectPhotoAdapter.SelectPhotoEntity imageItem) {
        if (mList != null){
            mList.remove(imageItem);
            notifyDataSetChanged();
        }
    }
}
