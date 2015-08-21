package com.fast.access.kam.global.loader.task;

import android.content.Context;
import android.os.AsyncTask;

import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.loader.impl.OnTaskLoading;
import com.fast.access.kam.global.model.ProgressModel;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class RestoreTasker extends AsyncTask<Void, ProgressModel, Void> {
    private Context context;
    private OnTaskLoading onTaskLoading;
    private OnProgress onProgress;

    public RestoreTasker(Context context, OnTaskLoading onTaskLoading, OnProgress onProgress) {
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
        onProgress.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onTaskLoading.onPostExecute(false);
    }

    @Override
    protected Void doInBackground(Void... params) {
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
                progressModel.setProgress(i);
                progressModel.setFileName(fileHeader.getFileName());
                publishProgress(progressModel);
            }
            zipFile.delete();
        } catch (ZipException e) {
            e.printStackTrace();
            onTaskLoading.onErrorExecuting(e.getMessage(), true);
        }

        return null;
    }
}
