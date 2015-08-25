package com.fast.access.kam.global.model;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.fast.access.kam.global.loader.cache.IconCache;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppsModel implements Parcelable {
    private String appName;
    private String packageName;
    private String filePath;
    private String versionName;
    private String versionCode;
    private long firstInstallTime;
    private long lastUpdateTime;
    private Bitmap bitmap;
    private ComponentName componentName;
    private List<String> permissions;
    private IconCache iconCache;
    private PackageManager pm;
    private ResolveInfo info;
    private HashMap<Object, CharSequence> labelCache;
    private String activityInfoName;

    public AppsModel(PackageManager pm, ResolveInfo info, IconCache iconCache, HashMap<Object, CharSequence> labelCache) {
        this.packageName = info.activityInfo.applicationInfo.packageName;
        this.componentName = new ComponentName(packageName, info.activityInfo.name);
        this.activityInfoName = info.activityInfo.name;
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            this.firstInstallTime = pi.firstInstallTime;
            this.lastUpdateTime = pi.lastUpdateTime;
            this.versionCode = Integer.toString(pi.versionCode);
            this.versionName = pi.versionName;
            this.filePath = info.activityInfo.applicationInfo.sourceDir;
            this.appName = info.loadLabel(pm).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        iconCache.getTitleAndIcon(this, info, labelCache);
    }

    public PackageManager getPm() {
        return pm;
    }

    public void setPm(PackageManager pm) {
        this.pm = pm;
    }

    public ResolveInfo getInfo() {
        return info;
    }

    public void setInfo(ResolveInfo info) {
        this.info = info;
    }

    public HashMap<Object, CharSequence> getLabelCache() {
        return labelCache;
    }

    public void setLabelCache(HashMap<Object, CharSequence> labelCache) {
        this.labelCache = labelCache;
    }

    public static Creator<AppsModel> getCREATOR() {
        return CREATOR;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getFirstInstallTime() {
        return firstInstallTime;
    }

    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ComponentName getComponentName() {
        return componentName;
    }

    public void setComponentName(ComponentName componentName) {
        this.componentName = componentName;
    }

    public IconCache getIconCache() {
        return iconCache;
    }

    public void setIconCache(IconCache iconCache) {
        this.iconCache = iconCache;
    }

    public String getActivityInfoName() {
        return activityInfoName;
    }

    public void setActivityInfoName(String activityInfoName) {
        this.activityInfoName = activityInfoName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appName);
        dest.writeString(this.packageName);
        dest.writeString(this.filePath);
        dest.writeString(this.versionName);
        dest.writeString(this.versionCode);
        dest.writeLong(this.firstInstallTime);
        dest.writeLong(this.lastUpdateTime);
        dest.writeString(this.activityInfoName);
    }

    public AppsModel() {
    }

    protected AppsModel(Parcel in) {
        this.appName = in.readString();
        this.packageName = in.readString();
        this.filePath = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readString();
        this.firstInstallTime = in.readLong();
        this.lastUpdateTime = in.readLong();
        this.activityInfoName = in.readString();
    }

    public static final Parcelable.Creator<AppsModel> CREATOR = new Parcelable.Creator<AppsModel>() {
        public AppsModel createFromParcel(Parcel source) {
            return new AppsModel(source);
        }

        public AppsModel[] newArray(int size) {
            return new AppsModel[size];
        }
    };

}
