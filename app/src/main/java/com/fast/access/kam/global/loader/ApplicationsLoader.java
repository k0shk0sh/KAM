package com.fast.access.kam.global.loader;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.fast.access.kam.global.loader.cache.AppIconCache;
import com.fast.access.kam.global.loader.cache.DiskCacheAppList;
import com.fast.access.kam.global.model.AppsModel;

import java.util.List;

/**
 * Created by Kosh on 8/19/2015. copyrights are reserved
 */
public class ApplicationsLoader extends AsyncTaskLoader<List<AppsModel>> {
    private static final String TAG = "ADP_AppListLoader";
    final Context context;
    private final AppIconCache appIconCache;
    private final AppListCreator appListCreator;
    DiskCacheAppList diskCacheAppList;
    final PackageManager mPm;
    private List<AppsModel> mApps;
    private InstalledAppsObserver mAppsObserver;

    public ApplicationsLoader(Context context) {
        super(context);
        this.context = context;
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 6;
        LruCache<String, Drawable> memoryIconCache = new LruCache<String, Drawable>(cacheSize);
        appIconCache = new AppIconCache(context.getExternalCacheDir(), memoryIconCache);
        this.appListCreator = new AppListCreator(context, appIconCache);
        mPm = getContext().getPackageManager();
    }

    @Override
    public List<AppsModel> loadInBackground() {
        if (diskCacheAppList == null) {
            diskCacheAppList = new DiskCacheAppList(context);
        }
        mApps = diskCacheAppList.getAppList(appIconCache);
        if (mApps != null && mApps.size() == appListCreator.getAppList().size()) {
            return mApps;
        }
        mApps = new AppListCreator(context, appIconCache).getAppList();
        diskCacheAppList.save(mApps);
        return mApps;
    }

    @Override
    public void deliverResult(List<AppsModel> apps) {
        super.deliverResult(apps);
    }

    @Override
    protected void onStartLoading() {
        Log.i(TAG, "+++ onStartLoading() called! +++");

        if (mApps != null) {
            Log.i(TAG, "+++ Delivering previously loaded data to the client...");
            deliverResult(mApps);
        }
        if (mAppsObserver == null) {
            mAppsObserver = new InstalledAppsObserver(this);
        }

        if (takeContentChanged() || mApps == null) {
            Log.i(TAG, "+++ A content change has been detected... so force load! +++");
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        Log.i(TAG, "+++ onStopLoading() called! +++");
        cancelLoad();
    }

    @Override
    protected void onReset() {
        Log.i(TAG, "+++ onReset() called! +++");
        onStopLoading();
        if (mAppsObserver != null) {
            getContext().unregisterReceiver(mAppsObserver);
            mAppsObserver = null;
        }

    }

    @Override
    public void onCanceled(List<AppsModel> apps) {
        super.onCanceled(apps);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

}