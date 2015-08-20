package com.fast.access.kam.global.loader.cache;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Kosh on 8/19/2015. copyrights are reserved
 */
public class AppIconCache {

    private final File cacheDir;
    private final LruCache<String, Drawable> memoryIconCache;

    public AppIconCache(File cacheDir, LruCache memoryIconCache) {
        this.cacheDir = cacheDir;
        this.memoryIconCache = memoryIconCache;
    }

    public String getImageFilePath(String packageName) {
        return cacheDir.getAbsolutePath() + File.pathSeparator + packageName + ".png";
    }

    public Drawable get(String packageName) {
        Drawable cachedImage = memoryIconCache.get(packageName);
        if (cachedImage != null) {
            return cachedImage;
        }
        cachedImage = Drawable.createFromPath(getImageFilePath(packageName));
        if (cachedImage != null) {
            putMemory(packageName, cachedImage);
        }
        return cachedImage;
    }


    public void putMemory(String packageName, Drawable drawable) {
        memoryIconCache.put(packageName, drawable);
    }

    public void putDisk(String packageName, Drawable drawable) {
        try {
            if (!new File(getImageFilePath(packageName)).exists()) {
                FileOutputStream out = new FileOutputStream(getImageFilePath(packageName));
                ((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
