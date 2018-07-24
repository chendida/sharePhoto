package com.zq.dynamicphoto.mylive.ui;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.DynamicSelectAdapter;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.mylive.bean.LiveDynamic;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.presenter.LiveGoodsPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import com.zq.dynamicphoto.view.AddLiveGoodView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddLiveGoodActivity extends BaseActivity<AddLiveGoodView,
        LiveGoodsPresenter<AddLiveGoodView>> implements AddLiveGoodView{
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rcl_select_photo_list)
    RecyclerView rclSelectPhotoList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.layout_clear_input)
    AutoRelativeLayout layoutClearInput;
    @BindView(R.id.layout_search)
    AutoRelativeLayout layoutSearch;
    @BindView(R.id.layout_filter)
    AutoRelativeLayout layoutFilter;
    private Integer pager = 1;
    private Integer pagerCount = 0;
    DynamicSelectAdapter mAdapter;
    ArrayList<Dynamic> dynamicsList = new ArrayList<>();
    private boolean mIsRefreshing = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_live_good;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void initView() {
        layoutBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.select_live_good));
        ivCamera.setVisibility(View.GONE);
        tvFinish.setVisibility(View.VISIBLE);
        tvFinish.setText(getResources().getString(R.string.finish));
        //竖直排列、正向排序
        rclSelectPhotoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DynamicSelectAdapter(dynamicsList,15);
        rclSelectPhotoList.setAdapter(mAdapter);
        rclSelectPhotoList.setHasFixedSize(true);
        rclSelectPhotoList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mIsRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftUtils.softShow(AddLiveGoodActivity.this);
                    pager = 1;
                    getLiveGoods(pager);
                    return true;
                }
                return false;
            }
        });
    }

    private void getLiveGoods(Integer pager) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        Dynamic dynamic = new Dynamic();
        dynamic.setUserId(userId);
        dynamic.setDynamicType(1);
        dynamic.setPage(pager);
        dynamic.setTitle(etSearch.getText().toString());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setDynamic(dynamic);
        if (mPresenter != null){
            mPresenter.getLiveGoodList(netRequestBean);
        }
    }

    @OnClick({R.id.layout_back, R.id.layout_finish, R.id.layout_search,
            R.id.layout_clear_input, R.id.layout_filter})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish:
                ArrayList<Dynamic> selectList = mAdapter.getSelectList();
                if (selectList.size() == 0){
                    ToastUtils.showShort("最少选择一个产品");
                    return;
                }else {
                    uploadLiveGoodList(selectList);
                }
                break;
            case R.id.layout_search:
                etSearch.setFocusable(true);
                etSearch.setFocusableInTouchMode(true);
                etSearch.requestFocus();
                SoftUtils.oneShowSoft(etSearch);
                break;
            case R.id.layout_clear_input:
                etSearch.setText(null);
                break;
            case R.id.layout_filter:
                pager = 1;
                getLiveGoods(pager);
                break;
        }
    }

    private void uploadLiveGoodList(ArrayList<Dynamic> selectList) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp =
                SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        NewLiveRoom newLiveRoom = new NewLiveRoom();
        ArrayList<LiveDynamic>liveDynamics = new ArrayList<>();
        newLiveRoom.setUserId(userId);
        for (Dynamic dynamic:selectList) {
            LiveDynamic liveDynamic = new LiveDynamic();
            liveDynamic.setDynamicId(dynamic.getId());
            liveDynamic.setPromoteImg(dynamic.getDynamicPhotos().get(0).getThumbnailURL());
            liveDynamics.add(liveDynamic);
        }
        newLiveRoom.setLiveDynamicList(liveDynamics);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setNewLiveRoom(newLiveRoom);
        if (mPresenter != null){
            mPresenter.uploadLiveGoods(netRequestBean);
        }
    }

    @Override
    protected void initData() {
        pager = 1;
        getLiveGoods(pager);
    }

    @Override
    protected LiveGoodsPresenter<AddLiveGoodView> createPresenter() {
        return new LiveGoodsPresenter<>();
    }

    @Override
    public void showGetLiveGoodListResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithResult(result);
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            pagerCount = jsonObject.optInt("pageCount");
            dynamicsList = new Gson().fromJson(jsonObject.optString("dynamicList"),
                    new TypeToken<List<Dynamic>>() {
                    }.getType());
            if (dynamicsList != null) {
                if (dynamicsList.size() != 0) {
                    if (pager == 1) {
                        mAdapter.initDynamicList(dynamicsList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.addDynamicList(dynamicsList);
                    }
                }else {
                    if (pager == 1){
                        mAdapter.initDynamicList(dynamicsList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAddLiveGoodsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                ToastUtils.showShort("添加产品成功");
                finish();
            } else {
                showFailed();
            }
        } else {
            showFailed();
        }
    }
}
