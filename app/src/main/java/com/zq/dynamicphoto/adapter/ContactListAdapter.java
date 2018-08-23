package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.utils.SaveContactSelectUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/23.
 */

public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEAD_TYPE = 00001;
    private static final int BODY_TYPE = 00002;
    private int headCount = 1;//头部个数，后续可以自己拓展
    private Activity mContext;
    private ArrayList<UserInfo> mList;

    public ContactListAdapter(Activity mContext, ArrayList<UserInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    private int getBodySize() {
        return mList.size();
    }

    private boolean isHead(int position) {
        return headCount != 0 && position < headCount;
    }


    @Override
    public int getItemViewType(int position) {
        if (isHead(position)) {
            return HEAD_TYPE;
        } else {
            return BODY_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD_TYPE:
                return new HeadViewHolder(View.inflate(mContext, R.layout.layout_no_limit_head, null));
            case BODY_TYPE:
                return new ContactListViewHolder(View.inflate(mContext, R.layout.contact_list_item, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, final int position) {
        if (parent instanceof HeadViewHolder){
            final HeadViewHolder holder = (HeadViewHolder) parent;
            holder.bind();
            holder.layoutNoLimit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.ivSelect.setVisibility(View.VISIBLE);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(-1);
                    userInfo.setRemarkName("不限");
                    SaveContactSelectUtils.getInstance().setUserInfo(userInfo);
                    mContext.finish();
                }
            });
        }else if (parent instanceof ContactListViewHolder){
            final ContactListViewHolder holder = (ContactListViewHolder) parent;
            holder.bind(position-1);
            holder.layoutUserInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.ivIsSelect.setVisibility(View.VISIBLE);
                    SaveContactSelectUtils.getInstance().setUserInfo(mList.get(position-1));
                    mContext.finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getBodySize()+headCount;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_no_limit)
        AutoRelativeLayout layoutNoLimit;
        @BindView(R.id.iv_select)
        ImageView ivSelect;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind() {
            UserInfo userInfo = SaveContactSelectUtils.getInstance().getUserInfo();
            if (userInfo != null){
                if (userInfo.getUserId() != null) {
                    if (userInfo.getUserId() == -1) {
                        ivSelect.setVisibility(View.VISIBLE);
                    } else {
                        ivSelect.setVisibility(View.GONE);
                    }
                }else {
                    ivSelect.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class ContactListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.iv_is_select)
        ImageView ivIsSelect;
        @BindView(R.id.tv_nick)
        TextView tvNick;
        @BindView(R.id.layout_userinfo)
        AutoRelativeLayout layoutUserInfo;

        ContactListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            UserInfo userInfo = mList.get(position);
            if (userInfo != null){
                if (!TextUtils.isEmpty(userInfo.getUserLogo())){
                    Glide.with(mContext).load(userInfo.getUserLogo()).into(ivAvatar);
                }
                if (!TextUtils.isEmpty(userInfo.getRemarkName())){
                    tvNick.setText(userInfo.getRemarkName());
                }
            }

            UserInfo SelectUserInfo = SaveContactSelectUtils.getInstance().getUserInfo();
            if (SelectUserInfo != null){
                if (userInfo.getUserId().equals(SelectUserInfo.getUserId())){
                    ivIsSelect.setVisibility(View.VISIBLE);
                }else {
                    ivIsSelect.setVisibility(View.GONE);
                }
            }
        }
    }

    public void addContactList(ArrayList<UserInfo> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initContactList(ArrayList<UserInfo> dynamicList) {
        if (mList != null) {
            this.mList.clear();
            addContactList(dynamicList);
        }
    }
}
