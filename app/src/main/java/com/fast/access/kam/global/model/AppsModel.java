package com.fast.access.kam.global.model;

import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppsModel {
    private String name;
    private String packageName;
    private File fileName;
    private int icon;
    private Drawable drawable;

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

    public File getFileName() {
        return fileName;
    }

    public void setFileName(File fileName) {
        this.fileName = fileName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
