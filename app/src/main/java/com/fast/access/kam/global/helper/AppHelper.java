package com.fast.access.kam.global.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.fast.access.kam.R;

/**
 * Created by Kosh on 8/17/2015. copyrights are reserved
 */
public class AppHelper {

    public static Drawable getDrawable(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            if (isLollipop()) {
                return context.getDrawable(R.drawable.ic_not_found);
            }
            return context.getResources().getDrawable(R.drawable.ic_not_found);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
