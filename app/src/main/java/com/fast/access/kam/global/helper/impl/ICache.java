package com.fast.access.kam.global.helper.impl;

import android.graphics.drawable.Drawable;

/**
 * Created by Kosh on 8/17/2015. copyrights are reserved
 */
public interface ICache {
    Drawable getBitmap(String url);

    void putBitmap(String url, Drawable bitmap);
}
