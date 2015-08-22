package com.fast.access.kam.global.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.fast.access.kam.R;
import com.fast.access.kam.global.model.AppsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static int[] colorChoice(Context context) {

        int[] mColorChoices = null;
        String[] color_array = context.getResources().getStringArray(R.array.default_color_choice_values);

        if (color_array != null && color_array.length > 0) {
            mColorChoices = new int[color_array.length];
            for (int i = 0; i < color_array.length; i++) {
                mColorChoices[i] = Color.parseColor(color_array[i]);
            }
        }
        return mColorChoices;
    }

    /**
     * Parse whiteColor
     *
     * @return
     */
    public static int parseWhiteColor() {
        return Color.parseColor("#FFFFFF");
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isDarkTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false);
    }

    public static int getAccentColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("accent_color", context.getResources().getColor(R.color.accent));
    }

    public static int getPrimaryColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("primary_color", context.getResources().getColor(R.color.primary));
    }

    public static int getPrimaryDarkColor(int color) {
        double tran = 0.8;
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(a, Math.max((int) (r * tran), 0), Math.max((int) (g * tran), 0), Math.max((int) (b * tran), 0));
    }

    public static String getCachedImagePath(Context context, String packageName) {
        if (context.getExternalCacheDir() != null)
            return context.getExternalCacheDir().getAbsolutePath() + File.pathSeparator + packageName + ".png";
        else
            return null;
    }

    public static String prettifyDate(long timestamp) {
        SimpleDateFormat dateFormat;
        if (DateUtils.isToday(timestamp)) {
            dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault());
        }
        return dateFormat.format(timestamp);
    }
}
