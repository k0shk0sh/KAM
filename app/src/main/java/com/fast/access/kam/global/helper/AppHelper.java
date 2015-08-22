package com.fast.access.kam.global.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.fast.access.kam.R;
import com.fast.access.kam.global.model.AppsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kosh on 8/17/2015. copyrights are reserved
 */
public class AppHelper {

    public static Bitmap getBitmap(Context context, String packageName) {
        try {
            return drawableToBitmap(context.getPackageManager().getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            if (isLollipop()) {
                return drawableToBitmap(context.getDrawable(R.drawable.ic_not_found));
            }
            return drawableToBitmap(context.getResources().getDrawable(R.drawable.ic_not_found));
        }
    }

    public static Drawable getDrawable(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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

    public static String saveBitmap(Context context, String packageName) {
        Bitmap bitmap = getBitmap(context, packageName);
        if (bitmap != null) {
            try {
                String file = FileUtil.getCacheFile(context, packageName);
                if (FileUtil.exists(file)) {
                    return file;
                }
                OutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                return file;
            } catch (Exception e) {
                Log.e("AppHelper", "SaveBitmap", e);
            }
        }
        return null;
    }

    public static List<String> getAppPermissions(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            return Arrays.asList(info.requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void installApk(Context context, File filename) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(Uri.fromFile(filename));
        context.startActivity(intent);
    }

    public static Result installApkSilently(String apkPath) {
        return RootManager.getInstance().installPackage(apkPath);
    }

    public static Result uninstallApkSilently(String apkPath) {
        return RootManager.getInstance().uninstallPackage(apkPath);
    }

    public static boolean isRootEnabled(Context context) {
        SharedPreferences preferenceScreen = PreferenceManager.getDefaultSharedPreferences(context);
        return preferenceScreen.getBoolean("isRootEnabled", false);
    }

    public static void setRootEnabled(Context context, boolean isRoot) {
        SharedPreferences preferenceScreen = PreferenceManager.getDefaultSharedPreferences(context);
        preferenceScreen.edit().putBoolean("isRootEnabled", isRoot).apply();
    }

    public static boolean isRoot() {
        return RootManager.getInstance().hasRooted();
    }


    public static AppsModel getNewAppDetails(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            AppsModel model = new AppsModel();
            model.setVersionName(info.versionName);
            model.setVersionCode(Integer.toString(info.versionCode));
            model.setLastUpdateTime(info.lastUpdateTime);
            model.setFirstInstallTime(info.firstInstallTime);
            model.setFilePath(info.applicationInfo.sourceDir);
            model.setAppName(context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA)).toString());
            model.setPackageName(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
