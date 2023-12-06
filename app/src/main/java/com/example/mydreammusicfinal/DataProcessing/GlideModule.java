package com.example.mydreammusicfinal.DataProcessing;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.mydreammusicfinal.R;


public class GlideModule extends AppGlideModule{
        private Context context;
        public ImageView img;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * 300;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        int diskCacheSizeBytes = 1024 * 1024 * 800;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
    }

    public static  void loadSongImage(Context context, ImageView img, String url) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.img_logo)
                    .skipMemoryCache(false)
                    .error(R.drawable.img_logo)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(img);
        }
    }

