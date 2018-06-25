package com.zq.dynamicphoto.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.ui.widge.NineGridImageLayout;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/11.
 */

public class DynamicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int HEAD_TYPE = 00001;
    private static final int BODY_TYPE = 00002;
    private int headCount = 1;//头部个数，后续可以自己拓展
    private ArrayList<Dynamic> mList;
    private Context mContext;
    private Integer userId;
    private String userLogo, realName, bgUrl;

    private HeadViewHolder headViewHolder;

    private DeviceProperties dr = DrUtils.getInstance();

    private MyClickListener mListener;

    private int getBodySize() {
        return mList.size();
    }

    private boolean isHead(int position) {
        return headCount != 0 && position < headCount;
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {
        void clickListener(View view,int position,NetRequestBean netRequestBean);
    }

    public ArrayList<Dynamic> getmList() {
        return mList;
    }

    public void setmList(ArrayList<Dynamic> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHead(position)) {
            return HEAD_TYPE;
        } else {
            return BODY_TYPE;
        }
    }

    public DynamicListAdapter(Context context, ArrayList<Dynamic> mList,MyClickListener listener) {
        this.mContext = context;
        this.mList = mList;
        this.mListener = listener;
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        userId = sp.getInt("userId", 0);
        userLogo = sp.getString("userLogo", "");
        realName = sp.getString("remarkName", "");
        bgUrl = sp.getString("bgImage", "");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD_TYPE:
                headViewHolder = new HeadViewHolder(View.inflate(mContext, R.layout.layout_dynamic_head, null));
                return headViewHolder;
            case BODY_TYPE:
                return new DynamicViewHolder(View.inflate(mContext, R.layout.photo_dynamic_list, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder parent, final int position) {
        if (parent instanceof HeadViewHolder){
            HeadViewHolder holder = (HeadViewHolder) parent;
            holder.bind(position);
        }else if (parent instanceof DynamicViewHolder){
            DynamicViewHolder holder = (DynamicViewHolder) parent;
            final Dynamic dynamic = mList.get(position - 1);
            holder.bind(position-1);
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetRequestBean netRequestBean = new NetRequestBean();
                    netRequestBean.setDeviceProperties(dr);
                    netRequestBean.setDynamic(dynamic);
                    mListener.clickListener(v,position - 1,netRequestBean);
                    mList.remove(position-1);
                    notifyDataSetChanged();
                }
            });
            holder.tvStick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetRequestBean netRequestBean = new NetRequestBean();
                    netRequestBean.setDeviceProperties(dr);
                    netRequestBean.setDynamic(dynamic);
                    mListener.clickListener(v,position - 1,netRequestBean);
                }
            });
            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoEditDynamicActivity(mContext,dynamic);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mList == null ? headCount : getBodySize()+headCount;
    }

    class DynamicViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.iv_icon_play)
        ImageView ivIconPlay;
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

        DynamicViewHolder(View view) {
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
                            ivIconPlay.setVisibility(View.VISIBLE);
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

    class HeadViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_bg)
        ImageView ivBg;
        @BindView(R.id.iv_my_avatar)
        ImageView ivMyAvatar;
        @BindView(R.id.layout_avatar)
        AutoRelativeLayout layoutAvatar;
        @BindView(R.id.tv_my_nick)
        TextView tvMyNick;
        @BindView(R.id.layout_bg)
        AutoRelativeLayout layoutBg;
        @BindView(R.id.iv_search)
        ImageView ivSearch;
        @BindView(R.id.et_search)
        EditText etSearch;
        @BindView(R.id.layout_search)
        AutoRelativeLayout layoutSearch;
        @BindView(R.id.iv_two_code)
        ImageView ivTwoCode;
        @BindView(R.id.layout_share_my_photo)
        AutoRelativeLayout layoutShareMyPhoto;
        @BindView(R.id.layout_dynamic_top)
        AutoLinearLayout layoutDynamicTop;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(int position) {
            if (!TextUtils.isEmpty(bgUrl)){
                ImageLoaderUtils.displayImg(ivBg,bgUrl);
            }
            if (!TextUtils.isEmpty(userLogo)){
                ImageLoaderUtils.displayImg(ivMyAvatar,userLogo);
            }
            if (!TextUtils.isEmpty(realName)){
                tvMyNick.setText(realName);
            }
        }
    }
}
