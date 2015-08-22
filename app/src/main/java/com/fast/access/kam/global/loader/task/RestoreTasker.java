package com.fast.access.kam.global.loader.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.chrisplus.rootmanager.container.Result;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.loader.impl.OnTaskLoading;
import com.fast.access.kam.global.model.ProgressModel;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class RestoreTasker extends AsyncTask<Context, ProgressModel, ProgressModel> {

    private OnTaskLoading onTaskLoading;
    private OnProgress onProgress;

    public RestoreTasker(OnTaskLoading onTaskLoading, OnProgress onProgress) {
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
        onProgress.onProgressUpdate(values[0], false);
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
        FileUtil fileUtil = new FileUtil();
        File zipFile = new File(fileUtil.getBaseFolderName() + "backup.zip");
        try {
            ZipFile zFile = new ZipFile(zipFile);
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
        } catch (Exception e) {
            e.printStackTrace();
            ProgressModel error = new ProgressModel();
            error.setMsg(e.getMessage());
            return error;
        }
        return null;
    }
}
