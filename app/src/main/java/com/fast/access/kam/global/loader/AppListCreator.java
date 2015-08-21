package com.fast.access.kam.global.loader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.fast.access.kam.global.loader.cache.AppIcon;
import com.fast.access.kam.global.loader.cache.AppIconCache;
import com.fast.access.kam.global.model.AppsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Kosh on 8/19/2015. copyrights are reserved
 */
public class AppListCreator {
    private Context context;
    private AppIconCache memoryIconCache;
    private boolean withoutCache;

    public AppListCreator(Context context, AppIconCache iconCache) {
        this.context = context;
        memoryIconCache = iconCache;
    }

    public AppListCreator(Context context, boolean withoutCache) {
        this.context = context;
        this.withoutCache = withoutCache;
    }


    public List<AppsModel> getAppList() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        List<AppsModel> appList = new ArrayList<>();
        Collections.sort(list, new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo resolveInfo : list) {
            AppsModel model = new AppsModel();
            if (!withoutCache) {
                model.setDrawable(new AppIcon(memoryIconCache, resolveInfo.activityInfo.packageName));
            }
            model.setPackageName(resolveInfo.activityInfo.packageName);
            model.setFilePath(resolveInfo.activityInfo.applicationInfo.sourceDir);
            model.setAppName(resolveInfo.loadLabel(pm).toString());
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(resolveInfo.activityInfo.packageName, PackageManager.GET_PERMISSIONS);
                model.setVersionCode(Integer.toString(info.versionCode));
                model.setVersionName(info.versionName);
                model.setFirstInstallTime(info.firstInstallTime);
                model.setLastUpdateTime(info.lastUpdateTime);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            appList.add(model);
        }
        return appList;
    }

}
