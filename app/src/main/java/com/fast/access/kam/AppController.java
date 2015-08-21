package com.fast.access.kam;

import com.activeandroid.app.Application;
import com.fast.access.kam.activities.Home;

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
    }

    public static AppController getController() {
        return controller;
    }

    public EventBus getBus() {
        return EventBus.getDefault();
    }

}
