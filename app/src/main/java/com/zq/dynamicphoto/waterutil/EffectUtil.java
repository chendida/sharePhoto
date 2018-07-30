package com.zq.dynamicphoto.waterutil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.Size;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.bean.Image;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.imageviewtouch.ImageViewTouch;
import com.zq.dynamicphoto.waterutil.customview.MyHighlightView;
import com.zq.dynamicphoto.waterutil.customview.MyImageViewDrawableOverlay;
import com.zq.dynamicphoto.waterutil.customview.drawable.StickerDrawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import static com.yalantis.ucrop.util.BitmapLoadUtils.calculateInSampleSize;

/**
 * Created by sky on 15/7/6.
 */
public class EffectUtil {
    public static List<Image> addonList                 = new ArrayList<Image>();
    private static List<MyHighlightView> hightlistViews = new CopyOnWriteArrayList<MyHighlightView>();

    public static List<MyHighlightView> getHightlistViews() {
        return hightlistViews;
    }

    public EffectUtil() {
    }

    /*static {
        addonList.add(new Addon(R.drawable.sticker1));
        addonList.add(new Addon(R.drawable.sticker2));
        addonList.add(new Addon(R.drawable.sticker3));
        addonList.add(new Addon(R.drawable.sticker4));
        addonList.add(new Addon(R.drawable.sticker5));
        addonList.add(new Addon(R.drawable.sticker6));
        addonList.add(new Addon(R.drawable.sticker7));
        addonList.add(new Addon(R.drawable.sticker8));
    }*/

    public static void clear() {
        hightlistViews.clear();
    }

    //删除贴纸的回调接口
    public static interface StickerCallback {
        public void onRemoveSticker(Image sticker);
    }

    public static Bitmap getSmallBitmap(final String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置为ture,只读取图片的大小，不把它加载到内存中去
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);// 此处，选取了480x800分辨率的照片

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;// 处理完后，同时需要记得设置为false

