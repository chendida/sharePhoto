package com.zq.dynamicphoto.bean;

/**
 * Created by Administrator on 2018/7/18.
 */

public class WaterEvent {
    private Image image;

    private int type;//类型，1添加水印，2保存图片，3修改水印透明度

    private int alpha;

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public WaterEvent(int type) {
        this.type = type;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
