package com.fast.access.kam.global.loader;

import android.content.Context;
import android.content.Intent;
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
    private final Context context;
    private final AppIconCache memoryIconCache;

    public AppListCreator(Context context, AppIconCache iconCache) {
        this.context = context;
        memoryIconCache = iconCache;
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
            model.setDrawable(new AppIcon(memoryIconCache, resolveInfo.activityInfo.packageName));
            model.setPackageName(resolveInfo.activityInfo.packageName);
            model.setName(resolveInfo.loadLabel(pm).toString());
            appList.add(model);
        }
        return appList;
    }

}
