package com.fast.access.kam.global.tasks.impl;

import com.fast.access.kam.global.model.AppsModel;

import java.util.List;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public interface IAppFetcher {

    void onStartFetching();

    void onUpdate(AppsModel appsModel);

    void onFinish(List<AppsModel> appsModels);

}
