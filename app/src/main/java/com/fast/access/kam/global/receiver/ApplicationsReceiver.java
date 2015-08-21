package com.fast.access.kam.global.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.fast.access.kam.AppController;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.global.model.EventsModel;

/**
 * Created by kosh on 12/12/2014. CopyRights @ styleme
 */
public class ApplicationsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            Uri data = intent.getData();
            String pkg = data != null ? data.getSchemeSpecificPart() : null;
            boolean isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
                if (!isReplacing) {
                    if (pkg != null) {
                        new AppsModel().deleteByPackageName(pkg);
                        post(pkg, EventsModel.EventType.DELETE);
                    }
                }
            } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                if (!isReplacing) {
                    if (pkg != null) {
                        post(pkg, EventsModel.EventType.NEW);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void post(String packageName, EventsModel.EventType type) {
        EventsModel eventsModel = new EventsModel();
        eventsModel.setPackageName(packageName);
        eventsModel.setEventType(type);
        AppController.getController().getBus().post(eventsModel);
    }
}
