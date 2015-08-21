package com.fast.access.kam.global.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.fast.access.kam.R;
import com.fast.access.kam.global.loader.impl.OnTaskLoading;
import com.fast.access.kam.global.loader.task.BackupTasker;
import com.fast.access.kam.global.loader.task.OnProgress;
import com.fast.access.kam.global.loader.task.RestoreTasker;
import com.fast.access.kam.global.model.ProgressModel;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class ExecutorService extends Service implements OnProgress, OnTaskLoading {

    private final int NOTIFICATION_ID = 1001;
    private final int GENERAL_NOTIFICATION_ID = 1002;
    private int max = 0;
    private BackupTasker backupTasker;
    private RestoreTasker restoreTasker;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, notification("Working..", 0, 0));
        if (backupTasker == null) {
            backupTasker = new BackupTasker(this, this, this);
        }
        if (restoreTasker == null) {
            restoreTasker = new RestoreTasker(this, this, this);
        }
        boolean isBackup = intent.getBooleanExtra("isBack", true);
        if (isBackup) {
            if (backupTasker.getStatus() != AsyncTask.Status.RUNNING && restoreTasker.getStatus() != AsyncTask.Status.RUNNING) {
                backupTasker.execute();
            }
        } else {
            if (restoreTasker.getStatus() != AsyncTask.Status.RUNNING && backupTasker.getStatus() != AsyncTask.Status.RUNNING) {
                restoreTasker.execute();
            }
        }
        return START_STICKY;
    }

    private Notification notification(String title, int progress, int max) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(false);
        builder.setNumber(progress != 0 ? progress : max);
        builder.setProgress(max, progress, false);
        builder.setContentTitle(title);
        builder.setSmallIcon(R.drawable.ic_notifications);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
        return notification;
    }

    @Override
    public void onProgressUpdate(ProgressModel progressModel) {
        if (progressModel.getFileName() == null) {
            max = progressModel.getMax();
            notification("Working...", 0, max);
        } else {
            notification(progressModel.getFileName(), progressModel.getProgress(), max);
        }
    }

    @Override
    public void onPreExecute() {
        //do nothing since we are using service to handle backup and restore.
    }

    @Override
    public void onPostExecute(boolean isBackup) {
        generateNotification(isBackup);
        stopSelf();
    }

    private void generateNotification(boolean isBackup) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentTitle(isBackup ? "Backup successfully done." : "Restore successfully done.");
        builder.setContentText("Swipe To Dismiss");
        builder.setSmallIcon(R.drawable.ic_notifications);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(GENERAL_NOTIFICATION_ID, notification);
    }

    @Override
    public void onErrorExecuting(String msg, boolean isBackup) {
        if (isBackup)
            Toast.makeText(ExecutorService.this, msg != null ? msg : "Error while backing up files.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ExecutorService.this, msg != null ? msg : "Error while restoring files.", Toast.LENGTH_SHORT).show();
    }
}
