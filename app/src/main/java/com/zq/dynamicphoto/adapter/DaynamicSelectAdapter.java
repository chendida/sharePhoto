package com.zq.dynamicphoto.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.luck.picture.lib.config.PictureConfig;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.common.Constans;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DaynamicSelectAdapter extends RecyclerView.Adapter<DaynamicSelectAdapter.DynamicItemViewHolder> {
    ArrayList<Dynamic>mList;

    public DaynamicSelectAdapter(ArrayList<Dynamic> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public DynamicItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic, null, false);
        return new DynamicItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class DynamicItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_select)
        CheckBox ivSelect;
        @BindView(R.id.layout_select)
        AutoRelativeLayout layoutSelect;
        @BindView(R.id.tv_article)
        TextView tvArticle;
        @BindView(R.id.rcl_dynamic_pic_list)
        RecyclerView rclDynamicPicList;
        DynamicPicAdapter mAdapter;
        ArrayList<String>urlList;

        DynamicItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            initView();
        }

        private void initView() {
            if (urlList == null){
                urlList = new ArrayList<>();
            }
            mAdapter = new DynamicPicAdapter(urlList);
            rclDynamicPicList.setAdapter(mAdapter);
        }

        public void bind(int position) {
            Dynamic dynamic = mList.get(position);
            if (dynamic != null){
                if (!TextUtils.isEmpty(dynamic.getContent())){
                    tvArticle.setText(dynamic.getContent());
                }
                if (dynamic.getDynamicType() == PictureConfig.TYPE_VIDEO){
                    if (dynamic.getDynamicVideos() != null){
                        if (dynamic.getDynamicVideos().size() != 0){
                            urlList.clear();
                            for (DynamicVideo video:dynamic.getDynamicVideos()) {
                                urlList.add(video.getVideoURL());
                            }
                            mAdapter.initDynamicPicList(urlList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    if (dynamic.getDynamicPhotos() != null){
                        if (dynamic.getDynamicPhotos().size() != 0){
                            urlList.clear();
                            for (DynamicPhoto photo:dynamic.getDynamicPhotos()) {
                                urlList.add(photo.getThumbnailURL());
                            }
                            mAdapter.initDynamicPicList(urlList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    public void addDynamicList(ArrayList<Dynamic> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initDynamicList(ArrayList<Dynamic> dynamicList) {
        this.mList.clear();
        addDynamicList(dynamicList);
    }
}
