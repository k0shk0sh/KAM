package com.fast.access.kam.global.model;

/**
 * Created by Kosh on 8/22/2015. copyrights are reserved
 */
public class AppDetailModel {

    public static final int HEADER = 1;
    public static final int ITEMS = 2;
    private int type;
    private AppsModel appsModel;
    private String appPermission;

    public AppsModel getAppsModel() {
        return appsModel;
    }

    public void setAppsModel(AppsModel appsModel) {
        this.appsModel = appsModel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAppPermission() {
        return appPermission;
    }

    public void setAppPermission(String appPermission) {
        this.appPermission = appPermission;
    }
}
