package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.MFGT;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已关注列表适配器
 * Created by Administrator on 2018/3/1.
 */

public class FollowListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FOOTER_TYPE = 00002;
    private static final int BODY_TYPE = 00001;
    private int footerCount = 1;//footer个数，后续可以自己拓展
    private Activity mContext;
    private ArrayList<UserInfo> mList;

    public FollowListAdapter(Activity mContext, ArrayList<UserInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    private boolean isFooter(int position) {
        return footerCount != 0 && position == getBodySize();
    }

    private int getBodySize() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return FOOTER_TYPE;
        } else {
            return BODY_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER_TYPE:
                return new FooterViewHolder(View.inflate(mContext, R.layout.layout_follow_list_footer, null));
            case BODY_TYPE:
                return new FollowListViewHolder(View.inflate(mContext, R.layout.follow_list_item, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, final int position) {
        if (parent instanceof FollowListViewHolder){
            FollowListViewHolder holder = (FollowListViewHolder) parent;
            holder.bind(position);
            holder.layoutAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MFGT.gotoHtmlPhotoDetailsActivity(mContext,"friends.html?userId="+
                            mList.get(position).getUserId(),
                            mContext.getResources().getString(R.string.tv_photo_details),
                            mList.get(position).getUserId());
                }
            });
        }else if (parent instanceof FooterViewHolder){
            FooterViewHolder holder = (FooterViewHolder) parent;
            holder.layoutNoSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoHtmlManagerActivity(mContext,"sharechild1.html",
                            mContext.getResources().getString(R.string.tv_no_see));
                }
            });
            holder.layoutNewRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoHtmlManagerActivity(mContext,"sharechild2.html",
                            mContext.getResources().getString(R.string.tv_new_user_must_read));
                }
            });
            holder.layoutPrivateProtected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoHtmlManagerActivity(mContext,"sharechild3.html",
                            mContext.getResources().getString(R.string.tv_private_protected));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? footerCount : getBodySize() + footerCount;
    }

    class FollowListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_nick)
        TextView tvNick;
        @BindView(R.id.tv_add_new_num)
        TextView tvAddNewNum;
        @BindView(R.id.tv_add_all_num)
        TextView tvAddAllNum;
        @BindView(R.id.tv_line)
        TextView tvLine;
        @BindView(R.id.layout_avatar)
        AutoRelativeLayout layoutAvatar;

        FollowListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            UserInfo userInfo = mList.get(position);
            if (userInfo != null){
                if (!TextUtils.isEmpty(userInfo.getUserLogo())){
                    ImageLoaderUtils.displayImg(ivAvatar,userInfo.getUserLogo());
                }
                if (!TextUtils.isEmpty(userInfo.getRemarkName())){
                    tvNick.setText(userInfo.getRemarkName());
                }
                if (position == mList.size()){
                    tvLine.setVisibility(View.GONE);
                }
                if (userInfo.getAddDynamicNum() != null){
                    tvAddNewNum.setText(userInfo.getAddDynamicNum()+"");
                }
                if (userInfo.getTotalDynamicNum() != null){
                    tvAddAllNum.setText(userInfo.getTotalDynamicNum()+"");
                }
            }
        }
    }

    public void addFollowList(ArrayList<UserInfo> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initFollowList(ArrayList<UserInfo> dynamicList) {
        if (mList != null) {
            this.mList.clear();
            addFollowList(dynamicList);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.layout_no_see)
        AutoRelativeLayout layoutNoSee;
        @BindView(R.id.layout_new_read)
        AutoRelativeLayout layoutNewRead;
        @BindView(R.id.layout_private_protected)
        AutoRelativeLayout layoutPrivateProtected;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
