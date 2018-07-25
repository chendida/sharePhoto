package com.zq.dynamicphoto.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.mylive.bean.LiveConsumeRecord;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/25.
 */

public class ConsumptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<LiveConsumeRecord> mList;

    public ConsumptionAdapter(ArrayList<LiveConsumeRecord> mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConsumptionHolder(View.inflate(parent.getContext(),
                R.layout.layout_consumption_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, int position) {
        ConsumptionHolder holder = (ConsumptionHolder) parent;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ConsumptionHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_live_title)
        TextView tvLiveTitle;
        @BindView(R.id.tv_live_start_time)
        TextView tvStartTime;
        @BindView(R.id.tv_live_end_time)
        TextView tvEndTime;
        @BindView(R.id.tv_watch_number)
        TextView tvWatchNumber;
        @BindView(R.id.tv_diamond)
        TextView tvDiamond;

        ConsumptionHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            LiveConsumeRecord liveConsumeRecord = mList.get(position);
            if (liveConsumeRecord != null){
                if (!TextUtils.isEmpty(liveConsumeRecord.getCover())){
                    ImageLoaderUtils.displayImg(ivCover,liveConsumeRecord.getCover());
                }
                if (!TextUtils.isEmpty(liveConsumeRecord.getTitle())){
                    tvLiveTitle.setText(liveConsumeRecord.getTitle());
                }
                if (liveConsumeRecord.getPeopleNum() != null){
                    tvWatchNumber.setText(String.valueOf(liveConsumeRecord.getPeopleNum()));
                }
                if (liveConsumeRecord.getConsumeCoin() != null){
                    tvDiamond.setText(String.valueOf(liveConsumeRecord.getConsumeCoin()));
                }
                if (!TextUtils.isEmpty(liveConsumeRecord.getStartTime())){
                    tvStartTime.setText(liveConsumeRecord.getStartTime());
                }
                if (!TextUtils.isEmpty(liveConsumeRecord.getEndTime())){
                    tvEndTime.setText(liveConsumeRecord.getEndTime());
                }
            }
        }
    }

    public void addConsumptionList(ArrayList<LiveConsumeRecord> liveConsumeRecords) {
        this.mList.addAll(liveConsumeRecords);
        notifyDataSetChanged();
    }

    public void initConsumptionList(ArrayList<LiveConsumeRecord> liveConsumeRecords) {
        if (mList != null) {
            this.mList.clear();
        }else {
            mList = new ArrayList<>();
        }
        addConsumptionList(liveConsumeRecords);
    }
}
