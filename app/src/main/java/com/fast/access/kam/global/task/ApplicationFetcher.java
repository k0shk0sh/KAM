package com.fast.access.kam.global.task;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;

import com.fast.access.kam.global.loader.AppListCreator;
import com.fast.access.kam.global.loader.cache.AppIconCache;
import com.fast.access.kam.global.loader.cache.DiskCacheAppList;
import com.fast.access.kam.global.task.impl.OnAppFetching;
import com.fast.access.kam.global.model.AppsModel;

import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class ApplicationFetcher extends AsyncTask<Void, AppsModel, List<AppsModel>> {

    private Context context;
    private AppIconCache appIconCache;
    private AppListCreator appListCreator;
    private DiskCacheAppList diskCacheAppList;
    private PackageManager mPm;
    private OnAppFetching onTask;

    public ApplicationFetcher(Context context, OnAppFetching onTask) {
        this.context = context;
        this.onTask = onTask;
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 6;
        LruCache<String, Drawable> memoryIconCache = new LruCache<String, Drawable>(cacheSize);
        appIconCache = new AppIconCache(context.getExternalCacheDir(), memoryIconCache);
        this.appListCreator = new AppListCreator(context, appIconCache);
        mPm = context.getPackageManager();
        if (diskCacheAppList == null) {
            diskCacheAppList = new DiskCacheAppList(context);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTask.onTaskStart();
    }

    @Override
    protected void onPostExecute(List<AppsModel> appsModels) {
        super.onPostExecute(appsModels);
        onTask.OnTaskFinished(appsModels);
    }

    @Override
    protected List<AppsModel> doInBackground(Void... params) {
        List<AppsModel> oldApps = diskCacheAppList.getAppList(appIconCache);
        if (oldApps != null && !oldApps.isEmpty()) {
            if (oldApps.size() == appListCreator.getAppList().size()) {
                //size only, since we are depending on the receiver to update the list.
                return oldApps;
            }
        }
        oldApps = new AppListCreator(context, appIconCache).getAppList();
        diskCacheAppList.save(oldApps);
        return oldApps;
    }
}
