package com.fast.access.kam.global.loader.cache;

import android.content.Context;

import com.fast.access.kam.global.model.AppsModel;

import java.util.List;

/**
 * Created by Kosh on 8/19/2015. copyrights are reserved
 */
public class DiskCacheAppList {
    private final Context mContext;

    public DiskCacheAppList(Context context) {
        mContext = context;
    }

    public List<AppsModel> getAppList(AppIconCache iconCache) {
        List<AppsModel> appList = new AppsModel().getAllApps();
        if (appList != null && !appList.isEmpty()) {
            for (AppsModel model : appList) {
                model.setDrawable(new AppIcon(iconCache, model.getPackageName()));
            }
        }
        return appList;
    }

    public void save(List<AppsModel> list) {
        new AppsModel().save(list);
    }
}
