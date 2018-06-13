package com.zq.dynamicphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zq.dynamicphoto.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/13.
 */

public class PicAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private AddPicListener mListener;
    private static int MAX_IMAGE_SIZE = 9;    //默认显示的图片个数


    public interface AddPicListener {
        void onAddButtonClick(View view, int i); //添加按钮点击事件
    }

    public PicAdapter(Context mContext, List<String> mList, AddPicListener mListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 1;
        }
        if (mList.size() > MAX_IMAGE_SIZE) {
            return MAX_IMAGE_SIZE + 1;
        }
        return mList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position < mList.size() ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PicViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
            holder = new PicViewHolder(convertView);
            holder.idItemAddPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddButtonClick(v,position);
                }
            });
            holder.ivItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddButtonClick(v,position);
                }
            });
            holder.ivDeletePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            convertView.setTag(holder);
        }else {
            holder = (PicViewHolder) convertView.getTag();
        }
        if (position == getCount() -1 && position < MAX_IMAGE_SIZE){
            holder.idItemAddPic.setVisibility(View.VISIBLE);
            holder.layoutShowPic.setVisibility(View.GONE);
        }else if (position < getCount() - 1){
            holder.idItemAddPic.setVisibility(View.VISIBLE);
            holder.layoutShowPic.setVisibility(View.VISIBLE);
        }else {
            holder.idItemAddPic.setVisibility(View.GONE);
            holder.layoutShowPic.setVisibility(View.GONE);
        }
        return convertView;
    }

    class PicViewHolder {
        @BindView(R.id.id_item_add_pic)
        ImageView idItemAddPic;
        @BindView(R.id.iv_item_image_view)
        ImageView ivItemImageView;
        @BindView(R.id.iv_delete_pic)
        ImageView ivDeletePic;
        @BindView(R.id.layout_show_pic)
        RelativeLayout layoutShowPic;

        PicViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
