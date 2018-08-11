package com.zq.dynamicphoto.adapter;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.widge.StrokeTextView;
import com.zq.dynamicphoto.utils.ImageSaveUtils;
import com.zq.dynamicphoto.view.WatermarkSeekBarListener;

import java.io.File;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2018/8/9.
 */

public class TextFontAdapter extends RecyclerView.Adapter<TextFontAdapter.TextFontViewHolder>
        implements ImageSaveUtils.DownLoadListener{
    private ArrayList<Drawable> mList;
    private ArrayList<String>fontNameList;
    private BaseActivity mContext;
    private WatermarkSeekBarListener mListener;
    private int position;
    private ImageView ivDownloadIcon;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TextFontAdapter(BaseActivity mContext, ArrayList<Drawable> mList,
                           ArrayList<String> fontNameList, WatermarkSeekBarListener listener) {
        this.mList = mList;
        this.fontNameList = fontNameList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public TextFontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextFontViewHolder(View.inflate(parent.getContext(),
                R.layout.text_font_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final TextFontViewHolder holder, final int position) {
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPosition(position);
                ivDownloadIcon = holder.ivDownloadIcon;
                if (holder.ivDownloadIcon.getVisibility() == View.VISIBLE){//要下载的
                    mContext.showLoading();
                    String url = "/common/typeface/" + fontNameList.get(position-1);
                    ImageSaveUtils.getInstance(TextFontAdapter.this).saveTextFonts(url);
                }else {//该字体已存在
                    if (position == 0){
                        mListener.onChangeTextFont(Typeface.DEFAULT);
                    }else {
                        Typeface typeface = Typeface.createFromFile(
                                Environment.getExternalStorageDirectory()+"/"
                                +fontNameList.get(position-1));
                        mListener.onChangeTextFont(typeface);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void callBack(int code, final String msg) {
        mContext.hideLoading();
        ImageSaveUtils.getInstance(this).clearListener();
        if (code == Constans.REQUEST_OK){
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Typeface typeface = Typeface.createFromFile(msg+"/"+fontNameList.get(position-1));
                    mListener.onChangeTextFont(typeface);
                    ivDownloadIcon.setVisibility(View.GONE);
                }
            });
        }else {
            ToastUtils.showShort("下载字体文件失败");
        }
    }

    class TextFontViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_text_font_pic)
        ImageView ivTextFontPic;
        @BindView(R.id.iv_download_icon)
        ImageView ivDownloadIcon;

        TextFontViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            Drawable drawable = mList.get(position);
            ivTextFontPic.setImageDrawable(drawable);
            Boolean isExists = false;
            if (position > 0) {
                String path = Environment.getExternalStorageDirectory() +"/"+ fontNameList.get(position-1);
                Log.i("position","path = "+ path);
                File file = new File(path);
                if (file.exists()) {
                    isExists = true;
                }
            }
            Log.i("position","position = " + position + ",isExists = " + isExists);
            if (position == 0 || isExists){
                ivDownloadIcon.setVisibility(View.GONE);
            }else {
                ivDownloadIcon.setVisibility(View.VISIBLE);
            }
        }
    }
}
