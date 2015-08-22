package com.fast.access.kam.global.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.fast.access.kam.global.loader.cache.AppIcon;

import java.util.List;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppsModel extends Model implements Parcelable {


    @Column
    private String appName;
    @Column
    private String packageName;
    @Column
    private String filePath;
    private AppIcon drawable;
    @Column
    private String versionName;
    @Column
    private String versionCode;
    @Column
    private long firstInstallTime;
    @Column
    private long lastUpdateTime;

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

    public AppIcon getDrawable() {
        return drawable;
    }

    public void setDrawable(AppIcon drawable) {
        this.drawable = drawable;
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

    public AppsModel getById(long id) {
        return new Select().from(AppsModel.class).where("id = ?", id).executeSingle();
    }

    public AppsModel getByPackage(String packageName) {
        return new Select().from(AppsModel.class).where("packageName = ?", packageName).executeSingle();
    }

    public void save(List<AppsModel> appsModels) {
        if (appsModels != null && !appsModels.isEmpty()) {
            for (AppsModel model : appsModels) {
                if (model != null) {
                    if (model.getPackageName() != null) {
                        if (getByPackage(model.getPackageName()) != null) {
                            swap(model, getByPackage(model.getPackageName())).save();
                        } else {
                            model.save();
                        }
                    }
                }
            }
        }
    }

    public void save(AppsModel appsModel) {
        if (appsModel.getByPackage(appsModel.getPackageName()) != null) {
            swap(appsModel, getByPackage(appsModel.getPackageName())).save();
        } else {
            appsModel.save();
        }
    }

    private AppsModel swap(AppsModel model, AppsModel app) {
        app.setFilePath(model.getFilePath());
        app.setLastUpdateTime(model.getLastUpdateTime());
        app.setFirstInstallTime(model.getFirstInstallTime());
        app.setVersionName(model.getVersionName());
        app.setVersionCode(model.getVersionCode());
        app.setPackageName(model.getPackageName());
        app.setAppName(model.getAppName());
        return app;
    }

    public List<AppsModel> getAllApps() {
        return new Select().from(AppsModel.class).orderBy("appName DESC").execute();
    }

    public List<AppsModel> getByFirstInstallation() {
        return new Select().from(AppsModel.class).orderBy("firstInstallTime DESC").execute();
    }

    public List<AppsModel> getByLastInstalltion() {
        return new Select().from(AppsModel.class).orderBy("lastUpdateTime DESC").execute();

    }

    public void deleteByPackageName(String packageName) {
        new Delete().from(AppsModel.class).where("packageName = ?", packageName).execute();
    }

    public void deleteAll() {
        new Delete().from(AppsModel.class).execute();
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
