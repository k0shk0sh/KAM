package com.fast.access.kam;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.app.Application;
import com.chrisplus.rootmanager.RootManager;
import com.fast.access.kam.activities.Home;
import com.fast.access.kam.global.helper.AppHelper;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import de.greenrobot.event.EventBus;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppController extends Application {

    private static AppController controller;

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
        CustomActivityOnCrash.setRestartActivityClass(Home.class);
        CustomActivityOnCrash.install(this);
        new RootChecker().execute(this);
    }

    public static AppController getController() {
        return controller;
    }

    public EventBus getBus() {
        return EventBus.getDefault();
    }


    private class RootChecker extends AsyncTask<Context, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            if (AppHelper.isRoot()) {
                AppHelper.setRootEnabled(params[0], RootManager.getInstance().obtainPermission());
            }
            return null;
        }
    }
}