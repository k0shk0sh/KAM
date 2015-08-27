package com.fast.access.kam.global.task.backup;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.loader.AppListCreator;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.impl.OnTaskLoading;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class BackupAppsTasker extends AsyncTask<Void, ProgressModel, ProgressModel> {

    private Context context;
    private OnTaskLoading onTaskLoading;
    private ZipFile zipFile;
    private List<AppsModel> appsModels;

    public BackupAppsTasker(Context context, OnTaskLoading onTaskLoading) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;
    }

    public BackupAppsTasker(Context context, OnTaskLoading onTaskLoading, List<AppsModel> appsModels) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;
        this.appsModels = appsModels;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskLoading.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(ProgressModel... values) {
        super.onProgressUpdate(values);
        onTaskLoading.onProgressUpdate(values[0], true);
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
        List<AppsModel> appsModelList;
        if (appsModels == null) {
            appsModelList = new AppListCreator(context).getAppList();
        } else {
            appsModelList = appsModels;
        }
        if (appsModelList == null) {
            return onError("Apps Are Empty, Please select ones");
        }
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
            boolean obtained = RootManager.getInstance().obtainPermission();
            for (AppsModel model : appsModelList) {
                if (model != null) {
                    ApplicationInfo packageInfo = context.getPackageManager().getApplicationInfo(model.getPackageName(), 0);
                    File file = new File(packageInfo.sourceDir);
                    if (file.exists()) {
                        count++;
                        File dataFolder = new File(packageInfo.dataDir);
                        File folderName = fileUtil.generateFolder(model.getAppName());
                        Result result = RootManager.getInstance().runCommand("cp -r " + dataFolder + " " + folderName + "\n");
                        RootManager.getInstance().runCommand("cp -r " + file + " " + folderName + "\n");
                        progressModel = new ProgressModel();
                        progressModel.setProgress(count);
                        progressModel.setFileName(model.getAppName());
                        publishProgress(progressModel);
                        ZipParameters parameters = new ZipParameters();
                        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
                        if (result.getStatusCode() == 0) {
                            if (folderName.exists()) {
                                zipFile.addFolder(folderName, parameters);
                            }
                        } else {
                            parameters.setSourceExternalStream(true);
                            parameters.setFileNameInZip(model.getAppName());
                            zipFile.addFile(file, parameters);
                        }
                        progressModel = new ProgressModel();
                        progressModel.setProgress(count);
                        progressModel.setFileName(model.getAppName());
                        publishProgress(progressModel);
                        FileUtils.forceDelete(folderName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return onError(e.getMessage());
        }

        return null;
    }

    private ProgressModel onError(String msg) {
        ProgressModel error = new ProgressModel();
        error.setMsg(msg);
        return error;
    }

    public void onStop() {
        if (zipFile != null) {
            if (zipFile.getProgressMonitor() != null) {
                zipFile.getProgressMonitor().cancelAllTasks();
                if (zipFile.getFile() != null && zipFile.getFile().exists())
                    zipFile.getFile().delete();
            }
        }
        cancel(true);
    }
}
