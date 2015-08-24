package com.fast.access.kam.global.task.backup;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;

import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.loader.AppListCreator;
import com.fast.access.kam.global.task.impl.OnTaskLoading;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.impl.OnProgress;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class BackupAppsTasker extends AsyncTask<Void, ProgressModel, ProgressModel> {

    private Context context;
    private OnTaskLoading onTaskLoading;
    private OnProgress onProgress;
    private ZipFile zipFile;

    public BackupAppsTasker(Context context, OnTaskLoading onTaskLoading, OnProgress onProgress) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;
        this.onProgress = onProgress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskLoading.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(ProgressModel... values) {
        super.onProgressUpdate(values);
        onProgress.onProgressUpdate(values[0], true);
    }

    @Override
    protected void onPostExecute(ProgressModel aVoid) {
        super.onPostExecute(aVoid);
        if (aVoid != null) {
            onTaskLoading.onErrorExecuting(aVoid.getMsg(), true);
        } else {
            onTaskLoading.onPostExecute(true);
        }
    }

    @Override
    protected ProgressModel doInBackground(Void... params) {
        List<AppsModel> appsModelList = new AppListCreator(context, false).getAppList();
        ProgressModel progressModel = new ProgressModel();
        progressModel.setMax(appsModelList.size());
        publishProgress(progressModel);
        try {
            FileUtil fileUtil = new FileUtil();
            zipFile = new ZipFile(fileUtil.getBaseFolderName() + "backup.zip");
            if (!zipFile.isValidZipFile()) {
                if (zipFile.getFile() != null && zipFile.getFile().exists())
                    zipFile.getFile().delete();
            }
            int count = 0;
            for (AppsModel model : appsModelList) {
                if (model != null) {
                    ApplicationInfo packageInfo = context.getPackageManager().getApplicationInfo(model.getPackageName(), 0);
                    File fileToSave = fileUtil.generateFile(model.getAppName());
                    File file = new File(packageInfo.sourceDir);
                    if (file.exists()) {
                        count++;
                        progressModel = new ProgressModel();
                        progressModel.setProgress(count);
                        progressModel.setFileName(fileToSave.getName());
                        publishProgress(progressModel);
                        ZipParameters parameters = new ZipParameters();
                        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
                        parameters.setSourceExternalStream(true);
                        parameters.setFileNameInZip(fileToSave.getName());
                        zipFile.addFile(file, parameters);
                        progressModel = new ProgressModel();
                        progressModel.setProgress(count);
                        progressModel.setFileName(fileToSave.getName());
                        publishProgress(progressModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ProgressModel error = new ProgressModel();
            error.setMsg(e.getMessage());
            return error;
        }

        return null;
    }

    public void onStop() {
        if (zipFile != null) {
            if (zipFile.getProgressMonitor() != null) {
                zipFile.getProgressMonitor().cancelAllTasks();
                if (zipFile.getFile() != null && zipFile.getFile().exists())
                    zipFile.getFile().delete();
            }
        }
    }
}
