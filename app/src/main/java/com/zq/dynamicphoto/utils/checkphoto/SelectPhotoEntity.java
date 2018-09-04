package com.zq.dynamicphoto.utils.checkphoto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/3.
 */

public class SelectPhotoEntity implements Serializable, Parcelable {
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
