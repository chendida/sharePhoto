package com.zq.dynamicphoto.adapter;

/**
 * Created by Alex on 2016/8/22.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.luck.picture.lib.entity.LocalMedia;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.utils.PicSelectUtils;
import com.zq.dynamicphoto.utils.checkphoto.AlxImageLoader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class SelectPhotoAdapter extends
        ArrayAdapter<SelectPhotoAdapter.SelectPhotoEntity>/* implements OnClickListener*/ {
    private Activity mActivity;
    public ArrayList<SelectPhotoEntity> allPhotoList;
    int maxSelectedPhotoCount = 100;

    public static final int REQ_CAMARA = 1000;
    private File mfile1;
    private AlxImageLoader alxImageLoader;
    private int destWidth, destHeight;
    int screenWidth;
    private Boolean isHide;
    public SelectPhotoAdapter(Activity activity, ArrayList<SelectPhotoEntity> array,Boolean isHide) {
        super(activity, R.layout.adapter_select_photo, array);
        this.mActivity = activity;
        this.allPhotoList = array;
        this.alxImageLoader = new AlxImageLoader(activity);
        screenWidth = getScreenWidth(activity);
        this.destWidth = (screenWidth - 20) / 3;
        this.destHeight = (screenWidth - 20) / 3;
        this.isHide = isHide;
    }

    @Override
    public int getCount() {
        return allPhotoList.size();//加一是为了那个相机图标
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("Alex","要显示的position是"+position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_select_photo, parent, false);
            viewHolder.rlPhoto = (RelativeLayout) convertView.findViewById(R.id.rlPhoto);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            viewHolder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.iv_photo.getLayoutParams() != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.iv_photo.getLayoutParams();
            lp.width = destWidth;
            lp.height = destHeight;
            viewHolder.iv_photo.setLayoutParams(lp);
        }

        viewHolder.iv_select.setVisibility(View.VISIBLE);
        viewHolder.iv_select.setImageDrawable(getDrawable(mActivity, R.drawable.unchoose));
        viewHolder.rlPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide){
                    ArrayList<LocalMedia> localMedia = new ArrayList<>();
                    for (SelectPhotoEntity entity:allPhotoList) {
                        LocalMedia media = new LocalMedia();
                        media.setPath(entity.url);
                        localMedia.add(media);
                    }
                    PicSelectUtils.getInstance().preview(position,localMedia,mActivity);
                    return;
                }
                Log.i("Alex","点击了rl photo");
                SelectPhotoEntity entity = (SelectPhotoEntity) v.getTag(R.id.rlPhoto);
                ImageView ivSelect = (ImageView) v.findViewById(R.id.iv_select);
                if (mActivity == null) return;
                if (checkIsExistedInSelectedPhotoArrayList(entity)) {
                    ivSelect.setImageDrawable(getDrawable(mActivity, R.drawable.unchoose));
                    removeSelectedPhoto(entity);
                    if (mActivity instanceof CallBackActivity)((CallBackActivity)mActivity).remove(entity);
                } else if (!isFullInSelectedPhotoArrayList()){
                    ivSelect.setImageDrawable(getDrawable(mActivity, R.drawable.choose));
                    addSelectedPhoto(entity);
                    if (mActivity instanceof CallBackActivity)((CallBackActivity)mActivity).add(entity);
                } else {
                    return;
                }
            }
        });
        if((allPhotoList != null) && (allPhotoList.size() >= position) && (allPhotoList.get(position) != null)){
                final SelectPhotoEntity photoEntity = allPhotoList.get(position);
                final String filePath = photoEntity.url;

                viewHolder.iv_select.setVisibility(View.VISIBLE);
                if (checkIsExistedInSelectedPhotoArrayList(photoEntity)) {
                    viewHolder.iv_select.setImageDrawable(getDrawable(mActivity, R.drawable.choose));
                } else {
                    viewHolder.iv_select.setImageDrawable(getDrawable(mActivity, R.drawable.unchoose));
                }

                alxImageLoader.setAsyncBitmapFromSD(filePath,viewHolder.iv_photo,screenWidth/3,false,true,true);
                viewHolder.rlPhoto.setTag(R.id.rlPhoto,photoEntity);
                //viewHolder.rlPhoto.setOnClickListener(this);

        }
        return convertView;
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlPhoto:
                if (isHide){
                    ArrayList<LocalMedia> localMedia = new ArrayList<>();
                    for (SelectPhotoEntity entity:allPhotoList) {
                        LocalMedia media = new LocalMedia();
                        media.setPath(entity.url);
                        localMedia.add(media);
                    }
                    return;
                }
                Log.i("Alex","点击了rl photo");
                SelectPhotoEntity entity = (SelectPhotoEntity) v.getTag(R.id.rlPhoto);
                ImageView ivSelect = (ImageView) v.findViewById(R.id.iv_select);
                if (mActivity == null) return;
                if (checkIsExistedInSelectedPhotoArrayList(entity)) {
                    ivSelect.setImageDrawable(getDrawable(mActivity, R.drawable.unchoose));
                    removeSelectedPhoto(entity);
                    if (mActivity instanceof CallBackActivity)((CallBackActivity)mActivity).remove(entity);
                } else if (!isFullInSelectedPhotoArrayList()){
                    ivSelect.setImageDrawable(getDrawable(mActivity, R.drawable.choose));
                    addSelectedPhoto(entity);
                    if (mActivity instanceof CallBackActivity)((CallBackActivity)mActivity).add(entity);
                } else {
                    return;
                }
                break;
        }
    }*/


    public interface CallBackActivity{
        void remove(SelectPhotoEntity entity);

        void add(SelectPhotoEntity entity);
    }

    class ViewHolder {
        public RelativeLayout rlPhoto;
        public ImageView iv_photo;
        public ImageView iv_select;
    }

    public static class SelectPhotoEntity implements Serializable, Parcelable {
        public String url;
        public int isSelect;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
            dest.writeInt(this.isSelect);
        }

        public SelectPhotoEntity() {
        }

        protected SelectPhotoEntity(Parcel in) {
            this.url = in.readString();
            this.isSelect = in.readInt();
        }

        public static final Parcelable.Creator<SelectPhotoEntity> CREATOR = new Parcelable.Creator<SelectPhotoEntity>() {
            @Override
            public SelectPhotoEntity createFromParcel(Parcel source) {
                return new SelectPhotoEntity(source);
            }

            @Override
            public SelectPhotoEntity[] newArray(int size) {
                return new SelectPhotoEntity[size];
            }
        };

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("SelectPhotoEntity{");
            sb.append("url='").append(url).append('\'');
            sb.append(", isSelect=").append(isSelect);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public int hashCode() {//使用hashcode和equals方法防止重复
            if(url != null)return url.hashCode();
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof SelectPhotoEntity){
                return o.hashCode() == this.hashCode();
            }
            return super.equals(o);

        }
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static Drawable getDrawable(Context context, int id) {
        if ((context == null) || (id < 0)) {
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, null);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * 判断某张照片是否已经被选择过
     * @param entity
     * @return
     */
    public HashSet<SelectPhotoEntity> selectedPhotosSet = new HashSet<>(maxSelectedPhotoCount);
    public boolean checkIsExistedInSelectedPhotoArrayList(SelectPhotoEntity photo) {
        if (selectedPhotosSet == null || selectedPhotosSet.size() == 0) return false;
        if(selectedPhotosSet.contains(photo))return true;
        return false;
    }

    public void removeSelectedPhoto(SelectPhotoEntity photo) {
        selectedPhotosSet.remove(photo);
    }
    public boolean isFullInSelectedPhotoArrayList() {
        if (maxSelectedPhotoCount > 0 && selectedPhotosSet.size() < maxSelectedPhotoCount) return false;
        return true;
    }
    public void addSelectedPhoto(SelectPhotoEntity photo) {
        selectedPhotosSet.add(photo);
    }
}