package com.fast.access.kam.global.loader;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.fast.access.kam.global.loader.cache.IconCache;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.global.receiver.ApplicationsReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsLoader extends AsyncTaskLoader<List<AppsModel>> {
    private static final String TAG = "AppsLoader";
    private ApplicationsReceiver mAppsObserver;
    private final PackageManager mPm;
    private List<AppsModel> mApps;
    private IconCache mIconCache;

    public AppsLoader(Context ctx, IconCache iconCache) {
        super(ctx);
        mPm = getContext().getPackageManager();
        mIconCache = iconCache;
    }

    @Override
    public List<AppsModel> loadInBackground() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = mPm.queryIntentActivities(mainIntent, 0);
        if (list == null) {
            list = new ArrayList<>();
        }
        Collections.sort(list, new ResolveInfo.DisplayNameComparator(mPm));
        List<AppsModel> entries = new ArrayList<AppsModel>(list.size());
        for (ResolveInfo resolveInfo : list) {
            AppsModel model = new AppsModel(mPm, resolveInfo, mIconCache, null);
            entries.add(model);
        }
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getPackageName().equals(getContext().getPackageName())) {
                entries.remove(i);
            }
        }


        return entries;
    }

    @Override
    public void deliverResult(List<AppsModel> apps) {
        if (isReset()) {
            if (apps != null) {
                return;
            }
        }
        List<AppsModel> oldApps = mApps;
        mApps = apps;
        if (isStarted()) {
            super.deliverResult(apps);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mApps != null) {
            deliverResult(mApps);
        }
        if (mAppsObserver == null) {
            mAppsObserver = new ApplicationsReceiver(this);
        }
        if (takeContentChanged()) {
            forceLoad();
        } else if (mApps == null) {
            forceLoad();
        }


    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mApps != null) {
            mApps = null;
        }
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
