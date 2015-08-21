package com.fast.access.kam.global.loader.task;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;

import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.loader.AppListCreator;
import com.fast.access.kam.global.loader.impl.OnTaskLoading;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.global.model.ProgressModel;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.List;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class BackupTasker extends AsyncTask<Void, ProgressModel, Void> {

    private Context context;
    private OnTaskLoading onTaskLoading;
    private OnProgress onProgress;

    public BackupTasker(Context context, OnTaskLoading onTaskLoading, OnProgress onProgress) {
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
        onTaskLoading.onPostExecute(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<AppsModel> appsModelList = new AppListCreator(context, false).getAppList();
        ProgressModel progressModel = new ProgressModel();
        progressModel.setMax(appsModelList.size());
        publishProgress(progressModel);
        try {
            FileUtil fileUtil = new FileUtil();
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
                        ZipFile zipFile = new ZipFile(fileUtil.getBaseFolderName() + "backup.zip");
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
            onTaskLoading.onErrorExecuting(e.getMessage(), true);
        }

        return null;
    }
}
