package com.fast.access.kam.global.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fast.access.kam.global.loader.cache.AppIcon;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppsModel implements Parcelable {


    private String name;
    private String packageName;
    private String filePath;
    private AppIcon drawable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.packageName);
        dest.writeString(this.filePath);
    }

    public AppsModel() {
    }

    protected AppsModel(Parcel in) {
        this.name = in.readString();
        this.packageName = in.readString();
        this.filePath = in.readString();
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
