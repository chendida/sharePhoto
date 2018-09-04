package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.utils.checkphoto.AlbumBean;
import com.zq.dynamicphoto.utils.checkphoto.AlxImageLoader;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/30.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoListViewHolder> {
    ArrayList<AlbumBean> mList;
    private Boolean isPhotoSelectView;//是否是图片选择界面
    private SelectListener mListener;
    private AlxImageLoader alxImageLoader;

    public void initFolders(ArrayList<AlbumBean> folders) {
        if (mList != null){
            mList.clear();
            mList.addAll(folders);
            notifyDataSetChanged();
        }
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface SelectListener {
        void selectListener(AlbumBean folder);
    }

    public PhotoListAdapter(ArrayList<AlbumBean> mList, Boolean flag, SelectListener listener) {
        this.mList = mList;
        this.isPhotoSelectView = flag;
        this.mListener = listener;
        alxImageLoader = new AlxImageLoader(MyApplication.getAppContext());
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
                /*if (isPhotoSelectView){
                    mListener.selectListener(mList.get(position));
                }else {
                    MFGT.gotoWaterPhotoListActivity(v.getContext(), mList.get(position),false);
                }*/
                mListener.selectListener(mList.get(position));
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

        public void bind(AlbumBean imageBucket) {
            if (imageBucket != null){
                if (imageBucket.getTopImagePath() != null){
                    alxImageLoader.setAsyncBitmapFromSD(imageBucket.getTopImagePath()
                            ,ivAvatar,300,false,
                            true,true);
                }
                tvPhotoName.setText(imageBucket.getFolderName());
                String num = imageBucket.getImageCounts()+"";
                tvImageNum.setText(num);
            }
        }
    }
}
