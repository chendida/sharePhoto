package com.zq.dynamicphoto.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Moments;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.utils.ImageLoaderUtils;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.SoftUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/5.
 */
public class FriendCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "FriendCircleAdapter";
    private Activity mContext;
    private ArrayList<Moments> mList;
    private MyClickListener mListener;
    private UserInfo userInfo;
    private String userLogo, realName,bgUrl;

    private static final int HEAD_TYPE = 00001;
    private static final int BODY_TYPE = 00002;
    private int headCount = 1;//头部个数，后续可以自己拓展
    private Activity mActivity;
    private String etSearchContent;

    public FriendCircleAdapter(Activity mContext, ArrayList<Moments> mList,
                               MyClickListener listener, Activity activity) {
        this.mContext = mContext;
        this.mList = mList;
        mListener = listener;
        mActivity = activity;
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        userLogo = sp.getString(Constans.USERLOGO, "");
        realName = sp.getString(Constans.REMARKNAME, "");
        bgUrl = sp.getString(Constans.BGIMAGE,"");
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

    public ArrayList<Moments> getmList() {
        return mList;
    }

    public void setEtSearchContent(String etSearchContent) {
        this.etSearchContent = etSearchContent;
    }

    public String getEtSearchContent() {
        return etSearchContent;
    }


    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {
        public void clickListener(View v, Moments moments);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD_TYPE:
                return new HeadViewHolder(View.inflate(mContext, R.layout.layout_friend_head, null));
            case BODY_TYPE:
                return new FriendCircleViewHolder(View.inflate(mContext, R.layout.friend_circle_list, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, final int position) {
        if (parent instanceof HeadViewHolder){
            final HeadViewHolder holder = (HeadViewHolder) parent;
            holder.bind();
            holder.layoutSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.etSearch.setFocusable(true);
                    holder.etSearch.setFocusableInTouchMode(true);
                    holder.etSearch.requestFocus();
                    SoftUtils.oneShowSoft(holder.etSearch);
                }
            });
            holder.layoutClearInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.etSearch.setText(null);
                }
            });

            /**
             * 输入框搜索完成按钮的监听
             */
            holder.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        SoftUtils.softShow(mActivity);
                        setEtSearchContent(holder.etSearch.getText().toString());
                        mListener.clickListener(holder.etSearch,null);
                        return true;
                    }
                    return false;
                }
            });
        }else if (parent instanceof FriendCircleViewHolder){
            FriendCircleViewHolder holder = (FriendCircleViewHolder) parent;
            holder.setIsRecyclable(false);
            holder.bind(position-1);
            final Moments moments = mList.get(position - 1);
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //删除朋友圈动态
                    mListener.clickListener(view,moments);
                    mList.remove(position-1);
                    notifyDataSetChanged();
                }
            });
            holder.layoutArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.clickListener(view,moments);
                }
            });
            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.clickListener(view,moments);
                }
            });
            holder.layoutOneKeyShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShareWx(moments);
                }
            });
        }
    }

    private void showShareWx(final Moments moments) {
        /*new ShareWxDialog(mActivity, R.style.dialog, new ShareWxDialog.OnItemClickListener() {
            @Override
            public void onClick(Dialog dialog, int position) {
                dialog.dismiss();
                switch (position) {
                    case 1:
                        ShareUtils.getInstance(mActivity).shareLink(moments,1);
                        break;
                    case 2://分享到微信朋友圈
                        ShareUtils.getInstance(mActivity).shareLink(moments,2);
                        break;
                }
            }
        }).show();*/
    }

    @Override
    public int getItemCount() {
        return headCount + getBodySize();
    }


    class FriendCircleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_mounth)
        TextView tvMounth;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.iv_article_pic)
        ImageView ivArticlePic;
        @BindView(R.id.tv_article_describe)
        TextView tvArticleDescribe;
        @BindView(R.id.layout_article)
        AutoRelativeLayout layoutArticle;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.layout_one_key_share)
        AutoRelativeLayout layoutOneKeyShare;

        FriendCircleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(int position) {
            Moments moments = mList.get(position);
            if (moments != null) {
                if (!TextUtils.isEmpty(moments.getCreateTime())) {
                    tvMounth.setText(moments.getCreateTime().substring(5, 7) + "月");
                    tvDate.setText(moments.getCreateTime().substring(8, 10));
                }
                if (!TextUtils.isEmpty(moments.getForwardLogo())) {
                    ImageLoaderUtils.displayImg(ivArticlePic,moments.getForwardLogo());
                }
                if (!TextUtils.isEmpty(moments.getTitle())) {
                    tvArticleDescribe.setText(moments.getTitle());
                }
            }
        }
    }

    public void addMomentsList(ArrayList<Moments> dynamicList) {
        this.mList.addAll(dynamicList);
        notifyDataSetChanged();
    }

    public void initMomentsList(ArrayList<Moments> dynamicList) {
        this.mList.clear();
        addMomentsList(dynamicList);
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_nick)
        TextView tvNick;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.iv_bg)
        ImageView ivBg;
        @BindView(R.id.tv_add_new_num)
        TextView tvAddNewNum;
        @BindView(R.id.tv_add_new)
        TextView tvAddNew;
        @BindView(R.id.layout_add_new)
        AutoRelativeLayout layoutAddNew;
        @BindView(R.id.tv_add_all_num)
        TextView tvAddAllNum;
        @BindView(R.id.tv_add_all)
        TextView tvAddAll;
        @BindView(R.id.layout_all_num)
        AutoRelativeLayout layoutAllNum;
        @BindView(R.id.layout_bg)
        AutoRelativeLayout layoutBg;
        @BindView(R.id.iv_search)
        ImageView ivSearch;
        @BindView(R.id.et_search)
        EditText etSearch;
        @BindView(R.id.layout_search)
        AutoRelativeLayout layoutSearch;
        @BindView(R.id.tv_today)
        TextView tvToday;
        @BindView(R.id.iv_add_friend_circle)
        ImageView ivAddFriendCircle;
        @BindView(R.id.layout_today)
        AutoRelativeLayout layoutToday;
        @BindView(R.id.tv_line)
        TextView tvLine;
        @BindView(R.id.layout_init)
        AutoRelativeLayout layoutInit;
        @BindView(R.id.layout_clear_input)
        AutoRelativeLayout layoutClearInput;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind() {
            if (!TextUtils.isEmpty(bgUrl)){
                ImageLoaderUtils.displayImg(ivBg,bgUrl);
            }
            if (!TextUtils.isEmpty(userLogo)){
                ImageLoaderUtils.displayImg(ivAvatar,userLogo);
            }
            if (!TextUtils.isEmpty(realName)){
                tvNick.setText(realName);
            }
            if (userInfo != null){
                if (userInfo.getAddDynamicNum() != null) {
                    tvAddNewNum.setText(userInfo.getAddDynamicNum()+"");
                }
                if (userInfo.getTotalDynamicNum() != null) {
                    tvAddAllNum.setText(userInfo.getTotalDynamicNum()+"");
                }
            }
        }
    }
}
