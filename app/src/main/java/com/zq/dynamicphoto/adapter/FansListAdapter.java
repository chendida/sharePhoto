package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
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
 * Created by Administrator on 2018/6/26.
 */

public class FansListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FOOTER_TYPE = 00002;
    private static final int BODY_TYPE = 00001;
    private int footerCount = 1;//footer个数，后续可以自己拓展
    private Activity mContext;
    private ArrayList<UserInfo> mList;

    public FansListAdapter(Activity mContext, ArrayList<UserInfo> mList) {
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER_TYPE:
                return new FooterViewHolder(View.inflate(mContext, R.layout.layout_fans_list_footer, null));
            case BODY_TYPE:
                return new FansListViewHolder(View.inflate(mContext, R.layout.follow_list_item, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder parent, final int position) {
        if (parent instanceof FansListViewHolder){
            FansListViewHolder holder = (FansListViewHolder) parent;
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
            holder.layoutHowAddFans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoHtmlManagerActivity(mContext,"fans.html",
                            mContext.getResources().getString(R.string.tv_how_to_have_more_fans));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? footerCount : getBodySize() + footerCount;
    }

    class FansListViewHolder extends RecyclerView.ViewHolder {
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

        FansListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            UserInfo userInfo = mList.get(position);
            if (userInfo != null) {
                if (!TextUtils.isEmpty(userInfo.getUserLogo())) {
                    ImageLoaderUtils.displayImg(ivAvatar, userInfo.getUserLogo());
                }
                if (!TextUtils.isEmpty(userInfo.getRemarkName())) {
                    tvNick.setText(userInfo.getRemarkName());
                }
                if (position == mList.size()) {
                    tvLine.setVisibility(View.GONE);
                }
                if (userInfo.getAddDynamicNum() != null) {
                    tvAddNewNum.setText(userInfo.getAddDynamicNum() + "");
                }
                if (userInfo.getTotalDynamicNum() != null) {
                    tvAddAllNum.setText(userInfo.getTotalDynamicNum() + "");
                }
            }
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_how_add_fans)
        AutoRelativeLayout layoutHowAddFans;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void addFansList(ArrayList<UserInfo> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initFansList(ArrayList<UserInfo> dynamicList) {
        if (mList != null) {
            this.mList.clear();
            addFansList(dynamicList);
        }
    }
}
