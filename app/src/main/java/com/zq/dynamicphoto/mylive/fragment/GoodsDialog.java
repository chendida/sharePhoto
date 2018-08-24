package com.zq.dynamicphoto.mylive.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.LiveGoodsAdapter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.mylive.bean.NewLiveRoom;
import com.zq.dynamicphoto.net.RetrofitApiService;
import com.zq.dynamicphoto.net.util.RetrofitUtil;
import com.zq.dynamicphoto.view.UploadLiveOrder;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * 主播界面产品列表弹窗
 * Created by Administrator on 2018/5/3.
 */

@SuppressLint("ValidFragment")
public class GoodsDialog extends DialogFragment implements UploadLiveOrder{
    @BindView(R.id.rcl_goods)
    RecyclerView rclGoods;
    Unbinder unbinder;
    @BindView(R.id.layout_init)
    AutoRelativeLayout layoutInit;
    private Activity mContext;
    private LiveGoodsAdapter mAdapter;
    Dialog dialogUtils;
    private NewLiveRoom newLiveRoom;
    ArrayList<Dynamic> goods = new ArrayList<>();
    private KProgressHUD mKProgressHUD;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_anchor_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initData();
        return view;
    }

    @SuppressLint("ValidFragment")
    public GoodsDialog(NewLiveRoom newLiveRoom) {
        this.newLiveRoom = newLiveRoom;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                dismiss();
            }
        };
        return dialog;
    }

    private void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rclGoods.setLayoutManager(manager);
        mAdapter = new LiveGoodsAdapter(mContext, goods, newLiveRoom,this);
        rclGoods.setAdapter(mAdapter);
        rclGoods.setHasFixedSize(true);
        getLiveRoomGoodList();
    }

    public void showLoading() {
        mKProgressHUD = KProgressHUD.create(mContext);
        mKProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public void hideLoading() {
        if (mKProgressHUD != null) {
            mKProgressHUD.dismiss();
        }
    }


    public void getLiveRoomGoodList() {
        if (newLiveRoom == null) {
            return;
        } else {
            if (newLiveRoom.getLiveId() == null) {
                return;
            }
        }
        showLoading();
        DeviceProperties dr = DrUtils.getInstance();
        Dynamic dynamic = new Dynamic();
        dynamic.setLiveId(newLiveRoom.getLiveId());
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setDynamic(dynamic);
        RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getRetrofitApiService();
        retrofitApiService.getLiveRoomGoodsList(netRequestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        hideLoading();
                        if (result != null){
                            if (result.getResultCode() == 0) {
                                dealWithResult(result);
                            } else {
                                Toast.makeText(mContext, result.getResultInfo(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                        ToastUtils.showShort(mContext.getResources().getString(R.string.data_error));
                    }
                });
    }

    private void dealWithResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            goods = new Gson().fromJson(jsonObject.optString("dynamicList"), new TypeToken<List<Dynamic>>() {
            }.getType());
            if (goods != null) {
                if (goods.size() != 0) {
                    if (layoutInit != null) {
                        layoutInit.setVisibility(View.GONE);
                    }
                    mAdapter.addLiveGoods(goods);
                } else {
                    if (layoutInit != null) {
                        layoutInit.setVisibility(View.VISIBLE);
                    }
                    mAdapter.addLiveGoods(goods);
                }
                mAdapter.addLiveGoods(goods);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_outside})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_outside:
                dismiss();
                break;
        }
    }

    @Override
    public void uploadLiveOrder(final Dialog dialog, NetRequestBean netRequestBean) {
        showLoading();
        RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getRetrofitApiService();
        retrofitApiService.uploadOrder(netRequestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        hideLoading();
                        if (result != null){
                            if (result.getResultCode() == 0) {
                                dialog.dismiss();
                                ToastUtils.showShort("订单提交成功");
                            } else {
                                Toast.makeText(mContext, result.getResultInfo(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                        ToastUtils.showShort(mContext.getResources().getString(R.string.data_error));
                    }
                });
    }
}
