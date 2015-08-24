package com.fast.access.kam;

import com.activeandroid.app.Application;
import com.crashlytics.android.Crashlytics;
import com.fast.access.kam.activities.Home;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.task.RootChecker;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;

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
        Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, crashlytics);
        if (!AppHelper.isRootEnabled(this)) {
            new RootChecker().execute(this);
        }
    }

    public static AppController getController() {
        return controller;
    }

    public EventBus getBus() {
        return EventBus.getDefault();
    }
}