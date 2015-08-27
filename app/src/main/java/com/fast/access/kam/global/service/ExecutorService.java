package com.fast.access.kam.global.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.backup.BackupAppsTasker;
import com.fast.access.kam.global.task.impl.OnTaskLoading;
import com.fast.access.kam.global.task.restore.RestoreAppsTasker;

import java.io.File;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class ExecutorService extends Service implements OnTaskLoading {

    private final int NOTIFICATION_ID = 1001;
    private int max = 0;
    private BackupAppsTasker backupAppsTasker;
    private RestoreAppsTasker restoreAppsTasker;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, notification("Working..", 0, 0));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getStringExtra("cancel") != null) {
                cancelIfRunning();
            } else if (intent.getStringExtra("isBack") != null) {
                handleIntent(intent.getStringExtra("isBack"));
                return START_STICKY;
            }
        }
        return START_NOT_STICKY;
    }

    private Notification notification(String title, int progress, int max) {
        Intent intent = new Intent(this, ExecutorService.class);
        intent.putExtra("cancel", "cancel");
        PendingIntent pendingIntent = PendingIntent.getService(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(false);
        builder.setNumber(progress != 0 ? progress : max);
        builder.setProgress(max, progress, false);
        builder.setContentTitle(title);
        builder.addAction(R.drawable.ic_cancel, "Cancel", pendingIntent);
        builder.setSmallIcon(R.drawable.ic_notifications);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
        return notification;
    }

    @Override
    public void onProgressUpdate(ProgressModel progressModel, boolean isBackup) {
        if (progressModel.getFileName() == null) {
            max = progressModel.getMax();
            notification("Working...", 0, max);
        } else {
            notification(progressModel.getFileName(), progressModel.getProgress(), max);
            if (!isBackup) {
                if (progressModel.getFilePath() != null && !progressModel.getFilePath().isEmpty()) {
                    AppHelper.installApk(this, new File(progressModel.getFileName()));
                }
            }
        }
    }

    @Override
    public void onPreExecute() { /*do nothing.*/ }

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
        manager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onErrorExecuting(String msg, boolean isBackup) {
        if (isBackup)
            Toast.makeText(ExecutorService.this, msg != null ? msg : "Error while backing up files.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ExecutorService.this, msg != null ? msg : "Error while restoring files.", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    private void cancelIfRunning() {
        if (backupAppsTasker != null) {
            if (backupAppsTasker.getStatus() == AsyncTask.Status.RUNNING) {
                backupAppsTasker.onStop();
            }
        }
        if (restoreAppsTasker != null) {
            if (restoreAppsTasker.getStatus() == AsyncTask.Status.RUNNING) {
                restoreAppsTasker.onStop();
            }
        }
        stopSelf();
    }

    private void handleIntent(String action) {
        if (action.equalsIgnoreCase("backup")) {
            if (restoreAppsTasker != null && restoreAppsTasker.getStatus() == AsyncTask.Status.RUNNING) {
                Toast.makeText(ExecutorService.this, "Please Wait, while restoring", Toast.LENGTH_LONG).show();
                return;
            }
            if (backupAppsTasker == null) {
                backupAppsTasker = new BackupAppsTasker(this, this);
            }
            if (backupAppsTasker.getStatus() != AsyncTask.Status.RUNNING) {
                backupAppsTasker.execute();
            }
        } else if (action.equalsIgnoreCase("restore")) {
            if (backupAppsTasker != null && backupAppsTasker.getStatus() == AsyncTask.Status.RUNNING) {
                Toast.makeText(ExecutorService.this, "Please Wait, while backing up", Toast.LENGTH_LONG).show();
                return;
            }
            if (restoreAppsTasker == null) {
                restoreAppsTasker = new RestoreAppsTasker(this);
            }
            if (restoreAppsTasker.getStatus() != AsyncTask.Status.RUNNING) {
                restoreAppsTasker.execute();
            }
        }
    }
}