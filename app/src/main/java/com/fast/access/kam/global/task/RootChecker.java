package com.fast.access.kam.global.task;

import android.content.Context;
import android.os.AsyncTask;

import com.chrisplus.rootmanager.RootManager;
import com.fast.access.kam.global.helper.AppHelper;

/**
 * Created by Kosh on 8/23/2015. copyrights are reserved
 */
public class RootChecker extends AsyncTask<Context, Boolean, Boolean> {
    @Override
    protected Boolean doInBackground(Context... params) {
        if (AppHelper.isRoot()) {
            AppHelper.setRootEnabled(params[0], RootManager.getInstance().obtainPermission());
        }
        return null;
    }
}