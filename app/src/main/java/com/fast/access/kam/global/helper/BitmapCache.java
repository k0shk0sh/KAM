package com.fast.access.kam.global.helper;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

import com.fast.access.kam.global.helper.impl.ICache;

/**
 * Created by Kosh on 8/17/2015. copyrights are reserved
 */
public class BitmapCache extends LruCache<String, Drawable> implements ICache {

    final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final static int cacheSize = maxMemory / 6;

    public BitmapCache() {
        this(cacheSize);
    }

    public BitmapCache(int max) {
        super(max);
    }

    @Override
    public Drawable getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Drawable bitmap) {
        if (get(url) == null) {
            put(url, bitmap);
        }
    }

    @Override
    protected int sizeOf(String key, Drawable value) {
        return value == null ? 0 : ((BitmapDrawable) value).getBitmap().getByteCount();
    }
}
