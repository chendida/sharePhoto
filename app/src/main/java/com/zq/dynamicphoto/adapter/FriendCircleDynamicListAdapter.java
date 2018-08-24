package com.zq.dynamicphoto.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.NineGridImageLayout;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.DynamicDelete;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/14.
 */

public class FriendCircleDynamicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DynamicDelete{
    private Context mContext;
    private ArrayList<Dynamic> mList;
    private String userLogo, realName;


    public ArrayList<Dynamic> getmList() {
        return mList;
    }

    public FriendCircleDynamicListAdapter(Context mContext, ArrayList<Dynamic> mList) {
        this.mContext = mContext;
        this.mList = mList;
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        userLogo = sp.getString(Constans.USERLOGO, "");
        realName = sp.getString(Constans.REMARKNAME, "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectDynamicListViewHolder(View.inflate(mContext, R.layout.select_dynamic_list, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, int position) {
        if (parent instanceof SelectDynamicListViewHolder){
            SelectDynamicListViewHolder holder = (SelectDynamicListViewHolder) parent;
            holder.setIsRecyclable(false);
            holder.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void deleteSuccess(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    class SelectDynamicListViewHolder extends RecyclerView.ViewHolder {
        SelectDynamicListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_nick)
        TextView tvNick;
        @BindView(R.id.tv_article)
        TextView tvArticle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.layout_nine_grid)
        NineGridImageLayout layoutNineImage;

        public void bind(int position) {
            final Dynamic dynamic = mList.get(position);
            if (dynamic != null) {
                if (!TextUtils.isEmpty(userLogo)) {
                    ImageLoaderUtils.displayImg(ivAvatar,userLogo);
                }
                tvNick.setText(realName);
                tvTime.setText(dynamic.getUpdateTime());
                if (!TextUtils.isEmpty(dynamic.getContent())) {
                    tvArticle.setVisibility(View.VISIBLE);
                    tvArticle.setText(dynamic.getContent());
                } else {
                    tvArticle.setVisibility(View.GONE);
                }
                if (dynamic.getDynamicType() == 1) {//图片展示
                    if (dynamic.getDynamicPhotos() != null) {
                        if (dynamic.getDynamicPhotos().size() != 0) {
                            layoutNineImage.setVisibility(View.VISIBLE);
                            ArrayList<String> imageUrls = new ArrayList<>();
                            for (DynamicPhoto photo : dynamic.getDynamicPhotos()) {
                                imageUrls.add(photo.getThumbnailURL());
                            }
                            layoutNineImage.setUrlList(imageUrls, false);
                        }else {
                            layoutNineImage.setVisibility(View.GONE);
                        }
                    }else {
                        layoutNineImage.setVisibility(View.GONE);
                    }
                } else if (dynamic.getDynamicType() == 2) {
                    if (dynamic.getDynamicVideos() != null) {
                        if (dynamic.getDynamicVideos().size() != 0) {
                            layoutNineImage.setVisibility(View.VISIBLE);
                            ArrayList<String> imageUrls = new ArrayList<>();
                            for (DynamicVideo video : dynamic.getDynamicVideos()) {
                                imageUrls.add(video.getVideoURL());
                            }
                            layoutNineImage.setUrlList(imageUrls, true);
                        }
                    }
                }
            }
        }
    }

    private void addDynamicList(ArrayList<Dynamic> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initDynamicList(ArrayList<Dynamic> dynamicList) {
        this.mList.clear();
        addDynamicList(dynamicList);
    }
}
