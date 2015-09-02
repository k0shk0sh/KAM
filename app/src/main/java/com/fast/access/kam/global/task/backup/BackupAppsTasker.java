package com.fast.access.kam.global.task.backup;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;

import com.chrisplus.rootmanager.RootManager;
import com.fast.access.kam.global.helper.AppHelper;
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
    private ZipFile zipData;
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
            File zipFileName = new File(fileUtil.getBaseFolderName() + "backup.zip");
            File zipFileData = new File(fileUtil.getBaseFolderName() + "data.zip");
            zipFile = new ZipFile(zipFileName);
            zipData = new ZipFile(zipFileData);
            if (!zipData.isValidZipFile()) {
                if (zipData.getFile() != null && zipData.getFile().exists())
                    zipData.getFile().delete();
            }
            if (!zipFile.isValidZipFile()) {
                if (zipFile.getFile() != null && zipFile.getFile().exists())
                    zipFile.getFile().delete();
            }
            int count = 0;
            boolean withData = AppHelper.isBackupData(context);
            if (withData) RootManager.getInstance().obtainPermission();
            for (AppsModel model : appsModelList) {
                if (isCancelled()) {
                    return onError("cancelled");
                }
                if (model != null) {
                    ApplicationInfo packageInfo = context.getPackageManager().getApplicationInfo(model.getPackageName(), 0);
                    File file = new File(packageInfo.sourceDir);
                    if (file.exists()) {
                        count++;
                        File fileToSave = fileUtil.generateFile(model.getAppName());
                        progressModel = new ProgressModel();
                        progressModel.setProgress(count);
                        progressModel.setFileName(fileToSave.getName());
                        publishProgress(progressModel);
                        ZipParameters parameters = new ZipParameters();
                        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
                        parameters.setSourceExternalStream(true);
                        parameters.setFileNameInZip(fileToSave.getName().replaceAll(" ", ""));
                        zipFile.addFile(file, parameters);
                        progressModel = new ProgressModel();
                        progressModel.setProgress(count);
                        progressModel.setFileName(fileToSave.getName());
                        publishProgress(progressModel);
//                        if (withData) {
//                            withData(model, packageInfo, file, fileUtil, count);
//                            Result r = RootManager.getInstance().runCommand("ls -ld " + packageInfo.dataDir);
//                            if (r.getMessage() != null) {
//                                String[] p = r.getMessage().split("\\s+");
//                                if (p.length == 6) {
////                              String permissions, String owner, String group, String date, String fileName
//                                } else if (p.length == 7) {
////                              String permissions, String owner, String group, String size, String date, String fileName
//                                }
//                            }
//                            Log.e("TAG", r.getMessage() + " " + r.getResult());
//
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return onError(e.getMessage());
        }

        return null;
    }


    private void withData(AppsModel model, ApplicationInfo packageInfo, File file, FileUtil fileUtil, int count) throws Exception {
        File dataFolder = new File(packageInfo.dataDir);
        File dataFile = new File(fileUtil.getBaseFolderName() + model.getPackageName());
        boolean z = RootManager.getInstance().copyFile(dataFolder.getPath(), fileUtil.getBaseFolderName());
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
        if (z) {
            if (dataFile.exists()) {
                zipData.addFolder(dataFile, parameters);
                FileUtils.deleteDirectory(dataFile);
            }
        }
        ProgressModel progressModel = new ProgressModel();
        progressModel.setProgress(count);
        progressModel.setFileName(model.getAppName());
        publishProgress(progressModel);
    }


    private ProgressModel onError(String msg) {
        ProgressModel error = new ProgressModel();
        error.setMsg(msg);
        return error;
    }

    public void onStop() {
        cancel(true);
        if (zipFile != null) {
            if (zipFile.getProgressMonitor() != null) {
                zipFile.getProgressMonitor().cancelAllTasks();
                if (zipFile.getFile() != null && zipFile.getFile().exists())
                    zipFile.getFile().delete();
            }
        }
        if (zipData != null) {
            RootManager.getInstance().runCommand("\nexit\n");
            if (zipData.getProgressMonitor() != null) {
                zipData.getProgressMonitor().cancelAllTasks();
                if (zipData.getFile() != null && zipData.getFile().exists())
                    zipData.getFile().delete();
            }
        }
    }
}
