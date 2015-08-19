package com.fast.access.kam.global.loader.cache;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Created by Kosh on 8/19/2015. copyrights are reserved
 */
public class AppIcon {

    private final AppIconCache appIconCache;
    private final String packageName;

    public AppIcon(AppIconCache cache, String packageName) {
        appIconCache = cache;
        this.packageName = packageName;
    }

    public Drawable get(Context context) {
        Drawable drawable = appIconCache.get(packageName);
        if (drawable == null) {
            try {
                drawable = context.getPackageManager().getApplicationIcon(packageName);
                appIconCache.putMemory(packageName, drawable);
                appIconCache.putDisk(packageName, drawable);
                return drawable;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return drawable;
    }
}
