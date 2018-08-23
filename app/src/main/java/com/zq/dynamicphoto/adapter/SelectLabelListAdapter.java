package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.utils.SaveSelectLabelUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索时的标签选择适配器
 * Created by Administrator on 2018/3/23.
 */

public class SelectLabelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SelectLabelListAdapter";
    private static final int HEAD_TYPE = 00001;
    private static final int BODY_TYPE = 00002;
    private int headCount = 1;//头部个数，后续可以自己拓展
    private Activity mContext;
    private ArrayList<DynamicLabel> mList;

    public SelectLabelListAdapter(Activity mContext, ArrayList<DynamicLabel> mList) {
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
                return new SelectLabelListViewHolder(View.inflate(mContext, R.layout.label_select_item, null));
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
                    DynamicLabel dynamicLabel = new DynamicLabel();
                    dynamicLabel.setId(-1);
                    dynamicLabel.setLabeltext("不限");
                    SaveSelectLabelUtils.getInstance().setDynamicLabel(dynamicLabel);
                    mContext.finish();
                }
            });
        }else if (parent instanceof SelectLabelListViewHolder){
            final SelectLabelListViewHolder holder = (SelectLabelListViewHolder) parent;
            holder.bind(position - 1);
            holder.layoutLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.ivIsSelect.setVisibility(View.VISIBLE);
                    SaveSelectLabelUtils.getInstance().setDynamicLabel(mList.get(position-1));
                    mContext.finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getBodySize() + headCount;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.layout_no_limit)
        AutoRelativeLayout layoutNoLimit;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind() {
            DynamicLabel dynamicLabel = SaveSelectLabelUtils.getInstance().getDynamicLabel();
            if (dynamicLabel != null){
                if (dynamicLabel.getId() != null) {
                    if (dynamicLabel.getId() == -1) {
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

    class SelectLabelListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.label_name)
        TextView labelName;
        @BindView(R.id.iv_is_select)
        ImageView ivIsSelect;
        @BindView(R.id.layout_label)
        AutoRelativeLayout layoutLabel;

        SelectLabelListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            DynamicLabel dynamicLabel = mList.get(position);
            if (dynamicLabel != null){
                if (!TextUtils.isEmpty(dynamicLabel.getLabeltext())){
                    labelName.setText(dynamicLabel.getLabeltext());
                }
            }

            DynamicLabel selectDynamicLabel = SaveSelectLabelUtils.getInstance().getDynamicLabel();
            if (selectDynamicLabel != null){
                if (dynamicLabel != null) {
                    Log.i(TAG,selectDynamicLabel.getId()+"");
                    if (selectDynamicLabel.getId() != null) {
                        if (selectDynamicLabel.getId().equals(dynamicLabel.getId())) {
                            ivIsSelect.setVisibility(View.VISIBLE);
                        } else {
                            ivIsSelect.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    public void addLabelList(ArrayList<DynamicLabel> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initLabelList(ArrayList<DynamicLabel> dynamicList) {
        this.mList.clear();
        addLabelList(dynamicList);
    }
}
