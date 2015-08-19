package com.fast.access.kam.global.loader;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Used by the {@link ApplicationsLoader}. An observer that listens for
 * application installs, removals, and updates (and notifies the loader when
 * these changes are detected).
 * Created by Kosh on 8/19/2015. copyrights are reserved
 */
public class InstalledAppsObserver extends BroadcastReceiver {
    private ApplicationsLoader mLoader;

    public InstalledAppsObserver(ApplicationsLoader loader) {
        mLoader = loader;
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        mLoader.getContext().registerReceiver(this, filter);
        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        mLoader.getContext().registerReceiver(this, sdFilter);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        mLoader.onContentChanged();
    }
}