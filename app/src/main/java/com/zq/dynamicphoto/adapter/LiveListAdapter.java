package com.zq.dynamicphoto.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.mylive.bean.LiveDynamic;
import com.zq.dynamicphoto.mylive.bean.LiveUser;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.view.GotoLiveRoomListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/7.
 */

public class LiveListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<NewLiveRoom> mList;
    private static final String TAG = "LiveListAdapter";
    private GotoLiveRoomListener listener;
    public LiveListAdapter(Context mContext, ArrayList<NewLiveRoom> mList,GotoLiveRoomListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LiveListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.live_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, final int position) {
        LiveListViewHolder holder = (LiveListViewHolder) parent;
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLiveRoom(mList.get(position));
            }
        });
    }

    private void gotoLiveRoom(NewLiveRoom newLiveRoom) {
        final DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt("userId", 0);
        String unionId = sp.getString("unionId", "");
        LiveUser liveUser = new LiveUser();
        liveUser.setUnionid(unionId);
        liveUser.setUserId(userId);
        liveUser.setNewLiveId(newLiveRoom.getId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setNewLiveRoom(newLiveRoom);
        listener.gotoLiveRoom(newLiveRoom,netRequestBean);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LiveListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.anchor_cover)
        ImageView anchorCover;
        @BindView(R.id.tv_live_title)
        TextView tvLiveTitle;
        @BindView(R.id.tv_nick)
        TextView tvNick;
        @BindView(R.id.iv_live_pic1)
        ImageView ivLivePic1;
        @BindView(R.id.iv_live_pic2)
        ImageView ivLivePic2;
        @BindView(R.id.iv_live_pic3)
        ImageView ivLivePic3;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_watch_number)
        TextView tvWatchNumber;
        @BindView(R.id.tv_live_room_dinazan_number)
        TextView tvDianzanNumber;
        @BindView(R.id.layout_goods_num)
        AutoRelativeLayout layoutGoodsNum;


        LiveListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            NewLiveRoom newLiveRoom = mList.get(position);
            if (newLiveRoom != null){
                if (!TextUtils.isEmpty(newLiveRoom.getCover())){
                    Log.i("cover","cover = "+newLiveRoom.getCover());
                    Glide.with(mContext).load(newLiveRoom.getCover()).into(anchorCover);
                }
                if (!TextUtils.isEmpty(newLiveRoom.getTitle())){
                    tvLiveTitle.setText(newLiveRoom.getTitle());
                }
                if (!TextUtils.isEmpty(newLiveRoom.getRemarkName())){
                    tvNick.setText(newLiveRoom.getRemarkName());
                }
                if (newLiveRoom.getAdmireCount() != null){
                    tvDianzanNumber.setText(String.valueOf(newLiveRoom.getAdmireCount()));
                }
                if (newLiveRoom.getPopularValue() != null){
                    tvWatchNumber.setText(String.valueOf(newLiveRoom.getPopularValue()));
                }
                List<LiveDynamic> liveDynamicList = newLiveRoom.getLiveDynamicList();
                if (liveDynamicList != null){
                    if (liveDynamicList.size() == 1){
                        ivLivePic1.setVisibility(View.VISIBLE);
                        layoutGoodsNum.setVisibility(View.VISIBLE);
                        ivLivePic2.setVisibility(View.GONE);
                        ivLivePic3.setVisibility(View.GONE);
                        tvNum.setText(String.valueOf(liveDynamicList.size()));
                        if (!TextUtils.isEmpty(liveDynamicList.get(0).getPromoteImg())){
                            Glide.with(mContext).load(liveDynamicList.get(0).getPromoteImg()).into(ivLivePic1);
                        }
                    }else if (liveDynamicList.size() == 2){
                        ivLivePic1.setVisibility(View.VISIBLE);
                        layoutGoodsNum.setVisibility(View.VISIBLE);
                        ivLivePic2.setVisibility(View.VISIBLE);
                        ivLivePic3.setVisibility(View.GONE);
                        tvNum.setText(String.valueOf(liveDynamicList.size()));
                        if (!TextUtils.isEmpty(liveDynamicList.get(0).getPromoteImg())){
                            Glide.with(mContext).load(liveDynamicList.get(0).getPromoteImg()).into(ivLivePic1);
                        }
                        if (!TextUtils.isEmpty(liveDynamicList.get(1).getPromoteImg())){
                            Glide.with(mContext).load(liveDynamicList.get(1).getPromoteImg()).into(ivLivePic2);
                        }
                    }else if (liveDynamicList.size() == 0){
                        ivLivePic1.setVisibility(View.GONE);
                        ivLivePic2.setVisibility(View.GONE);
                        ivLivePic3.setVisibility(View.GONE);
                        layoutGoodsNum.setVisibility(View.GONE);
                    }else {
                        ivLivePic1.setVisibility(View.VISIBLE);
                        layoutGoodsNum.setVisibility(View.VISIBLE);
                        ivLivePic2.setVisibility(View.VISIBLE);
                        ivLivePic3.setVisibility(View.VISIBLE);
                        tvNum.setText(String.valueOf(liveDynamicList.size()));
                        if (!TextUtils.isEmpty(liveDynamicList.get(0).getPromoteImg())){
                            Glide.with(mContext).load(liveDynamicList.get(0).getPromoteImg()).into(ivLivePic1);
                        }
                        if (!TextUtils.isEmpty(liveDynamicList.get(1).getPromoteImg())){
                            Glide.with(mContext).load(liveDynamicList.get(1).getPromoteImg()).into(ivLivePic2);
                        }
                        if (!TextUtils.isEmpty(liveDynamicList.get(2).getPromoteImg())){
                            Glide.with(mContext).load(liveDynamicList.get(2).getPromoteImg()).into(ivLivePic3);
                        }
                    }
                }else {
                    ivLivePic1.setVisibility(View.GONE);
                    ivLivePic2.setVisibility(View.GONE);
                    ivLivePic3.setVisibility(View.GONE);
                    layoutGoodsNum.setVisibility(View.GONE);
                }
                if (newLiveRoom.getLiveDynamicList() == null){
                    layoutGoodsNum.setVisibility(View.GONE);
                }else {
                    if (newLiveRoom.getLiveDynamicList().size() == 0){
                        layoutGoodsNum.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public void addLiveList(ArrayList<NewLiveRoom> newLiveRooms) {
        this.mList.addAll(newLiveRooms);
        notifyDataSetChanged();
    }

    public void initLiveList(ArrayList<NewLiveRoom> newLiveRooms) {
        if (mList != null) {
            this.mList.clear();
        }else {
            mList = new ArrayList<>();
        }
        addLiveList(newLiveRooms);
    }
}
