package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.config.PictureConfig;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.DynamicPhoto;
import com.zq.dynamicphoto.bean.DynamicVideo;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DynamicSelectAdapter extends RecyclerView.Adapter<DynamicSelectAdapter.DynamicItemViewHolder> {
    ArrayList<Dynamic>mList;
    private ArrayList<Dynamic> selectList = new ArrayList<>();
    private Integer mostSelectDynamic;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();

    public DynamicSelectAdapter(ArrayList<Dynamic> mList,Integer mostSelectDynamic) {
        this.mList = mList;
        this.mostSelectDynamic = mostSelectDynamic;
    }

    public ArrayList<Dynamic> getSelectList(){
        return selectList;
    }

    @NonNull
    @Override
    public DynamicItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic, null, false);
        return new DynamicItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DynamicItemViewHolder holder, final int position) {
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                holder.mCheckBox.setTag(mList.get(position).getId());
                Integer pos = (int) compoundButton.getTag();
                if (isChecked) {
                    mCheckStates.put(pos, true);
                    Boolean flag = false;
                    if (selectList.size() == 0){
                        selectList.add(mList.get(position));
                    }else {
                        for (Dynamic dynamic : selectList) {
                            if (mList.get(position).getId().equals(dynamic.getId())) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag && selectList.size() <= mostSelectDynamic){
                            selectList.add(mList.get(position));
                        }
                    }
                } else {
                    mCheckStates.delete(pos);
                    Boolean flag = false;
                    int selectPosition = 0;
                    for (int i = 0;i<selectList.size(); i ++) {
                        if (mList.get(position).getId().equals(selectList.get(i).getId())){
                            flag = true;
                            selectPosition = i;
                            break;
                        }
                    }
                    if (flag){
                        selectList.remove(selectPosition);
                    }
                }
                if (mCheckStates.size() > mostSelectDynamic){
                    mCheckStates.delete(pos);
                    ToastUtils.showShort("最多选择" + mostSelectDynamic + "条动态");
                    holder.mCheckBox.setChecked(mCheckStates.get(mList.get(position).getId(), false));
                }
            }
        });
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class DynamicItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_select)
        CheckBox mCheckBox;
        @BindView(R.id.layout_select)
        AutoRelativeLayout layoutSelect;
        @BindView(R.id.tv_article)
        TextView tvArticle;
        @BindView(R.id.rcl_dynamic_pic_list)
        RecyclerView rclDynamicPicList;
        DynamicPicAdapter mAdapter;
        ArrayList<String>urlList;

        DynamicItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            initView();
        }

        private void initView() {
            if (urlList == null){
                urlList = new ArrayList<>();
            }
            LinearLayoutManager manager = new LinearLayoutManager(mCheckBox.getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            rclDynamicPicList.setLayoutManager(manager);
        }

        public void bind(int position) {
            Dynamic dynamic = mList.get(position);
            if (dynamic != null){
                mCheckBox.setChecked(mCheckStates.get(dynamic.getId(), false));
                if (dynamic.getIsCheck() != null || dynamic.getIsMoments() != null){
                    mCheckBox.setChecked(true);
                }
                if (!TextUtils.isEmpty(dynamic.getContent())){
                    tvArticle.setVisibility(View.VISIBLE);
                    tvArticle.setText(dynamic.getContent());
                }else {
                    tvArticle.setVisibility(View.GONE);
                }
                if (dynamic.getDynamicType() == PictureConfig.TYPE_VIDEO){
                    if (dynamic.getDynamicVideos() != null){
                        if (dynamic.getDynamicVideos().size() != 0){
                            urlList.clear();
                            for (DynamicVideo video:dynamic.getDynamicVideos()) {
                                urlList.add(video.getVideoURL());
                            }
                            mAdapter = new DynamicPicAdapter(urlList);
                            rclDynamicPicList.setAdapter(mAdapter);
                        }
                    }
                }else {
                    if (dynamic.getDynamicPhotos() != null){
                        if (dynamic.getDynamicPhotos().size() != 0){
                            urlList.clear();
                            for (DynamicPhoto photo:dynamic.getDynamicPhotos()) {
                                urlList.add(photo.getThumbnailURL());
                            }
                            mAdapter = new DynamicPicAdapter(urlList);
                            rclDynamicPicList.setAdapter(mAdapter);
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
}
