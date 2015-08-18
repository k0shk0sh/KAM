package com.fast.access.kam.global.helper.impl;

import android.graphics.Bitmap;

/**
 * Created by Kosh on 8/17/2015. copyrights are reserved
 */
public interface ICache {
    Bitmap getBitmap(String url);

    void putBitmap(String url, Bitmap bitmap);
}
