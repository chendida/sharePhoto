package com.zq.dynamicphoto.mylive.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.header.waveswipe.DisplayUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.MessageAdapter;
import com.zq.dynamicphoto.base.BaseFragment;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.mylive.bean.MessageBean;
import com.zq.dynamicphoto.mylive.bean.MessageEvent;
import com.zq.dynamicphoto.mylive.heart.RxHeartLayout;
import com.zq.dynamicphoto.mylive.ui.LiveActivity;
import com.zq.dynamicphoto.ui.widge.GlideCircleTransformWithBorder;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftKeyBoardListener;
import com.zq.dynamicphoto.view.GroupListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 该Fragment是用于dialogFragment中的pager，为了实现滑动隐藏交互Fragment的
 * 交互的操作都在这个界面实现的，如果大家要改交互主要修改这个界面就可以了
 * <p>
 * Success is the sum of small efforts, repeated day in and day out.
 * 成功就是日复一日那一点点小小努力的积累。
 * AndroidGroup：158423375
 * Author：Johnny
 * AuthorQQ：956595454
 * AuthorWX：Qiang_it
 * AuthorPhone：nothing
 * Created by 2016/9/22.
 */
public class LayerFragment extends LiveFragment implements Observer, GroupListener {
    private static final String TAG = "LayerFragment";
    Unbinder unbinder;
    @BindView(R.id.lvmessage)
    ListView lvmessage;
    @BindView(R.id.iv_chat)
    ImageView ivChat;
    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.sendInput)
    TextView sendInput;
    @BindView(R.id.llinputparent)
    AutoLinearLayout llInputParent;
    @BindView(R.id.llgiftcontent)
    AutoLinearLayout llgiftcontent;
    @BindView(R.id.iv_anchor_avatar)
    ImageView ivAnchorAvatar;//圆形头像
    @BindView(R.id.tv_anchor_nick)
    TextView tvAnchorNick;
    @BindView(R.id.tv_on_line_num)
    TextView tvOnLineNum;
    @BindView(R.id.iv_zan)
    ImageView ivZan;
    @BindView(R.id.heart_layout)
    RxHeartLayout mRxHeartLayout;
    @BindView(R.id.iv_custom)
    ImageView ivCustom;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.layout_bottom_btn)
    AutoRelativeLayout layoutBottomBtn;
    @BindView(R.id.layout_report_anchor)
    AutoRelativeLayout layoutReportAnchor;

    private Random random = new Random();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    };
    private Boolean upvoteFlag = false;//点赞


    /**
     * 数据相关
     */
    private List<MessageBean> messageData = new LinkedList<>();
    private MessageAdapter messageAdapter;

    /**
     * 接口和宿主activity交互
     */
    public static final String INTERFACE_SHOW_GOOS_LIST = LayerFragment.class.getName() + "showGoosList";//展示商品列表
    public static final String INTERFACE_SHOW_CONTAT = LayerFragment.class.getName() + "showContact";//展示联系方式
    public static final String INTERFACE_CLOSE_LIVE = LayerFragment.class.getName() + "closeLive";//关闭直播间
    public static final String INTERFACE_UPVOTE = LayerFragment.class.getName() + "upvote";//点赞
    public static final String INTERFACE_SHARE_TO_WX = LayerFragment.class.getName() + "shareToWx";//分享到微信
    public static final String INTERFACE_ANCHOR_EXIT = LayerFragment.class.getName() + "anchorExit";//主播退出直播间
    //摄像头前后切换
    public static final String INTERFACE_ChANGE_CAMERA = LayerFragment.class.getName() + "changeCamera";
    public static final String INTERFACE_REPORT_ANCHOR = LayerFragment.class.getName() + "reportAnchor";//举报主播


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layer;
    }

    @Override
    protected void initView(View view) {
        MyApplication.getInstance().setGroupListener(this);
        getGroupMembers();
        if (LiveActivity.getIsAnchor()) {
            ivCustom.setVisibility(View.GONE);
        } else {
            ivCamera.setVisibility(View.GONE);
            //判断主播是否传了电话号码或者微信
            if (LiveActivity.getLiveRoom() != null) {
                if (TextUtils.isEmpty(LiveActivity.getLiveRoom().getPhone()) &&
                        TextUtils.isEmpty(LiveActivity.getLiveRoom().getWx())) {
                    ivCustom.setVisibility(View.GONE);
                }
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llInputParent.getVisibility() == View.VISIBLE) {
                    ivChat.setVisibility(View.VISIBLE);
                    llInputParent.setVisibility(View.GONE);
                    hideKeyboard();
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {
        UserInfo userInfo = LiveActivity.getUserInfo();
        if (userInfo != null) {
            if (TextUtils.isEmpty(userInfo.getMobile()) && TextUtils.isEmpty(userInfo.getWx())) {
                ivCustom.setVisibility(View.GONE);
            }
        }
        softKeyboardListnenr();
        messageAdapter = new MessageAdapter(getActivity(), messageData);
        lvmessage.setAdapter(messageAdapter);
        lvmessage.setSelection(messageData.size());
        updateView();
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
    }

    private void updateView() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_img)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (LiveActivity.getUserInfo() != null) {
            if (!TextUtils.isEmpty(LiveActivity.getUserInfo().getUserLogo())) {
                Glide.with(this).load(LiveActivity.getUserInfo().getUserLogo())
                        .apply(new RequestOptions().error(getResources()
                                .getDrawable(R.drawable.vip_avatar))
                                .placeholder(R.drawable.vip_avatar)
                                .centerCrop()
                                .transform(new GlideCircleTransformWithBorder(getActivity(), 1,
                                        getActivity().getResources().getColor(R.color.white)))
                        ).into(ivAnchorAvatar);
            }
            if (!TextUtils.isEmpty(LiveActivity.getUserInfo().getRemarkName())) {
                tvAnchorNick.setText(LiveActivity.getUserInfo().getRemarkName());
            }
        }
    }

    @OnClick({R.id.iv_chat, R.id.sendInput, R.id.iv_custom, R.id.iv_share, R.id.iv_goods_btn,
            R.id.layout_close_live, R.id.iv_zan, R.id.iv_camera, R.id.layout_report_anchor})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_zan:
                if (!upvoteFlag) {
                    upvoteFlag = true;
                    mFunctionManager.invokeFuction(INTERFACE_UPVOTE);
                }
                mRxHeartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        mRxHeartLayout.addHeart(rgb);
                    }
                });
                break;
            case R.id.layout_close_live:
                mFunctionManager.invokeFuction(INTERFACE_CLOSE_LIVE);
                break;
            case R.id.iv_chat:
                showChat();
                break;
            case R.id.iv_custom:
                mFunctionManager.invokeFuction(INTERFACE_SHOW_CONTAT);
                break;
            case R.id.iv_goods_btn:
                mFunctionManager.invokeFuction(INTERFACE_SHOW_GOOS_LIST);
                break;
            case R.id.iv_share:
                //showGift("9999");
                mFunctionManager.invokeFuction(INTERFACE_SHARE_TO_WX);
                break;
            case R.id.sendInput:
                sendText();
                break;
            case R.id.iv_camera://切换摄像头
                mFunctionManager.invokeFuction(INTERFACE_ChANGE_CAMERA);
                break;
            case R.id.layout_report_anchor://举报主播
                mFunctionManager.invokeFuction(INTERFACE_REPORT_ANCHOR);
                break;
        }
    }


    /**
     * 显示聊天布局
     */
    private void showChat() {
        ivChat.setVisibility(View.GONE);
        llInputParent.setVisibility(View.VISIBLE);
        llInputParent.requestFocus();
        showKeyboard();
    }

    /**
     * 发送消息
     */
    private void sendText() {
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        String remarkName = sp.getString("remarkName", "");
        if (!etInput.getText().toString().trim().isEmpty()) {
            Log.i(TAG, "发消息");
            String text = etInput.getText().toString().trim();
            MessageBean messageBean = new MessageBean(remarkName, text);
            messageData.add(messageBean);
            etInput.setText("");
            messageAdapter.NotifyAdapter(messageData);
            lvmessage.setSelection(messageData.size());
            //获取群聊会话
            String groupId = "" + LiveActivity.getNewLiveRoom().getLiveId();  //获取与群组 "TGID1LTTZEAEO" 的会话
            TIMConversation conversation = TIMManager.getInstance().getConversation(
                    TIMConversationType.Group,      //会话类型：群组
                    groupId);
            //构造一条消息
            TIMMessage msg = new TIMMessage();

            //添加文本内容
            TIMTextElem elem = new TIMTextElem();
            elem.setText(text);

            //将elem添加到消息
            if (msg.addElement(elem) != 0) {
                //Log.d(tag, "addElement failed");
                return;
            }

            //发送消息
            conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                @Override
                public void onError(int code, String desc) {//发送消息失败
                    //错误码 code 和错误描述 desc，可用于定位请求失败原因
                    //错误码 code 含义请参见错误码表
                    Log.i(TAG, "send message failed. code: " + code + " errmsg: " + desc);
                }

                @Override
                public void onSuccess(TIMMessage msg) {//发送消息成功
                    Log.i(TAG, "SendMsg ok");
                }
            });
            hideKeyboard();
        } else
            hideKeyboard();
    }


    /**
     * 显示软键盘并因此头布局
     */
    private void showKeyboard() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘并显示头布局
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                layoutBottomBtn.setVisibility(View.GONE);
                dynamicChangeListviewH(100);
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                layoutBottomBtn.setVisibility(View.VISIBLE);
                ivChat.setVisibility(View.VISIBLE);
                llInputParent.setVisibility(View.GONE);
                dynamicChangeListviewH(150);
            }
        });
    }

    /**
     * 动态的修改listview的高度
     *
     * @param heightPX
     */
    private void dynamicChangeListviewH(int heightPX) {
        ViewGroup.LayoutParams layoutParams = lvmessage.getLayoutParams();
        layoutParams.height = dip2px(getActivity(), heightPX);
        lvmessage.setLayoutParams(layoutParams);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            if (data instanceof TIMMessage) {
                TIMMessage msg = (TIMMessage) data;
                msgToString(msg);
            }
        }
    }

    /**
     * 消息转换成字符串类型
     *
     * @param msg
     */
    private void msgToString(TIMMessage msg) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < msg.getElementCount(); ++i) {
            TIMElem elem = msg.getElement(i);
            //获取当前元素的类型
            TIMElemType elemType = elem.getType();
            if (elemType == TIMElemType.Text) {
                //处理文本消息
                TIMTextElem textElem = (TIMTextElem) msg.getElement(i);
                result.append(textElem.getText());
            } else if (elemType == TIMElemType.Image) {
                //处理图片消息
            } else if (elemType == TIMElemType.GroupTips) {
                //处理文本消息
                TIMTextElem textElem = (TIMTextElem) msg.getElement(i);
                result.append(textElem.getText());
            } else if (elemType == TIMElemType.GroupSystem) {
            }
        }
        String nickName = "";
        if (msg.getSenderProfile() != null) {
            nickName = msg.getSenderProfile().getNickName();
        }
        String message = result.toString();
        Log.i(TAG, message);
        Log.i(TAG, nickName);
        if (!TextUtils.isEmpty(message)) {
            MessageBean messageBean = new MessageBean(nickName, message);
            messageData.add(messageBean);
            if (messageAdapter != null) {
                messageAdapter.NotifyAdapter(messageData);
            }
            if (lvmessage != null) {
                lvmessage.setSelection(messageData.size());
            }
        }
    }

    @Override
    public void onMemberJoin(String s, List<TIMGroupMemberInfo> list, String nick) {
        Log.i(TAG, "有人进群");
        getGroupMembers();
        sendSystemMsg(1, nick);
    }

    /**
     * 给主播发送进群退群通知
     */
    private void sendSystemMsg(int flag, String nickName) {
        if (LiveActivity.getIsAnchor()) {
            MessageBean messageBean;
            if (flag == 1) {//进群
                messageBean = new MessageBean("", nickName + "进入直播间");
            } else {//退群
                messageBean = new MessageBean("", nickName + "退出直播间");
            }
            messageData.add(messageBean);
            if (messageAdapter != null) {
                messageAdapter.NotifyAdapter(messageData);
            }
            if (lvmessage != null) {
                lvmessage.setSelection(messageData.size());
            }
        }
    }

    @Override
    public void onMemberQuit(String s, List<String> list, String nick) {
        Log.i(TAG, "有人退群" + s);
        getGroupMembers();
        sendSystemMsg(2, nick);
    }

    @Override
    public void onMemberUpdate(String s, List<TIMGroupMemberInfo> list) {
        Log.i(TAG, "群成员更新");
        if (list != null) {
            tvOnLineNum.setText(String.valueOf(list.size()));
        }
    }

    @Override
    public void onGroupAdd(TIMGroupCacheInfo timGroupCacheInfo) {

    }

    @Override
    public void onGroupDelete(String s) {
        Log.i(TAG, "主播退出");
        mFunctionManager.invokeFuction(INTERFACE_ANCHOR_EXIT);
    }

    @Override
    public void onGroupUpdate(TIMGroupCacheInfo timGroupCacheInfo) {
        Log.i(TAG, "群更新");
    }

    /**
     *
     */
    public void getGroupMembers() {
        //创建回调
        TIMValueCallBack<List<TIMGroupMemberInfo>> cb = new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> infoList) {//参数返回群组成员信息
                if (infoList != null) {
                    int total = infoList.size();
                    if (!LiveActivity.getIsAnchor()) {
                        if (LiveActivity.getNewLiveRoom() != null) {
                            //初始人气
                            Integer startNum = LiveActivity.getNewLiveRoom().getWatchNum();
                            //递增人气
                            Integer watchNumRate = LiveActivity.getNewLiveRoom().getWatchNumRate();
                            if (startNum != null) {
                                if (watchNumRate != null) {
                                    total = startNum + watchNumRate * (infoList.size() - 1);
                                }
                            }
                        }
                    }
                    tvOnLineNum.setText(String.valueOf(total));
                }
            }
        };

        //获取群组成员信息
        TIMGroupManagerExt.getInstance().getGroupMembers(
                LiveActivity.getNewLiveRoom().getLiveId() + "", //群组 ID
                cb);//回调
    }
}