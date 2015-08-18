package com.fast.access.kam.global.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppsModel implements Parcelable {

    private String name;
    private String packageName;
    private File file;
    private int icon;
    private String imageLocation;

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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.packageName);
        dest.writeSerializable(this.file);
        dest.writeInt(this.icon);
    }

    public AppsModel() {
    }

    protected AppsModel(Parcel in) {
        this.name = in.readString();
        this.packageName = in.readString();
        this.file = (File) in.readSerializable();
        this.icon = in.readInt();
    }

    public static final Parcelable.Creator<AppsModel> CREATOR = new Parcelable.Creator<AppsModel>() {
        public AppsModel createFromParcel(Parcel source) {
            return new AppsModel(source);
        }

        public AppsModel[] newArray(int size) {
            return new AppsModel[size];
        }
    };

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}
