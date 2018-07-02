package com.zq.dynamicphoto.bean;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/30.
 */

public class ImageProvider extends AsyncTask<Object, Object, Integer> {
    //相册列表
    private List<ImageBucket> mImageBucketList = new ArrayList<>();
    //相册列表加载完成后的回调
    private OnLoadedBucketListListener onLoadedBucketListListener;

    //单例模式
    private static ImageProvider mImageProvider = null;

    public static ImageProvider getInstance() {
        if (mImageProvider == null)
            mImageProvider = new ImageProvider();
        return mImageProvider;
    }

    /**
     * 获得相册列表。
     * 如果在这之前没有调用过 loadImageBucketList()方法来加载列表，则不能获取到正确的相册列表
     * 可以通过调用 hasLoadBucketList()方法来检查是否加载过相册列表
     * 若要刷新列表，请调用@loadImageBucketList
     *
     * @return 相册列表
     * @see #loadImageBucketList(Context, OnLoadedBucketListListener)
     */
    public List<ImageBucket> getImageBucketList() {
        return mImageBucketList;
    }

    /**
     * 加载相册列表。
     * 如果在这之前已经调用过该方法，且不需要刷新列表，则可以调用getImageBucketList来直接获得相册列表
     * 可以通过调用 hasLoadBucketList()方法来检查是否加载过相册列表
     *
     * @param context  上下文
     * @param listener 当相册列表加载完成后的回调
     */
    public void loadImageBucketList(Context context, @Nullable OnLoadedBucketListListener listener) {
        onLoadedBucketListListener = listener;
        this.execute(context);
    }

    /**
     * 判断相册列表是否已经加载了
     *
     * @return true 如果相册列表长度不为0
     * false 如果相册列表长度为0
     */
    public boolean hasLoadBucketList() {
        if (mImageBucketList.size() != 0) {
            return true;
        }
        return false;
    }

    //构建图像列表
    private void buildImageBucketList(Context context) {
        //HashMap作为临时容器，以相册名为键来对图片分类。最后转换会为List
        HashMap<String, ImageBucket> tBucketMap = new HashMap<>();
        //新建查询列
        String columns[] = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        //新建查询
        Cursor cur = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cur.moveToFirst()) {
            int indexPhotoID = cur.getColumnIndexOrThrow(columns[0]);
            int indexPhotoPath = cur.getColumnIndexOrThrow(columns[1]);
            int indexBucketID = cur.getColumnIndexOrThrow(columns[2]);
            int indexBucketDisplay = cur.getColumnIndexOrThrow(columns[3]);
            do {
                String id = cur.getString(indexPhotoID);
                String path = cur.getString(indexPhotoPath);
                String bucketID = cur.getString(indexBucketID);
                String bucketDisplay = cur.getString(indexBucketDisplay);
                //得到相册
                ImageBucket bucket = tBucketMap.get(bucketID);
                //如果没有该相册，则新建一个
                if (bucket == null) {
                    bucket = new ImageBucket();
                    bucket.setImageList(new ArrayList<ImageItem>());
                    bucket.setBucketName(bucketDisplay);
                    tBucketMap.put(bucketID, bucket);
                }
                //更新相册
                ImageItem image = new ImageItem();
                image.setImageId(id);
                image.setImagePath(path);
                image.setBucket(bucket);
                bucket.getImageList().add(image);
            } while (cur.moveToNext());
        }
        cur.close();
        //HashMap转List
        List<ImageBucket> tmpList = new ArrayList<>();
        Iterator<Map.Entry<String, ImageBucket>> itr = tBucketMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ImageBucket> entry = itr.next();
            tmpList.add(entry.getValue());
        }
        mImageBucketList = tmpList;
    }

    //相册列表加载完成后的回调
    public interface OnLoadedBucketListListener {
        void onLoaded(List<ImageBucket> list);
    }

    @Override
    protected Integer doInBackground(Object... params) {
        buildImageBucketList((Context) (params[0]));
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (onLoadedBucketListListener != null) {
            onLoadedBucketListListener.onLoaded(mImageBucketList);
        }
    }
}
