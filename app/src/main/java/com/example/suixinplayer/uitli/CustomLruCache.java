package com.example.suixinplayer.uitli;

import android.graphics.Bitmap;
import android.util.LruCache;

public class CustomLruCache {
    private LruCache<String, Bitmap> stringBitmapLruCache;
    int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大内存
    int cacheSize = maxMemory / 16;//大小为最大内存的1/16
    private static CustomLruCache customLruCache;

    /**
     * 私有化构造方法
     */
    private CustomLruCache() {
        stringBitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 单例模式获取实例，保证只有一个CustomLruCache对象，同时保证只有一个CustomLruCache.stringBitmapLruCache
     *
     * @return
     */
    public static CustomLruCache getInstance() {
        if (customLruCache == null) {
            customLruCache = new CustomLruCache();
        }
        return customLruCache;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) != bitmap)//如果缓存中不存在bitmap,就存入缓存
            stringBitmapLruCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return stringBitmapLruCache.get(key);
    }
}
