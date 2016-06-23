package com.frame.base.utl.util.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frame.base.utl.R;
import com.frame.base.utl.application.BaseApplication;
import com.frame.base.utl.cache.ACache;


/**
 * <Pre>
 *     glide图片加载工具类
 * </Pre>
 *
 * @author YANGQIYUN
 * @version 1.0
 *          <p/>
 *          Create by 2016/1/28 14:07
 */
public class GlideUtil {

    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_holding)
                .error(R.mipmap.ic_error)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public static void loadImageByApplication(String url, ImageView imageView){
        Glide.with(BaseApplication.getContext())
                .load(url)
                .placeholder(R.mipmap.ic_holding)
                .error(R.mipmap.ic_error)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
    /**
     * 清理缓存
     * */
    public static void clear(Context context){
        Glide.get(context).clearDiskCache();
        Glide.get(context).clearMemory();
    }
}
