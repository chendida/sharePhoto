package com.zq.dynamicphoto.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.WaterMouldItemAdapter;
import com.zq.dynamicphoto.adapter.WaterMouldListAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.Watermark;
import com.zq.dynamicphoto.bean.WatermarkType;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.WaterMouldPresenter;
import com.zq.dynamicphoto.ui.LabelWatermarkActivity;
import com.zq.dynamicphoto.utils.MFGT;
import com.zq.dynamicphoto.view.EditWaterListener;
import com.zq.dynamicphoto.view.IGetWaterMouldView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * 水印模板的获取
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class WaterMouldFragment extends BaseFragment<IGetWaterMouldView,
        WaterMouldPresenter<IGetWaterMouldView>> implements IGetWaterMouldView,EditWaterListener{
    private static final String TAG = "WaterMouldFragment";
    @BindView(R.id.rcl_water_list)
    RecyclerView rclWaterList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private WaterMouldListAdapter mAdapter;
    private ArrayList<WatermarkType> watermarks = new ArrayList<>();
    private ArrayList<Watermark>singleWatermarks = new ArrayList<>();
    private int position = 0;
    private WaterMouldItemAdapter adapter;
    @SuppressLint("ValidFragment")
    public WaterMouldFragment(int position) {
        this.position = position;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_water_mould;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected WaterMouldPresenter<IGetWaterMouldView> createPresenter() {
        return new WaterMouldPresenter<>();
    }

    @Override
    protected void loadData() {
        Log.i("loadData","position = " + position);
        if (position == 0) {
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            rclWaterList.setLayoutManager(manager);
            mAdapter = new WaterMouldListAdapter(watermarks, position,this);
            rclWaterList.setAdapter(mAdapter);
        }else {
            rclWaterList.setLayoutManager(new StaggeredGridLayoutManager(3
                    ,StaggeredGridLayoutManager.VERTICAL));
            adapter = new WaterMouldItemAdapter(singleWatermarks,this);
            rclWaterList.setAdapter(adapter);
        }
        rclWaterList.setNestedScrollingEnabled(false);
        rclWaterList.setHasFixedSize(true);
        DeviceProperties dr = DrUtils.getInstance();
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        Watermark watermark = new Watermark();
        if (mPresenter != null){
            if (position != 0){
                watermark.setWatermarkType(position);
                netRequestBean.setWatermark(watermark);
                mPresenter.getSingleWaterList(netRequestBean);
            }else {
                mPresenter.getAllWaterList(netRequestBean);
            }
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            watermarks = new Gson().fromJson(jsonObject.optString("watermarkTypeList"), new TypeToken<List<WatermarkType>>() {
            }.getType());
            Log.i("dynamicList", "size = " + watermarks.size());
            if (watermarks.size() != 0) {
                mAdapter.initWatermarkList(watermarks);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllWaterMould(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    @Override
    public void getSingleWaterMould(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithSingleResult(result);
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    private void dealWithSingleResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            singleWatermarks = new Gson().fromJson(jsonObject.optString("watermarkList"), new TypeToken<List<Watermark>>() {
            }.getType());
            Log.i("loadData", "size = " + singleWatermarks.size());
            if (singleWatermarks.size() != 0) {
                adapter.initWatermarkList(singleWatermarks);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void click(Watermark watermark) {
        String watermarkId = watermark.getWatermarkId();
        if (watermarkId.startsWith("5")) {
            if (watermarkId.equals("5009") || watermarkId.equals("5014")){
                MFGT.gotoEditWatermark5009Activity(this.getActivity(),watermarkId);
            }else {
                MFGT.gotoEditWaterActivity(this.getActivity(), watermarkId);
            }
        }else if (watermarkId.startsWith("6")){
            MFGT.gotoMoneyWatermarkActivity(this.getActivity(),watermarkId);
        }else if (watermarkId.startsWith("7")||watermarkId.startsWith("3")
                || watermarkId.startsWith("4")
                || watermarkId.startsWith("2")){
            MFGT.gotoRecommendActivity(this.getActivity(),watermarkId);
        }else if (watermarkId.startsWith("1")){
            MFGT.gotoLabelWatermarkActivity(this.getActivity(),watermarkId);
        }
    }
}
