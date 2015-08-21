package com.fast.access.kam.global.loader.impl;

import com.fast.access.kam.global.model.AppsModel;

import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public interface OnAppFetching {
    void onTaskStart();

    void OnTaskFinished(List<AppsModel> appsModels);

}
