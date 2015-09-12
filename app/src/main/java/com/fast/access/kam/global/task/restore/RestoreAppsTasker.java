package com.fast.access.kam.global.task.restore;

import android.content.Context;
import android.os.AsyncTask;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.impl.OnTaskLoading;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class RestoreAppsTasker extends AsyncTask<Context, ProgressModel, ProgressModel> {

    private OnTaskLoading onTaskLoading;

    private ZipFile zFile;
    private ZipFile dataZip;

    public RestoreAppsTasker(OnTaskLoading onTaskLoading) {
        this.onTaskLoading = onTaskLoading;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskLoading.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(ProgressModel... values) {
        super.onProgressUpdate(values);
        onTaskLoading.onProgressUpdate(values[0], false);
    }

    @Override
    protected void onPostExecute(ProgressModel aVoid) {
        super.onPostExecute(aVoid);
        if (aVoid != null) {
            onTaskLoading.onErrorExecuting(aVoid.getMsg(), true);
        } else {
            onTaskLoading.onPostExecute(false);
        }
    }

    @Override
    protected ProgressModel doInBackground(Context... params) {
        try {
            FileUtil fileUtil = new FileUtil();
            boolean withData = AppHelper.isRestoreData(params[0]);
            if (withData) RootManager.getInstance().obtainPermission();
            File zipFile = new File(fileUtil.getBaseFolderName() + "backup.zip");
            if (!zipFile.exists()) {
//                if (withData) {
//                    withData();
//                }
                return error("Backup Folder Doe Not Exits!");
            }
            zFile = new ZipFile(zipFile);
            List fileHeaderList = zFile.getFileHeaders();
            ProgressModel progressModel = new ProgressModel();
            progressModel.setMax(fileHeaderList.size());
            publishProgress(progressModel);
            for (int i = 0; i < fileHeaderList.size(); i++) {
                if (isCancelled()) {
                    return error("cancelled");
                }
                FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
                zFile.extractFile(fileHeader, fileUtil.getBaseFolderName());
                progressModel = new ProgressModel();
                progressModel.setProgress(i);
                progressModel.setFileName(fileHeader.getFileName());
                publishProgress(progressModel);
                if (AppHelper.isRoot()) {
                    Result result = AppHelper.installApkSilently(new File(fileUtil.getBaseFolderName() + fileHeader.getFileName()).getPath());
                    if (result != null && result.getStatusCode() == Result.ResultEnum.INSTALL_SUCCESS.getStatusCode()) {
                        boolean deleteApk = new File(fileUtil.getBaseFolderName() + fileHeader.getFileName()).delete();
                    }
                } else {
                    progressModel.setFilePath(fileUtil.getBaseFolderName() + fileHeader.getFileName());
                }
            }
            zipFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return null;
    }

    private ProgressModel error(String msg) {
        ProgressModel error = new ProgressModel();
        error.setMsg(msg);
        return error;
    }

    private void withData() throws Exception {
        FileUtil fileUtil = new FileUtil();
        File dataFile = new File(fileUtil.getBaseFolderName() + "data.zip");
        dataZip = new ZipFile(dataFile);
        if (dataZip.getFileHeaders() != null) {
            List fileHeaderList = dataZip.getFileHeaders();
            ProgressModel progressModel = new ProgressModel();
            progressModel.setMax(fileHeaderList.size());
            publishProgress(progressModel);
            List<FileHeader> headers = dataZip.getFileHeaders();
            for (FileHeader header : headers) {
                dataZip.extractFile(header, fileUtil.getBaseFolderName());
                copyFileToData(fileUtil.getBaseFolderName(), header.getFileName());
            }
        }
        FileUtils.forceDelete(dataFile);
    }

    private void copyFileToData(String base, String file) throws Exception {
        RootManager.getInstance().copyFile(base + File.separator + file, " /data/data/" + file);
        FileUtils.forceDelete(new File(base + File.separator + file));
//        Log.e("Result", result.getMessage() + "  " + result.getStatusCode());
    }

    public void onStop() {
        if (zFile != null) {
            if (zFile.getProgressMonitor() != null) {
                zFile.getProgressMonitor().cancelAllTasks();
            }
        }
        if (dataZip != null) {
            if (dataZip.getProgressMonitor() != null) {
                dataZip.getProgressMonitor().cancelAllTasks();
            }
        }
        cancel(true);
    }
}

