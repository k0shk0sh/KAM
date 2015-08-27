package com.fast.access.kam.global.task.restore;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.impl.OnTaskLoading;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

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
            File zipFile = new File(fileUtil.getBaseFolderName() + "backup.zip");
            boolean withData = AppHelper.isRestoreData(params[0]);
            if (withData) RootManager.getInstance().obtainPermission();
            zFile = new ZipFile(zipFile);
            List fileHeaderList = zFile.getFileHeaders();
            ProgressModel progressModel = new ProgressModel();
            progressModel.setMax(fileHeaderList.size());
            publishProgress(progressModel);
            for (int i = 0; i < fileHeaderList.size(); i++) {
                FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
                zFile.extractFile(fileHeader, fileUtil.getBaseFolderName());
                progressModel = new ProgressModel();
                if (AppHelper.isRoot()) {
                    Result result = AppHelper.installApkSilently(fileUtil.getBaseFolderName() + fileHeader.getFileName());
                    if (result != null && result.getStatusCode() == Result.ResultEnum.INSTALL_SUCCESS.getStatusCode()) {
                        boolean deleteApk = new File(fileUtil.getBaseFolderName() + fileHeader.getFileName()).delete();
                        Log.e("Result", result.getMessage() + " deleteApk: " + Boolean.toString(deleteApk));
                    }
                } else {
                    progressModel.setFilePath(fileUtil.getBaseFolderName() + fileHeader.getFileName());
                }
                progressModel.setProgress(i);
                progressModel.setFileName(fileHeader.getFileName());
                publishProgress(progressModel);
            }
            zipFile.delete();
            if (withData) {
                withData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ProgressModel error = new ProgressModel();
            error.setMsg(e.getMessage());
            return error;
        }
        return null;
    }

    private void withData() throws ZipException {
        FileUtil fileUtil = new FileUtil();
        File dataFile = new File(fileUtil.getBaseFolderName() + "data.zip");
        dataZip = new ZipFile(dataFile);
        if (dataZip.getFileHeaders() != null) {
            List fileHeaderList = dataZip.getFileHeaders();
            ProgressModel progressModel = new ProgressModel();
            progressModel.setMax(fileHeaderList.size());
            publishProgress(progressModel);
            for (int i = 0; i < fileHeaderList.size(); i++) {
                FileHeader header = (FileHeader) fileHeaderList.get(i);
                if (header != null) {
                    RootManager.getInstance().remount("/data/data/", "rw");
                    Result result = RootManager.getInstance().runCommand(" mount -o rw,remount -t yaffs2 /data/data\nmkdir " + "/data/data/" + header
                            .getFileName() + "\ncp -r " + header.getFileName() + " " + "/data/data/");
                    Log.e("Result", result.getMessage() + " " + result.getStatusCode() + "  " + result.getResult());
                    progressModel.setProgress(i);
                    progressModel.setFileName(header.getFileName());
                    publishProgress(progressModel);
                }
            }
            RootManager.getInstance().remount("/data/data/", "ro");
            dataFile.delete();
        }
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