        return BitmapFactory.decodeFile(filePath, options);
    }

    //添加贴纸
    public static void addStickerImage(final ImageViewTouch processImage,
                                                  final Image sticker,
                                                  final StickerCallback callback) {
        if (sticker.getPath().startsWith("http")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestOptions myOptions = new RequestOptions()
                            .fitCenter();
                    try {
                        Bitmap bitmap = Glide.with(MyApplication.getAppContext())
                                .asBitmap()
                                .apply(myOptions)
                                .load(sticker.getPath())
                                .into(480, 800)
                                .get();
                        addWatermark(bitmap,sticker,processImage,callback);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            Bitmap bitmap = getSmallBitmap(sticker.getPath());
            addWatermark(bitmap,sticker,processImage,callback);
        }
    }

    private static MyHighlightView addWatermark(Bitmap bitmap, final Image sticker,
                                                final ImageViewTouch processImage,
                                                final StickerCallback callback) {
        if (bitmap == null) {
            return null;
        }
        StickerDrawable drawable = new StickerDrawable(MyApplication.getInstance().getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setMinSize(30, 30);

        final MyHighlightView hv = new MyHighlightView(processImage, R.style.AppTheme, drawable);
        //设置贴纸padding
        hv.setPadding(10);
        hv.setOnDeleteClickListener(new MyHighlightView.OnDeleteClickListener() {
            @Override
            public void onDeleteClick() {
                ((MyImageViewDrawableOverlay) processImage).removeHightlightView(hv);
                hightlistViews.remove(hv);
                ((MyImageViewDrawableOverlay) processImage).invalidate();
                callback.onRemoveSticker(sticker);
            }
        });

        Matrix mImageMatrix = processImage.getImageViewMatrix();

        int cropWidth, cropHeight;
        int x, y;

        final int width = processImage.getWidth();
        final int height = processImage.getHeight();

        // width/height of the sticker
        cropWidth = (int) drawable.getCurrentWidth();
        cropHeight = (int) drawable.getCurrentHeight();

        final int cropSize = Math.max(cropWidth, cropHeight);
        final int screenSize = Math.min(processImage.getWidth(), processImage.getHeight());
        RectF positionRect = null;
        if (cropSize > screenSize) {
            float ratio;
            float widthRatio = (float) processImage.getWidth() / cropWidth;
            float heightRatio = (float) processImage.getHeight() / cropHeight;

            if (widthRatio < heightRatio) {
                ratio = widthRatio;
            } else {
                ratio = heightRatio;
            }

            cropWidth = (int) ((float) cropWidth * (ratio / 2));
            cropHeight = (int) ((float) cropHeight * (ratio / 2));

            int w = processImage.getWidth();
            int h = processImage.getHeight();
            positionRect = new RectF(w / 2 - cropWidth / 2, h / 2 - cropHeight / 2,
                    w / 2 + cropWidth / 2, h / 2 + cropHeight / 2);
            positionRect.inset((positionRect.width() - cropWidth) / 2,
                    (positionRect.height() - cropHeight) / 2);
        }

        if (positionRect != null) {
            x = (int) positionRect.left;
            y = (int) positionRect.top;
        } else {
            x = (width - cropWidth) / 2;
            y = (height - cropHeight) / 2;
        }

        Matrix matrix = new Matrix(mImageMatrix);
        matrix.invert(matrix);

        float[] pts = new float[] { x, y, x + cropWidth, y + cropHeight };
        MatrixUtils.mapPoints(matrix, pts);
        RectF cropRect = new RectF(pts[0], pts[1], pts[2], pts[3]);
        Log.i("positionRect","pts[0] = "+ pts[0]);
        Log.i("positionRect","pts[1] = "+ pts[1]);
        Log.i("positionRect","pts[2] = "+ pts[2]);
        Log.i("positionRect","pts[3] = "+ pts[3]);
        Log.i("positionRect","width = "+ width);
        Log.i("positionRect","height = "+ height);
        Rect imageRect = new Rect(0, 0, width, height);
        hv.setup(MyApplication.getAppContext(), mImageMatrix, imageRect, cropRect, false);

        ((MyImageViewDrawableOverlay) processImage).addHighlightView(width,height,hv,1);
        ((MyImageViewDrawableOverlay) processImage).setSelectedHighlightView(hv);
        hightlistViews.add(hv);
        return hv;
    }


    //----添加标签-----
    /*public static void addLabelEditable(MyImageViewDrawableOverlay overlay, ViewGroup container,
                                        LabelView label, int left, int top) {
        addLabel(container, label, left, top);
        addLabel2Overlay(overlay, label);
    }

    private static void addLabel(ViewGroup container, LabelView label, int left, int top) {
        label.addTo(container, left, top);
    }*/

   /* public static void removeLabelEditable(MyImageViewDrawableOverlay overlay, ViewGroup container,
                                           LabelView label) {
        container.removeView(label);
        overlay.removeLabel(label);
    }*/

    public static int getStandDis(float realDis, float baseWidth) {
        float imageWidth = baseWidth <= 0 ? MyApplication.getInstance().getScreenWidth() : baseWidth;
        float radio = Constans.DEFAULT_PIXEL / imageWidth;
        return (int) (radio * realDis);
    }

    public static int getRealDis(float standardDis, float baseWidth) {
        float imageWidth = baseWidth <= 0 ? MyApplication.getInstance().getScreenWidth() : baseWidth;
        float radio = imageWidth / Constans.DEFAULT_PIXEL;
        return (int) (radio * standardDis);
    }

    /**
     * 使标签在Overlay上可以移动
     */
    /*private static void addLabel2Overlay(final MyImageViewDrawableOverlay overlay,
                                         final LabelView label) {
        //添加事件，触摸生效
        overlay.addLabel(label);
        label.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 手指按下时
                        overlay.setCurrentLabel(label, event.getRawX(), event.getRawY());
                        return false;
                    default:
                        return false;
                }
            }
        });
    }*/


    //添加水印
    public static void applyOnSave(Canvas mCanvas, ImageViewTouch processImage,
                                   int sourceImgWidth,int sourceImgHeight) {
        for (MyHighlightView view : hightlistViews) {
            applyOnSave(mCanvas, processImage, view,sourceImgWidth,sourceImgHeight);
        }
    }

    private static void applyOnSave(Canvas mCanvas, ImageViewTouch processImage,MyHighlightView view,
    int sourceImgWidth,int sourceImgHeight) {

        if (view != null && view.getContent() instanceof StickerDrawable) {

            final StickerDrawable stickerDrawable = ((StickerDrawable) view.getContent());
            RectF cropRectF = view.getCropRectF();
            float top = cropRectF.top;
            float left = cropRectF.left;
            float right = cropRectF.right;
            float bottom = cropRectF.bottom;
            int height = view.getmImageRect().height();
            int width = view.getmImageRect().width();
            float centerX = (left + right) / 2;
            float centerY = (top + bottom) / 2;
            float realCenterX = centerX / width * sourceImgWidth;
            float realCenterY = centerY / height * sourceImgHeight;

            left = left + realCenterX - centerX;
            right = right + realCenterX - centerX;
            top = top + realCenterY - centerY;
            bottom = bottom + realCenterY - centerY;
            Log.i("PictureSlideFragment", "left = " + left);
            Log.i("PictureSlideFragment", "top = " + top);
            Log.i("PictureSlideFragment", "right = " + right);
            Log.i("PictureSlideFragment", "bottom = " + bottom);
            Rect rect = new Rect((int) left, (int) top, (int) right,
                    (int) bottom);
            Matrix rotateMatrix = view.getCropRotationMatrix();
            Matrix matrix = new Matrix(processImage.getImageMatrix());
            if (!matrix.invert(matrix)) {
            }
            @SuppressLint("WrongConstant") int saveCount = mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
            mCanvas.concat(rotateMatrix);

            stickerDrawable.setDropShadow(false);
            view.getContent().setBounds(rect);
            view.getContent().draw(mCanvas);
            mCanvas.restoreToCount(saveCount);
        }
    }

}
