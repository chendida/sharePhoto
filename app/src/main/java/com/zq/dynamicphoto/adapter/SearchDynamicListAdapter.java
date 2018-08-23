package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.NineGridImageLayout;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.DynamicDelete;
import com.zq.dynamicphoto.view.SearchViewClick;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/23.
 */

public class SearchDynamicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DynamicDelete {
    private ArrayList<Dynamic> mList;
    private Context mContext;
    private DeviceProperties dr = DrUtils.getInstance();
    private Integer userId;
    private SearchViewClick mListener;
    public SearchDynamicListAdapter(ArrayList<Dynamic> mList,SearchViewClick listener) {
        this.mList = mList;
        userId = SharedPreferencesUtils.getInstance().getInt(Constans.USERID,0);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new SearchDynamicViewHolder(View.inflate(parent.getContext(), R.layout.photo_dynamic_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder parent, final int position) {
        final SearchDynamicViewHolder holder = (SearchDynamicViewHolder) parent;
        holder.setIsRecyclable(false);
        holder.bind(position);
        final Dynamic dynamic = mList.get(position);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetRequestBean netRequestBean = new NetRequestBean();
                netRequestBean.setDeviceProperties(dr);
                netRequestBean.setDynamic(dynamic);
                mListener.clickListener(v,position,netRequestBean,
                        SearchDynamicListAdapter.this);
            }
        });
        holder.tvStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetRequestBean netRequestBean = new NetRequestBean();
                netRequestBean.setDeviceProperties(dr);
                netRequestBean.setDynamic(dynamic);
                mListener.clickListener(v,position,netRequestBean,
                        SearchDynamicListAdapter.this);
            }
        });
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoEditDynamicActivity(mContext,dynamic);
            }
        });
        holder.layoutOneKeyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iuserId = mList.get(position).getUserId();
                if (userId.equals(iuserId)){//本人的直接调分享显示
                    NetRequestBean netRequestBean = new NetRequestBean();
                    netRequestBean.setDeviceProperties(dr);
                    netRequestBean.setDynamic(dynamic);
                    mListener.clickListener(v,position,netRequestBean,
                            SearchDynamicListAdapter.this);
                }else {
                    MFGT.gotoEditDynamicActivity(mContext,dynamic);
                }
            }
        });
        holder.tvArticle.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View view) {
                if(flag){
                    flag = false;
                    holder.tvArticle.setMaxLines(100);
                    holder.tvArticle.setEllipsize(null); // 展开
                }else {
                    flag = true;
                    holder.tvArticle.setMaxLines(8);
                    holder.tvArticle.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                }
            }
        });
        holder.tvAllSave.setOnClickListener(new View.OnClickListener() {//批量保存功能
            @Override
            public void onClick(View view) {
                NetRequestBean netRequestBean = new NetRequestBean();
                netRequestBean.setDynamic(mList.get(position));
                mListener.clickListener(view,position,netRequestBean,
                        SearchDynamicListAdapter.this);
            }
        });
        holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer iuserId = mList.get(position).getUserId();
                if (userId.equals(iuserId)){//本人
                    MFGT.gotoHtmlPhotoDetailsActivity(mContext,"friends.html?userId="+
                                    userId,
                            mContext.getResources().getString(R.string.tv_photo_details),
                            userId);
                }else {
                    NetRequestBean netRequestBean = new NetRequestBean();
                    netRequestBean.setDynamic(mList.get(position));
                    mListener.clickListener(view,position,netRequestBean,
                            SearchDynamicListAdapter.this);
                }
            }
        });

        holder.tvPicSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer isForward = mList.get(position).getIsForward();
                if (isForward == 1) {
                    MFGT.gotoDynamicDetailsActivity((Activity) mContext,
                            "detail.html?id=" + mList.get(position).getUserId()
                            , mContext.getResources().getString(R.string.tv_dynamic_details));
                }
            }
        });
    }

    class SearchDynamicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_nick)
        TextView tvNick;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_pic_source)
        TextView tvPicSource;
        @BindView(R.id.iv_locked)
        ImageView ivLocked;
        @BindView(R.id.tv_article)
        TextView tvArticle;
        @BindView(R.id.layout_nine_grid)
        NineGridImageLayout layoutNineGrid;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.tv_stick)
        TextView tvStick;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_all_save)
        TextView tvAllSave;
        @BindView(R.id.layout_operate_type)
        AutoRelativeLayout layoutOperateType;
        @BindView(R.id.layout_one_key_share)
        AutoRelativeLayout layoutOneKeyShare;
        @BindView(R.id.layout_nine_image)
        AutoRelativeLayout layoutNineImage;

        SearchDynamicViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(int position) {
            Log.i("position","position = "+position);
            Dynamic dynamic = mList.get(position);
            if (dynamic != null) {
                if (dynamic.getUserInfo() != null) {
                    if (!TextUtils.isEmpty(dynamic.getUserInfo().getUserLogo())) {
                        ImageLoaderUtils.displayImg(ivAvatar,dynamic.getUserInfo().getUserLogo());
                    }
                    tvNick.setText(dynamic.getUserInfo().getRemarkName());
                    tvTime.setText(dynamic.getUpdateTime());
                    Log.i("position","time = "+dynamic.getUpdateTime());
                    if (!TextUtils.isEmpty(dynamic.getContent())) {
                        tvArticle.setVisibility(View.VISIBLE);
                        tvArticle.setText(dynamic.getContent());
                        Log.i("position","content = "+dynamic.getContent());
                    } else {
                        tvArticle.setVisibility(View.GONE);
                    }
                }

                if (dynamic.getIsForward() == 0) {//判断是否是转发的
                    ivLocked.setVisibility(View.GONE);
                    tvPicSource.setVisibility(View.GONE);
                } else if (dynamic.getIsForward() == 1) {//已经转发（显示图片来源）
                    tvPicSource.setVisibility(View.VISIBLE);
                } else if (dynamic.getIsForward() == 2) {//已分享（显示已分享）
                    tvPicSource.setText(mContext.getResources().getString(R.string.repeat));
                }
                if (!dynamic.getUserId().equals(userId)) {//是否是自己发布的
                    tvStick.setVisibility(View.GONE);
                    tvDelete.setVisibility(View.GONE);
                    tvAllSave.setVisibility(View.VISIBLE);
                }else {
                    tvStick.setVisibility(View.VISIBLE);
                    tvDelete.setVisibility(View.VISIBLE);
                    tvAllSave.setVisibility(View.GONE);
                }
                if (dynamic.getDynamicType() == 1) {//图片展示
                    if (dynamic.getDynamicPhotos() != null) {
                        if (dynamic.getDynamicPhotos().size() != 0) {
                            layoutNineImage.setVisibility(View.VISIBLE);
                            ArrayList<String> imageUrls = new ArrayList<>();
                            for (DynamicPhoto photo : dynamic.getDynamicPhotos()) {
                                imageUrls.add(photo.getThumbnailURL());
                            }
                            layoutNineGrid.setUrlList(imageUrls, false);
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
                            layoutNineGrid.setUrlList(imageUrls, true);
                            //ivIconPlay.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void deleteSuccess(int position) {
        mList.remove(position);
        notifyDataSetChanged();
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
