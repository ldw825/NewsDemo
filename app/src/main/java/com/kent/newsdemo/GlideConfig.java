package com.kent.newsdemo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * author Kent
 * date 2018/8/1 001
 * version 1.0
 */
@GlideModule
public final class GlideConfig extends AppGlideModule {

    private static final long MAX_MEMORY_CACHE_SIZE = 30 * 1024 * 1024;
    private static final long MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024;

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(MAX_MEMORY_CACHE_SIZE));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "news_image", MAX_DISK_CACHE_SIZE));
    }

}
