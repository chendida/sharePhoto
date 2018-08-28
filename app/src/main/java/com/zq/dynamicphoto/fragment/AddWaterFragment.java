package com.zq.dynamicphoto.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.PhotoListAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.Folder;
import com.zq.dynamicphoto.bean.ImageModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 朋友圈
 */
public class AddWaterFragment extends BaseFragment implements PhotoListAdapter.SelectListener{
    private static final String TAG = "AddWaterFragment";
    @BindView(R.id.rcl_photo_dir_list)
    RecyclerView rclPhotoDirList;
    private ArrayList<Folder> imageBuckets;
    private PhotoListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_circle;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {
        if (imageBuckets == null){
            imageBuckets = new ArrayList<>();
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rclPhotoDirList.setLayoutManager(manager);
        mAdapter = new PhotoListAdapter(imageBuckets,false,this);
        rclPhotoDirList.setAdapter(mAdapter);
        rclPhotoDirList.setNestedScrollingEnabled(false);
        rclPhotoDirList.setHasFixedSize(true);
    }

    /**
     * 从SDCard加载图片。
     */
    private void loadImageForSDCard() {
        ImageModel.loadImageForSDCard(getActivity(), new ImageModel.DataCallback() {
            @Override
            public void onSuccess(final ArrayList<Folder> folders) {
                //folders是图片文件夹的列表，每个文件夹中都有若干张图片。
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.initFolders(folders);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadImageForSDCard();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {
        //loadImageForSDCard();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageBuckets = null;
    }

    @Override
    public void selectListener(Folder folder) {

    }
}
