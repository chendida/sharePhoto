package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.ui.widge.EditLabelDialog;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/22.
 */

public class LabelListAdapter extends RecyclerView.Adapter {
    ArrayList<DynamicLabel> mList;
    private Activity mContext;
    EditLabelDialog editLabelDialog;
    private int position;
    private MyClickListener mListener;
    private View editView;
    public LabelListAdapter(ArrayList<DynamicLabel> mList, Activity mContext,MyClickListener listener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LabelListViewHolder(View.inflate(mContext, R.layout.label_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, final int position) {
        LabelListViewHolder holder = (LabelListViewHolder) parent;
        holder.bind(position);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLabel(view,mList.get(position),position);
            }
        });
        holder.layoutLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editView = view;
                setPosition(position);
                showEditDialog(mList.get(position).getLabeltext());
            }
        });
    }

    private void showEditDialog(String labeltext) {
        editLabelDialog = new EditLabelDialog(mContext, R.style.dialog, clickListener,labeltext);
        editLabelDialog.show();
    }

    private void deleteLabel(View view,DynamicLabel dynamicLabel, int position) {
        DeviceProperties dr = DrUtils.getInstance();
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setDynamicLabel(dynamicLabel);
        mListener.clickListener(view,position,netRequestBean);
        mList.remove(position);
        notifyDataSetChanged();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private EditLabelDialog.OnItemClickListener clickListener = new EditLabelDialog.OnItemClickListener(){
        @Override
        public void onClick(Dialog dialog, int position) {
            dialog.dismiss();
            switch (position){
                case 1:
                    String labelName = editLabelDialog.et_label.getText().toString();
                    editLabelName(labelName);
                    break;
            }
        }
    };

    private void editLabelName(String labelName) {
        if (TextUtils.isEmpty(labelName)){
            ToastUtils.showShort("标签名不能为空");
            return;
        }
        DynamicLabel dynamicLabel = mList.get(getPosition());
        DeviceProperties dr = DrUtils.getInstance();
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        dynamicLabel.setLabeltext(labelName);
        netRequestBean.setDynamicLabel(dynamicLabel);
        mListener.clickListener(editView,getPosition(),netRequestBean);
        mList.get(getPosition()).setLabeltext(labelName);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LabelListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.tv_label_count)
        TextView tvLabelCount;
        @BindView(R.id.layout_label)
        AutoRelativeLayout layoutLabel;
        @BindView(R.id.btnDelete)
        Button btnDelete;

        LabelListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(int position) {
            DynamicLabel dynamicLabel = mList.get(position);
            if (dynamicLabel != null){
                content.setText(dynamicLabel.getLabeltext());
            }
            if (dynamicLabel.getCountNum() != null){
                tvLabelCount.setText("("+ dynamicLabel.getCountNum()+")");
            }
        }
    }

    private void addDynamicLabelList(ArrayList<DynamicLabel> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initDynamicLabelList(ArrayList<DynamicLabel> dynamicList) {
        this.mList.clear();
        addDynamicLabelList(dynamicList);
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {
        void clickListener(View view,int position,NetRequestBean netRequestBean);
    }
}
